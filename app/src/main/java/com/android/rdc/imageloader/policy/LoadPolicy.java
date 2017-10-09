package com.android.rdc.imageloader.policy;


import com.android.rdc.imageloader.request.BitmapRequest;

public interface LoadPolicy {
    /**
     * 将 compareTo 委托给 LoadPolicy 对象的 compare。
     * */
    int compare(BitmapRequest b1, BitmapRequest b2);

}
