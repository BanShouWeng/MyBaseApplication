package com.banshouweng.mybaseapplication.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */

public interface MovieService {
    @GET("top250")
    Call<ResponseBody> getTopMovie(@Query("start") int start, @Query("count") int count);
}
