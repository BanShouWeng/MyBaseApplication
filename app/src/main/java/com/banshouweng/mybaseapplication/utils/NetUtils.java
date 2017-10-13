package com.banshouweng.mybaseapplication.utils;

import com.banshouweng.mybaseapplication.BuildConfig;
import com.banshouweng.mybaseapplication.service.RetrofitGetService;
import com.banshouweng.mybaseapplication.service.RetrofitPostJsonService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */
public class NetUtils {
    private RetrofitGetService getService;
    private RetrofitPostJsonService jsonService;
    private Retrofit retrofit;
    private Map<String, String> headerParams;

    public void initHeader() {
        headerParams = new HashMap<>();
        headerParams.put("Content-Type", "application/json");
    }

    private void initBaseData(final String action) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                LogUtil.i("zzz", "request====" + action);
                LogUtil.i("zzz", "request====" + request.headers().toString());
                LogUtil.i("zzz", "request====" + request.toString());
                okhttp3.Response proceed = chain.proceed(request);
                LogUtil.i("zzz", "proceed====" + proceed.headers().toString());
                return proceed;
            }
        });

        Retrofit.Builder builder1 = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        builder1.baseUrl(BuildConfig.BASE_URL + action.substring(0, action.lastIndexOf("/") + 1));

        retrofit = builder1.build();
    }

    /**
     * Get请求
     *
     * @param action   请求接口的尾址
     * @param params   索要传递的参数
     * @param observer 求情观察者
     */
    public void get(final String action, Map<String, String> params, Observer<ResponseBody> observer) {
        initBaseData(action);

        if (getService == null) {
            getService = retrofit.create(RetrofitGetService.class);
        }
        if (params == null) {
            params = new HashMap<>();
        }

        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("USER_TOKEN", "aaa");
        headerParams.put("DEVICE_ID", "bbbbb");
        headerParams.put("Content-Type", "application/json");
        headerParams.put("SYSTEM_NAME", "smallPlace");

        getService.getResult(action.substring(action.lastIndexOf("/") + 1), headerParams, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * Post请求
     *
     * @param action   请求接口的尾址
     * @param observer 求情观察者
     */
    public void post(final String action, String json, Observer<ResponseBody> observer) {
        initBaseData(action);
        if (jsonService == null) {
            jsonService = retrofit.create(RetrofitPostJsonService.class);
        }
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        json);

        jsonService.postResult(action.substring(action.lastIndexOf("/") + 1), requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
