package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.alibaba.fastjson.JSONObject;
import com.trello.rxlifecycle2.components.RxActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.bean.ZizhiInfoBean;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.ZizhiInfoView;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class ZizhiInfoPresenter extends IBasePresenter<ZizhiInfoView,RxActivity> {
    private final String TAG = ZizhiInfoPresenter.class.getSimpleName();

    private ZizhiInfoBean bean;

    public ZizhiInfoPresenter(ZizhiInfoView view, RxActivity activity) {
        super(view, activity);
    }

    /**
     * 获取资质信息
     */
    public void getZizhiInfo() {

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
                    getView().showzizhiResult(null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                bean= JSONObject.parseObject(response.toString(),ZizhiInfoBean.class);
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showzizhiResult(bean);
                }
            }
        };

        if(null==getView())return;
        HttpRxObservable.getObservable(ApiUtils.getZizhiApi().getInfo(""), getActivity()).subscribe(httpRxObserver);

    }
}


