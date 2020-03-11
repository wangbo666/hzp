package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.trello.rxlifecycle2.components.RxActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.bean.CompanyCenterBean;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.CompanyCenterView;
import com.qgyyzs.globalcosmetics.utils.AesUtils;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class CompanyCenterPresenter extends IBasePresenter<CompanyCenterView,RxActivity> {
    private final String TAG = CompanyCenterPresenter.class.getSimpleName();

    private CompanyCenterBean bean;
    public CompanyCenterPresenter(CompanyCenterView view, RxActivity activity) {
        super(view, activity);
    }

    /**
     * 获取名企展区列表
     */
    public void getCompanyList() {

        final HttpRxObserver httpRxObserver = new HttpRxObserver(TAG + "getInfo") {

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
                    getView().showCompanyListResult(null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                try {
                    bean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), CompanyCenterBean.class);
                }catch (Exception e){

                }
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showCompanyListResult(bean);
                }
            }
        };

        if(null==getView())return;
        HttpRxObservable.getObservable(ApiUtils.getCompanyCenterApi().getCompanyList(AesUtils.AesString(getView().getJsonString())), getActivity()).subscribe(httpRxObserver);

    }
}
