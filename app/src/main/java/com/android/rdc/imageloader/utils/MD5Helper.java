package com.android.rdc.imageloader.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Helper {

    private static MessageDigest sMessageDigest;

    static {
        try {
            sMessageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private MD5Helper() {
    }

    /**
     * 对key进行MD5加密，如果无MD5加密算法，则直接使用key对应的hash值。</br>
     *
     * @param key
     * @return
     */
    public static String toMD5(String key) {
        if (sMessageDigest == null) {
            return String.valueOf(key.hashCode());
        }
        String cacheKey;
        sMessageDigest.update(key.getBytes());
        cacheKey = bytesToHexString(sMessageDigest.digest());
        return cacheKey;
    }

    /**
     * @param bytes
     * @return
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


}
