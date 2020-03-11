package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.alibaba.fastjson.JSONObject;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.bean.IntentionBean;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.IntentionMsgView;
import com.qgyyzs.globalcosmetics.utils.AesUtils;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class IntentionMsgPresenter extends IBasePresenter<IntentionMsgView,RxFragmentActivity> {
    private final String TAG = IntentionMsgPresenter.class.getSimpleName();
    private IntentionBean bean;

    public IntentionMsgPresenter(IntentionMsgView view, RxFragmentActivity activity) {
        super(view, activity);
    }

    /**
     * 获取意向留言列表
     */
    public void getMsgList() {

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
                    getView().showIntentionResult(null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                try {
                    bean = JSONObject.parseObject(response.toString(), IntentionBean.class);
                }catch (Exception e){}
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showIntentionResult(bean);
                }
            }
        };
        if(null==getView())return;

        HttpRxObservable.getObservable(ApiUtils.getIntentionMsgApi().getMsgList(AesUtils.AesString(getView().getJsonString())), getActivity()).subscribe(httpRxObserver);
    }
}
