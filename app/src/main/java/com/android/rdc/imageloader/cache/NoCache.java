package com.android.rdc.imageloader.cache;

import android.graphics.Bitmap;

import com.android.rdc.imageloader.request.BitmapRequest;


public class NoCache implements BitmapCache{
    @Override
    public Bitmap get(BitmapRequest key) {
        return null;
    }

    @Override
    public void put(BitmapRequest key, Bitmap bitmap) {

    }

    @Override
    public void remove(BitmapRequest key) {

    }
}
