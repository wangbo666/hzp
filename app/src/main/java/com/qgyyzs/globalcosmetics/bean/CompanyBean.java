package com.qgyyzs.globalcosmetics.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */

public class CompanyBean implements Serializable {
    private int companyid;
    private String companyname;
    private String companylogo;
    private String username;
    private String lastesttime;//最新发布时间（产品/招聘/企业信息/资讯）
    private String linkText;

    private String productnum;//产品个数
    private String campaignnum;//活动个数
    private String recruitnum;//招聘个数
    private String newsnum;//资讯个数

    private int follownum;//关注次数
    private String attestation;//是否认证

    private List<MedicineBean> mMedicineBeanList;//医药

    public CompanyBean() {
        super();
    }

    public CompanyBean(String companyname, String companylogo, String username,String linkText) {
        this.companyname = companyname;
        this.companylogo = companylogo;
        this.username = username;
        this.linkText=linkText;
    }

    public CompanyBean(int companyid, String companyname, String companylogo, String username, List<MedicineBean> medicineBeanList) {
        this.companyid = companyid;
        this.companyname = companyname;
        this.companylogo = companylogo;
        this.username = username;
        mMedicineBeanList = medicineBeanList;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getCompanylogo() {
        return companylogo;
    }

    public void setCompanylogo(String companylogo) {
        this.companylogo = companylogo;
    }

    public String getLastesttime() {
        return lastesttime;
    }

    public void setLastesttime(String lastesttime) {
        this.lastesttime = lastesttime;
    }

    public String getProductnum() {
        return productnum;
    }

    public void setProductnum(String productnum) {
        this.productnum = productnum;
    }

    public String getCampaignnum() {
        return campaignnum;
    }

    public void setCampaignnum(String campaignnum) {
        this.campaignnum = campaignnum;
    }

    public String getRecruitnum() {
        return recruitnum;
    }

    public void setRecruitnum(String recruitnum) {
        this.recruitnum = recruitnum;
    }

    public String getNewsnum() {
        return newsnum;
    }

    public void setNewsnum(String newsnum) {
        this.newsnum = newsnum;
    }

    public int getFollownum() {
        return follownum;
    }

    public void setFollownum(int follownum) {
        this.follownum = follownum;
    }

    public String getAttestation() {
        return attestation;
    }

    public void setAttestation(String attestation) {
        this.attestation = attestation;
    }

    public List<MedicineBean> getMedicineBeanList() {
        return mMedicineBeanList;
    }

    public void setMedicineBeanList(List<MedicineBean> medicineBeanList) {
        mMedicineBeanList = medicineBeanList;
    }

    @Override
    public String toString() {
        return "CompanyBean{" +
                "companyid='" + companyid + '\'' +
                ", companyname='" + companyname + '\'' +
                ", companylogo='" + companylogo + '\'' +
                ", lastesttime='" + lastesttime + '\'' +
                ", productnum='" + productnum + '\'' +
                ", campaignnum='" + campaignnum + '\'' +
                ", recruitnum='" + recruitnum + '\'' +
                ", newsnum='" + newsnum + '\'' +
                ", follownum=" + follownum +
                ", attestation='" + attestation + '\'' +
                ", mMedicineBeanList=" + mMedicineBeanList +
                '}';
    }
}
