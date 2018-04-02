package com.banshouweng.mybaseapplication.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

/**
 * @author leiming
 * @date 2017/10/11
 */
public class TxtUtils {
    public static String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static String getText(TextView textText) {
        return textText.getText().toString().trim();
    }

    public static String getText(String text) {
        return TextUtils.isEmpty(text) ? "-" : text;
    }

    public static String getText(int text) {
        return TextUtils.isEmpty(String.format(Locale.CHINA, "%d", text)) ? "-" : String.format(Locale.CHINA, "%d", text);
    }

    public static String getText(long text) {
        return TextUtils.isEmpty(String.format(Locale.CHINA, "%d", text)) ? "-" : String.format(Locale.CHINA, "%d", text);
    }

    public static String getText(double text) {
        return TextUtils.isEmpty(String.format("%s", text)) ? "-" : String.format("%s", text);
    }

    public static String getText(Context context, int resourceId) {
        return context.getResources().getString(resourceId);
    }
}
