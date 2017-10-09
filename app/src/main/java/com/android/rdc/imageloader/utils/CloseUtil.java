package com.android.rdc.imageloader.utils;

import java.io.Closeable;
import java.io.IOException;

public final class CloseUtil {
    private CloseUtil() {
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
