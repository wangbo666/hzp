package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.alibaba.fastjson.JSONObject;
import com.trello.rxlifecycle2.components.RxActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.bean.IntentionDetialBean;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.IntentionDetialView;
import com.qgyyzs.globalcosmetics.utils.AesUtils;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class IntentionDetialPresenter extends IBasePresenter<IntentionDetialView,RxActivity> {
    private final String TAG = IntentionDetialPresenter.class.getSimpleName();
    private IntentionDetialBean bean;

    public IntentionDetialPresenter(IntentionDetialView view, RxActivity activity) {
        super(view, activity);
    }

    /**
     * 获取留言详情
     */
    public void getDetial() {

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
                    getView().showDetialResult(null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                try {
                    bean = JSONObject.parseObject(response.toString(), IntentionDetialBean.class);
                }catch (Exception e){}
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showDetialResult(bean);
                }
            }
        };
        if(null==getView())return;

        HttpRxObservable.getObservable(ApiUtils.getIntentionDetialApi().getDetial(AesUtils.AesString(getView().getJsonString())), getActivity()).subscribe(httpRxObserver);

    }
}
