package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.bean.CompanyBean;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.HomeCompanyView;
import com.qgyyzs.globalcosmetics.utils.AesUtils;
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

public class HomeCompanyPresenter extends IBasePresenter<HomeCompanyView,RxFragmentActivity> {
    private List<CompanyBean> mCompanyBeanList=null;
    private String com_username, company, comp_logo,lintext;
    private int totalPage=1;
    private final String TAG = HomeCompanyPresenter.class.getSimpleName();

    public HomeCompanyPresenter(HomeCompanyView view, RxFragmentActivity activity) {
        super(view, activity);
    }

    /**
     * 获取首页企业列表
     */
    public void getCompanyList() {

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
                    getView().showCompanyResult(null,1);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    try{
                    totalPage=Integer.parseInt(jsonObject.getString("Msg"));
                    } catch (NumberFormatException e) {}
                    JSONArray array = jsonObject.getJSONArray("jsonData");
                    mCompanyBeanList=new ArrayList<>();
                    for (int k = 0; k < array.length(); k++) {
                        JSONObject jsonObject1 = new JSONObject(array.get(k).toString());
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("Information");
                        com_username = jsonObject2.getString("PcUsername");
                        company = jsonObject2.getString("company");
                        comp_logo = jsonObject1.getString("ImageUrl");
                        lintext = jsonObject1.getString("LinkText");
                        CompanyBean companyBean = new CompanyBean(company, comp_logo, com_username, lintext);
                        mCompanyBeanList.add(companyBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showCompanyResult(mCompanyBeanList,totalPage);
                }
            }
        };
        if(null==getView())return;

        HttpRxObservable.getObservable(ApiUtils.getHomeCompanyApi().getHomeCompany(AesUtils.AesString(getView().getJsonString())), getActivity()).subscribe(httpRxObserver);

    }
}

