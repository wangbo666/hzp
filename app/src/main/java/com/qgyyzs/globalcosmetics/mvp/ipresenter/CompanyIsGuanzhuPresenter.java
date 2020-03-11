package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.trello.rxlifecycle2.components.RxActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.utils.AesUtils;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class CompanyIsGuanzhuPresenter extends IBasePresenter<StringView,RxFragmentActivity> {
    private String msg;
    private final String TAG = CompanyIsGuanzhuPresenter.class.getSimpleName();

    public CompanyIsGuanzhuPresenter(StringView view, RxFragmentActivity activity) {
        super(view, activity);
    }

    /**
     * 获取是否关注企业
     */
    public void getIsGuanzhu() {

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
                    getView().showStringResult(null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                try {
                    JSONObject jsonObject = new org.json.JSONObject(response.toString());
                    msg = jsonObject.getString("Msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showStringResult(msg);
                }
            }
        };
        if(null==getView())return;

        HttpRxObservable.getObservable(ApiUtils.getCompanyIsGuanzhuApi().getState(AesUtils.AesString(getView().getJsonString())), getActivity()).subscribe(httpRxObserver);
    }
}


