package com.qgyyzs.globalcosmetics.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public class HomeOptionBean {
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

        private String OptionName;

        private int OptionOrder;

        public void setId(int Id){
            this.Id = Id;
        }
        public int getId(){
            return this.Id;
        }
        public void setOptionName(String OptionName){
            this.OptionName = OptionName;
        }
        public String getOptionName(){
            return this.OptionName;
        }
        public void setOptionOrder(int OptionOrder){
            this.OptionOrder = OptionOrder;
        }
        public int getOptionOrder(){
            return this.OptionOrder;
        }

    }
}
