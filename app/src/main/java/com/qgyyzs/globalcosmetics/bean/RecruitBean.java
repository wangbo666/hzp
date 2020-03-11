package com.qgyyzs.globalcosmetics.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public class RecruitBean {
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
        private String newWorkYear;

        private String newJobType;

        private String newSalary;

        private String newEducation;

        private int Id;

        private int UserId;

        private String company;

        private String jobType;

        private String province;

        private String city;

        private String jobName;

        private String hit;

        private String workYear;

        private String salary;

        private String Education;

        private String Email;

        private String jobDes;

        private String addtime;

        private String ExpireTime;

        private boolean isdel;

        private int shenhe;

        public void setNewWorkYear(String newWorkYear){
            this.newWorkYear = newWorkYear;
        }
        public String getNewWorkYear(){
            return this.newWorkYear;
        }
        public void setNewJobType(String newJobType){
            this.newJobType = newJobType;
        }
        public String getNewJobType(){
            return this.newJobType;
        }
        public void setNewSalary(String newSalary){
            this.newSalary = newSalary;
        }
        public String getNewSalary(){
            return this.newSalary;
        }
        public void setNewEducation(String newEducation){
            this.newEducation = newEducation;
        }
        public String getNewEducation(){
            return this.newEducation;
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
        public void setCompany(String company){
            this.company = company;
        }
        public String getCompany(){
            return this.company;
        }
        public void setJobType(String jobType){
            this.jobType = jobType;
        }
        public String getJobType(){
            return this.jobType;
        }
        public void setProvince(String province){
            this.province = province;
        }
        public String getProvince(){
            return this.province;
        }
        public void setCity(String city){
            this.city = city;
        }
        public String getCity(){
            return this.city;
        }
        public void setJobName(String jobName){
            this.jobName = jobName;
        }
        public String getJobName(){
            return this.jobName;
        }
        public void setHit(String hit){
            this.hit = hit;
        }
        public String getHit(){
            return this.hit;
        }
        public void setWorkYear(String workYear){
            this.workYear = workYear;
        }
        public String getWorkYear(){
            return this.workYear;
        }
        public void setSalary(String salary){
            this.salary = salary;
        }
        public String getSalary(){
            return this.salary;
        }
        public void setEducation(String Education){
            this.Education = Education;
        }
        public String getEducation(){
            return this.Education;
        }
        public void setEmail(String Email){
            this.Email = Email;
        }
        public String getEmail(){
            return this.Email;
        }
        public void setJobDes(String jobDes){
            this.jobDes = jobDes;
        }
        public String getJobDes(){
            return this.jobDes;
        }
        public void setAddtime(String addtime){
            this.addtime = addtime;
        }
        public String getAddtime(){
            return this.addtime;
        }
        public void setExpireTime(String ExpireTime){
            this.ExpireTime = ExpireTime;
        }
        public String getExpireTime(){
            return this.ExpireTime;
        }
        public void setIsdel(boolean isdel){
            this.isdel = isdel;
        }
        public boolean getIsdel(){
            return this.isdel;
        }
        public void setShenhe(int shenhe){
            this.shenhe = shenhe;
        }
        public int getShenhe(){
            return this.shenhe;
        }

    }
}
