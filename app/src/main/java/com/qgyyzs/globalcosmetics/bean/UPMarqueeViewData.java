package com.qgyyzs.globalcosmetics.bean;

/**
 * Created by Administrator on 2017/6/6.
 */

public class UPMarqueeViewData {
    private int id;
    private String title;
    private String value;
    private String url;

    public UPMarqueeViewData(int id, String title, String value, String url) {
        super();
        this.id = id;
        this.title = title;
        this.value = value;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
