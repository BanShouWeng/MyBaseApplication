package com.banshouweng.mybaseapplication.widget.CustomAlertDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class CustomAlertDialogFactory {

    /**
     * 上下文信息
     */
    private Context context;
    /**
     * 确认/取消点击事件接口
     */
    private OnDialogClickListener listener;

    /**
     * 自定义弹窗
     */
    private CustomAlertDialog dialog;

    /**
     * 弹窗构造工程
     */
    @SuppressLint("StaticFieldLeak")
    private static CustomAlertDialogFactory factory;

    /**
     * 设置标题
     *
     * @param title the title
     * @return the title
     */
    public CustomAlertDialogFactory setTitle(String title) {
        dialog.setTitle(title);
        return factory;
    }

    /**
     * 设置信息
     *
     * @param content the content
     * @return the content
     */
    public CustomAlertDialogFactory setContent(String content) {
        dialog.setContent(content);
        return factory;
    }

    /**
     * 设置取消键文本
     *
     * @param cancel the cancel
     * @return the cancel
     */
    public CustomAlertDialogFactory setCancel(String cancel) {
        dialog.setCancel(cancel);
        return factory;
    }

    /**
     * 设置确认键文本
     *
     * @param confirm the confirm
     * @return the confirm
     */
    public CustomAlertDialogFactory setConfirm(String confirm) {
        dialog.setConfirm(confirm);
        return factory;
    }

    private CustomAlertDialogFactory(Context context, String tag, OnDialogClickListener listener) {
        dialog = new CustomAlertDialog(context, tag, listener);
    }

    /**
     * 获取弹窗
     *
     * @param context  上下文
     * @param tag      区分标签
     * @param listener 点击事件点击监听
     * @return 弹窗
     */
    public static CustomAlertDialogFactory getCustomAlertDialog(Context context, String tag, OnDialogClickListener listener) {
        factory = new CustomAlertDialogFactory(context, tag, listener);
        return factory;
    }

    /**
     * 只显示确认键.
     *
     * @return 构造工程
     */
    public CustomAlertDialogFactory onlyMakeSure() {
        dialog.onlyMakeSure();
        return factory;
    }

    public CustomAlertDialogFactory setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        dialog.setOnKeyListener(onKeyListener);
        return factory;
    }

    /**
     * 外部点击禁止关闭
     *
     * @return 构造工程
     */
    public CustomAlertDialogFactory touchOutside() {
        dialog.touchOutside();
        return factory;
    }

    /**
     * 展示弹窗
     *
     * @return 弹窗
     */
    public CustomAlertDialog show() {
        dialog.show();
        return dialog;
    }

    public CustomAlertDialog showNoNetDoalog() {
        return dialog;
    }
}
