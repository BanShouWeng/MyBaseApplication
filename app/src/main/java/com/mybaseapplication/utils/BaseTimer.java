package com.mybaseapplication.utils;

import android.os.CountDownTimer;

/**
 * Created by dell on 2017/7/10.
 */

public class BaseTimer extends CountDownTimer {

    private OnBaseTimerCallBack onBaseTimerCallBack;

    /**
     * @param millisInFuture    时间间隔是多长时间
     * @param countDownInterval 回调onTick方法，每隔多久执行一次
     */
    public BaseTimer(long millisInFuture, long countDownInterval, OnBaseTimerCallBack onBaseTimerCallBack) {
        super(millisInFuture, countDownInterval);
        this.onBaseTimerCallBack = onBaseTimerCallBack;
    }

    //间隔时间内执行的操作
    @Override
    public void onTick(long millisUntilFinished) {
        onBaseTimerCallBack.onTick(millisUntilFinished);
    }

    //间隔时间结束的时候才会调用
    @Override
    public void onFinish() {
        onBaseTimerCallBack.onFinish();
    }

    /**
     * 图片二点击回调接口
     */
    public interface OnBaseTimerCallBack {
        /**
         * 间隔时间内执行的操作
         *
         * @param millisUntilFinished 距离结束剩余时间
         */
        void onTick(long millisUntilFinished);

        /**
         * 间隔时间结束的时候才会调用
         */
        void onFinish();
    }
}
