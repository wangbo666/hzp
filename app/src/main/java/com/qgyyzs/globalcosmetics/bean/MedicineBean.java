package com.qgyyzs.globalcosmetics.bean;

/**
 * Created by Administrator on 2017/4/12.
 */

public class MedicineBean {
    private int medicineid;
    private String companyid;//厂家的id
    private String muser;//产品的发布者
    private String companyanme;//厂家
    private String name;//药品名称
    private String imgurl;//图片地址
    private String url;//浏览地址
    private String type;//类型
    private String advantage;//优势
    private int looknum;//浏览次数
    private String spec;//规格
    private String appuser;
    private String LinkTel;

    public String getLinkTel() {
        return LinkTel;
    }

    public void setLinkTel(String linkTel) {
        LinkTel = linkTel;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getNimID() {
        return NimID;
    }

    public void setNimID(String nimID) {
        NimID = nimID;
    }

    private int UserId;
    private String NimID;
    private boolean IsOpen;

    public boolean isOpen() {
        return IsOpen;
    }

    public void setOpen(boolean open) {
        IsOpen = open;
    }

    public String getAppuser() {
        return appuser;
    }

    public void setAppuser(String appuser) {
        this.appuser = appuser;
    }
    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public MedicineBean() {
        super();
    }

    public MedicineBean(int medicineid, String companyanme, String muser, String name, String imgurl, String type, String advantage, int looknum, String spec) {
        this.medicineid = medicineid;
        this.companyanme = companyanme;
        this.muser = muser;
        this.name = name;
        this.imgurl = imgurl;
        this.type = type;
        this.advantage = advantage;
        this.looknum = looknum;
        this.spec = spec;
    }
    public MedicineBean(int medicineid, String companyanme, String muser, String name, String imgurl, String type, String advantage, int looknum, String spec,String appuser) {
        this.medicineid = medicineid;
        this.companyanme = companyanme;
        this.muser = muser;
        this.name = name;
        this.imgurl = imgurl;
        this.type = type;
        this.advantage = advantage;
        this.looknum = looknum;
        this.spec = spec;
        this.appuser=appuser;
    }

    public MedicineBean(int medicineid, String muser, String name, String imgurl) {
        this.medicineid = medicineid;
        this.muser = muser;
        this.name = name;
        this.imgurl = imgurl;
    }

    public MedicineBean(String companyanme, String name, String imgurl) {
        this.companyanme = companyanme;
        this.name = name;
        this.imgurl = imgurl;
    }

    public String getMuser() {
        return muser;
    }

    public void setMuser(String muser) {
        this.muser = muser;
    }

    public int getMedicineid() {
        return medicineid;
    }

    public void setMedicineid(int medicineid) {
        this.medicineid = medicineid;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getCompanyanme() {
        return companyanme;
    }

    public void setCompanyanme(String companyanme) {
        this.companyanme = companyanme;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLooknum() {
        return looknum;
    }

    public void setLooknum(int looknum) {
        this.looknum = looknum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdvantage() {
        return advantage;
    }

    public void setAdvantage(String advantage) {
        this.advantage = advantage;
    }

    @Override
    public String toString() {
        return "MedicineBean{" +
                "medicineid='" + medicineid + '\'' +
                ", companyid='" + companyid + '\'' +
                ", companyanme='" + companyanme + '\'' +
                ", name='" + name + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", advantage='" + advantage + '\'' +
                ", looknum=" + looknum +
                '}';
    }
}
