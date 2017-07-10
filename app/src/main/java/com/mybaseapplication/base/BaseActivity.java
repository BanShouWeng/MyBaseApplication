package com.mybaseapplication.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mybaseapplication.R;
import com.mybaseapplication.event.NetBroadcastReceiver;
import com.mybaseapplication.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener, NetBroadcastReceiver.NetEvevt {

    /**
     * 网络状态监听接受者
     */
    public static NetBroadcastReceiver.NetEvevt evevt;
    /**
     * 用于传递的上下文信息
     */
    public Activity context;
    /**
     * 是否输出日志信息
     */
    private boolean isDebug = true;
    /**
     * title所需控件
     */
    private ImageView baseBack, baseRightIcon2, baseRightIcon1;
    private TextView baseTitle, baseRightText;
    /**
     * 点击回调方法
     */
    private OnClickRightIcon1CallBack onClickRightIcon1;
    private OnClickRightIcon2CallBack onClickRightIcon2;
    private OnClickRightTextCallBack onClickRightText;

    private static List<Activity> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        if (!(this instanceof MainActivity)) {
            activities.add(this);
        }
        ButterKnife.inject(this);
        context = this;
        initView();
    }

    /**
     * 隐藏返回键
     */
    private void hideBack() {
        baseBack.setVisibility(View.GONE);
    }

    /**
     * 获取返回键
     */
    public ImageView getBaseBack() {
        return baseBack;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        baseRightIcon1 = (ImageView) findViewById(R.id.base_right_icon1);
        baseRightIcon2 = (ImageView) findViewById(R.id.base_right_icon2);
        baseBack = (ImageView) findViewById(R.id.base_back);
        baseTitle = (TextView) findViewById(R.id.base_title);
        baseRightText = (TextView) findViewById(R.id.base_right_text);
        baseBack.setOnClickListener(this);
    }

    /**
     * 设置标题
     *
     * @param title 标题的文本
     */
    public void setTitle(String title) {
        baseTitle.setText(title);
    }

    /**
     * 设置右侧图片1（最右侧）
     *
     * @param resId             图片的资源id
     * @param alertText         提示文本
     * @param onClickRightIcon1 点击处理接口
     */
    public void setBaseRightIcon1(int resId, String alertText, OnClickRightIcon1CallBack onClickRightIcon1) {
        this.onClickRightIcon1 = onClickRightIcon1;
        baseRightIcon1.setImageResource(resId);
        baseRightIcon1.setVisibility(View.VISIBLE);
        baseRightIcon1.setOnClickListener(this);
        //语音辅助提示的时候读取的信息
        baseRightIcon1.setContentDescription(alertText);
    }

    /**
     * 设置右侧图片2（右数第二个图片）
     *
     * @param resId     图片的资源id
     * @param alertText 提示文本
     */
    public void setBaseRightIcon2(int resId, String alertText, OnClickRightIcon2CallBack onClickRightIcon2) {
        this.onClickRightIcon2 = onClickRightIcon2;
        baseRightIcon2.setImageResource(resId);
        baseRightIcon2.setVisibility(View.VISIBLE);
        baseRightIcon2.setOnClickListener(this);
        //语音辅助提示的时候读取的信息
        baseRightIcon2.setContentDescription(alertText);
    }

    /**
     * 设置右侧文本信息
     *
     * @param text 所需要设置的文本
     */
    public void setBaseRightText(String text, OnClickRightTextCallBack onClickRightText) {
        this.onClickRightText = onClickRightText;
        baseRightText.setText(text);
        baseRightText.setVisibility(View.VISIBLE);
        baseRightText.setOnClickListener(this);
    }

    /**
     * 引用头部布局
     *
     * @param layoutId 布局id
     */
    public void setBaseContentView(int layoutId) {
        //当子布局高度值不足ScrollView时，用这个方法可以充满ScrollView，防止布局无法显示
        ((ScrollView) findViewById(R.id.base_scroll_view)).setFillViewport(true);
        LinearLayout layout = (LinearLayout) findViewById(R.id.base_main_layout);

        //获取布局，并在BaseActivity基础上显示
        final View view = getLayoutInflater().inflate(layoutId, null);
        //关闭键盘
        hideKeyBoard();
        //给EditText的父控件设置焦点，防止键盘自动弹出
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layout.addView(view, params);
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyBoard() {
        View view = ((Activity) context).getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按键
            case R.id.base_back:
                finish();
                break;

            //图片1
            case R.id.base_right_icon1:
                onClickRightIcon1.clickRightIcon1();
                break;

            //图片2
            case R.id.base_right_icon2:
                onClickRightIcon2.clickRightIcon2();
                break;

            //右侧文本
            case R.id.base_right_text:
                onClickRightText.clickRightText();
                break;

            default:
                break;
        }
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(this, clz));
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转页面
     *
     * @param clz         所跳转的Activity类
     * @param requestCode 请求码
     */
    public void startActivityForResult(Class<?> clz, int requestCode) {
        startActivityForResult(new Intent(this, clz), requestCode);
    }

    /**
     * 跳转页面
     *
     * @param clz         所跳转的Activity类
     * @param bundle      跳转所携带的信息
     * @param requestCode 请求码
     */
    public void startActivityForResult(Class<?> clz, int requestCode, Bundle bundle) {
        startActivityForResult(new Intent(this, clz), requestCode);
    }

    /**
     * 消息提示框
     *
     * @param message 提示消息文本
     */
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 消息提示框
     *
     * @param messageId 提示消息文本ID
     */
    public void showToast(int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 输出日志
     *
     * @param msg 日志信息
     */
    public void log(String msg) {
        if (isDebug) {
            Log.d("debug", msg);
        }
    }

    /**
     * 关闭所有Activity（除MainActivity以外）
     */
    public void finishActivity() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }

    /**
     * 跳转到指定的Activity
     *
     * @param activity 指定的Activity
     */
    public void goTo(Activity activity) {
        if (activity instanceof MainActivity) {
            finishActivity();
        } else {
            for (int i = activities.size() - 1; i >= 0; i--) {
                if (activity.getClass().equals(activities.get(i).getClass())) {
                    break;
                } else {
                    activity.finish();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activities.remove(this);
    }

    /**
     * 网络变化回调方法
     *
     * @param mobileNetState 当前的网络状态
     */
    @Override
    public void onNetChanged(int mobileNetState) {

    }


    /**
     * 图片一点击回调接口
     */
    public interface OnClickRightIcon1CallBack {
        void clickRightIcon1();
    }

    /**
     * 图片二点击回调接口
     */
    public interface OnClickRightIcon2CallBack {
        void clickRightIcon2();
    }

    /**
     * 右侧文字点击回调接口
     */
    public interface OnClickRightTextCallBack {
        void clickRightText();
    }
}
