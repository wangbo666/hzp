package com.qgyyzs.globalcosmetics.badge;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

public class BadgeNumberManagerSamSung {
    /**
     * 向三星手机发送未读消息数广播
     * @param count
     */
    public static void sendToSamsumg(Context context, int count){
        String launcherClassName = BadgeNumberManager.getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }
}
