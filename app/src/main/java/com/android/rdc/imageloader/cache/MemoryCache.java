package com.android.rdc.imageloader.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.rdc.imageloader.request.BitmapRequest;


public class MemoryCache implements BitmapCache {

    private LruCache<BitmapRequest, Bitmap> mMemoryCache;

    public MemoryCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);//单位：MB

        final int cacheSize = maxMemory / 4;//取最大内存的缓存
        mMemoryCache = new LruCache<BitmapRequest, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(BitmapRequest key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap get(BitmapRequest key) {
        return mMemoryCache.get(key);
    }

    @Override
    public void put(BitmapRequest key, Bitmap bitmap) {
        mMemoryCache.put(key, bitmap);
    }

    @Override
    public void remove(BitmapRequest key) {
        mMemoryCache.remove(key);
    }
}
