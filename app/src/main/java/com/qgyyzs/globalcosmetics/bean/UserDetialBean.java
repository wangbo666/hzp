package com.qgyyzs.globalcosmetics.bean;

/**
 * Created by Administrator on 2017/11/22 0022.
 */

public class UserDetialBean {
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
        private boolean IsDaiLiShang;

        private int Id;

        private String AccountName;

        private String Mobile;

        private String AccountPW;

        private int ismd5;

        private String Pwd_dl;

        private String SeeDlProvince;

        private int flag;

        private String province_login;

        private String Province;

        private String city;

        private String userip;

        private int logintimes;

        private int AppShuaNum;

        private String AppShuaTime;

        private int shuanum;

        private String userclass;

        private String vip_start;

        private String vip_end;

        private String PrintFlag;

        private String Postcode;

        private String PcUsername;

        private boolean IsPrimary;

        private int IsDaiLi;

        private int ShenFen;

        private String NimID;

        private String NimPW;

        private String HeadImg;

        private String RealName;

        private String LinkTel;

        private boolean IsOpen;

        private String ResPlace;

        private String Company;

        private String JieShao;

        private String KefuArea;

        private String RegTime;

        private String Token;

        private String LoginMobileGUID;

        private String LoginPosition;

        private String LastRequestTime;

        private String LastLoginTime;

        private boolean LoginState;

        private String daili_sort;

        public String getDaili_sort() {
            return daili_sort;
        }

        public void setDaili_sort(String daili_sort) {
            this.daili_sort = daili_sort;
        }

        private int movetel;

        public void setIsDaiLiShang(boolean IsDaiLiShang){
            this.IsDaiLiShang = IsDaiLiShang;
        }
        public boolean getIsDaiLiShang(){
            return this.IsDaiLiShang;
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
        public void setMobile(String Mobile){
            this.Mobile = Mobile;
        }
        public String getMobile(){
            return this.Mobile;
        }
        public void setAccountPW(String AccountPW){
            this.AccountPW = AccountPW;
        }
        public String getAccountPW(){
            return this.AccountPW;
        }
        public void setIsmd5(int ismd5){
            this.ismd5 = ismd5;
        }
        public int getIsmd5(){
            return this.ismd5;
        }
        public void setPwd_dl(String Pwd_dl){
            this.Pwd_dl = Pwd_dl;
        }
        public String getPwd_dl(){
            return this.Pwd_dl;
        }
        public void setSeeDlProvince(String SeeDlProvince){
            this.SeeDlProvince = SeeDlProvince;
        }
        public String getSeeDlProvince(){
            return this.SeeDlProvince;
        }
        public void setFlag(int flag){
            this.flag = flag;
        }
        public int getFlag(){
            return this.flag;
        }
        public void setProvince_login(String province_login){
            this.province_login = province_login;
        }
        public String getProvince_login(){
            return this.province_login;
        }
        public void setProvince(String Province){
            this.Province = Province;
        }
        public String getProvince(){
            return this.Province;
        }
        public void setCity(String city){
            this.city = city;
        }
        public String getCity(){
            return this.city;
        }
        public void setUserip(String userip){
            this.userip = userip;
        }
        public String getUserip(){
            return this.userip;
        }
        public void setLogintimes(int logintimes){
            this.logintimes = logintimes;
        }
        public int getLogintimes(){
            return this.logintimes;
        }
        public void setAppShuaNum(int AppShuaNum){
            this.AppShuaNum = AppShuaNum;
        }
        public int getAppShuaNum(){
            return this.AppShuaNum;
        }
        public void setAppShuaTime(String AppShuaTime){
            this.AppShuaTime = AppShuaTime;
        }
        public String getAppShuaTime(){
            return this.AppShuaTime;
        }
        public void setShuanum(int shuanum){
            this.shuanum = shuanum;
        }
        public int getShuanum(){
            return this.shuanum;
        }
        public void setUserclass(String userclass){
            this.userclass = userclass;
        }
        public String getUserclass(){
            return this.userclass;
        }
        public void setVip_start(String vip_start){
            this.vip_start = vip_start;
        }
        public String getVip_start(){
            return this.vip_start;
        }
        public void setVip_end(String vip_end){
            this.vip_end = vip_end;
        }
        public String getVip_end(){
            return this.vip_end;
        }
        public void setPrintFlag(String PrintFlag){
            this.PrintFlag = PrintFlag;
        }
        public String getPrintFlag(){
            return this.PrintFlag;
        }
        public void setPostcode(String Postcode){
            this.Postcode = Postcode;
        }
        public String getPostcode(){
            return this.Postcode;
        }
        public void setPcUsername(String PcUsername){
            this.PcUsername = PcUsername;
        }
        public String getPcUsername(){
            return this.PcUsername;
        }
        public void setIsPrimary(boolean IsPrimary){
            this.IsPrimary = IsPrimary;
        }
        public boolean getIsPrimary(){
            return this.IsPrimary;
        }
        public void setIsDaiLi(int IsDaiLi){
            this.IsDaiLi = IsDaiLi;
        }
        public int getIsDaiLi(){
            return this.IsDaiLi;
        }
        public void setShenFen(int ShenFen){
            this.ShenFen = ShenFen;
        }
        public int getShenFen(){
            return this.ShenFen;
        }
        public void setNimID(String NimID){
            this.NimID = NimID;
        }
        public String getNimID(){
            return this.NimID;
        }
        public void setNimPW(String NimPW){
            this.NimPW = NimPW;
        }
        public String getNimPW(){
            return this.NimPW;
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
        public void setLinkTel(String LinkTel){
            this.LinkTel = LinkTel;
        }
        public String getLinkTel(){
            return this.LinkTel;
        }
        public void setIsOpen(boolean IsOpen){
            this.IsOpen = IsOpen;
        }
        public boolean getIsOpen(){
            return this.IsOpen;
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
        public void setJieShao(String JieShao){
            this.JieShao = JieShao;
        }
        public String getJieShao(){
            return this.JieShao;
        }
        public void setKefuArea(String KefuArea){
            this.KefuArea = KefuArea;
        }
        public String getKefuArea(){
            return this.KefuArea;
        }
        public void setRegTime(String RegTime){
            this.RegTime = RegTime;
        }
        public String getRegTime(){
            return this.RegTime;
        }
        public void setToken(String Token){
            this.Token = Token;
        }
        public String getToken(){
            return this.Token;
        }
        public void setLoginMobileGUID(String LoginMobileGUID){
            this.LoginMobileGUID = LoginMobileGUID;
        }
        public String getLoginMobileGUID(){
            return this.LoginMobileGUID;
        }
        public void setLoginPosition(String LoginPosition){
            this.LoginPosition = LoginPosition;
        }
        public String getLoginPosition(){
            return this.LoginPosition;
        }
        public void setLastRequestTime(String LastRequestTime){
            this.LastRequestTime = LastRequestTime;
        }
        public String getLastRequestTime(){
            return this.LastRequestTime;
        }
        public void setLastLoginTime(String LastLoginTime){
            this.LastLoginTime = LastLoginTime;
        }
        public String getLastLoginTime(){
            return this.LastLoginTime;
        }
        public void setLoginState(boolean LoginState){
            this.LoginState = LoginState;
        }
        public boolean getLoginState(){
            return this.LoginState;
        }
        public void setMovetel(int movetel){
            this.movetel = movetel;
        }
        public int getMovetel(){
            return this.movetel;
        }

    }
}
