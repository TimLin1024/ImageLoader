package com.android.rdc.imageloader.core;

import android.util.Log;

import com.android.rdc.imageloader.loader.Loader;
import com.android.rdc.imageloader.loader.LoaderManager;
import com.android.rdc.imageloader.request.BitmapRequest;

import java.util.concurrent.BlockingQueue;


public class RequestDispatcher extends Thread {
    private static final String TAG = "RequestDispatcher";
    private BlockingQueue<BitmapRequest> mRequestBlockingQueue;

    public RequestDispatcher(BlockingQueue<BitmapRequest> requestBlockingQueue) {
        mRequestBlockingQueue = requestBlockingQueue;
    }

    @Override
    public void run() {

        try {
            while (!this.isInterrupted()) {
                final BitmapRequest bitmapRequest = mRequestBlockingQueue.take();
                if (bitmapRequest.isCancel()) {
                    continue;
                }

                final String schema = parseSchema(bitmapRequest.mUri);
                //根据 schema 获取 loader
                Loader imageLoader = LoaderManager.getInstance().getLoader(schema);
                //加载图片
                imageLoader.loadImage(bitmapRequest);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private String parseSchema(String uri) {
        if (uri.contains("://")) {
            return uri.split("://")[0];
        }
        Log.e(TAG, "### wrong scheme,image uri is :  " + uri);
        return "";
    }
}
