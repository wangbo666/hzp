package com.qgyyzs.globalcosmetics.uikit.business.session.actions;

import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseModel;
import com.qgyyzs.globalcosmetics.mvp.model.CompanyBean;
import com.qgyyzs.globalcosmetics.mvp.presenter.MyCompanyPresenter;
import com.qgyyzs.globalcosmetics.mvp.view.CompanyInfoView;
import com.qgyyzs.globalcosmetics.nim.session.extension.CompanyAttachment;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/4/16 0016.
 */

public class CompanyAction extends BaseAction implements CompanyInfoView {
    private MyCompanyPresenter myCompanyPresenter;

    public CompanyAction() {
        super(R.drawable.icon_nim_shop, R.string.input_panel_shop);
    }

    @Override
    public void onClick() {
        myCompanyPresenter = new MyCompanyPresenter(this);
        myCompanyPresenter.getCompanyInfo();
    }

    public String getJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pcusername", UserUtil.getInstance().getUserBean().username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.e(jsonObject.toString());
        return jsonObject.toString();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public void onErrorCode(BaseModel model) {

    }

    @Override
    public void showCompanyResult(CompanyBean bean) {
        if (bean == null) return;
        CompanyAttachment attachment = new CompanyAttachment();
        attachment.setComp_logo(bean.comp_logo);
        attachment.setDanwei(bean.company);
        attachment.setPcUsername(bean.PcUsername);
        IMMessage message =
                MessageBuilder.createCustomMessage(getAccount(),
                        getSessionType(), attachment);
        sendMessage(message);
    }
}
