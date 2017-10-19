package com.banshouweng.mybaseapplication.utils;

import com.banshouweng.mybaseapplication.BuildConfig;
import com.banshouweng.mybaseapplication.service.RetrofitGetService;
import com.banshouweng.mybaseapplication.service.RetrofitPostJsonService;

import org.json.JSONObject;

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
    private Map<String, String> headerParams;

    /**
     * 初始化请求头，具体情况根据需求设置
     */
    public void initHeader() {
        headerParams = new HashMap<>();
        // 传参方式为json
        headerParams.put("Content-Type", "application/json");
    }

    /**
     * 初始化数据
     *
     * @param action 当前请求的尾址
     */
    private Retrofit initBaseData(final String action) {
        // 监听请求条件
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Logger.i("zzz", "request====" + action);
                Logger.i("zzz", "request====" + request.headers().toString());
                Logger.i("zzz", "request====" + request.toString());
                okhttp3.Response proceed = chain.proceed(request);
                Logger.i("zzz", "proceed====" + proceed.headers().toString());
                return proceed;
            }
        });

        Retrofit.Builder builder1 = new Retrofit.Builder()
                .client(builder.build())                                    // 配置监听请求
                .addConverterFactory(GsonConverterFactory.create())         // 请求结果转换（当前为GSON）
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()); // 请求接受工具（当前为RxJava2）
        builder1.baseUrl(BuildConfig.BASE_URL + action.substring(0, action.lastIndexOf("/") + 1));

        return builder1.build();
    }

    /**
     * Get请求
     *
     * @param action   请求接口的尾址
     * @param params   索要传递的参数
     * @param observer 求情观察者
     */
    public void get(final String action, Map<String, String> params, Observer<ResponseBody> observer) {
        RetrofitGetService getService = initBaseData(action).create(RetrofitGetService.class);
        if (params == null) {
            params = new HashMap<>();
        }

        Map<String, String> headerParams = new HashMap<>();
        headerParams.put("USER_TOKEN", "aaa");
        headerParams.put("DEVICE_ID", "bbbbb");
        headerParams.put("Content-Type", "application/json");
        headerParams.put("SYSTEM_NAME", "smallPlace");

        Logger.i("zzz", "request====" + new JSONObject(params));

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
        RetrofitPostJsonService jsonService = initBaseData(action).create(RetrofitPostJsonService.class);
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                        json);

        Logger.i("zzz", "request====" + json);

        jsonService.postResult(action.substring(action.lastIndexOf("/") + 1), requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
