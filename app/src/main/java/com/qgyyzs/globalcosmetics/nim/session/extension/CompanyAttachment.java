package com.qgyyzs.globalcosmetics.nim.session.extension;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class CompanyAttachment extends CustomAttachment implements Serializable {

    private String danwei;
    private String PcUsername;
    private String comp_logo;

    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei;
    }

    public String getPcUsername() {
        return PcUsername;
    }

    public void setPcUsername(String pcUsername) {
        PcUsername = pcUsername;
    }

    public String getComp_logo() {
        return comp_logo;
    }

    public void setComp_logo(String comp_logo) {
        this.comp_logo = comp_logo;
    }

    public CompanyAttachment(){
        super(CustomAttachmentType.Company);
    }

    public CompanyAttachment(String str) {
        this();
    }

    @Override
    protected void parseData(JSONObject data) {
        danwei=data.getString("danwei");
        PcUsername=data.getString("PcUsername");
        comp_logo=data.getString("comp_logo");
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("comp_logo",comp_logo);
        data.put("danwei",danwei);
        data.put("PcUsername",PcUsername);
        return data;
    }
}
