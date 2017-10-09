package com.android.rdc.imageloader.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.android.rdc.imageloader.request.BitmapRequest;
import com.android.rdc.imageloader.utils.BitmapDecoder;
import com.android.rdc.imageloader.utils.CloseUtil;
import com.android.rdc.imageloader.utils.MD5Helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DiskCache implements BitmapCache {

    private static final String TAG = "DiskCache";
    public static final int MB = 1024 * 1024;

    private static final String IMAGE_DISK_CACHE = "bitmap";

    private DiskLruCache mDiskLruCache;
    private static volatile DiskCache sDiskCache;

    private DiskCache(Context context) {
        initDiskCache(context);
    }

    public static DiskCache getInstance(Context context) {
        if (sDiskCache == null) {
            synchronized (DiskCache.class) {
                if (sDiskCache == null) {
                    sDiskCache = new DiskCache(context.getApplicationContext());
                }
            }
        }
        return sDiskCache;
    }

    private void initDiskCache(Context context) {
        File cacheDir = getDiskCacheDir(context, IMAGE_DISK_CACHE);
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        try {
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 50 * MB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 1;
    }

    /**
     * 获取sd缓存的目录,如果挂载了sd卡则使用sd卡缓存，否则使用应用的缓存目录。
     *
     * @param context    Context
     * @param uniqueName 缓存目录名,比如bitmap
     * @return
     */
    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }


    @Override
    public Bitmap get(final BitmapRequest key) {
        BitmapDecoder b = new BitmapDecoder() {
            @Override
            public Bitmap decodeBitmapWithOption(BitmapFactory.Options options) {
                InputStream is = getInputStream(MD5Helper.toMD5(key.mUri));
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
                CloseUtil.close(is);
                return bitmap;
            }
        };
        return b.decodeBitmap(key.getImageViewWidth(), key.getImageViewHeight());
    }

    private InputStream getInputStream(String md5) {
        DiskLruCache.Snapshot snapshot;
        try {
            snapshot = mDiskLruCache.get(md5);
            if (snapshot != null) {
                return snapshot.getInputStream(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * sd卡缓存只缓存从网络下下载下来的图片,本地图片则不缓存
     */
    @Override
    public void put(BitmapRequest key, Bitmap bitmap) {
        if (key.justCacheInMemory) {
            Log.d(TAG, "### 仅缓存在内存中");
            return;
        }
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskLruCache.edit(key.mUri);
            OutputStream outputStream = editor.newOutputStream(0);
            if (writeBitmapToDisk(bitmap, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            CloseUtil.close(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean writeBitmapToDisk(Bitmap bitmap, OutputStream outputStream) {
        BufferedOutputStream bos = new BufferedOutputStream(outputStream, 8 * 1024);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        boolean result = true;
        try {
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            CloseUtil.close(bos);
        }
        return result;
    }

    @Override
    public void remove(BitmapRequest key) {
        try {
            mDiskLruCache.remove(MD5Helper.toMD5(key.mUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
