package com.qgyyzs.globalcosmetics.bean;

/**
 * Created by Administrator on 2017/10/12 0012.
 */

public class IntentionDetialBean {
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
        private UserInfo UserInfo;

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

        private int CreateFrom;

        public void setUserInfo(UserInfo UserInfo){
            this.UserInfo = UserInfo;
        }
        public UserInfo getUserInfo(){
            return this.UserInfo;
        }
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
        public void setCreateFrom(int CreateFrom){
            this.CreateFrom = CreateFrom;
        }
        public int getCreateFrom(){
            return this.CreateFrom;
        }
        public class UserInfo {
            private int Id;

            private String AccountName;

            private String PcUsername;

            private String NimID;

            private boolean IsPrimary;

            private String HeadImg;

            private String JieShao;

            private String RealName;

            private boolean IsOpen;

            private String LinkTel;

            private String ResPlace;

            private String Company;

            private String KefuArea;

            private String LoginTime;

            private boolean LoginState;

            private String LoginPosition;

            private String LastRequestTime;

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
            public void setIsPrimary(boolean IsPrimary){
                this.IsPrimary = IsPrimary;
            }
            public boolean getIsPrimary(){
                return this.IsPrimary;
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
            public void setLoginTime(String LoginTime){
                this.LoginTime = LoginTime;
            }
            public String getLoginTime(){
                return this.LoginTime;
            }
            public void setLoginState(boolean LoginState){
                this.LoginState = LoginState;
            }
            public boolean getLoginState(){
                return this.LoginState;
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

        }
    }
}
