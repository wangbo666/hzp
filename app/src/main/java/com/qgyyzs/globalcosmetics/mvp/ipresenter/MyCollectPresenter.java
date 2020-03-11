package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.alibaba.fastjson.JSONObject;
import com.trello.rxlifecycle2.components.RxActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.bean.MyCollectBean;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.CollectView;
import com.qgyyzs.globalcosmetics.utils.AesUtils;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class MyCollectPresenter extends IBasePresenter<CollectView,RxActivity> {
    private final String TAG = MyCollectPresenter.class.getSimpleName();

    private MyCollectBean bean;

    public MyCollectPresenter(CollectView view, RxActivity activity) {
        super(view, activity);
    }

    /**
     * 获取收藏列表
     */
    public void getCollectList() {

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
                    getView().showCollectResult(null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                try {
                    bean = JSONObject.parseObject(response.toString(), MyCollectBean.class);
                }catch (Exception e){}
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showCollectResult(bean);
                }
            }
        };
        if(null==getView())return;

        HttpRxObservable.getObservable(ApiUtils.getMyCollectApi().getCollectList(AesUtils.AesString(getView().getJsonString())), getActivity()).subscribe(httpRxObserver);
    }
}
