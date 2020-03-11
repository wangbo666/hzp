package com.qgyyzs.globalcosmetics.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public class NewsExpBean {
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
        private int Artcle_id;

        private String Artcle_title;

        private int Assort_id;

        private String Assort_name;

        private String Artcle_key;

        private String Artcle_from;

        private String Artcle_setup;

        private String Artcle_user;

        private int Artcle_hit;

        private String Artcle_date;

        private String StartTime;

        private String EndTime;

        private String DiDian;

        public void setArtcle_id(int Artcle_id){
            this.Artcle_id = Artcle_id;
        }
        public int getArtcle_id(){
            return this.Artcle_id;
        }
        public void setArtcle_title(String Artcle_title){
            this.Artcle_title = Artcle_title;
        }
        public String getArtcle_title(){
            return this.Artcle_title;
        }
        public void setAssort_id(int Assort_id){
            this.Assort_id = Assort_id;
        }
        public int getAssort_id(){
            return this.Assort_id;
        }
        public void setAssort_name(String Assort_name){
            this.Assort_name = Assort_name;
        }
        public String getAssort_name(){
            return this.Assort_name;
        }
        public void setArtcle_key(String Artcle_key){
            this.Artcle_key = Artcle_key;
        }
        public String getArtcle_key(){
            return this.Artcle_key;
        }
        public void setArtcle_from(String Artcle_from){
            this.Artcle_from = Artcle_from;
        }
        public String getArtcle_from(){
            return this.Artcle_from;
        }
        public void setArtcle_setup(String Artcle_setup){
            this.Artcle_setup = Artcle_setup;
        }
        public String getArtcle_setup(){
            return this.Artcle_setup;
        }
        public void setArtcle_user(String Artcle_user){
            this.Artcle_user = Artcle_user;
        }
        public String getArtcle_user(){
            return this.Artcle_user;
        }
        public void setArtcle_hit(int Artcle_hit){
            this.Artcle_hit = Artcle_hit;
        }
        public int getArtcle_hit(){
            return this.Artcle_hit;
        }
        public void setArtcle_date(String Artcle_date){
            this.Artcle_date = Artcle_date;
        }
        public String getArtcle_date(){
            return this.Artcle_date;
        }
        public void setStartTime(String StartTime){
            this.StartTime = StartTime;
        }
        public String getStartTime(){
            return this.StartTime;
        }
        public void setEndTime(String EndTime){
            this.EndTime = EndTime;
        }
        public String getEndTime(){
            return this.EndTime;
        }
        public void setDiDian(String DiDian){
            this.DiDian = DiDian;
        }
        public String getDiDian(){
            return this.DiDian;
        }

    }
}
