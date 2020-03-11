package com.qgyyzs.globalcosmetics.http.Api;

import com.qgyyzs.globalcosmetics.http.retrofit.HttpResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public interface ZhongbiaoApi {
    @FormUrlEncoded
    @POST("product/zhongbiao_get_list")
    Observable<HttpResponse> getBid(@Field("jsonstr") String parm);
}
