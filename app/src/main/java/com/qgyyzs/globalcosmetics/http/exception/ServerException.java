package com.qgyyzs.globalcosmetics.http.exception;

/**
 * 自定义服务器错误
 *
 * @author ZhongDaFeng
 */
public class ServerException extends RuntimeException {
    private int Result;
    private String Msg;

    public ServerException(int Result, String Msg) {
        this.Result = Result;
        this.Msg = Msg;
    }

    public int getResult() {
        return Result;
    }

    public String getMsg() {
        return Msg;
    }
}
