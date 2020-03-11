package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.application.MyApplication;
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

public class VersionPresenter extends IBasePresenter<StringView,RxFragmentActivity> {
    private String msg="";

    private final String TAG = VersionPresenter.class.getSimpleName();

    public VersionPresenter(StringView view, RxFragmentActivity activity) {
        super(view, activity);
    }

    /**
     * 获取版本信息
     */
    public void getVersion() {

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
                    JSONObject jsonObject1 = new JSONObject(response.toString());
                    msg = jsonObject1.getString("Msg");
                    jsonObject1 = jsonObject1.getJSONObject("jsonData");
                        MyApplication.server_version = jsonObject1.get("version").toString();
                        MyApplication.server_apkurl = jsonObject1.get("httppath").toString();
                        MyApplication.server_apktitle = jsonObject1.get("verdesc").toString();
                        MyApplication.ForceUpdate = jsonObject1.getInt("ForceUpdate");
                        MyApplication.isUpdate = jsonObject1.getInt("IsUpdate");
                }catch (JSONException e){}
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showStringResult(msg);
                }
            }
        };

        if(null==getView())return;
        HttpRxObservable.getObservable(ApiUtils.getVersionApi().getVersion(AesUtils.AesString(getView().getJsonString())), getActivity()).subscribe(httpRxObserver);

    }
}
