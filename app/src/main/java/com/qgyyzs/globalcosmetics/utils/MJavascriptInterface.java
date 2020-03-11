package com.qgyyzs.globalcosmetics.utils;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSONObject;
import com.qgyyzs.globalcosmetics.activity.PhotoBrowserActivity;
import com.qgyyzs.globalcosmetics.bean.WebImgBean;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class MJavascriptInterface {
    private WebImgBean bean;
    private Context context;

    public MJavascriptInterface(Context context){
        this.context=context;
    }

    @JavascriptInterface
    public void getImg(String paramFromJS) {
        LogUtils.e("imgJS="+paramFromJS);
        try{
            bean= JSONObject.parseObject(paramFromJS,WebImgBean.class);
            Intent intent = new Intent(context,PhotoBrowserActivity.class);
            intent.putExtra("data",  bean);
            context.startActivity(intent);
        }catch (Exception e){}
    }
}
