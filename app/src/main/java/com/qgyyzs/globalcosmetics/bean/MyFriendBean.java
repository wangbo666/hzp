package com.qgyyzs.globalcosmetics.bean;

import com.qgyyzs.globalcosmetics.utils.IndexBar.bean.BaseIndexPinyinBean;

/**
 * Created by Administrator on 2017/12/5 0005.
 */

public class MyFriendBean extends BaseIndexPinyinBean {
    private String userid;
    private String nimId;
    private String headUrl;
    private String nickName;
    private String province;
    private String city;

    public MyFriendBean(String userid,String nimId,String headUrl,String nickName){
        this.userid=userid;
        this.nimId=nimId;
        this.headUrl=headUrl;
        this.nickName=nickName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNimId() {
        return nimId;
    }

    public void setNimId(String nimId) {
        this.nimId = nimId;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    private boolean isTop;//是否是最上面的 不需要被转化成拼音的

    @Override
    public String getTarget() {
        return nickName;
    }

    public boolean isTop() {
        return isTop;
    }

    public MyFriendBean setTop(boolean top) {
        isTop = top;
        return this;
    }

    @Override
    public boolean isNeedToPinyin() {
        return !isTop;
    }

    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }
}
