package com.banshouweng.mybaseapplication.base.fragment;

import android.os.Bundle;
import android.text.TextUtils;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.base.BaseBean;
import com.banshouweng.mybaseapplication.utils.Logger;
import com.banshouweng.mybaseapplication.netWork.NetUtils;
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

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public abstract class BaseNetFragment extends BaseFragment {

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
        netUtils = new NetUtils(context);
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
    public<T extends BaseBean> void get(final String action, Class<T> clazz, boolean showDialog) {
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
    public<T extends BaseBean> void post(final String action, Class<T> clazz, boolean showDialog) {
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
    public<T extends BaseBean> void post(final String action, String json, final Class<T> clazz, boolean showDialog) {
        if (showDialog) {
            showLoadDialog();
        }
        netUtils.post(action, json, new MyObserver(action, clazz));
    }

    public abstract void success(String action, BaseBean baseBean);

    public abstract void error(String action, Throwable e);

    private class MyObserver<T extends BaseBean> implements Observer<ResponseBody> {

        private Class<T> clazz;
        private String action;
        /**
         * 返回结果状态：0、正常Bean；1、Bitmap；2、数据流
         */
        private int resultStatus = 0;
        private String errorbody;

        MyObserver(String action, Class<T> clazz) {
            this.clazz = clazz;
            this.action = action;
        }

        MyObserver(String action, Class<T> clazz, int resultStatus) {
            this.clazz = clazz;
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
                if (e instanceof HttpException) {
                    errorbody = ((HttpException) e).response().errorBody().string();
                    Logger.i("responseString", String.format("%s********** responseString get error %s content %s", action, e.toString(), TextUtils.isEmpty(errorbody) ? "" : errorbody));
                } else {
                    Logger.i("responseString", String.format("%s********** responseString get error %s", action, e.toString()));
                }
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
