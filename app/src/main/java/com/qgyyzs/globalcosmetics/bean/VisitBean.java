package com.qgyyzs.globalcosmetics.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public class VisitBean {
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

        private String AddTime;

        private String F_PcUsername;

        private int F_proid;

        private String F_proname;

        private String U_name;

        private boolean IsLook;

        public void setIsLook(boolean IsLook){
            this.IsLook = IsLook;
        }
        public boolean getIsLook(){
            return this.IsLook;
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
        public void setAddTime(String AddTime){
            this.AddTime = AddTime;
        }
        public String getAddTime(){
            return this.AddTime;
        }
        public void setF_PcUsername(String F_PcUsername){
            this.F_PcUsername = F_PcUsername;
        }
        public String getF_PcUsername(){
            return this.F_PcUsername;
        }
        public void setF_proid(int F_proid){
            this.F_proid = F_proid;
        }
        public int getF_proid(){
            return this.F_proid;
        }
        public void setF_proname(String F_proname){
            this.F_proname = F_proname;
        }
        public String getF_proname(){
            return this.F_proname;
        }
        public void setU_name(String U_name){
            this.U_name = U_name;
        }
        public String getU_name(){
            return this.U_name;
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

            private boolean LoginState;

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
            public void setLoginState(boolean LoginState){
                this.LoginState = LoginState;
            }
            public boolean getLoginState(){
                return this.LoginState;
            }

        }
    }
}
