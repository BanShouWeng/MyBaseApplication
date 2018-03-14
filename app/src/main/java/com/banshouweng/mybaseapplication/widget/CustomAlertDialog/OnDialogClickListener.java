package com.banshouweng.mybaseapplication.widget.CustomAlertDialog;

import android.view.View;

/**
 * 弹窗点击事件监听者
 *
 * @author leiming
 * @date 2017/10/11
 */
public interface OnDialogClickListener {
    /**
     * 点击事件回调
     *
     * @param tag  标签
     * @param view 点击的控件
     */
    void onClick(String tag, View view);
}
