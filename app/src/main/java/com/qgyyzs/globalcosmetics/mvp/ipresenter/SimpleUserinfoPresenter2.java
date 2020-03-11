package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.bean.UserinfoBean;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.UserInfoView;
import com.qgyyzs.globalcosmetics.utils.AesUtils;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class SimpleUserinfoPresenter2 extends IBasePresenter<UserInfoView,Activity> {
    private final String TAG = SimpleUserinfoPresenter2.class.getSimpleName();

    private UserinfoBean bean;

    public SimpleUserinfoPresenter2(UserInfoView view, Activity activity) {
        super(view, activity);
    }

    /**
     * 获取用户详情
     */
    public void getUserInfo() {

        HttpRxObserver httpRxObserver = new HttpRxObserver(TAG + "getInfo") {

            @Override
            protected void onStart(Disposable d) {
                if (getView() != null)
                    getView().showLoading();
            }

            @Override
            protected void onError(ApiException e) {
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showToast(e.getMsg());
                    getView().showUserResult(null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                try {
                    bean = JSONObject.parseObject(response.toString(), UserinfoBean.class);
                }catch (Exception e){}
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showUserResult(bean);
                }
            }
        };

        if(null==getView())return;
        HttpRxObservable.getObservable(ApiUtils.getSimpleUserInfoApi().getUserInfo(AesUtils.AesString(getView().getJsonString()))).subscribe(httpRxObserver);

    }
}
