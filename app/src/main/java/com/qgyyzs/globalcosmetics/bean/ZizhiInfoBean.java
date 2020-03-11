package com.qgyyzs.globalcosmetics.bean;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class ZizhiInfoBean {
    private int Result;

    private String Msg;

    private JsonData jsonData;

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
    public void setJsonData(JsonData jsonData){
        this.jsonData = jsonData;
    }
    public JsonData getJsonData(){
        return this.jsonData;
    }
    public class JsonData {
        private int id;

        private String zsname;

        private String zsjiguan;

        private String zsstart;

        private String zsend;

        private String PcUsername;

        private String fbtime;

        private String zsimage;

        private int isdel;

        private boolean shenghe;

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setZsname(String zsname){
            this.zsname = zsname;
        }
        public String getZsname(){
            return this.zsname;
        }
        public void setZsjiguan(String zsjiguan){
            this.zsjiguan = zsjiguan;
        }
        public String getZsjiguan(){
            return this.zsjiguan;
        }
        public void setZsstart(String zsstart){
            this.zsstart = zsstart;
        }
        public String getZsstart(){
            return this.zsstart;
        }
        public void setZsend(String zsend){
            this.zsend = zsend;
        }
        public String getZsend(){
            return this.zsend;
        }
        public void setPcUsername(String PcUsername){
            this.PcUsername = PcUsername;
        }
        public String getPcUsername(){
            return this.PcUsername;
        }
        public void setFbtime(String fbtime){
            this.fbtime = fbtime;
        }
        public String getFbtime(){
            return this.fbtime;
        }
        public void setZsimage(String zsimage){
            this.zsimage = zsimage;
        }
        public String getZsimage(){
            return this.zsimage;
        }
        public void setIsdel(int isdel){
            this.isdel = isdel;
        }
        public int getIsdel(){
            return this.isdel;
        }
        public void setShenghe(boolean shenghe){
            this.shenghe = shenghe;
        }
        public boolean getShenghe(){
            return this.shenghe;
        }

    }
}
