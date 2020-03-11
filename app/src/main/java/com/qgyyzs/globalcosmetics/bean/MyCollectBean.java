package com.qgyyzs.globalcosmetics.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/30.
 */

public class MyCollectBean {
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
        private Eqinfoz eqinfoz;

        private int Id;

        private int UserId;

        private String Addtime;

        private int ProId;

        private int State;

        public void setEqinfoz(Eqinfoz eqinfoz){
            this.eqinfoz = eqinfoz;
        }
        public Eqinfoz getEqinfoz(){
            return this.eqinfoz;
        }
        public void setId(int Id){
            this.Id = Id;
        }
        public int getId(){
            return this.Id;
        }
        public void setUserId(int UserId){
            this.UserId = UserId;
        }
        public int getUserId(){
            return this.UserId;
        }
        public void setAddtime(String Addtime){
            this.Addtime = Addtime;
        }
        public String getAddtime(){
            return this.Addtime;
        }
        public void setProId(int ProId){
            this.ProId = ProId;
        }
        public int getProId(){
            return this.ProId;
        }
        public void setState(int State){
            this.State = State;
        }
        public int getState(){
            return this.State;
        }
        public class Eqinfoz {
            private int id;

            private String PcUsername;

            private String AppUserId;

            private String first_zimu;

            private String Subject;

            private int isjbyw;

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

            private int newhit;

            private int shua;

            private String time;

            private String fbtime;

            private String Content;

            private int ClassID;

            private String ppai_name;

            public String getPpai_name() {
                return ppai_name;
            }

            public void setPpai_name(String ppai_name) {
                this.ppai_name = ppai_name;
            }

            public int getClassID() {
                return ClassID;
            }

            public void setClassID(int classID) {
                ClassID = classID;
            }

            public String getClassName() {
                return ClassName;
            }

            public void setClassName(String className) {
                ClassName = className;
            }

            public int getnClassID() {
                return nClassID;
            }

            public void setnClassID(int nClassID) {
                this.nClassID = nClassID;
            }

            public String getnClassName() {
                return nClassName;
            }

            public void setnClassName(String nClassName) {
                this.nClassName = nClassName;
            }

            private String ClassName;

            private int nClassID;

            private String nClassName;

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
            public void setIsjbyw(int isjbyw){
                this.isjbyw = isjbyw;
            }
            public int getIsjbyw(){
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
            public void setNewhit(int newhit){
                this.newhit = newhit;
            }
            public int getNewhit(){
                return this.newhit;
            }
            public void setShua(int shua){
                this.shua = shua;
            }
            public int getShua(){
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
