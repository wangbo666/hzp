package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.google.gson.Gson;
import com.qgyyzs.globalcosmetics.bean.VersionBean;
import com.qgyyzs.globalcosmetics.mvp.iface.VersionView;
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

public class VersionPresenter extends IBasePresenter<VersionView, RxFragmentActivity> {
    private String msg = "";

    private final String TAG = VersionPresenter.class.getSimpleName();

    public VersionPresenter(VersionView view, RxFragmentActivity activity) {
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
                    getView().showVersion(null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                VersionBean bean = null;
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String jsonData = jsonObject.getString("jsonData");
                    bean = new Gson().fromJson(jsonData, VersionBean.class);
                    MyApplication.versionBean = bean;
                } catch (JSONException e) {
                }
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showVersion(bean);
                }
            }
        };

        if (null == getView()) return;
        HttpRxObservable.getObservable(ApiUtils.getVersionApi().getVersion(AesUtils.AesString(getView().getJsonString())), getActivity()).subscribe(httpRxObserver);

    }
}
