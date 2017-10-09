package com.android.rdc.imageloader.config;


import com.android.rdc.imageloader.cache.BitmapCache;
import com.android.rdc.imageloader.cache.MemoryCache;
import com.android.rdc.imageloader.cache.NoCache;
import com.android.rdc.imageloader.policy.LoadPolicy;
import com.android.rdc.imageloader.policy.SerialPolicy;

public class ImageLoaderConfig {

    private BitmapCache mBitmapCache = new MemoryCache();//默认为内存缓存

    private LoadPolicy mLoadPolicy = new SerialPolicy();//默认为顺序加载

    private DisplayConfig mDisplayConfig = new DisplayConfig();

    private int mThreadCount = Runtime.getRuntime().availableProcessors() + 1;

    public ImageLoaderConfig setBitmapCache(BitmapCache bitmapCache) {
        if (bitmapCache != null) {
            mBitmapCache = bitmapCache;
        } else {
            mBitmapCache = new NoCache();//如果设置为 null，则设置为 NoCache
        }
        return this;
    }

    public ImageLoaderConfig setLoadPolicy(LoadPolicy loadPolicy) {
        if (loadPolicy != null) {
            mLoadPolicy = loadPolicy;
        }
        return this;
    }

    public ImageLoaderConfig setDisplayConfig(DisplayConfig displayConfig) {
        if (displayConfig != null) {
            mDisplayConfig = displayConfig;
        }
        return this;
    }

    public ImageLoaderConfig setThreadCount(int threadCount) {
        if (threadCount < 0) {
            throw new IllegalArgumentException("threadCount should not less than 0");
        }
        mThreadCount = threadCount;
        return this;
    }

    public BitmapCache getBitmapCache() {
        return mBitmapCache;
    }

    public LoadPolicy getLoadPolicy() {
        return mLoadPolicy;
    }

    public DisplayConfig getDisplayConfig() {
        return mDisplayConfig;
    }

    public int getThreadCount() {
        return mThreadCount;
    }
}
