package com.android.rdc.imageloader.core;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.rdc.imageloader.request.BitmapRequest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestQueue {
    private static final String TAG = "RequestQueue";

    private BlockingQueue<BitmapRequest> mRequestBlockingQueue = new PriorityBlockingQueue<>();

    /**
     * 请求的序列化生成器
     */
    private AtomicInteger mSerialNumGenerator = new AtomicInteger(0);

    /**
     * 默认的核心数
     */
    private static int DEFAULT_CORE_NUM = Runtime.getRuntime().availableProcessors() + 1;

    /**
     * CPU核心数 + 1个分发线程数
     */
    private int mDispatcherNums = DEFAULT_CORE_NUM;

    /**
     * NetworkExecutor,执行网络请求的线程
     */
    private RequestDispatcher[] mRequestDispatchers;

    protected RequestQueue() {
        this(DEFAULT_CORE_NUM);
    }

    public RequestQueue(int mDispatcherNums) {
        this.mDispatcherNums = mDispatcherNums;
    }


    public void start() {
        stop();
        startDispatcher();
    }

    private void stop() {
        if (mRequestDispatchers != null && mRequestDispatchers.length > 0) {
            for (RequestDispatcher requestDispatcher : mRequestDispatchers) {
                requestDispatcher.interrupt();
            }
        }
    }

    private final void startDispatcher() {
        mRequestDispatchers = new RequestDispatcher[mDispatcherNums];
        for (RequestDispatcher requestDispatcher : mRequestDispatchers) {
            requestDispatcher.start();
        }
    }

    public void addRequest(BitmapRequest request) {
        if (!mRequestBlockingQueue.contains(request)) {
            request.serialNum = this.getSerialNum();
        } else {
            Log.e(TAG, "### 请求队列中已含有该请求 ");
        }
    }

    public int getSerialNum() {
        return mSerialNumGenerator.incrementAndGet();
    }

    /**
     * 图片加载Listener
     *
     * @author mrsimple
     */
    public interface ImageListener {
        void onComplete(ImageView imageView, Bitmap bitmap, String uri);
    }
}
