package com.banshouweng.bswBase.utils;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class TimerUtils extends CountDownTimer {

    private OnBaseTimerCallBack onBaseTimerCallBack;
    private int viewId = 0;

    /**
     * @param viewId              需要定时器的ViewId
     * @param millisInFuture      时间间隔是多长时间
     * @param countDownInterval   回调onTick方法，每隔多久执行一次
     * @param onBaseTimerCallBack 回调方法
     */
    public TimerUtils(@IdRes int viewId, long millisInFuture, long countDownInterval, OnBaseTimerCallBack onBaseTimerCallBack) {
        super(millisInFuture, countDownInterval);
        this.viewId = viewId;
        this.onBaseTimerCallBack = onBaseTimerCallBack;
    }

    /**
     * @param millisInFuture      时间间隔是多长时间
     * @param countDownInterval   回调onTick方法，每隔多久执行一次
     * @param onBaseTimerCallBack 回调方法
     */
    public TimerUtils(long millisInFuture, long countDownInterval, OnBaseTimerCallBack onBaseTimerCallBack) {
        super(millisInFuture, countDownInterval);
        this.onBaseTimerCallBack = onBaseTimerCallBack;
    }

    //间隔时间内执行的操作
    @Override
    public void onTick(long millisUntilFinished) {
        onBaseTimerCallBack.onTick(viewId, millisUntilFinished);
    }

    //间隔时间结束的时候才会调用
    @Override
    public void onFinish() {
        onBaseTimerCallBack.onFinish(viewId);
    }

    public void stop() {
        mHandler.sendEmptyMessage(0);
    }

    @SuppressLint("han")
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            cancel();
            return false;
        }
    });

    /**
     * 图片二点击回调接口
     */
    public interface OnBaseTimerCallBack {
        /**
         * 间隔时间内执行的操作
         *
         * @param millisUntilFinished 距离结束剩余时间
         */
        void onTick(int viewId, long millisUntilFinished);

        /**
         * 间隔时间结束的时候才会调用
         */
        void onFinish(int viewId);
    }
}
