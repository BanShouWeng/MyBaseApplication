package com.banshouweng.mybaseapplication.base;

import android.os.Bundle;

import com.banshouweng.mybaseapplication.reciever.ResultCallBack;
import com.banshouweng.mybaseapplication.service.RetrofitGetService;
import com.banshouweng.mybaseapplication.service.RetrofitPostJsonService;
import com.banshouweng.mybaseapplication.utils.LogUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseNetActivity extends BaseActivity {
    private String baseUrl = "https://api.douban.com/v2/movie/";
    private RetrofitGetService getService;
    private RetrofitPostJsonService jsonService;
    private Retrofit retrofit;
    private Map<String, String> headerParams;
    private String action = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initHeader();
    }

    public void refreshHeader() {
        initHeader();
    }

    private void initHeader() {
        headerParams = new HashMap<>();
        headerParams.put("Content-Type", "application/json");
    }

    private void initBaseData(final String url, boolean isFromLogin) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                LogUtil.info("zzz", "request====" + action);
                LogUtil.info("zzz", "request====" + request.headers().toString());
                LogUtil.info("zzz", "request====" + request.toString());
                okhttp3.Response proceed = chain.proceed(request);
                LogUtil.info("zzz", "proceed====" + proceed.headers().toString());
                return proceed;
            }
        });

        Retrofit.Builder builder1 = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            builder1.baseUrl(baseUrl + url);

        retrofit = builder1.build();
    }

    /**
     * Get请求
     *
     * @param action     请求接口的尾址
     * @param clazz      要转换的Bean类型（需继承BaseBean）
     * @param showDialog 显示加载进度条
     * @param callBack   结果回调接口
     * @param <T>        用于继承BaseBean的占位变量
     */
    public <T extends BaseBean> void get(final String action, final Class<T> clazz, boolean showDialog, final ResultCallBack callBack) {
        this.action = action;
        if (showDialog) {
            showLoadDialog();
        }
        String action1 = action.substring(action.lastIndexOf("/") + 1);
        String action2 = action.substring(0, action.lastIndexOf("/") + 1);
        initBaseData(action2, false);

        if (getService == null) {
            getService = retrofit.create(RetrofitGetService.class);
        }
        if (params == null) {
            params = new HashMap<>();
        }

        getService.getResult(action1, headerParams, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        hideLoadDialog();
                        try {
                            String responseString = responseBody.string();
                            LogUtil.info("responseString", action + "********** responseString get  " + responseString);
                            callBack.success(action, new Gson().fromJson(responseString, clazz));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.info("responseString", "responseString get  " + e.toString());
                        callBack.error(action, e);
                    }

                    @Override
                    public void onComplete() {
                        params = null;
                    }
                });
    }

    /**
     * Post请求
     *
     * @param action     请求接口的尾址
     * @param clazz      要转换的Bean类型（需继承BaseBean）
     * @param showDialog 显示加载进度条
     * @param callBack   结果回调接口
     * @param <T>        用于继承BaseBean的占位变量
     */
    public <T extends BaseBean> void post(final String action, final Class<T> clazz, boolean showDialog, final ResultCallBack callBack) {
        this.action = action;
        if (showDialog) {
            showLoadDialog();
        }
        String action1 = action.substring(action.lastIndexOf("/") + 1);
        String action2 = action.substring(0, action.lastIndexOf("/") + 1);
        initBaseData(action2, false);
        if (jsonService == null) {
            jsonService = retrofit.create(RetrofitPostJsonService.class);
        }

        if (params == null) {
            params = new HashMap<>();
        }

        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        String.valueOf(new JSONObject(params)));

        jsonService.postResult(action1, headerParams, requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        hideLoadDialog();
                        try {
                            String responseString = responseBody.string();
                            LogUtil.info("responseString", action + "********** responseString post  " + responseString);
                            callBack.success(action, new Gson().fromJson(responseString, clazz));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBack.error(action, e);
                    }

                    @Override
                    public void onComplete() {
                        params = null;
                    }
                });
    }

    /**
     * Get请求
     *
     * @param action     请求接口的尾址
     * @param clazz      要转换的Bean类型（需继承BaseBean）
     * @param showDialog 显示加载进度条
     * @param callBack   结果回调接口
     * @param <T>        用于继承BaseBean的占位变量
     */
    public <T extends BaseBean> void getLogin(final String action, final Class<T> clazz, boolean showDialog, final ResultCallBack callBack) {
        this.action = action;
        if (showDialog) {
            showLoadDialog();
        }
        String action1 = action.substring(action.lastIndexOf("/") + 1);
        String action2 = action.substring(0, action.lastIndexOf("/") + 1);
        initBaseData(action2, true);

        if (getService == null) {
            getService = retrofit.create(RetrofitGetService.class);
        }
        if (params == null) {
            params = new HashMap<>();
        }

        getService.getResult(action1, headerParams, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        hideLoadDialog();
                        try {
                            String responseString = responseBody.string();
                            LogUtil.info("responseString", "responseString get  " + responseString);
                            callBack.success(action, new Gson().fromJson(responseString, clazz));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.info("responseString", action + "********** responseString get  " + e.toString());
                        callBack.error(action, e);
                    }

                    @Override
                    public void onComplete() {
                        params = null;
                    }
                });
    }

    /**
     * Post请求
     *
     * @param action     请求接口的尾址
     * @param clazz      要转换的Bean类型（需继承BaseBean）
     * @param showDialog 显示加载进度条
     * @param callBack   结果回调接口
     * @param <T>        用于继承BaseBean的占位变量
     */
    public <T extends BaseBean> void postLogin(final String action, final Class<T> clazz, boolean showDialog, final ResultCallBack callBack) {
        this.action = action;
        if (showDialog) {
            showLoadDialog();
        }
        String action1 = action.substring(action.lastIndexOf("/") + 1);
        String action2 = action.substring(0, action.lastIndexOf("/") + 1);
        initBaseData(action2, true);
        if (jsonService == null) {
            jsonService = retrofit.create(RetrofitPostJsonService.class);
        }

        if (params == null) {
            params = new HashMap<>();
        }

        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        String.valueOf(new JSONObject(params)));

        jsonService.postResult(action1, headerParams, requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        hideLoadDialog();
                        try {
                            String responseString = responseBody.string();
                            LogUtil.info("responseString", action + "********** responseString post  " + responseString);
                            callBack.success(action, new Gson().fromJson(responseString, clazz));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBack.error(action, e);
                    }

                    @Override
                    public void onComplete() {
                        params = null;
                    }
                });
    }

    /**
     * Post请求
     *
     * @param action     请求接口的尾址
     * @param clazz      要转换的Bean类型（需继承BaseBean）
     * @param showDialog 显示加载进度条
     * @param callBack   结果回调接口
     * @param <T>        用于继承BaseBean的占位变量
     */
    public <T extends BaseBean> void post(final String action, String json, final Class<T> clazz, boolean showDialog, final ResultCallBack callBack) {
        this.action = action;
        if (showDialog) {
            showLoadDialog();
        }
        String action1 = action.substring(action.lastIndexOf("/") + 1);
        String action2 = action.substring(0, action.lastIndexOf("/") + 1);
        initBaseData(action2, false);
        if (jsonService == null) {
            jsonService = retrofit.create(RetrofitPostJsonService.class);
        }

        if (params == null) {
            params = new HashMap<>();
        }

        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        json);

        jsonService.postResult(action1, headerParams, requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        hideLoadDialog();
                        try {
                            String responseString = responseBody.string();
                            LogUtil.info("responseString", action + "********** responseString post  " + responseString);
                            callBack.success(action, new Gson().fromJson(responseString, clazz));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBack.error(action, e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void success(String action, BaseBean baseBean){

    }

    public void error(String action, Throwable e){

    }
}
