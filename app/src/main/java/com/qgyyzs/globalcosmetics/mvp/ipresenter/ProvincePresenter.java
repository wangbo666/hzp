package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.trello.rxlifecycle2.components.RxActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class ProvincePresenter extends IBasePresenter<StringView,RxActivity> {
    private String provinceString="";

    private final String TAG = ProvincePresenter.class.getSimpleName();

    public ProvincePresenter(StringView view, RxActivity activity) {
        super(view, activity);
    }

    /**
     * 获取省份
     */
    public void getProvince() {

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
                        provinceString=provinceString+jsonObject1.getString("provinceName")+",";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showStringResult(provinceString);
                }
            }
        };

        if(null==getView())return;
        HttpRxObservable.getObservable(ApiUtils.getProvinceApi().getProvince(""), getActivity()).subscribe(httpRxObserver);
    }
}
