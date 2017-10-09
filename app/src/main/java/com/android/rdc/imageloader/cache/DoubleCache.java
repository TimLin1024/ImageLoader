package com.android.rdc.imageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.rdc.imageloader.request.BitmapRequest;


public class DoubleCache implements BitmapCache {

    private MemoryCache mMemoryCache;
    private DiskCache mDiskCache;

    public DoubleCache(Context context) {
        mDiskCache = DiskCache.getInstance(context);
        mMemoryCache = new MemoryCache();
    }


    @Override
    public Bitmap get(BitmapRequest key) {
        Bitmap value = mMemoryCache.get(key);
        if (value == null) {
            value = mDiskCache.get(key);
            saveBitmapIntoMemory(key, value);
        }
        return value;
    }

    /**
     * 将 Bitmap 存储到内存缓存中
     */
    private void saveBitmapIntoMemory(BitmapRequest key, Bitmap value) {
        if (value != null) {
            mMemoryCache.put(key, value);
        }
    }

    @Override
    public void put(BitmapRequest key, Bitmap bitmap) {
        mMemoryCache.put(key, bitmap);
        mDiskCache.put(key, bitmap);
    }

    @Override
    public void remove(BitmapRequest key) {
        mMemoryCache.remove(key);
        mDiskCache.remove(key);
    }
}
