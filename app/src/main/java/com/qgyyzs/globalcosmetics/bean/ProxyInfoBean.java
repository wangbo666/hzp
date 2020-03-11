package com.qgyyzs.globalcosmetics.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */

public class ProxyInfoBean {
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

        private int AppUserId;

        private String PcUsername;

        private String DailiArea;

        private String Content;

        private String Qudao;

        private String LinkTel;

        private String LinkMan;

        private String DailiType;

        private String ShenFen;

        private String Company;

        private int ProId;

        private String ProName;

        private String OTC;

        private String ZsCompany;

        private int IsJBYW;

        private String ZsUsername;

        private int IsDel;

        private int Flag;

        private String gqtime;

        public void setId(int Id){
            this.Id = Id;
        }
        public int getId(){
            return this.Id;
        }
        public void setAppUserId(int AppUserId){
            this.AppUserId = AppUserId;
        }
        public int getAppUserId(){
            return this.AppUserId;
        }
        public void setPcUsername(String PcUsername){
            this.PcUsername = PcUsername;
        }
        public String getPcUsername(){
            return this.PcUsername;
        }
        public void setDailiArea(String DailiArea){
            this.DailiArea = DailiArea;
        }
        public String getDailiArea(){
            return this.DailiArea;
        }
        public void setContent(String Content){
            this.Content = Content;
        }
        public String getContent(){
            return this.Content;
        }
        public void setQudao(String Qudao){
            this.Qudao = Qudao;
        }
        public String getQudao(){
            return this.Qudao;
        }
        public void setLinkTel(String LinkTel){
            this.LinkTel = LinkTel;
        }
        public String getLinkTel(){
            return this.LinkTel;
        }
        public void setLinkMan(String LinkMan){
            this.LinkMan = LinkMan;
        }
        public String getLinkMan(){
            return this.LinkMan;
        }
        public void setDailiType(String DailiType){
            this.DailiType = DailiType;
        }
        public String getDailiType(){
            return this.DailiType;
        }
        public void setShenFen(String ShenFen){
            this.ShenFen = ShenFen;
        }
        public String getShenFen(){
            return this.ShenFen;
        }
        public void setCompany(String Company){
            this.Company = Company;
        }
        public String getCompany(){
            return this.Company;
        }
        public void setProId(int ProId){
            this.ProId = ProId;
        }
        public int getProId(){
            return this.ProId;
        }
        public void setProName(String ProName){
            this.ProName = ProName;
        }
        public String getProName(){
            return this.ProName;
        }
        public void setOTC(String OTC){
            this.OTC = OTC;
        }
        public String getOTC(){
            return this.OTC;
        }
        public void setZsCompany(String ZsCompany){
            this.ZsCompany = ZsCompany;
        }
        public String getZsCompany(){
            return this.ZsCompany;
        }
        public void setIsJBYW(int IsJBYW){
            this.IsJBYW = IsJBYW;
        }
        public int getIsJBYW(){
            return this.IsJBYW;
        }
        public void setZsUsername(String ZsUsername){
            this.ZsUsername = ZsUsername;
        }
        public String getZsUsername(){
            return this.ZsUsername;
        }
        public void setIsDel(int IsDel){
            this.IsDel = IsDel;
        }
        public int getIsDel(){
            return this.IsDel;
        }
        public void setFlag(int Flag){
            this.Flag = Flag;
        }
        public int getFlag(){
            return this.Flag;
        }
        public void setGqtime(String gqtime){
            this.gqtime = gqtime;
        }
        public String getGqtime(){
            return this.gqtime;
        }

    }

}
