package com.qgyyzs.globalcosmetics.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/10.
 */

public class ProductClassBean implements Serializable{
    private int id;
    private String type1;

    public ProductClassBean(int id, String type1) {
        this.id = id;
        this.type1 = type1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    @Override
    public String toString() {
        return "ProductClassBean{" +
                "id=" + id +
                ", type1='" + type1 + '\'' +
                '}';
    }
}
