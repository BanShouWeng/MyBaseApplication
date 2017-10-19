package com.banshouweng.mybaseapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */

public class JumpToUtils {
    /**
     * 跳转页面
     *
     * @param context        当前上下文
     * @param targetActivity 所跳转的目的Activity类
     */
    public static void jumpTo(Context context, Class<?> targetActivity) {
        context.startActivity(new Intent(context, targetActivity));
    }

    /**
     * 跳转页面
     *
     * @param context        当前上下文
     * @param targetActivity 所跳转的目的Activity类
     * @param bundle         跳转所携带的信息
     */
    public static void jumpTo(Context context, Class<?> targetActivity, Bundle bundle) {
        Intent intent = new Intent(context, targetActivity);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 跳转页面
     *
     * @param context        当前上下文
     * @param targetActivity 所跳转的Activity类
     * @param requestCode    请求码
     */
    public static void jumpTo(Context context, Class<?> targetActivity, int requestCode) {
        ((Activity) context).startActivityForResult(new Intent(context, targetActivity), requestCode);
    }

    /**
     * 跳转页面
     *
     * @param context        当前上下文
     * @param targetActivity 所跳转的Activity类
     * @param bundle         跳转所携带的信息
     * @param requestCode    请求码
     */
    public static void jumpTo(Context context, Class<?> targetActivity, int requestCode, Bundle bundle) {
        Intent intent = new Intent(context, targetActivity);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        ((Activity) context).startActivityForResult(intent, requestCode);
    }
}
