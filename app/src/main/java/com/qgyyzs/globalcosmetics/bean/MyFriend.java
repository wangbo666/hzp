package com.qgyyzs.globalcosmetics.bean;

import java.util.List;

/**
 * Created by AMing on 16/1/14.
 * Company RongCloud
 */
public class MyFriend {
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
        private UserInfo UserInfo;

        private int Id;

        private int UserId;

        private int F_UserId;

        private int State;

        private String is_kehu;

        private String AddTime;

        private String Alias;

        public String getAlias() {
            return Alias;
        }

        public void setAlias(String alias) {
            Alias = alias;
        }

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
        public void setUserId(int UserId){
            this.UserId = UserId;
        }
        public int getUserId(){
            return this.UserId;
        }
        public void setF_UserId(int F_UserId){
            this.F_UserId = F_UserId;
        }
        public int getF_UserId(){
            return this.F_UserId;
        }
        public void setState(int State){
            this.State = State;
        }
        public int getState(){
            return this.State;
        }
        public void setIs_kehu(String is_kehu){
            this.is_kehu = is_kehu;
        }
        public String getIs_kehu(){
            return this.is_kehu;
        }
        public void setAddTime(String AddTime){
            this.AddTime = AddTime;
        }
        public String getAddTime(){
            return this.AddTime;
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

            private String LastLoginTime;

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
