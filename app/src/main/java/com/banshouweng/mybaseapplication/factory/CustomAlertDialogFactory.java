package com.banshouweng.mybaseapplication.factory;

import android.content.Context;

/**
 * @author leiming
 * @date 2017/10/11
 */
public class CustomAlertDialogFactory {

    private Context context;
    private OnDialogClickListener listener;

    private CustomAlertDialog dialog;
    private static CustomAlertDialogFactory factory;

    public CustomAlertDialogFactory setTitle(String title) {
        dialog.setTitle(title);
        return factory;
    }

    public CustomAlertDialogFactory setContent(String content) {
        dialog.setContent(content);
        return factory;
    }

    public CustomAlertDialogFactory setCancel(String cancel) {
        dialog.setCancel(cancel);
        return factory;
    }

    public CustomAlertDialogFactory setConfirm(String confirm) {
        dialog.setConfirm(confirm);
        return factory;
    }

    private CustomAlertDialogFactory(Context context, String tag, OnDialogClickListener listener) {
        dialog = new CustomAlertDialog(context, tag, listener);
    }

    public static CustomAlertDialogFactory getCustomAlertDialog(Context context, String tag, OnDialogClickListener listener) {
        factory = new CustomAlertDialogFactory(context, tag, listener);
        return factory;
    }

    public CustomAlertDialogFactory onlyMakeSure() {
        dialog.onlyMakeSure();
        return factory;
    }

    public CustomAlertDialogFactory touchOutside() {
        dialog.touchOutside();
        return factory;
    }

    public CustomAlertDialog show() {
        dialog.show();
        return dialog;
    }

}
