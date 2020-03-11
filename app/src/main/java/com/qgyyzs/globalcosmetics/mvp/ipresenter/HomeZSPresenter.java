package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.bean.ReleaseBean;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.HomeInfoView;
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

public class HomeZSPresenter extends IBasePresenter<HomeInfoView,RxFragmentActivity> {
    private int releaseid;//唯一的id
    private String muser;
    private String title, imgurl;//发布的标题
    private String producttime;//发布时间
    private String remark;//备注
    private List<ReleaseBean> mReleaseBeanList;
    private final String TAG = HomeZSPresenter.class.getSimpleName();

    public HomeZSPresenter(HomeInfoView view, RxFragmentActivity activity) {
        super(view, activity);
    }

    /**
     * 获取首页招商列表
     */
    public void getZsList() {

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
                    getView().showHomeInfoResult(null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray array = jsonObject.getJSONArray("jsonData");
                    mReleaseBeanList=new ArrayList<>();
                    for (int k = 0; k < array.length(); k++) {
                        JSONObject jsonObject1 = new JSONObject(array.get(k).toString());
                        releaseid = jsonObject1.getInt("id");
                        title = jsonObject1.getString("Subject");
                        imgurl = jsonObject1.getString("image");
                        producttime = jsonObject1.getString("time");
                        remark=jsonObject1.getString("ppai_name");
                        muser=jsonObject1.getString("PcUsername");

                        ReleaseBean releaseBean = new ReleaseBean(releaseid, "招商", title, remark,muser,producttime,"");
                        releaseBean.setPhoto(imgurl);
                        mReleaseBeanList.add(releaseBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showHomeInfoResult(mReleaseBeanList);
                }
            }
        };
        if(null==getView())return;

        HttpRxObservable.getObservable(ApiUtils.getHomeZSApi().getHomeZsList(""), getActivity()).subscribe(httpRxObserver);
    }
}

