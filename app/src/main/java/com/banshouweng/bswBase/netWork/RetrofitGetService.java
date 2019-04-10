package com.banshouweng.bswBase.netWork;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */

public interface RetrofitGetService {
    @GET("{action}")
    Observable<ResponseBody> getResult(@Path("action") String action, @HeaderMap Map<String, String> headerParams, @QueryMap Map<String, Object> params);
}
