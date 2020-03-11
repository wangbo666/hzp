package com.qgyyzs.globalcosmetics.bean;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class ProxySetInfoBean {
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

        private int UserId;

        private String daili_sort;

        private String daili_type;

        private String qudao;

        private String czd;

        private String keshi;

        private String AddTime;

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
        public void setAddTime(String AddTime){
            this.AddTime = AddTime;
        }
        public String getAddTime(){
            return this.AddTime;
        }
        public class UserInfo {
            private int id;

            private String NimID;

            private String HeadImg;

            private boolean IsOpen;

            private String JieShao;

            private String Name;

            private String Tel;

            public void setId(int id){
                this.id = id;
            }
            public int getId(){
                return this.id;
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
            public void setIsOpen(boolean IsOpen){
                this.IsOpen = IsOpen;
            }
            public boolean getIsOpen(){
                return this.IsOpen;
            }
            public void setJieShao(String JieShao){
                this.JieShao = JieShao;
            }
            public String getJieShao(){
                return this.JieShao;
            }
            public void setName(String Name){
                this.Name = Name;
            }
            public String getName(){
                return this.Name;
            }
            public void setTel(String Tel){
                this.Tel = Tel;
            }
            public String getTel(){
                return this.Tel;
            }

        }

    }
}
