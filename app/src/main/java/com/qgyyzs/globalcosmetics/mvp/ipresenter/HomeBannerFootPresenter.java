package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.BannerView;
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

public class HomeBannerFootPresenter extends IBasePresenter<BannerView,RxFragmentActivity> {
    private List<String> images=new ArrayList<>(),lunboUrs=new ArrayList<>(),jsonString=new ArrayList<>(),linkText=new ArrayList<>();
    private final String TAG = HomeBannerFootPresenter.class.getSimpleName();

    public HomeBannerFootPresenter(BannerView view, RxFragmentActivity activity) {
        super(view, activity);
    }

    /**
     * 获取首页头部轮播图
     */
    public void getBannerFoot() {

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
                    getView().showBannerResult(null,null,null,null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e("onSuccess response:" + response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray array = jsonObject.getJSONArray("jsonData");
                    images.clear();
                    lunboUrs.clear();
                    jsonString.clear();
                    linkText.clear();
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject jsonObject1 = new JSONObject(array.get(j).toString());
                        images.add(jsonObject1.getString("ImageUrl"));
                        lunboUrs.add(jsonObject1.getInt("Type")+"");
                        linkText.add(jsonObject1.getString("LinkText"));
                        if(jsonObject1.getInt("Type")==0){
                            jsonString.add(jsonObject1.getString("Information"));
                        }else{
                            jsonString.add(jsonObject1.getJSONObject("Information").toString());
                        }
                    } } catch (JSONException e) {
                    e.printStackTrace();
                }
//                List<BannerBean> list = new Gson().fromJson(response.toString(), new TypeToken<List<BannerBean>>(){}.getType());
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showBannerResult(images,lunboUrs,jsonString,linkText);
                }
            }
        };
        if(null==getView())return;

        HttpRxObservable.getObservable(ApiUtils.getHomeBannerFootApi().getBannerFoot(""), getActivity()).subscribe(httpRxObserver);

    }
}

