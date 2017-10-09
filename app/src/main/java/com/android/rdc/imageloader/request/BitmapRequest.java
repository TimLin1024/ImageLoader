package com.android.rdc.imageloader.request;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.android.rdc.imageloader.config.DisplayConfig;
import com.android.rdc.imageloader.core.RequestQueue;
import com.android.rdc.imageloader.core.SimpleImageLoader;
import com.android.rdc.imageloader.policy.LoadPolicy;
import com.android.rdc.imageloader.utils.ImageViewHelper;


public class BitmapRequest implements Comparable<BitmapRequest> {
    public ImageView mImageView;
    public String mUri;
    public DisplayConfig mDisplayConfig;
    public RequestQueue.ImageListener mImageListener;
    public boolean mCancel;
    public boolean justCacheInMemory;
    private boolean mIvTagValid;
    public int serialNum = 0;


    /**
     * 加载策略
     */
    private LoadPolicy mLoadPolicy;


    public BitmapRequest(ImageView imageView, String uri,
                         DisplayConfig displayConfig, RequestQueue.ImageListener imageListener) {
        mImageView = imageView;
        mUri = uri;
        mDisplayConfig = displayConfig;
        mImageListener = imageListener;
        mLoadPolicy = SimpleImageLoader.getInstance().getImageLoaderConfig().getLoadPolicy();
    }

    public int getImageViewWidth() {
        return ImageViewHelper.getImageViewWidth(mImageView);
    }

    public int getImageViewHeight() {
        return ImageViewHelper.getImageViewHeight(mImageView);
    }

    public boolean isCancel() {
        return mCancel;
    }

    public boolean isIvTagValid() {
        return mIvTagValid;
    }

    @Override
    public int compareTo(@NonNull BitmapRequest o) {
        return mLoadPolicy.compare(this, o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitmapRequest that = (BitmapRequest) o;

        if (mImageView != null ? !mImageView.equals(that.mImageView) : that.mImageView != null)
            return false;
        if (mUri != null ? !mUri.equals(that.mUri) : that.mUri != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = mImageView != null ? mImageView.hashCode() : 0;
        result = 31 * result + (mUri != null ? mUri.hashCode() : 0);
        return result;
    }
}
