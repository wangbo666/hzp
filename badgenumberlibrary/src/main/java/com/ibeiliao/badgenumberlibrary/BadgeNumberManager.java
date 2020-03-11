package com.ibeiliao.badgenumberlibrary;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

/**
 * 应用桌面角标设置的管理类
 * Created by zlq on 2017 17/8/23 14:50.
 */

public class BadgeNumberManager {

    private Context mContext;

    private BadgeNumberManager(Context context) {
        mContext = context;
    }

    public static BadgeNumberManager from(Context context) {
        return new BadgeNumberManager(context);
    }

    private static final BadgeNumberManager.Impl IMPL;


    /**
     * 设置应用在桌面上显示的角标数字
     *
     * @param number 显示的数字
     */
    public void setBadgeNumber(int number) {
        IMPL.setBadgeNumber(mContext, number);
    }

    interface Impl {

        void setBadgeNumber(Context context, int number);

    }

    static class ImplHuaWei implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {
            BadgeNumberManagerHuaWei.setBadgeNumber(context, number);
        }
    }

    static class ImplXiaoMi implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {
            //小米机型的桌面应用角标API跟通知绑定在一起了，所以单独做处理
            //BadgeNumberManagerXiaoMi.setBadgeNumber(context, number);
        }
    }

    static class ImplVIVO implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {
            BadgeNumberManagerVIVO.setBadgeNumber(context, number);
        }
    }

    static class ImplOPPO implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {
            BadgeNumberManagerOPPO.setBadgeNumber(context, number);
        }
    }

    static class ImplSamSumg implements Impl{

        @Override
        public void setBadgeNumber(Context context, int number) {
            BadgeNumberManagerSamSung.sendToSamsumg(context,number);
        }
    }

    static class ImplSony implements Impl{

        @Override
        public void setBadgeNumber(Context context, int number) {
            BadgeNumberManagerSony.sendToSony(context,number);
        }
    }

    static class ImplBase implements Impl {

        @Override
        public void setBadgeNumber(Context context, int number) {
            //do nothing
        }
    }

    static {
        String manufacturer = Build.MANUFACTURER;
        if (manufacturer.equalsIgnoreCase(MobileBrand.HUAWEI)) {
            IMPL = new ImplHuaWei();
        } else if (manufacturer.equalsIgnoreCase(MobileBrand.XIAOMI)) {
            IMPL = new ImplXiaoMi();
        } else if (manufacturer.equalsIgnoreCase(MobileBrand.VIVO)) {
            IMPL = new ImplVIVO();
        } else if (manufacturer.equalsIgnoreCase(MobileBrand.OPPO)) {
            IMPL = new ImplOPPO();
        } else if(manufacturer.equalsIgnoreCase(MobileBrand.SAMSUNG)){
            IMPL = new ImplSamSumg();
        }else if(manufacturer.equalsIgnoreCase(MobileBrand.SONY)){
            IMPL = new ImplSony();
        }else {
            IMPL = new ImplBase();
        }
    }

    /**
     * Retrieve launcher activity name of the application from the context
     *
     * @param context The context of the application package.
     * @return launcher activity name of this application. From the
     *         "android:name" attribute.
     */
    public static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        // To limit the components this Intent will resolve to, by setting an
        // explicit package name.
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // All Application must have 1 Activity at least.
        // Launcher activity must be found!
        ResolveInfo info = packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER
        // if there is no Activity which has filtered by CATEGORY_DEFAULT
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }

        return info.activityInfo.name;
    }
}
