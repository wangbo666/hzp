package com.qgyyzs.globalcosmetics.application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.mixpush.NIMPushClient;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.qgyyzs.globalcosmetics.bean.VersionBean;
import com.qgyyzs.globalcosmetics.nim.NIMInitManager;
import com.qgyyzs.globalcosmetics.nim.event.DemoOnlineStateContentProvider;
import com.qgyyzs.globalcosmetics.nim.mixpush.DemoMixPushMessageHandler;
import com.qgyyzs.globalcosmetics.nim.mixpush.DemoPushContentProvider;
import com.qgyyzs.globalcosmetics.uikit.api.NimUIKit;
import com.qgyyzs.globalcosmetics.uikit.api.UIKitOptions;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.SplashActivity;
import com.qgyyzs.globalcosmetics.nim.DemoCache;
import com.qgyyzs.globalcosmetics.nim.config.ExtraOptions;
import com.qgyyzs.globalcosmetics.nim.config.preference.Preferences;
import com.qgyyzs.globalcosmetics.nim.config.preference.UserPreferences;
import com.qgyyzs.globalcosmetics.nim.contact.ContactHelper;
import com.qgyyzs.globalcosmetics.nim.session.SessionHelper;
import com.qgyyzs.globalcosmetics.utils.DynamicTimeFormat;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/3/28.
 */

public class MyApplication extends MultiDexApplication implements IApplication {
    private static MyApplication instance;
    public static Handler handler = new Handler();
    private static Context context;
    public static VersionBean versionBean;

    public static String TOKEN = "";
    public static boolean islogin = false;
    public static String userId;
    public static String Toutiao;
    public static String username;
    public static SharedPreferences mSharedPreferences;


    public static final String USERSPINFO = "user_info";
    public static final String CONSTACTDATA = "DATA_SET";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // android 7.0系统解决拍照的问题
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//        builder.detectFileUriExposure();
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        mSharedPreferences = getApplicationContext().getSharedPreferences(USERSPINFO, Context.MODE_PRIVATE);
        if (TextUtils.isEmpty(TOKEN)) {
            TOKEN = mSharedPreferences.getString("token", "");
        }

        if (islogin == false) {
            islogin = mSharedPreferences.getBoolean("islogin", false);
        }
        if (TextUtils.isEmpty(userId)) {
            userId = mSharedPreferences.getString("userid", "");
        }
        if (TextUtils.isEmpty(username)) {
            username = mSharedPreferences.getString("username", "");
        }
        context = getApplicationContext();
        UMShareAPI.get(this);

        ExtraOptions.provide();
        DemoCache.setContext(this);
        // 4.6.0 开始，第三方推送配置入口改为 SDKOption#mixPushConfig，旧版配置方式依旧支持。
        NIMClient.init(this, getLoginInfo(), NimSDKOptionConfig.getSDKOptions(this));

        // crash handler
//        AppCrashHandler.getInstance(this);

        if (NIMUtil.isMainProcess(this)) {
            // 注册自定义推送消息处理，这个是可选项
            NIMPushClient.registerMixPushMessageHandler(new DemoMixPushMessageHandler());
            initUIKit();
            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
            // 云信sdk相关业务初始化
            NIMInitManager.getInstance().init(true);
        }
    }

    public static MyApplication getInstance() {
        return instance;
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
            }
        });
    }

    // 创建服务用于捕获崩溃异常
    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            restartApp();//发生崩溃异常时,重启应用
        }
    };

    public void restartApp() {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }

    {
//        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setWeixin("wxc47f17b609bb330e", "760c95a75b333b368009f3076ea9568e");
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setQQZone("101455515", "e90d31b82ee1d2474b8d522b865ae20d");
        Config.DEBUG = true;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    private LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    private void initUIKit() {
        NimUIKit.init(this, buildUIKitOptions());
        // IM 会话窗口的定制初始化。
        SessionHelper.init();
        // 通讯录列表定制初始化
        ContactHelper.init();
        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
        NimUIKit.setCustomPushContentProvider(new DemoPushContentProvider());
        NimUIKit.setOnlineStateContentProvider(new DemoOnlineStateContentProvider());
    }

    private UIKitOptions buildUIKitOptions() {
        UIKitOptions options = new UIKitOptions();
        // 设置app图片/音频/日志等缓存目录
        options.appCacheDir = NimSDKOptionConfig.getAppCacheDir(this) + "/app";
        return options;
    }

    @Override
    public boolean isRelease() {
        return false;
    }

    @Override
    public Handler getHandler() {
        return handler  ;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void loginInvalid() {

    }

    @Override
    public String getBASE_URL() {
        return null;
    }

    @Override
    public DisplayMetrics getDisplayMetrics() {
        return null;
    }

    @Override
    public int getWidthPixels() {
        return 0;
    }

    @Override
    public int getHeightPixels() {
        return 0;
    }

    @Override
    public Float getDensity() {
        return null;
    }
}
