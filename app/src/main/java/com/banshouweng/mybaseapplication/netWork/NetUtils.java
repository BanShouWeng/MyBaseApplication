package com.banshouweng.mybaseapplication.netWork;

import android.annotation.SuppressLint;
import android.content.Context;

import com.banshouweng.mybaseapplication.BuildConfig;
import com.banshouweng.mybaseapplication.netWork.RetrofitGetService;
import com.banshouweng.mybaseapplication.netWork.RetrofitPostJsonService;
import com.banshouweng.mybaseapplication.utils.Logger;

import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
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
    private final Context context;
    private Map<String, String> headerParams;

    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

    public NetUtils(Context context) {
        this.context = context;
    }

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

        // https信任管理
        TrustManager[] trustManager = new TrustManager[] {
                new X509TrustManager() {

                    @SuppressLint("TrustAllX509TrustManager")
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[0];
                    }
                }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 请求超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        // 请求参数获取
        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(@android.support.annotation.NonNull Chain chain) throws IOException {
                Request request = chain.request();
                Logger.i("zzz", String.format("request====%s", request.headers().toString()));
                Logger.i("zzz", String.format("request====%s", request.toString()));
                okhttp3.Response proceed = chain.proceed(request);
                Logger.i("zzz", String.format("proceed====%s", proceed.headers().toString()));
                return proceed;
            }
        });
        CookieManager cookieManager = new CookieManager(new InDiskCookieStore(context), CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));

        try {
            // https信任
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManager, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @SuppressLint("BadHostnameVerifier")
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    // 全部信任
                    return true;
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        // 构建Builder，请求结果RxJava接收，使用GSON转化为Bean，
        Retrofit.Builder builder1 = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

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

        if (null == headerParams) {
            headerParams = new HashMap<>();
        }

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
