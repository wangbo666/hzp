package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.trello.rxlifecycle2.components.RxActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class UpdateProductImgPresenter extends IBasePresenter<StringView,RxActivity> {
    private String msg="";

    private final String TAG = UpdateProductImgPresenter.class.getSimpleName();

    public UpdateProductImgPresenter(StringView view, RxActivity activity) {
        super(view, activity);
    }

    /**
     * 修改产品图片
     */
    public void updateImg(List<MultipartBody.Part> partList) {

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
                    getView().showStringResult(null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    msg=jsonObject.getString("jsonData");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showStringResult(msg);
                }
            }
        };

        if(null==getView())return;
        HttpRxObservable.getObservable(ApiUtils.getUpdateProductImgApi().updateImg(partList), getActivity()).subscribe(httpRxObserver);

    }
}
