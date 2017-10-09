package com.android.rdc.imageloader.loader;


import com.android.rdc.imageloader.request.BitmapRequest;

public interface Loader {
    void loadImage(BitmapRequest request);
}
