package com.qgyyzs.globalcosmetics.http.Api;

import com.qgyyzs.globalcosmetics.http.retrofit.HttpResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by admin on 2017/12/15.
 */

public interface ISshowSalemanApi {
    @FormUrlEncoded
    @POST("user/get_isshow_tjcode")
    Observable<HttpResponse> IsShowsaleman(@Field("jsonstr") String parm);
}
