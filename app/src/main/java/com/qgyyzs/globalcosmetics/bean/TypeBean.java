package com.qgyyzs.globalcosmetics.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/29 0029.
 */

public class TypeBean {
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
        private int TypeId;

        private String TypeName;

        private int TypeOrder;

        public void setTypeId(int TypeId){
            this.TypeId = TypeId;
        }
        public int getTypeId(){
            return this.TypeId;
        }
        public void setTypeName(String TypeName){
            this.TypeName = TypeName;
        }
        public String getTypeName(){
            return this.TypeName;
        }
        public void setTypeOrder(int TypeOrder){
            this.TypeOrder = TypeOrder;
        }
        public int getTypeOrder(){
            return this.TypeOrder;
        }

    }
}
