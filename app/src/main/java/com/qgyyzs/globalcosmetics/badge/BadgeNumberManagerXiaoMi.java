package com.qgyyzs.globalcosmetics.badge;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 小米机型的桌面角标设置管理类
 * Created by zlq on 2017 17/8/23 16:35.
 */

public class BadgeNumberManagerXiaoMi {

    public static void setBadgeNumber(Notification notification, Context context, int number) {
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, number);
        } catch (Exception e) {
            e.printStackTrace();
            // miui 6之前的版本
            Intent localIntent = new Intent(
                    "android.intent.action.APPLICATION_MESSAGE_UPDATE");
            localIntent.putExtra(
                    "android.intent.extra.update_application_component_name",
                    context.getPackageName() + "/" + BadgeNumberManager.getLauncherClassName(context));
            localIntent.putExtra(
                    "android.intent.extra.update_application_message_text", String.valueOf(number == 0 ? "" : number));
            context.sendBroadcast(localIntent);
        }
    }
}
