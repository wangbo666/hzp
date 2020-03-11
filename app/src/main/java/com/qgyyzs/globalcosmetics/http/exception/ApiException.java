package com.qgyyzs.globalcosmetics.http.exception;

/**
 * api接口错误/异常统一处理类
 * 异常=[程序异常,网络异常,解析异常..]
 * 错误=[接口逻辑错误eg:{code:-101,msg:账号密码错误}]
 *
 * @author ZhongDaFeng
 */
public class ApiException extends Exception {
    private int Result;//错误码
    private String Msg;//错误信息

    public ApiException(Throwable throwable, int Result) {
        super(throwable);
        this.Result = Result;
    }

    public ApiException(int Result, String Msg) {
        this.Result = Result;
        this.Msg = Msg;
    }

    public int getResult() {
        return Result;
    }

    public void setResult(int result) {
        Result = result;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }
}
