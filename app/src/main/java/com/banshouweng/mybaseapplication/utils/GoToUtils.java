package com.banshouweng.mybaseapplication.utils;

import android.app.Activity;
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

public class GoToUtils {
    /**
     * 跳转页面
     *
     * @param activity 当前activity
     * @param targetActivity      所跳转的目的Activity类
     */
    public void goTo(Activity activity, Class<?> targetActivity) {
        activity.startActivity(new Intent(activity, targetActivity));
    }

    /**
     * 跳转页面
     *
     * @param activity 当前activity
     * @param targetActivity      所跳转的目的Activity类
     * @param bundle   跳转所携带的信息
     */
    public void goTo(Activity activity, Class<?> targetActivity, Bundle bundle) {
        Intent intent = new Intent(activity, targetActivity);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        activity.startActivity(intent);
    }

    /**
     * 跳转页面
     *
     * @param activity    当前activity
     * @param targetActivity         所跳转的Activity类
     * @param requestCode 请求码
     */
    public void goTo(Activity activity, Class<?> targetActivity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, targetActivity), requestCode);
    }

    /**
     * 跳转页面
     *
     * @param activity    当前activity
     * @param targetActivity         所跳转的Activity类
     * @param bundle      跳转所携带的信息
     * @param requestCode 请求码
     */
    public void goTo(Activity activity, Class<?> targetActivity, int requestCode, Bundle bundle) {
        Intent intent = new Intent(activity, targetActivity);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }
}
