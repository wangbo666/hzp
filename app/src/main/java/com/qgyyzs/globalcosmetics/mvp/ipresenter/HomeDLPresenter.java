package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.bean.ProxyLibraryBean;
import com.qgyyzs.globalcosmetics.bean.ReleaseBean;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.HomeInfoView;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/11/22 0022.
 */

public class HomeDLPresenter extends IBasePresenter<HomeInfoView,RxFragmentActivity> {
    private int releaseid;//唯一的id
    private String muser;
    private String title;//发布的标题
    private String dailitime;//发布时间
    private String remark;//备注
    private String headImg;
    private List<ReleaseBean> mReleaseBeanList;
    private final String TAG = HomeDLPresenter.class.getSimpleName();

    public HomeDLPresenter(HomeInfoView view, RxFragmentActivity activity) {
        super(view, activity);
    }

    /**
     * 获取首页代理列表
     */
    public void getDlList() {

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
                try{
                    ProxyLibraryBean bean = com.alibaba.fastjson.JSONObject.parseObject(response.toString(), ProxyLibraryBean.class);
                    mReleaseBeanList=new ArrayList<>();
                    for (int i=0;i<bean.getJsonData().size();i++){
                        releaseid=bean.getJsonData().get(i).getUserId();
                        remark=bean.getJsonData().get(i).getDaili_type();
                        dailitime=bean.getJsonData().get(i).getLastRequestTime();
                        title=bean.getJsonData().get(i).getRealName();
                        muser=bean.getJsonData().get(i).getPcUsername();
                        headImg=bean.getJsonData().get(i).getHeadImg();
                        ReleaseBean releaseBean = new ReleaseBean(releaseid, "代理", title, remark,muser,dailitime,headImg);
                        mReleaseBeanList.add(releaseBean);
                    }
                }catch (Exception e){}
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showHomeInfoResult(mReleaseBeanList);
                }
            }
        };
        if(null==getView())return;

        HttpRxObservable.getObservable(ApiUtils.getHomeDLApi().getHomeDlList(""), getActivity()).subscribe(httpRxObserver);
    }
}

