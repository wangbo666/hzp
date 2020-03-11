package com.qgyyzs.globalcosmetics.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class ProxyLibraryBean {
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
        private int Id;

        private int UserId;

        private String daili_sort;

        private String daili_type;

        private String qudao;

        private String czd;

        private String keshi;

        private String AccountName;

        private String PcUsername;

        private String NimID;

        private String HeadImg;

        private String RealName;

        private boolean IsOpen;

        private String LinkTel;

        private String ResPlace;

        private String LastRequestTime;

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
        public void setDaili_sort(String daili_sort){
            this.daili_sort = daili_sort;
        }
        public String getDaili_sort(){
            return this.daili_sort;
        }
        public void setDaili_type(String daili_type){
            this.daili_type = daili_type;
        }
        public String getDaili_type(){
            return this.daili_type;
        }
        public void setQudao(String qudao){
            this.qudao = qudao;
        }
        public String getQudao(){
            return this.qudao;
        }
        public void setCzd(String czd){
            this.czd = czd;
        }
        public String getCzd(){
            return this.czd;
        }
        public void setKeshi(String keshi){
            this.keshi = keshi;
        }
        public String getKeshi(){
            return this.keshi;
        }
        public void setAccountName(String AccountName){
            this.AccountName = AccountName;
        }
        public String getAccountName(){
            return this.AccountName;
        }
        public void setPcUsername(String PcUsername){
            this.PcUsername = PcUsername;
        }
        public String getPcUsername(){
            return this.PcUsername;
        }
        public void setNimID(String NimID){
            this.NimID = NimID;
        }
        public String getNimID(){
            return this.NimID;
        }
        public void setHeadImg(String HeadImg){
            this.HeadImg = HeadImg;
        }
        public String getHeadImg(){
            return this.HeadImg;
        }
        public void setRealName(String RealName){
            this.RealName = RealName;
        }
        public String getRealName(){
            return this.RealName;
        }
        public void setIsOpen(boolean IsOpen){
            this.IsOpen = IsOpen;
        }
        public boolean getIsOpen(){
            return this.IsOpen;
        }
        public void setLinkTel(String LinkTel){
            this.LinkTel = LinkTel;
        }
        public String getLinkTel(){
            return this.LinkTel;
        }
        public void setResPlace(String ResPlace){
            this.ResPlace = ResPlace;
        }
        public String getResPlace(){
            return this.ResPlace;
        }
        public void setLastRequestTime(String LastRequestTime){
            this.LastRequestTime = LastRequestTime;
        }
        public String getLastRequestTime(){
            return this.LastRequestTime;
        }
    }
}
