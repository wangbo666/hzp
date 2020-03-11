package com.qgyyzs.globalcosmetics.http.Api;

import com.qgyyzs.globalcosmetics.http.retrofit.HttpResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public interface CompanyCenterApi {
    @FormUrlEncoded
    @POST("user/user_get_companylist")
    Observable<HttpResponse> getCompanyList(@Field("jsonstr") String parm);
}
