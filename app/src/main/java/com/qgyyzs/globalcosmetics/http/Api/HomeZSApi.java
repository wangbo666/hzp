package com.qgyyzs.globalcosmetics.http.Api;

import com.qgyyzs.globalcosmetics.http.retrofit.HttpResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public interface HomeZSApi {
    @FormUrlEncoded
    @POST("product/get_product_list_index")
    Observable<HttpResponse> getHomeZsList(@Field("jsonstr") String parm);
}
