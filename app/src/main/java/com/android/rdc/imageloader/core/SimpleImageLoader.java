package com.android.rdc.imageloader.core;


import com.android.rdc.imageloader.cache.BitmapCache;
import com.android.rdc.imageloader.cache.MemoryCache;
import com.android.rdc.imageloader.config.ImageLoaderConfig;

/**
 * 图片加载类,支持url和本地图片的uri形式加载.根据图片路径格式来判断是网络图片还是本地图片,如果是网络图片则交给SimpleNet框架来加载，
 * 如果是本地图片那么则交给mExecutorService从sd卡中加载
 * .加载之后直接更新UI，无需用户干预.如果用户设置了缓存策略,那么会将加载到的图片缓存起来.用户也可以设置加载策略，例如顺序加载{@see
 * SerialPolicy}和逆向加载{@see ReversePolicy}.
 */
public final class SimpleImageLoader {

    private volatile static SimpleImageLoader sInstance;
    //请求队列
    private RequestQueue mImgQueue;
    //缓存
    private volatile BitmapCache mBitmapCache = new MemoryCache();
    //图片加载配置对象
    private ImageLoaderConfig mImageLoaderConfig;

    private SimpleImageLoader() {
    }

    public static SimpleImageLoader getInstance() {
        if (sInstance == null) {
            synchronized (SimpleImageLoader.class) {
                if (sInstance == null) {
                    sInstance = new SimpleImageLoader();
                }
            }
        }
        return sInstance;
    }

    public void init(ImageLoaderConfig imageLoaderConfig) {
        mImageLoaderConfig = imageLoaderConfig;
        mBitmapCache = mImageLoaderConfig.getBitmapCache();
        checkConfig();
        mImgQueue = new RequestQueue(mImageLoaderConfig.getThreadCount());
        mImgQueue.start();
    }

    private void checkConfig() {
        if (mImageLoaderConfig == null) {
            throw new RuntimeException(
                    "The config of SimpleImageLoader is Null, please call the init(ImageLoaderConfig config) method to initialize");
        }
    }

    public ImageLoaderConfig getImageLoaderConfig() {
        return mImageLoaderConfig;
    }
}
