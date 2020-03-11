package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.bean.ProductClassBean;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.SortProductView;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class SortProductBigPresenter2 extends IBasePresenter<SortProductView,RxFragmentActivity> {
    private List<ProductClassBean> mProductClassBeenList;
    private int id;
    private String type;
    private final String TAG = SortProductBigPresenter2.class.getSimpleName();

    public SortProductBigPresenter2(SortProductView view, RxFragmentActivity activity) {
        super(view, activity);
    }

    /**
     * 获取产品大类
     */
    public void getBigSort() {

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
                    getView().showSortResult(null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray array = jsonObject.getJSONArray("jsonData");
                        mProductClassBeenList=new ArrayList<>();
                        for (int k = 0; k < array.length(); k++) {
                            JSONObject jsonObject1 = new JSONObject(array.get(k).toString());
                            type = jsonObject1.getString("ClassName");
                            id = jsonObject1.getInt("ClassID");
                            ProductClassBean productClassBean = new ProductClassBean(id, type);
                            mProductClassBeenList.add(productClassBean);
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showSortResult(mProductClassBeenList);
                }
            }
        };

        if(null==getView())return;
        HttpRxObservable.getObservable(ApiUtils.getSortProductBigApi().getSortBig(""), getActivity()).subscribe(httpRxObserver);

    }
}
