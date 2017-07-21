//package com.banshouweng.mybaseapplication.apimanager;
//
//import com.banshouweng.mybaseapplication.base.BaseBean;
//import com.banshouweng.mybaseapplication.service.MovieService;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * 《一个Android工程的从零开始》
// *
// * @author 半寿翁
// * @博客：
// * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
// * @简书 http://www.jianshu.com/p/1410051701fe
// */
//
//public class Api {
//    //进行网络请求
//    private <T extends BaseBean> void getMovie(Callback<T> callback){
//        String baseUrl = "https://api.douban.com/v2/movie/";
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        MovieService movieService = retrofit.create(MovieService.class);
//        Call<MovieEntity> call = movieService.getTopMovie(0, 10);
//        call.enqueue(new Callback<MovieEntity>() {
//            @Override
//            public void onResponse(Call<MovieEntity> call, Response<MovieEntity> response) {
//            }
//
//            @Override
//            public void onFailure(Call<MovieEntity> call, Throwable t) {
//            }
//        });
//    }
//}
