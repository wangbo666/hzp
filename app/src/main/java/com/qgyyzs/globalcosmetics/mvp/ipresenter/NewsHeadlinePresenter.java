package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.bean.MedicalNewsBean;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.NewsView;
import com.qgyyzs.globalcosmetics.utils.AesUtils;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class NewsHeadlinePresenter extends IBasePresenter<NewsView,RxFragmentActivity> {
    private List<MedicalNewsBean> mMedicalNewsBeanList;
    private String Msg;
    private int id;
    private String title;
    private String looknum;
    private String time;
    private String img;

    private final String TAG = NewsHeadlinePresenter.class.getSimpleName();
    public NewsHeadlinePresenter(NewsView view, RxFragmentActivity activity) {
        super(view, activity);
    }

    /**
     * 获取资讯列表
     */
    public void getNewsList() {

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
                    getView().showNewsResult(null,"1");
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                mMedicalNewsBeanList=new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    Msg = jsonObject.getString("Msg");
                    JSONArray array = jsonObject.getJSONArray("jsonData");
                    for (int k = 0; k < array.length(); k++) {
                        JSONObject jsonObject1 = new JSONObject(array.get(k).toString());
                        id = jsonObject1.getInt("Artcle_id");//
                        title = jsonObject1.getString("Artcle_title");
                        img = jsonObject1.getString("vIndexpic");
                        time = jsonObject1.getString("Artcle_date");
                        looknum = "" + jsonObject1.getInt("Artcle_hit");
                        time = time.substring(6, time.length() - 2);
                        long lt = new Long(time);
                        Date date = new Date(lt);
                        String res = TimeUtils.twoDateDistance(date);
                        MedicalNewsBean medicalNewsBean = new MedicalNewsBean(id, title, looknum, res, img);
                        mMedicalNewsBeanList.add(medicalNewsBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showNewsResult(mMedicalNewsBeanList,Msg);
                }
            }
        };
        if(null==getView())return;

        HttpRxObservable.getObservable(ApiUtils.getNewsHeadlineApi().getHeadLine(AesUtils.AesString(getView().getJsonString())), getActivity()).subscribe(httpRxObserver);

    }
}

