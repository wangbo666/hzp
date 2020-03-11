package com.qgyyzs.globalcosmetics.bean;

/**
 * Created by Administrator on 2017/4/19.
 */

public class BidBean {
    private int id;
    private String name;//药品名称
    private String spec;//规格
    private String price;//中标价
    private String agent;//剂型
    private String vender;//厂家
    private String area;//地区
    private String type;//是中标 还是医保

    public BidBean(int id, String name, String spec, String price, String agent, String vender, String area, String type) {
        this.id = id;
        this.name = name;
        this.spec = spec;
        this.price = price;
        this.agent = agent;
        this.vender = vender;
        this.area = area;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getVender() {
        return vender;
    }

    public void setVender(String vender) {
        this.vender = vender;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "BidBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", spec='" + spec + '\'' +
                ", price='" + price + '\'' +
                ", agent='" + agent + '\'' +
                ", vender='" + vender + '\'' +
                ", area='" + area + '\'' +
                '}';
    }
}
