package com.banshouweng.bswBase.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * @author leiming
 * @date 2018/6/21.
 */

public class ScreenUtil {
    /**
     * 获取屏幕宽度，单位为px
     *
     * @return 屏幕宽度，单位px
     */
    public int getScreenWidth(Context mContext) {
        return getDisplayMetrics(mContext, false).widthPixels;
    }

    /**
     * 获取屏幕宽度，单位为px
     *
     * @return 屏幕宽度，单位px
     */
    public int getScreenWidth(Activity activity) {
        return getDisplayMetrics(activity, true).widthPixels;
    }

    /**
     * 获取屏幕高度，单位为px
     *
     * @return 屏幕高度，单位px
     */
    public int getScreenHeight(Activity activity) {
        return getDisplayMetrics(activity, true).heightPixels;
    }

    /**
     * 获取系统dp尺寸密度值
     *
     * @return
     */
    public float getDensity(Context mContext) {
        return getDisplayMetrics(mContext, false).density;
    }

    /**
     * 获取系统字体sp密度值
     *
     * @return
     */
    public float getScaledDensity(Context mContext) {
        return getDisplayMetrics(mContext, false).scaledDensity;
    }

    /**
     * dip转换为px大小
     *
     * @param dpValue dp值
     * @return 转换后的px值
     */
    public int dp2px(Context mContext, int dpValue) {
        return (int) (dpValue * getDensity(mContext) + 0.5f);
    }

    /**
     * px转换为dp值
     *
     * @param pxValue px值
     * @return 转换后的dp值
     */
    public int px2dp(Context mContext, int pxValue) {
        return (int) (pxValue / getDensity(mContext) + 0.5f);
    }

    /**
     * sp转换为px
     *
     * @param spValue sp值
     * @return 转换后的px值
     */
    public int sp2px(Context mContext, int spValue) {
        return (int) (spValue * getScaledDensity(mContext) + 0.5f);
    }

    /**
     * px转换为sp
     *
     * @param pxValue px值
     * @return 转换后的sp值
     */
    public int px2sp(Context mContext, int pxValue) {
        return (int) (pxValue / getScaledDensity(mContext) + 0.5f);
    }

    /**
     * 获得状态栏的高度
     *
     * @return
     */
    public int getStatusHeight(Context mContext) {
        int statusHeight = - 1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = mContext.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public Bitmap snapShotWithStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bitmap = null;
        bitmap = Bitmap.createBitmap(bmp, 0, 0, width, height);
        decorView.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public Bitmap snapShotWithoutStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap bmp = decorView.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bitmap = null;
        bitmap = Bitmap.createBitmap(bmp, 0, statusHeight, width, height - statusHeight);
        decorView.destroyDrawingCache();
        return bitmap;
    }

    public DisplayMetrics getDisplayMetrics(Context context, boolean isActivity) {
        if (CommonUtils.isEmpty(context)) {
            throw new IllegalArgumentException("context is null");
        }
        WindowManager manager;
        if (isActivity) {
            Activity activity = ((Activity) context);
            manager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        } else {
            manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }
}
