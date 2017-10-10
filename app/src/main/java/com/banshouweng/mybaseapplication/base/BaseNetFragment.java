package com.banshouweng.mybaseapplication.base;

import android.os.Bundle;

import com.banshouweng.mybaseapplication.R;
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

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public abstract class BaseNetFragment<T extends BaseBean> extends BaseFragment {

    /**
     * 加载提示框
     */
    private CustomProgressDialog customProgressDialog;
    private NetUtils netUtils;
    public Map<String, String> params;


    @Override
    public void onCreate(Bundle savedInstanceState) {
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
    public void get(final String action, Class<T> clazz, boolean showDialog) {
        if (showDialog) {
            showLoadDialog();
        }
        if (params == null) {
            params = new HashMap<>();
        }
        netUtils.get(action, params, new MyObserver(action, clazz));
        params = null;
    }

    /**
     * Post请求
     *
     * @param action     请求接口的尾址
     * @param clazz      要转换的Bean类型（需继承BaseBean）
     * @param showDialog 显示加载进度条
     */
    public void post(final String action, Class<T> clazz, boolean showDialog) {
        if (showDialog) {
            showLoadDialog();
        }
        if (params == null) {
            params = new HashMap<>();
        }
        netUtils.post(action, String.valueOf(new JSONObject(params)), new MyObserver(action, clazz));
        params = null;
    }

    /**
     * Post请求
     *
     * @param action     请求接口的尾址
     * @param clazz      要转换的Bean类型（需继承BaseBean）
     * @param showDialog 显示加载进度条
     */
    public void post(final String action, String json, final Class<T> clazz, boolean showDialog) {
        if (showDialog) {
            showLoadDialog();
        }
        netUtils.post(action, json, new MyObserver(action, clazz));
    }

    public abstract void success(String action, BaseBean baseBean);

    public abstract void error(String action, Throwable e);

    class MyObserver implements Observer<ResponseBody> {
        private Class<T> clazz;
        private String action;

        public MyObserver(String action, Class<T> clazz) {
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
                success(action, new Gson().fromJson(responseString, clazz));
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

    /**
     * 显示加载提示框
     */
    public void showLoadDialog() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customProgressDialog.show();
            }
        });
    }

    /**
     * 隐藏加载提示框
     */
    public void hideLoadDialog() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (customProgressDialog != null && customProgressDialog.isShowing()) {
                    customProgressDialog.dismiss();
                }
            }
        });
    }
}
