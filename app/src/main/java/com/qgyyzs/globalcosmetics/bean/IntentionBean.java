package com.qgyyzs.globalcosmetics.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/12 0012.
 */

public class IntentionBean {
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

        private String FaJianRen;

        private String ShouJianRen;

        private String Title;

        private String Content;

        private String CreateTime;

        private int IsLook;

        private int IsDel;

        private String IpAddress;

        private String DailiArea;

        private int DailiId;

        private int IsDLS;

        public void setId(int Id){
            this.Id = Id;
        }
        public int getId(){
            return this.Id;
        }
        public void setFaJianRen(String FaJianRen){
            this.FaJianRen = FaJianRen;
        }
        public String getFaJianRen(){
            return this.FaJianRen;
        }
        public void setShouJianRen(String ShouJianRen){
            this.ShouJianRen = ShouJianRen;
        }
        public String getShouJianRen(){
            return this.ShouJianRen;
        }
        public void setTitle(String Title){
            this.Title = Title;
        }
        public String getTitle(){
            return this.Title;
        }
        public void setContent(String Content){
            this.Content = Content;
        }
        public String getContent(){
            return this.Content;
        }
        public void setCreateTime(String CreateTime){
            this.CreateTime = CreateTime;
        }
        public String getCreateTime(){
            return this.CreateTime;
        }
        public void setIsLook(int IsLook){
            this.IsLook = IsLook;
        }
        public int getIsLook(){
            return this.IsLook;
        }
        public void setIsDel(int IsDel){
            this.IsDel = IsDel;
        }
        public int getIsDel(){
            return this.IsDel;
        }
        public void setIpAddress(String IpAddress){
            this.IpAddress = IpAddress;
        }
        public String getIpAddress(){
            return this.IpAddress;
        }
        public void setDailiArea(String DailiArea){
            this.DailiArea = DailiArea;
        }
        public String getDailiArea(){
            return this.DailiArea;
        }
        public void setDailiId(int DailiId){
            this.DailiId = DailiId;
        }
        public int getDailiId(){
            return this.DailiId;
        }
        public void setIsDLS(int IsDLS){
            this.IsDLS = IsDLS;
        }
        public int getIsDLS(){
            return this.IsDLS;
        }

    }
}
