package com.banshouweng.mybaseapplication.ui.activity;

import android.os.Bundle;
import android.util.Log;

import com.banshouweng.mybaseapplication.R;
import com.banshouweng.mybaseapplication.base.BaseActivity;
import com.banshouweng.mybaseapplication.base.BaseNetActivity;
import com.banshouweng.mybaseapplication.service.IpService;
import com.banshouweng.mybaseapplication.service.Movie2Service;
import com.banshouweng.mybaseapplication.service.MovieService;
import com.banshouweng.mybaseapplication.ui.fragment.MineFragment;
import com.banshouweng.mybaseapplication.utils.LogUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
public class MainActivity extends BaseActivity {

    private MineFragment mineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_main);

        //设置title文本
        setTitle("MainActivity");
        //设置返回拦截
        setBaseBack(new OnClickBackCallBack() {
            @Override
            public void clickBack() {
                startActivity(BaseNetActivity.class);
            }
        });
        mineFragment = new MineFragment();
        //设置功能键，以及点击方法回调监听
        setBaseRightIcon1(R.mipmap.more, "更多", new OnClickRightIcon1CallBack() {
            @Override
            public void clickRightIcon1() {
                showFragment(mineFragment);
            }
        });
        setBaseRightIcon2(R.mipmap.add, "更多", new OnClickRightIcon2CallBack() {
            @Override
            public void clickRightIcon2() {
                getMovie2();
            }
        });

        addFragment(R.id.fragment_layout, mineFragment);
//        hideTitle();
        getMovie2();
    }

    //进行网络请求
    private void getMovie() {
        String baseUrl = "https://api.douban.com/v2/movie/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService movieService = retrofit.create(MovieService.class);
        Call<ResponseBody> call = movieService.getTopMovie(0, 2);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.i("response", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    //进行网络请求
    private void getMovie2() {
        String baseUrl = "https://api.douban.com/v2/movie/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        Movie2Service movieService = retrofit.create(Movie2Service.class);

        Map<String, String> params = new HashMap<>();
        params.put("start", "0");
        params.put("count", "10");

        movieService.getTopMovie("top250", params)
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
                            Log.i("responseString", responseString);
                            LogUtil.info("response", responseString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //进行网络请求
    private void postTaobao() {
        String baseUrl = "http://192.168.31.242:8080/springmvc_users/user/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        IpService ipService = retrofit.create(IpService.class);

        ipService.postIP("202.202.32.202")
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
                            Log.i("responseString", responseString);
                            LogUtil.info("response", responseString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
