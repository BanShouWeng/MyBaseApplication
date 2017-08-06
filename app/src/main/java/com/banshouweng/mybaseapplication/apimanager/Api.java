package com.banshouweng.mybaseapplication.apimanager;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */

public class Api {
//    //进行网络请求
//    private void get() {
//        String baseUrl = "https://api.douban.com/v2/movie/";
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build();
//
//        RetrofitGetService movieService = retrofit.create(RetrofitGetService.class);
//
//        if ()
//        Map<String, String> params = new HashMap<>();
//        params.put("start", "0");
//        params.put("count", "10");
//
//        movieService.getTopMovie("top250", params)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ResponseBody>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull ResponseBody responseBody) {
//                        try {
//                            String responseString = responseBody.string();
//                            Log.i("responseString", responseString);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }
}
