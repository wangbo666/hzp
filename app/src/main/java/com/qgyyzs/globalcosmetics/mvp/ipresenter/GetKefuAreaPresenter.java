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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class GetKefuAreaPresenter extends IBasePresenter<StringView,RxActivity> {
    private String msg="";

    private final String TAG = GetKefuAreaPresenter.class.getSimpleName();

    public GetKefuAreaPresenter(StringView view, RxActivity activity) {
        super(view, activity);
    }

    /**
     * 获取客服区域
     */
    public void getArea() {

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
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray array=jsonObject.getJSONArray("jsonData");
                    for (int k = 0; k < array.length(); k++) {
                        JSONObject jsonObject1 = new JSONObject(array.get(k).toString());
                        msg=msg+jsonObject1.getString("provinceName")+",";
                    }
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

        HttpRxObservable.getObservable(ApiUtils.getKefuAreaGetApi().getArea(AesUtils.AesString(getView().getJsonString())), getActivity()).subscribe(httpRxObserver);


    }
}
