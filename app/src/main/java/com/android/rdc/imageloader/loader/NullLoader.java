package com.android.rdc.imageloader.loader;

import android.graphics.Bitmap;

import com.android.rdc.imageloader.request.BitmapRequest;


public class NullLoader extends AbsLoader {

    @Override
    protected Bitmap onLoadImage(BitmapRequest request) {
        return null;
    }
}
