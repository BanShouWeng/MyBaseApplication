package com.banshouweng.bswBase.widget.BswAlertDialog;

import android.content.Context;
import android.content.DialogInterface;

/**
 * @author leiming
 * @date 2017/10/11
 */
public class BswAlertDialogFactory {

    private static BswAlertDialogFactory factory;

    private BswAlertDialog dialog;

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

    public BswAlertDialogFactory setTitle(int title) {
        dialog.setTitle(title);
        return factory;
    }

    public BswAlertDialogFactory setContent(int content) {
        dialog.setContent(content);
        return factory;
    }

    public BswAlertDialogFactory setCancel(int cancel) {
        dialog.setCancel(cancel);
        return factory;
    }

    public BswAlertDialogFactory setConfirm(int confirm) {
        dialog.setConfirm(confirm);
        return factory;
    }

    private BswAlertDialogFactory(Context context, String tag, OnDialogClickListener listener) {
        dialog = new BswAlertDialog(context, tag, listener);
    }

    public static BswAlertDialogFactory getBswAlertDialog(Context context, String tag, OnDialogClickListener listener) {
        factory = new BswAlertDialogFactory(context, tag, listener);
        return factory;
    }

    public BswAlertDialogFactory onlyMakeSure() {
        dialog.onlyMakeSure();
        return factory;
    }

    public BswAlertDialogFactory setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        dialog.setOnKeyListener(onKeyListener);
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
