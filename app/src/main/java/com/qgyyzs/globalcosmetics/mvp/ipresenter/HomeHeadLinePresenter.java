package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.bean.MedicalNewsBean;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.MedicalNewsView;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/11/22 0022.
 */

public class HomeHeadLinePresenter extends IBasePresenter<MedicalNewsView,RxFragmentActivity> {
    private List<MedicalNewsBean> mMedicalNewsBeanList;
    private final String TAG = HomeHeadLinePresenter.class.getSimpleName();

    public HomeHeadLinePresenter(MedicalNewsView view, RxFragmentActivity activity) {
        super(view, activity);
    }

    /**
     * 获取首页头条
     */
    public void getHeadLineList() {

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
                    getView().showHeadLineResult(null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray array = jsonObject.getJSONArray("jsonData");
                    mMedicalNewsBeanList = new ArrayList<>();
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject jsonObject1 = new JSONObject(array.get(j).toString());
                        int id = jsonObject1.getInt("Artcle_id");
                        String title = jsonObject1.getString("Artcle_title");
                        MedicalNewsBean medicalNewsBean = new MedicalNewsBean(id, title);
                        mMedicalNewsBeanList.add(medicalNewsBean);
                    }
                    MyApplication.Toutiao = mMedicalNewsBeanList.get(0).getTitle();
                }catch (JSONException e){}
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showHeadLineResult(mMedicalNewsBeanList);
                }
            }
        };
        if(null==getView())return;

        HttpRxObservable.getObservable(ApiUtils.getHomeHeadLineApi().getHomeHeadLine(""), getActivity()).subscribe(httpRxObserver);
    }
}

