package com.banshouweng.mybaseapplication.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.banshouweng.mybaseapplication.R;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public abstract class BaseViewFragment extends BaseNetFragment {

    private ImageView baseBack;
    private RelativeLayout baseTitleLayout;

    /**
     * 隐藏头布局
     */
    public void hideTitle() {
        baseTitleLayout.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isNoTitle = false;
        setBaseContentView(getLayoutId());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context.getApplicationContext();
        activity = (Activity) context;
    }

    /**
     * 隐藏返回键
     */
    private void hideBack() {
        baseBack.setVisibility(View.GONE);
    }

    /**
     * 设置标题
     *
     * @param title 标题的文本
     */
    public void setTitle(String title) {
        ((TextView) getView(R.id.base_title)).setText(title);
    }

    /**
     * 设置返回点击事件
     *
     * @param clickListener 点击事件监听者
     */
    public void setBaseBack(View.OnClickListener clickListener) {
        if (clickListener == null) {
            baseBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        } else {
            baseBack.setOnClickListener(clickListener);
        }
    }

    /**
     * 最右侧图片功能键设置方法
     *
     * @param resId         图片id
     * @param alertText     语音辅助提示读取信息
     * @param clickListener 点击事件
     * @return 将当前ImageView返回方便进一步处理
     */
    public ImageView setBaseRightIcon1(int resId, String alertText, View.OnClickListener clickListener) {
        ImageView baseRightIcon1 = getView(R.id.base_right_icon1);
        baseRightIcon1.setImageResource(resId);
        baseRightIcon1.setVisibility(View.VISIBLE);
        //语音辅助提示的时候读取的信息
        baseRightIcon1.setContentDescription(alertText);
        baseRightIcon1.setOnClickListener(clickListener);
        return baseRightIcon1;
    }

    /**
     * 右数第二个图片功能键设置方法
     *
     * @param resId         图片id
     * @param alertText     语音辅助提示读取信息
     * @param clickListener 点击事件
     * @return 将当前ImageView返回方便进一步处理
     */
    public ImageView setBaseRightIcon2(int resId, String alertText, View.OnClickListener clickListener) {
        ImageView baseRightIcon2 = getView(R.id.base_right_icon2);
        baseRightIcon2.setImageResource(resId);
        baseRightIcon2.setVisibility(View.VISIBLE);
        //语音辅助提示的时候读取的信息
        baseRightIcon2.setContentDescription(alertText);
        baseRightIcon2.setOnClickListener(clickListener);
        return baseRightIcon2;
    }

    /**
     * 最右侧文本功能键设置方法
     *
     * @param text          文本信息
     * @param clickListener 点击事件
     * @return 将当前TextView返回方便进一步处理
     */
    public TextView setBaseRightText(String text, View.OnClickListener clickListener) {
        TextView baseRightText = getView(R.id.base_right_text);
        baseRightText.setText(text);
        baseRightText.setVisibility(View.VISIBLE);
        baseRightText.setOnClickListener(clickListener);
        return baseRightText;
    }

    /**
     * 最右侧文本功能键设置方法
     *
     * @param textId        文本信息id
     * @param clickListener 点击事件
     * @return 将当前TextView返回方便进一步处理
     */
    public TextView setBaseRightText(int textId, View.OnClickListener clickListener) {
        TextView baseRightText = getView(R.id.base_right_text);
        baseRightText.setText(textId);
        baseRightText.setVisibility(View.VISIBLE);
        baseRightText.setOnClickListener(clickListener);
        return baseRightText;
    }

    /**
     * 引用头部布局
     *
     * @param layoutId 布局id
     */
    private void setBaseContentView(int layoutId) {
        LinearLayout layout = getView(R.id.base_main_layout);

        //获取布局，并在BaseActivity基础上显示
        final View view = activity.getLayoutInflater().inflate(layoutId, null);
        //关闭键盘
        hideKeyBoard();
        //给EditText的父控件设置焦点，防止键盘自动弹出
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layout.addView(view, params);
    }
}
