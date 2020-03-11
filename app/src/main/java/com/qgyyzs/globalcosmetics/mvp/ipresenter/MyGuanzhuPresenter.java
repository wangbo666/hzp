package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.trello.rxlifecycle2.components.RxActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.bean.CompanyGuanBean;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.GuanzhuView;
import com.qgyyzs.globalcosmetics.utils.AesUtils;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class MyGuanzhuPresenter extends IBasePresenter<GuanzhuView,RxActivity> {
    private List<CompanyGuanBean> mCompanyBeanList=null;
    private int companyid;
    private String Msg;
    private String companyname, company_username, gzTime;
    private String companylogo;
    private final String TAG = MyGuanzhuPresenter.class.getSimpleName();

    public MyGuanzhuPresenter(GuanzhuView view, RxActivity activity) {
        super(view, activity);
    }

    /**
     * 获取关注列表
     */
    public void getGuanzhuList() {

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
                    getView().showGuanzhuResult(null,"1");
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    Msg = jsonObject.getString("Msg");
                    JSONArray array = jsonObject.getJSONArray("jsonData");
                    mCompanyBeanList=new ArrayList<>();
                        for (int k = 0; k < array.length(); k++) {
                            org.json.JSONObject jsonObject1 = new JSONObject(array.get(k).toString());
                            companyid = jsonObject1.getInt("Id");
                            company_username = jsonObject1.getString("PcUsername");//企业usname
                            gzTime = jsonObject1.getString("GZTime");//企业的name
                            companyname = jsonObject1.getString("company");//企业的name
                            companylogo = jsonObject1.getString("comp_logo");
                            String res;
                            if (!gzTime.equals("null")) {
                                gzTime = gzTime.substring(6, gzTime.length() - 2);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                long lt = new Long(gzTime);
                                Date date = new Date(lt);
                                res = TimeUtils.twoDateDistance(date);
                            } else {
                                res = "";
                            }
                            String[] str = companylogo.split("\\|");
                            CompanyGuanBean companyBean = new CompanyGuanBean(companyid, companyname, str[0], company_username, res);
                            mCompanyBeanList.add(companyBean);
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showGuanzhuResult(mCompanyBeanList,Msg);
                }
            }
        };

        if(null==getView())return;
        HttpRxObservable.getObservable(ApiUtils.getMyguanzhuApi().getGuanzhuList(AesUtils.AesString(getView().getJsonString())), getActivity()).subscribe(httpRxObserver);

    }
}
