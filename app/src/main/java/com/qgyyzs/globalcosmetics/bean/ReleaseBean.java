package com.qgyyzs.globalcosmetics.bean;

/**
 * Created by Administrator on 2017/4/11.
 */

public class ReleaseBean {
    private int releaseid;//唯一的id
    private String type;//发布类型（求购 或者招标）
    private String title;//发布的标题
    private String time;//发布时间
    private String remark;//备注
    private String nickname;
    private String photo;
    private String time1;
    private int id;
    private String linkTel;
    private String muser;

    public String getMuser() {
        return muser;
    }

    public void setMuser(String muser) {
        this.muser = muser;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public boolean isopen() {
        return isopen;
    }

    public void setIsopen(boolean isopen) {
        this.isopen = isopen;
    }

    public String getNimID() {
        return NimID;
    }

    public void setNimID(String nimID) {
        NimID = nimID;
    }

    private int UserId;
    private boolean isopen;
    private String NimID;


    private String username;

    public ReleaseBean() {
        super();
    }


    public ReleaseBean(int releaseid, String type, String title, String remark,String muser,String time,String photo) {
        this.releaseid = releaseid;
        this.type = type;
        this.title = title;
        this.remark = remark;
        this.muser=muser;
        this.time=time;
        this.photo=photo;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getReleaseid() {
        return releaseid;
    }

    public void setReleaseid(int releaseid) {
        this.releaseid = releaseid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    @Override
    public String toString() {
        return "ReleaseBean{" +
                "releaseid=" + releaseid +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", remark='" + remark + '\'' +
                ", nickname='" + nickname + '\'' +
                ", photo='" + photo + '\'' +
                ", time1='" + time1 + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
