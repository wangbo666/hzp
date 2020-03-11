package com.qgyyzs.globalcosmetics.bean;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class UserinfoBean {
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
        private int state;

        private boolean IsPrimary;

        private int Id;

        public int getPrimaryFlag() {
            return PrimaryFlag;
        }

        public void setPrimaryFlag(int primaryFlag) {
            PrimaryFlag = primaryFlag;
        }

        private int PrimaryFlag;

        private String AccountName;

        private String PcUsername;

        private String NimID;

        private String HeadImg;

        private String JieShao;

        private String RealName;

        private boolean IsOpen;

        private String LinkTel;

        private String ResPlace;

        private String Company;

        private String KefuArea;

        private boolean LoginState;

        private String Alias;

        private String ShenFen;

        private String Province;

        private String city;

        public String getProvince() {
            return Province;
        }

        public void setProvince(String province) {
            Province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getShenFen() {
            return ShenFen;
        }

        public void setShenFen(String shenFen) {
            ShenFen = shenFen;
        }

        public String getAlias() {
            return Alias;
        }

        public void setAlias(String alias) {
            Alias = alias;
        }

        public int getState() {
            return state;
        }
        public void setState(int state) {
            this.state = state;
        }
        public boolean isPrimary() {
            return IsPrimary;
        }
        public void setPrimary(boolean primary) {
            IsPrimary = primary;
        }
        public void setId(int Id){
            this.Id = Id;
        }
        public int getId(){
            return this.Id;
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
        public void setJieShao(String JieShao){
            this.JieShao = JieShao;
        }
        public String getJieShao(){
            return this.JieShao;
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
        public void setCompany(String Company){
            this.Company = Company;
        }
        public String getCompany(){
            return this.Company;
        }
        public void setKefuArea(String KefuArea){
            this.KefuArea = KefuArea;
        }
        public String getKefuArea(){
            return this.KefuArea;
        }
        public void setLoginState(boolean LoginState){
            this.LoginState = LoginState;
        }
        public boolean getLoginState(){
            return this.LoginState;
        }

    }
}
