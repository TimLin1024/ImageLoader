package com.android.rdc.imageloader.cache;

import android.graphics.Bitmap;

import com.android.rdc.imageloader.request.BitmapRequest;


public interface BitmapCache {

    Bitmap get(BitmapRequest key);

    void put(BitmapRequest key, Bitmap bitmap);

    void remove(BitmapRequest key);

}
