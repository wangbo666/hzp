package com.qgyyzs.globalcosmetics.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class WebImgBean implements Serializable{
    private List<String> list ;

    private String index;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
