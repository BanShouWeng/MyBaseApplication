package com.banshouweng.mybaseapplication.factory;

import android.content.Context;

/**
 * @author leiming
 * @date 2017/10/11
 */
public class BswAlertDialogFactory {

    private Context context;
    private OnDialogClickListener listener;

    private BswAlertDialog dialog;
    private static BswAlertDialogFactory factory;

    public BswAlertDialogFactory setTitle(String title) {
        dialog.setTitle(title);
        return factory;
    }

    public BswAlertDialogFactory setContent(String content) {
        dialog.setContent(content);
        return factory;
    }

    public BswAlertDialogFactory setCancel(String cancel) {
        dialog.setCancel(cancel);
        return factory;
    }

    public BswAlertDialogFactory setConfirm(String confirm) {
        dialog.setConfirm(confirm);
        return factory;
    }

    public BswAlertDialogFactory setTitle(int title) throws Exception{
        dialog.setTitle(context.getResources().getString(title));
        return factory;
    }

    public BswAlertDialogFactory setContent(int content) throws Exception{
        dialog.setContent(context.getResources().getString(content));
        return factory;
    }

    public BswAlertDialogFactory setCancel(int cancel) throws Exception{
        dialog.setCancel(context.getResources().getString(cancel));
        return factory;
    }

    public BswAlertDialogFactory setConfirm(int confirm) throws Exception{
        dialog.setConfirm(context.getResources().getString(confirm));
        return factory;
    }

    private BswAlertDialogFactory(Context context, String tag, OnDialogClickListener listener) {
        this.context = context;
        dialog = new BswAlertDialog(context, tag, listener);
    }

    public static BswAlertDialogFactory getCustomAlertDialog(Context context, String tag, OnDialogClickListener listener) {
        factory = new BswAlertDialogFactory(context, tag, listener);
        return factory;
    }

    public BswAlertDialogFactory onlyMakeSure() {
        dialog.onlyMakeSure();
        return factory;
    }

    public BswAlertDialogFactory touchOutside() {
        dialog.touchOutside();
        return factory;
    }

    public BswAlertDialog show() {
        dialog.show();
        return dialog;
    }

}
