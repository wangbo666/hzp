package com.qgyyzs.globalcosmetics.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class JobTypeBean {
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
        private int Key;

        private String Value;

        public void setKey(int Key){
            this.Key = Key;
        }
        public int getKey(){
            return this.Key;
        }
        public void setValue(String Value){
            this.Value = Value;
        }
        public String getValue(){
            return this.Value;
        }

    }
}
