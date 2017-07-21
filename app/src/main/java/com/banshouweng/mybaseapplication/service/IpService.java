package com.banshouweng.mybaseapplication.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 《一个Android工程的从零开始》
 *
 * @author 半寿翁
 * @博客：
 * @CSDN http://blog.csdn.net/u010513377/article/details/74455960
 * @简书 http://www.jianshu.com/p/1410051701fe
 */

public interface IpService {
    @FormUrlEncoded
    @POST("getIpInfo.php")
//    1.4.通过@Field来指定key，后面跟上value
    Observable<ResponseBody> postIP(@Field("ip") String ip);
}
