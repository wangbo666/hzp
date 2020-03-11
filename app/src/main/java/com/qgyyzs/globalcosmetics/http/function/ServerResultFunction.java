package com.qgyyzs.globalcosmetics.http.function;

import com.qgyyzs.globalcosmetics.activity.LoginActivity;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.http.exception.ServerException;
import com.qgyyzs.globalcosmetics.http.retrofit.HttpResponse;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 服务器结果处理函数
 *
 * @author ZhongDaFeng
 */
public class ServerResultFunction implements Function<HttpResponse, Object> {
    @Override
    public Object apply(@NonNull HttpResponse response) throws Exception {
        //打印服务器回传结果
//        LogUtils.e(response.toString());
//        if(!response.isSuccess()){
//            ToastUtil.showToast(MyApplication.getInstance(),response.getMsg(),true);
//            throw new ServerException(response.getResult(), response.getMsg());
//        }
//        return response.toString();
        if(response.isTokenError()) {
            MyApplication.mSharedPreferences.edit().clear();
            MyApplication.islogin = false;
            MyApplication.TOKEN = "";
            ToastUtil.showToast(MyApplication.getContextObject(), "登录过期，请重新登陆", true);
            LoginActivity.start(MyApplication.getContextObject());
        }
        if (!response.isSuccess()&&!response.isTokenError()) {
            ToastUtil.showToast(MyApplication.getContextObject(), response.getMsg(), true);
            throw new ServerException(response.getResult(), response.getMsg());
        }
        return response.toString();
    }
}