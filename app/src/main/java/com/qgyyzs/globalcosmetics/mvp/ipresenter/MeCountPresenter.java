package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.CountView;
import com.qgyyzs.globalcosmetics.utils.AesUtils;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class MeCountPresenter extends IBasePresenter<CountView,RxFragmentActivity> {
    private int Pcount=-1,Dcount=-1,Jcount=-1,Ccount=-1,Gcount=-1;
    private List<Integer> list;
    private final String TAG = MeCountPresenter.class.getSimpleName();

    public MeCountPresenter(CountView view, RxFragmentActivity activity) {
        super(view, activity);
    }

    /**
     * 获取我的页面数量
     */
    public void getCount() {

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
                    getView().showCountResult(null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                try {
                    list=new ArrayList<>();
                    JSONObject jsonObject = new org.json.JSONObject(response.toString());
                    org.json.JSONObject jsonObject1 = jsonObject.getJSONObject("jsonData");
                    Pcount = jsonObject1.getInt("ProductCount");
                    Dcount = jsonObject1.getInt("DailiCount");
                    Jcount = jsonObject1.getInt("JobCount");
                    Gcount = jsonObject1.getInt("GuanZhuCount");
                    Ccount = jsonObject1.getInt("ShouCangCount");
                    list.add(Pcount);
                    list.add(Dcount);
                    list.add(Jcount);
                    list.add(Gcount);
                    list.add(Ccount);
                }catch (JSONException e){}
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showCountResult(list);
                }
            }
        };

        if(null==getView())return;
        HttpRxObservable.getObservable(ApiUtils.getMeInfoCountApi().getCount(AesUtils.AesString(getView().getJsonString())), getActivity()).subscribe(httpRxObserver);

    }
}
