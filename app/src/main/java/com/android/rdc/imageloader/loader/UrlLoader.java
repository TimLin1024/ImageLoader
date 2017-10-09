package com.android.rdc.imageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.rdc.imageloader.request.BitmapRequest;
import com.android.rdc.imageloader.utils.BitmapDecoder;
import com.android.rdc.imageloader.utils.CloseUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlLoader extends AbsLoader {
    @Override
    protected Bitmap onLoadImage(BitmapRequest request) {
        String imgUrlStr = request.mUri;
        InputStream is = null;
        try {
            URL url = new URL(imgUrlStr);
            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(urlConnection.getInputStream());
            is.mark(is.available());

            final InputStream inputStream = is;
            final BitmapDecoder bitmapDecoder = new BitmapDecoder() {
                @Override
                public Bitmap decodeBitmapWithOption(BitmapFactory.Options options) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    if (options.inJustDecodeBounds) {
                        try {
                            inputStream.reset();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        urlConnection.disconnect();
                    }
                    return bitmap;
                }
            };

            return bitmapDecoder.decodeBitmap(request.getImageViewWidth(),
                    request.getImageViewHeight());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(is);
        }
        return null;
    }
}
