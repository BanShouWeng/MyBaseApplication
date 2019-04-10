package com.banshouweng.bswBase.widget.BswAlertDialog;

import android.view.View;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
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
