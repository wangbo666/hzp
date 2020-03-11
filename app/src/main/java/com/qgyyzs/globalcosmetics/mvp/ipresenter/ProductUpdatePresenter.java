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

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class ProductUpdatePresenter extends IBasePresenter<StringView,RxActivity> {
    private String Msg;
    private final String TAG = ProductUpdatePresenter.class.getSimpleName();

    public ProductUpdatePresenter(StringView view, RxActivity activity) {
        super(view, activity);
    }

    /**
     * 更新产品
     */
    public void Updateproduct() {

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
                    getView().showStringResult(null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    Msg=jsonObject.getString("Msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showStringResult(Msg);
                }
            }
        };

        if(null==getView())return;
        HttpRxObservable.getObservable(ApiUtils.getProductUpdateApi().updateProduct(AesUtils.AesString(getView().getJsonString())), getActivity()).subscribe(httpRxObserver);

    }
}
