package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import android.text.TextUtils;

import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.ListStringView;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class KefuPresenter extends IBasePresenter<ListStringView,RxFragmentActivity> {
    private String str[] = null;
    private List<String> list;
    private final String TAG = KefuPresenter.class.getSimpleName();

    public KefuPresenter(ListStringView view, RxFragmentActivity activity) {
        super(view, activity);
    }

    /**
     * 获取客服热线
     */
    public void getkefuTel() {

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
                    getView().showStringListResult(null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String kefu = jsonObject.getString("jsonData");
                    list = new ArrayList<>();
                    if (!TextUtils.isEmpty(kefu) && kefu != null) {
                        str = kefu.split(",");
                        for (int j = 0; j < str.length; j++) {
                            if (!str[j].equals(null)) {
                                list.add(str[j]);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showStringListResult(list);
                }
            }
        };

        if(null==getView())return;
        HttpRxObservable.getObservable(ApiUtils.getKefuTelApi().getKefu(""), getActivity()).subscribe(httpRxObserver);
    }
}
