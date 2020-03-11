package com.qgyyzs.globalcosmetics.http.retrofit;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * http响应参数实体类
 * 通过Gson解析属性名称需要与服务器返回字段对应,或者使用注解@SerializedName
 * 备注:这里与服务器约定返回格式
 *
 * @author ZhongDaFeng
 */
public class HttpResponse {

    /**
     * 状态码
     */
    @SerializedName("Result")
    private int Result;

    /**
     * 描述信息
     */
    @SerializedName("Msg")
    private String Msg;


    /**
     * 数据对象[成功返回对象,失败返回错误说明]
     */
    @SerializedName("jsonData")
    private Object jsonData;

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public int getResult() {
        return Result;
    }

    public void setResult(int result) {
        Result = result;
    }

    public Object getJsonData() {
        return jsonData;
    }

    public void setJsonData(Object jsonData) {
        this.jsonData = jsonData;
    }

    /**
     * 是否成功(这里约定1)
     *
     * @return
     */
    public boolean isSuccess() {
        return Result == 1 ? true : false;
    }

    public boolean isTokenError(){ return Result == -2 ?true:false;  }

    public String toString() {
        String response = "{\"Result\": " + Result + ",\"Msg\":" + "\""+Msg + "\""+ ",\"jsonData\":" + new Gson().toJson(jsonData) + "}";
        return response;
    }

}
