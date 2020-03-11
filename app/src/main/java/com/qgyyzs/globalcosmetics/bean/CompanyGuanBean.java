package com.qgyyzs.globalcosmetics.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/12.
 */

public class CompanyGuanBean implements Serializable {
    private int companyid;
    private String companyname;
    private String companylogo;
    private String username;//谁关注的
    private String lastesttime;//最新发布时间（产品/招聘/企业信息/资讯）


    public CompanyGuanBean() {
        super();
    }

    public CompanyGuanBean(int companyid, String companyname, String companylogo, String username, String lastesttime) {
        this.companyid = companyid;
        this.companyname = companyname;
        this.companylogo = companylogo;
        this.username = username;
        this.lastesttime = lastesttime;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastesttime() {
        return lastesttime;
    }

    public void setLastesttime(String lastesttime) {
        this.lastesttime = lastesttime;
    }

    @Override
    public String toString() {
        return "CompanyGuanBean{" +
                "companyid=" + companyid +
                ", companyname='" + companyname + '\'' +
                ", companylogo='" + companylogo + '\'' +
                ", username='" + username + '\'' +
                ", lastesttime='" + lastesttime + '\'' +
                '}';
    }
}
