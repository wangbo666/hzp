package com.qgyyzs.globalcosmetics.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class CompanyCenterBean {
    private int Result;

    private String Msg;

    private List<JsonData> jsonData ;

    public void setResult(int Result){
        this.Result = Result;
    }
    public int getResult(){
        return this.Result;
    }
    public void setMsg(String Msg){
        this.Msg = Msg;
    }
    public String getMsg(){
        return this.Msg;
    }
    public void setJsonData(List<JsonData> jsonData){
        this.jsonData = jsonData;
    }
    public List<JsonData> getJsonData(){
        return this.jsonData;
    }
    public class JsonData {
        private List<Prolist> prolist ;

        private int id;

        private String PcUsername;

        private String company;

        private String comp_logo;

        private int flag;

        private String content;

        private String ProShuanTime;

        public void setProlist(List<Prolist> prolist){
            this.prolist = prolist;
        }
        public List<Prolist> getProlist(){
            return this.prolist;
        }
        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setPcUsername(String PcUsername){
            this.PcUsername = PcUsername;
        }
        public String getPcUsername(){
            return this.PcUsername;
        }
        public void setCompany(String company){
            this.company = company;
        }
        public String getCompany(){
            return this.company;
        }
        public void setComp_logo(String comp_logo){
            this.comp_logo = comp_logo;
        }
        public String getComp_logo(){
            return this.comp_logo;
        }
        public void setFlag(int flag){
            this.flag = flag;
        }
        public int getFlag(){
            return this.flag;
        }
        public void setContent(String content){
            this.content = content;
        }
        public String getContent(){
            return this.content;
        }
        public void setProShuanTime(String ProShuanTime){
            this.ProShuanTime = ProShuanTime;
        }
        public String getProShuanTime(){
            return this.ProShuanTime;
        }
        public class Prolist {
            private int id;

            private String PcUsername;

            private String AppUserId;

            private String first_zimu;

            private String Subject;

            private String isjbyw;

            private String mode;

            private String otc;

            private String danwei_sc;

            private String danwei;

            private String image;

            private String app_video;

            private String x_gg;

            private String x_bz;

            private String x_yfyl;

            private String maidian;

            private String name;

            private String x_pzwh;

            private String app_keshi;

            private String zc;

            private String newhit;

            private String shua;

            private String time;

            private String fbtime;

            private String Content;

            public void setId(int id){
                this.id = id;
            }
            public int getId(){
                return this.id;
            }
            public void setPcUsername(String PcUsername){
                this.PcUsername = PcUsername;
            }
            public String getPcUsername(){
                return this.PcUsername;
            }
            public void setAppUserId(String AppUserId){
                this.AppUserId = AppUserId;
            }
            public String getAppUserId(){
                return this.AppUserId;
            }
            public void setFirst_zimu(String first_zimu){
                this.first_zimu = first_zimu;
            }
            public String getFirst_zimu(){
                return this.first_zimu;
            }
            public void setSubject(String Subject){
                this.Subject = Subject;
            }
            public String getSubject(){
                return this.Subject;
            }
            public void setIsjbyw(String isjbyw){
                this.isjbyw = isjbyw;
            }
            public String getIsjbyw(){
                return this.isjbyw;
            }
            public void setMode(String mode){
                this.mode = mode;
            }
            public String getMode(){
                return this.mode;
            }
            public void setOtc(String otc){
                this.otc = otc;
            }
            public String getOtc(){
                return this.otc;
            }
            public void setDanwei_sc(String danwei_sc){
                this.danwei_sc = danwei_sc;
            }
            public String getDanwei_sc(){
                return this.danwei_sc;
            }
            public void setDanwei(String danwei){
                this.danwei = danwei;
            }
            public String getDanwei(){
                return this.danwei;
            }
            public void setImage(String image){
                this.image = image;
            }
            public String getImage(){
                return this.image;
            }
            public void setApp_video(String app_video){
                this.app_video = app_video;
            }
            public String getApp_video(){
                return this.app_video;
            }
            public void setX_gg(String x_gg){
                this.x_gg = x_gg;
            }
            public String getX_gg(){
                return this.x_gg;
            }
            public void setX_bz(String x_bz){
                this.x_bz = x_bz;
            }
            public String getX_bz(){
                return this.x_bz;
            }
            public void setX_yfyl(String x_yfyl){
                this.x_yfyl = x_yfyl;
            }
            public String getX_yfyl(){
                return this.x_yfyl;
            }
            public void setMaidian(String maidian){
                this.maidian = maidian;
            }
            public String getMaidian(){
                return this.maidian;
            }
            public void setName(String name){
                this.name = name;
            }
            public String getName(){
                return this.name;
            }
            public void setX_pzwh(String x_pzwh){
                this.x_pzwh = x_pzwh;
            }
            public String getX_pzwh(){
                return this.x_pzwh;
            }
            public void setApp_keshi(String app_keshi){
                this.app_keshi = app_keshi;
            }
            public String getApp_keshi(){
                return this.app_keshi;
            }
            public void setZc(String zc){
                this.zc = zc;
            }
            public String getZc(){
                return this.zc;
            }
            public void setNewhit(String newhit){
                this.newhit = newhit;
            }
            public String getNewhit(){
                return this.newhit;
            }
            public void setShua(String shua){
                this.shua = shua;
            }
            public String getShua(){
                return this.shua;
            }
            public void setTime(String time){
                this.time = time;
            }
            public String getTime(){
                return this.time;
            }
            public void setFbtime(String fbtime){
                this.fbtime = fbtime;
            }
            public String getFbtime(){
                return this.fbtime;
            }
            public void setContent(String Content){
                this.Content = Content;
            }
            public String getContent(){
                return this.Content;
            }

        }
    }
}
