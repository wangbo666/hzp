package com.qgyyzs.globalcosmetics.bean;

/**
 * Created by Administrator on 2017/4/17.
 */

public class MedicalNewsBean {
    private int id;
    private String title;
    private String looknum;
    private String time;
    private String img;

    public MedicalNewsBean(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public MedicalNewsBean(int id, String title, String looknum, String time, String img) {
        this.id = id;
        this.title = title;
        this.looknum = looknum;
        this.time = time;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLooknum() {
        return looknum;
    }

    public void setLooknum(String looknum) {
        this.looknum = looknum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "MedicalNewsBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", looknum='" + looknum + '\'' +
                ", time='" + time + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
