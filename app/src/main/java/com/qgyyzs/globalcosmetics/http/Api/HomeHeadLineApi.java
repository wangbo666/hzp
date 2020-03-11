package com.qgyyzs.globalcosmetics.http.Api;

import com.qgyyzs.globalcosmetics.http.retrofit.HttpResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public interface HomeHeadLineApi {
    @FormUrlEncoded
    @POST("common/sys_get_headlines")
    Observable<HttpResponse> getHomeHeadLine(@Field("jsonstr") String parm);
}
