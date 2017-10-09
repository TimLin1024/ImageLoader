package com.android.rdc.imageloader.loader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoaderManager {
    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String FILE = "file";
    private static LoaderManager sInstance;

    private Map<String, Loader> mLoaderMap = new ConcurrentHashMap<>();
    private Loader mNullLoader = new NullLoader();

    /**
     * 将两个 Loader 注入到 LoaderManager 中，然后在加载图片时根据图片的 schema 来获取对应的 Loader 完成加载功能
     */
    private LoaderManager() {
        register(HTTP, new UrlLoader());
        register(HTTPS, new UrlLoader());
        register(FILE, new LocalLoader());
    }

    /**
     */
    public static LoaderManager getInstance() {
        if (sInstance == null) {
            synchronized (LoaderManager.class) {
                if (sInstance == null) {
                    sInstance = new LoaderManager();
                }
            }
        }
        return sInstance;
    }


    public final synchronized void register(String schema, Loader loader) {
        mLoaderMap.put(schema, loader);
    }


    public Loader getLoader(String schema) {
        if (mLoaderMap.containsKey(schema)) {
            return mLoaderMap.get(schema);
        }
        return mNullLoader;
    }
}
