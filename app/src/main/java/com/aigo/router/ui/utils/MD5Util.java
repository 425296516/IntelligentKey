package com.aigo.router.ui.utils;

import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by zhangcirui on 2017/1/15.
 */

public class MD5Util {
    private static final String TAG = MD5Util.class.getSimpleName();

    /**
     * MD5 加密
     */
    public static String createMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, e.toString());
            ;
            return null;
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, e.toString());
            ;
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    public static String createSignParameter(String url, String action) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(url);
        stringBuffer.append("2013-03-29");
        stringBuffer.append(action);
        String md5Str = createMD5Str(stringBuffer.toString()).toLowerCase();
        return md5Str;
    }

    public static String createDeviceOnlineSignParameter(HashMap<String, String> params, String secret) throws IOException {
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<String, String>(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder basestring = new StringBuilder();
        for (Map.Entry<String, String> param : entrys) {
            basestring.append(param.getKey()).append("=").append(param.getValue()).append("&");
        }
        String str = basestring.substring(0, basestring.length() - 1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str).append(secret);
        //basestring.append(secret);
        Log.d(TAG, "" + stringBuilder);

        // 使用MD5对待签名串求签
        byte[] bytes = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(stringBuilder.toString().getBytes());
        } catch (GeneralSecurityException ex) {
            throw new IOException(ex);
        }

        // 将MD5输出的二进制结果转换为小写的十六进制
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex);
        }
        return sign.toString();
    }

}
