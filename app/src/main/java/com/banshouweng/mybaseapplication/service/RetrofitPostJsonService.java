package com.banshouweng.mybaseapplication.service;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitPostJsonService {

    @POST("{action}")
    Observable<ResponseBody> postResult(@Path("action") String action, @HeaderMap Map<String, String> headerParams, @Body RequestBody requestBody);
}
