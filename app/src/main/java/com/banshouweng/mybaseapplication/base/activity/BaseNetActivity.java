package com.banshouweng.mybaseapplication.base.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.base.BaseBean;
import com.banshouweng.mybaseapplication.utils.Logger;
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
import retrofit2.HttpException;

public abstract class BaseNetActivity extends BaseLayoutActivity {

    /**
     * 加载提示框
     */
    private CustomProgressDialog customProgressDialog;

    private NetUtils netUtils;

    protected Map<String, String> params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customProgressDialog = new CustomProgressDialog(activity, R.style.progress_dialog_loading, "玩命加载中。。。");
        netUtils = new NetUtils();
        netUtils.initHeader();
    }

    protected void refreshHeader() {
        netUtils.initHeader();
    }

    /**
     * Get请求
     *
     * @param action     请求接口的尾址
     * @param clazz      要转换的Bean类型（需继承BaseBean）
     * @param showDialog 显示加载进度条
     */
    protected <T extends BaseBean> void get(final String action, Class<T> clazz, boolean showDialog) {
        if (!isNetworkAvailable()) {
            toast("网络异常，请检查网络是否连接");
            error(action, new Exception("网络异常，请检查网络是否连接"));
            return;
        }
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
    protected <T extends BaseBean> void getImage(final String action, boolean showDialog) {
        if (!isNetworkAvailable()) {
            toast("网络异常，请检查网络是否连接");
            error(action, new Exception("网络异常，请检查网络是否连接"));
            return;
        }
        if (showDialog) {
            showLoadDialog();
        }
        if (params == null) {
            params = new HashMap<>();
        }
        netUtils.get(action, params, new MyObserver<>(action, 1));
        params = null;
    }

    /**
     * Post请求
     *
     * @param action     请求接口的尾址
     * @param clazz      要转换的Bean类型（需继承BaseBean）
     * @param showDialog 显示加载进度条
     */
    protected <T extends BaseBean> void post(final String action, Class<T> clazz, boolean showDialog) {
        if (!isNetworkAvailable()) {
            toast("网络异常，请检查网络是否连接");
            error(action, new Exception("网络异常，请检查网络是否连接"));
            return;
        }
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
    protected <T extends BaseBean> void post(final String action, String json, final Class<T> clazz, boolean showDialog) {
        if (!isNetworkAvailable()) {
            toast("网络异常，请检查网络是否连接");
            error(action, new Exception("网络异常，请检查网络是否连接"));
            return;
        }
        if (showDialog) {
            showLoadDialog();
        }
        netUtils.post(action, json, new MyObserver<>(action, clazz));
    }

    /**
     * 访问成功回调抽象方法
     *
     * @param action   网络访问尾址
     * @param baseBean 返回的数据Bean
     */
    protected abstract void success(String action, BaseBean baseBean);

    /**
     * 访问成功回调方法
     *
     * @param action 网络访问尾址
     * @param bitmap 获取的Bitmap
     */
    protected void success(String action, Bitmap bitmap) {
    }

    protected abstract void error(String action, Throwable e);

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
        /**
         * 返回结果状态：0、正常Bean；1、Bitmap
         */
        private int resultStatus = 0;
        private String errorbody;

        MyObserver(String action, Class<T> clazz) {
            this.clazz = clazz;
            this.action = action;
        }

        MyObserver(String action, int resultStatus) {
            this.action = action;
            this.resultStatus = resultStatus;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull ResponseBody responseBody) {
            hideLoadDialog();
            try {
                switch (resultStatus) {
                    case 0:
                        String responseString = responseBody.string();
                        Logger.i("responseString", action + "********** responseString get  " + responseString);
                        success(action, (T) new Gson().fromJson(responseString, clazz));
                        break;

                    case 1:
                        success(action, BitmapFactory.decodeStream(responseBody.byteStream()));
                        Logger.i("responseString", action + "********** 图片获取成功 ");
                        break;

                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(@NonNull Throwable e) {
            try {
                errorbody = ((HttpException) e).response().errorBody().string();
                Logger.i("responseString", String.format("%s********** responseString get error %s content %s", action, e.toString(), TextUtils.isEmpty(errorbody) ? "" : errorbody));
            } catch (IOException | NullPointerException e1) {
                e1.printStackTrace();
            }
            error(action, e);
        }

        @Override
        public void onComplete() {
            params = null;
        }
    }
}