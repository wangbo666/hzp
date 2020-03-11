package com.qgyyzs.globalcosmetics.bean;

/**
 * Created by AMing on 16/1/14.
 * Company RongCloud
 */
public class FriendBean{
    private int UserId;
    private int F_UserId;
    private int State;
    private boolean is_kehu;
    private String AddTime;
    private String NickName;
    private String HeadImg;
    private String NimID;
    private String Alias;

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public String getNimID() {
        return NimID;
    }

    public void setNimID(String nimID) {
        NimID = nimID;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    private String letters;

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
    public void setIs_kehu(boolean is_kehu){
        this.is_kehu = is_kehu;
    }
    public boolean getIs_kehu(){
        return this.is_kehu;
    }
    public void setAddTime(String AddTime){
        this.AddTime = AddTime;
    }
    public String getAddTime(){
        return this.AddTime;
    }
    public void setNickName(String NickName){
        this.NickName = NickName;
    }
    public String getNickName(){
        return this.NickName;
    }
    public void setHeadImg(String HeadImg){
        this.HeadImg = HeadImg;
    }
    public String getHeadImg(){
        return this.HeadImg;
    }
    public FriendBean(int UserId, int F_UserId, int State, String AddTime, String NickName, String HeadImg, String Nimid){
        this.UserId=UserId;
        this.F_UserId=F_UserId;
        this.State=State;
        this.AddTime=AddTime;
        this.NickName=NickName;
        this.HeadImg=HeadImg;
        this.NimID=Nimid;
    }
    public FriendBean(){

    }
}
