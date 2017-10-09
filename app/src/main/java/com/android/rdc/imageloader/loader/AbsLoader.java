package com.android.rdc.imageloader.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.rdc.imageloader.cache.BitmapCache;
import com.android.rdc.imageloader.config.DisplayConfig;
import com.android.rdc.imageloader.core.SimpleImageLoader;
import com.android.rdc.imageloader.request.BitmapRequest;


public abstract class AbsLoader implements Loader {
    private static BitmapCache mCache = SimpleImageLoader.getInstance()
            .getImageLoaderConfig().getBitmapCache();//缓存类
    /**
     * 加载图片的四个步骤（模板方法）
     * 1. 从缓存中获取
     * 2. 显示加载占位图
     * 3. 从缓存中获取。如果有缓存直接调用主线程加载，如果没有缓存则调用 onLoadImage 获取
     * 4. 将获取到的图片插入缓存中
     * 5. 将结果发送到 UI 线程中更新
     * */
    @Override
    public final void loadImage(BitmapRequest request) {
        //先从缓存中获取
        Bitmap resultBitmap = mCache.get(request);
        if (resultBitmap == null) {
            showLoading(request);//显示加载状态占位图
            resultBitmap = onLoadImage(request);//加载图片，具体下载逻辑交由子类处理
            cacheBitmap(request, resultBitmap);
        } else {
            request.justCacheInMemory = true;
        }
        //将结果传递到 UI 线程
        deliveryToUIThread(request, resultBitmap);

    }

    private void showLoading(final BitmapRequest request) {
        final ImageView imageView = request.mImageView;
        if (request.isIvTagValid() && hasLoadingPlaceholder(request.mDisplayConfig)) {
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(request.mDisplayConfig.loadingResId);
                }
            });
        }
    }

    private void cacheBitmap(BitmapRequest request, Bitmap resultBitmap) {
        if (resultBitmap != null && mCache != null) {
            synchronized (mCache) {
                mCache.put(request, resultBitmap);
            }
        }
    }

    /**
     * 获取图片，交由子类处理。
     * */
    protected abstract Bitmap onLoadImage(BitmapRequest request);

    private void deliveryToUIThread(final BitmapRequest request, final Bitmap resultBitmap) {
        final ImageView imageView = request.mImageView;
        if (imageView != null) {
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    updateImageView(request, resultBitmap);
                }
            });
        }
    }

    private void updateImageView(BitmapRequest request, Bitmap resultBitmap) {
        ImageView imageView = request.mImageView;
        String uri = request.mUri;
        if (resultBitmap != null && request.mImageView.getTag().equals(request.mUri)) {
            imageView.setImageBitmap(resultBitmap);
        }
        //加载失败，设置失败占位图
        if (resultBitmap == null && hasFailedPlaceHolder(request.mDisplayConfig)) {
            imageView.setImageResource(request.mDisplayConfig.failedResId);
        }
        //回调接口
        if (request.mImageListener != null) {
            request.mImageListener.onComplete(imageView, resultBitmap, uri);
        }
    }

    private boolean hasLoadingPlaceholder(DisplayConfig displayConfig) {
        return displayConfig != null && displayConfig.loadingResId > 0;
    }

    private boolean hasFailedPlaceHolder(DisplayConfig displayConfig) {
        return displayConfig != null && displayConfig.failedResId > 0;
    }
}
