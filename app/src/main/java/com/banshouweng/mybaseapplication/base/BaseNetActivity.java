package com.banshouweng.mybaseapplication.base;

import android.os.Bundle;
import android.util.Log;

import com.banshouweng.mybaseapplication.service.RetrofitGetService;
import com.banshouweng.mybaseapplication.service.RetrofitPostService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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
public class BaseNetActivity extends BaseActivity {
    private String baseUrl = "https://api.douban.com/v2/movie/";
    private RetrofitGetService getService;
    private RetrofitPostService postService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBaseData();
    }

    private void initBaseData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        getService = retrofit.create(RetrofitGetService.class);
        postService = retrofit.create(RetrofitPostService.class);
    }

    //进行网络请求
    public <T extends BaseBean> void get(final String action, final Class<T> clazz, final ResultCallBack callBack) {

        if (params == null) {
            params = new HashMap<>();
        }
        params.put("start", "0");
        params.put("count", "10");

        getService.getResult("top250", params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            String responseString = responseBody.string();
                            Log.i("responseString", "responseString get  " + responseString);
                            callBack.success(action, new Gson().fromJson(responseBody.string(), clazz));
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

    //进行网络请求
    public <T extends BaseBean> void post(final String action, final Class<T> clazz, final ResultCallBack callBack) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put("start", "0");
        params.put("count", "10");

        postService.postResult("top250", params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            String responseString = responseBody.string();
                            Log.i("responseString", "responseString post  " + responseString);
                            callBack.success(action, new Gson().fromJson(responseBody.string(), clazz));
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

    public interface ResultCallBack<T extends BaseBean> {
        void success(String action, T t);

        void error(String action, Throwable e);
    }
}
