package com.qgyyzs.globalcosmetics.bean;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class RegisterBean {
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
        private String register;

        private String tjcode;

        public void setRegister(String register){
            this.register = register;
        }
        public String getRegister(){
            return this.register;
        }
        public void setTjcode(String tjcode){
            this.tjcode = tjcode;
        }
        public String getTjcode(){
            return this.tjcode;
        }

    }
}
