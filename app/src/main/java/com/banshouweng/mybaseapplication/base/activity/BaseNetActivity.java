package com.banshouweng.mybaseapplication.base.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.base.BaseBean;
import com.banshouweng.mybaseapplication.bean.BitmapBean;
import com.banshouweng.mybaseapplication.utils.LogUtil;
import com.banshouweng.mybaseapplication.utils.NetUtils;
import com.banshouweng.mybaseapplication.widget.CustomProgressDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public abstract class BaseNetActivity extends BaseActivity {

    /**
     * 加载提示框
     */
    private CustomProgressDialog customProgressDialog;

    private NetUtils netUtils;

    public Map<String, String> params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customProgressDialog = new CustomProgressDialog(activity, R.style.progress_dialog_loading, "玩命加载中。。。");
        netUtils = new NetUtils();
        netUtils.initHeader();
    }

    public void refreshHeader() {
        netUtils.initHeader();
    }

    /**
     * Get请求
     *
     * @param action     请求接口的尾址
     * @param clazz      要转换的Bean类型（需继承BaseBean）
     * @param showDialog 显示加载进度条
     */
    public <T extends BaseBean> void get(final String action, Class<T> clazz, boolean showDialog) {
        if (showDialog) {
            showLoadDialog();
        }
        if (params == null) {
            params = new HashMap<>();
        }
        netUtils.get(action, params, new MyObserver<>(action, clazz));
        params = null;
    }

    /**
     * Get请求
     *
     * @param action     请求接口的尾址
     * @param showDialog 显示加载进度条
     */
    public <T extends BaseBean> void getImage(final String action, boolean showDialog) {
        if (showDialog) {
            showLoadDialog();
        }
        if (params == null) {
            params = new HashMap<>();
        }
        netUtils.get(action, params, new ImageObserver(action));
        params = null;
    }

    /**
     * Post请求
     *
     * @param action     请求接口的尾址
     * @param clazz      要转换的Bean类型（需继承BaseBean）
     * @param showDialog 显示加载进度条
     */
    public <T extends BaseBean> void post(final String action, Class<T> clazz, boolean showDialog) {
        if (showDialog) {
            showLoadDialog();
        }
        if (params == null) {
            params = new HashMap<>();
        }
        netUtils.post(action, String.valueOf(new JSONObject(params)), new MyObserver<>(action, clazz));
        params = null;
    }

    /**
     * Post请求
     *
     * @param action     请求接口的尾址
     * @param clazz      要转换的Bean类型（需继承BaseBean）
     * @param showDialog 显示加载进度条
     */
    public <T extends BaseBean> void post(final String action, String json, final Class<T> clazz, boolean showDialog) {
        if (showDialog) {
            showLoadDialog();
        }
        netUtils.post(action, json, new MyObserver<>(action, clazz));
    }

    public abstract void success(String action, BaseBean baseBean);

    public abstract void error(String action, Throwable e);

    /**
     * 显示加载提示框
     */
    private void showLoadDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customProgressDialog.show();
            }
        });
    }

    /**
     * 隐藏加载提示框
     */
    private void hideLoadDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (customProgressDialog != null && customProgressDialog.isShowing()) {
                    customProgressDialog.dismiss();
                }
            }
        });
    }

    private class MyObserver<T extends BaseBean> implements Observer<ResponseBody> {
        private Class<T> clazz;
        private String action;

        MyObserver(String action, Class<T> clazz) {
            this.clazz = clazz;
            this.action = action;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull ResponseBody responseBody) {
            hideLoadDialog();
            try {
                String responseString = responseBody.string();
                LogUtil.i("responseString", action + "********** responseString get  " + responseString);
                success(action, (T)new Gson().fromJson(responseString, clazz));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            LogUtil.i("responseString", "responseString get  " + e.toString());
            error(action, e);
        }

        @Override
        public void onComplete() {
            params = null;
        }
    }

    private class ImageObserver implements Observer<ResponseBody> {
        private String action;

        ImageObserver(String action) {
            this.action = action;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull ResponseBody responseBody) {
            hideLoadDialog();
            success(action, new BitmapBean(BitmapFactory.decodeStream(responseBody.byteStream())));
        }

        @Override
        public void onError(@NonNull Throwable e) {
            LogUtil.i("responseString", "responseString get  " + e.toString());
            error(action, e);
        }

        @Override
        public void onComplete() {
            params = null;
        }
    }
}
