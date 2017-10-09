package com.android.rdc.imageloader.policy;


import com.android.rdc.imageloader.request.BitmapRequest;

public class SerialPolicy implements LoadPolicy {
    @Override
    public int compare(BitmapRequest b1, BitmapRequest b2) {
        return b1.serialNum - b2.serialNum;
    }
}
