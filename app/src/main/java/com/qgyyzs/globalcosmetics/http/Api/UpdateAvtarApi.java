package com.qgyyzs.globalcosmetics.http.Api;

import com.qgyyzs.globalcosmetics.http.retrofit.HttpResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by admin on 2017/12/15.
 */

public interface UpdateAvtarApi {
    @Multipart
    @POST("user/upload_user_headimg")
    Observable<HttpResponse> updateAvtar(@Part List<MultipartBody.Part> partList);
}
