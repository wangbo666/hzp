package com.qgyyzs.globalcosmetics.nim.session.extension;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class ProductAttachment extends CustomAttachment implements Serializable {

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMaiDian() {
        return maiDian;
    }

    public void setMaiDian(String maiDian) {
        this.maiDian = maiDian;
    }

    private String Subject;
    private int id;
    private String danwei;
    private String PcUsername;
    private String image;
    private String maiDian;


    public ProductAttachment(){
        super(CustomAttachmentType.Product);
    }

    public ProductAttachment(String str) {
        this();
    }

    @Override
    protected void parseData(JSONObject data) {
        Subject=data.getString("Subject");
        id=data.getInteger("id");
        danwei=data.getString("danwei");
        PcUsername=data.getString("PcUsername");
        image=data.getString("image");
        maiDian=data.getString("maiDian");
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("Subject",Subject);
        data.put("id",id);
        data.put("danwei",danwei);
        data.put("PcUsername",PcUsername);
        data.put("image",image);
        data.put("maiDian",maiDian);
        return data;
    }
}
