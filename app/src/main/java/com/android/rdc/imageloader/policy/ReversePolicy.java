package com.android.rdc.imageloader.policy;


import com.android.rdc.imageloader.request.BitmapRequest;

public class ReversePolicy implements LoadPolicy {
    @Override
    public int compare(BitmapRequest b1, BitmapRequest b2) {
        return b2.serialNum - b1.serialNum;
    }
}
