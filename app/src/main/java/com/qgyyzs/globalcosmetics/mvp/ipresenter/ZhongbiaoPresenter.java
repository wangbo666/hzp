package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.bean.BidBean;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.BidView;
import com.qgyyzs.globalcosmetics.utils.AesUtils;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class ZhongbiaoPresenter extends IBasePresenter<BidView,RxFragmentActivity> {
    private int id;
    private int Msg;
    private String yaopinName, zhongbiaojia, guige, baozhuang,scqiye,area;
    private List<BidBean> mBidaBeanList=null;
    private final String TAG = ZhongbiaoPresenter.class.getSimpleName();

    public ZhongbiaoPresenter(BidView view, RxFragmentActivity activity) {
        super(view, activity);
    }

    /**
     * 获取中标列表
     */
    public void getBidList() {

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
                    getView().showBidResult(null,1);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                mBidaBeanList=new ArrayList<>();
                Msg=1;
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray array = jsonObject.getJSONArray("jsonData");
                    Msg=Integer.parseInt(jsonObject.getString("Msg"));
                    for (int k = 0; k < array.length(); k++) {
                        JSONObject jsonObject1 = new JSONObject(array.get(k).toString());
                        id = jsonObject1.getInt("id");//
                        yaopinName = jsonObject1.getString("yaopinName");
                        zhongbiaojia = jsonObject1.getString("zhongbiaojia");
                        guige = jsonObject1.getString("guige");
                        baozhuang = jsonObject1.getString("baozhuang");
                        scqiye = jsonObject1.getString("scqiye");
                        area = jsonObject1.getString("shengshi");

                        BidBean bidaBean = new BidBean(id, yaopinName, "规格：" + guige, zhongbiaojia, "剂型：" + baozhuang, "厂家：" + scqiye, area, "中标价：");
                        mBidaBeanList.add(bidaBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showBidResult(mBidaBeanList,Msg);
                }
            }
        };

        if(null==getView())return;
        HttpRxObservable.getObservable(ApiUtils.getZhongbiaoApi().getBid(AesUtils.AesString(getView().getJsonString())), getActivity()).subscribe(httpRxObserver);
    }
}
