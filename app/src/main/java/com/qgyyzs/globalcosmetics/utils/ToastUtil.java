package com.qgyyzs.globalcosmetics.utils;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.qgyyzs.globalcosmetics.application.MyApplication;

/**
 * Created by ZFG on 2017/03/08.
 * 描述:吐司的工具类
 */

public class ToastUtil {
    public static Toast mToast;

    /**
     * 显示吐司
     *
     * @param context
     * @param message
     */
    public static void showToast(final Context context, final String message, Boolean Debug) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }

    private static Toast toast;

    public static void showmsg(final String msg, final int time) {
        if (TextUtils.isEmpty(msg)) return;
        if (msg.contains("未认证") || msg.equals("参数错误")) return;
        MyApplication.getInstance().getHandler().post(() -> {
            if (toast == null) {
                toast = Toast.makeText(MyApplication.getInstance().getContext(), msg, time);
            } else {
                toast.setText(msg);
            }
            toast.show();
        });
    }


    public static void showShortMsg(final String msg) {
        showmsg(msg, Toast.LENGTH_SHORT);
    }

    public static void showLongMsg(final String msg) {
        showmsg(msg, Toast.LENGTH_LONG);
    }
}
