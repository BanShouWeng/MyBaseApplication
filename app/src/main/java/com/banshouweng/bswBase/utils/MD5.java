package com.banshouweng.bswBase.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author leiming
 * @date 2017/10/11
 */
public class MD5 {
    private static final String _MD5 = "MD5";
    private static final String _UTF8 = "UTF-8";

    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        if (str == null) {
            return null;
        }
        try {
            messageDigest = MessageDigest.getInstance(_MD5);
            messageDigest.reset();
            messageDigest.update(str.getBytes(_UTF8));
        } catch (NoSuchAlgorithmException ignored) {
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuilder md5StrBuff = new StringBuilder();

//        for (int i = 0; i < byteArray.length; i++) {
//            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
//                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
//            else
//                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
//        }
        for (byte b : byteArray) {
            if (Integer.toHexString(0xFF & b).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & b));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & b));
        }

        return md5StrBuff.toString();
    }

    public static String getMD5Upper(String str) {
        String md5 = getMD5Str(str);
        return md5.toUpperCase();
    }
}
