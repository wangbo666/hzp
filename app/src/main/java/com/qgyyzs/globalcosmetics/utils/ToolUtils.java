package com.qgyyzs.globalcosmetics.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.FileProvider;

import com.qgyyzs.globalcosmetics.application.MyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.pm.PackageManager.GET_META_DATA;
import static android.content.pm.PackageManager.GET_SIGNATURES;
import static android.content.pm.PackageManager.NameNotFoundException;

public class ToolUtils {

    public static boolean isApplicationInstalledByPackageName(String package_name) {
        if (TextUtils.isEmpty(package_name)) {
            return false;
        }
        try {
            MyApplication.getInstance().getContext().getPackageManager().getPackageInfo(package_name, 0);
        } catch (NameNotFoundException e) {
            return false;
        }
        return true;
    }

    public static String getAppProcessName() {
        String pageName = "";
        final PackageManager packageManager = MyApplication.getInstance().getContext().getPackageManager();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    continue;
                }
                String myAppInfo = packageInfo.packageName;
//                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
//                    continue;
//                }
                pageName += myAppInfo + ",";
            }
        } catch (Exception e) {
            LogUtils.e("获取应用包信息失败");
        }
        return !TextUtils.isEmpty(pageName) && pageName.endsWith(",") ? pageName.substring(0, pageName.length() - 1) : pageName;
    }


    public static boolean isFirstIn(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(
                "first_in", true);
    }

    public static void setIsFirstIn(Context c, boolean b) {
        PreferenceManager.getDefaultSharedPreferences(c).edit()
                .putBoolean("first_in", false).commit();
    }

    public static String getPicName(String message) {
        String result = null;
        if (message.length() > 4) {
            result = message.substring(2, message.length() - 2);
            if (result != null && (result.endsWith(".png") || result.endsWith(".PNG")
                    || result.endsWith(".jpeg")
                    || result.endsWith(".JPEG")
                    || result.endsWith(".jpg") || result.endsWith(".JPG"))) {
                return result;
            } else {
                return null;
            }
        }
        return result;
    }

    public static boolean isMobileNum(String mobiles) {
        if (!TextUtils.isEmpty(mobiles)) {
            Pattern p = Pattern.compile("^1[0-9]{10}$");
            Matcher m = p.matcher(mobiles);
            return m.matches();
        }
        return false;
    }

    public static boolean isEmail(String strEmail) {
        String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    public static boolean checkPasswordFormat(String pwd) {
        Pattern p = Pattern.compile("[a-zA-Z0-9]{6,14}");
        Matcher m = p.matcher(pwd);
        return m.matches();
    }

    public static boolean checkDigitFormat(String pwd) {
        Pattern p = Pattern.compile("[0-9]{6}");
        Matcher m = p.matcher(pwd);
        return m.matches();
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static String formatDateLong2String(long time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDateLong2String2(long time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            return dateFormat.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDateLong2StringEntire(long time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return dateFormat.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDateLong2StringMMdd(long time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
            return dateFormat.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDateLong2StringMDHM(long time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
            return dateFormat.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDateLong2StringHM(long time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            return dateFormat.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long formatDateString2Long(String time) {
        long timelong = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = dateFormat.parse(time);
            timelong = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timelong;
    }

    public static String getBeforeDate(String oldTime) {
        StringBuffer sb = new StringBuffer();
        try {
            long time = System.currentTimeMillis() - Long.parseLong(oldTime);
            long mill = (long) Math.ceil(time / 1000);
            long minute = (long) Math.ceil(time / 60 / 1000.0f);
            long hour = (long) Math.floor(time / 60 / 60 / 1000.0f);
            long day = (long) Math.floor(time / 24 / 60 / 60 / 1000.0f);
            if (day > 0) {
                sb.append(day + "天");
            } else if (hour > 0) {
                if (hour >= 24) {
                    sb.append("1天");
                } else {
                    sb.append(hour + "小时");
                }
            } else if (minute - 1 > 0) {
                if (minute == 60) {
                    sb.append("1小时");
                } else {
                    sb.append(minute + "分钟");
                }
            } else if (mill - 1 > 0) {
                if (mill == 60) {
                    sb.append("1分钟");
                } else {
                    sb.append(mill + "秒");
                }
            } else {
                sb.append("刚刚");
            }
            if (!sb.toString().equals("刚刚")) {
                sb.append("前");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getBeforeDateOnlyDay(String oldTime) {
        StringBuffer sb = new StringBuffer();
        try {
            long time = System.currentTimeMillis() - Long.parseLong(oldTime);
            long day = (long) Math.floor(time / 24 / 60 / 60 / 1000.0f);
            if (day > 0) {
                sb.append(day + "天");
            } else {
                sb.append("今天");
            }
            if (!sb.toString().equals("今天")) {
                sb.append("前");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
        }
    }

    public static String getVersionName() {
        PackageManager pm = MyApplication.getInstance().getContext().getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(MyApplication.getInstance().getContext().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getChannel() {
        try {
            ApplicationInfo info = MyApplication.getInstance().getContext()
                    .getPackageManager()
                    .getApplicationInfo(MyApplication.getInstance().getContext().getPackageName(),
                            GET_META_DATA);
            return info.metaData.get("UMENG_CHANNEL").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isActivityDestroyed(Activity activity) {
        boolean isDestroyed = activity.isFinishing();
        if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {
            isDestroyed = true;
        }
        return isDestroyed;
    }

    public static boolean deleteFile(File rootFile, boolean showMsg) {
        boolean result = false;
        if (rootFile.exists()) {
            if (rootFile.isDirectory()) {
                File[] fileList = rootFile.listFiles();
                if (fileList != null) {
                    for (int i = 0; i < fileList.length; i++) {
                        deleteFile(fileList[i], false);
                    }
                }
            }
            result = rootFile.delete();
            if (showMsg) {
                if (result) {
                    ToastUtil.showShortMsg("清除成功");
                } else {
                    ToastUtil.showShortMsg("清除失败");
                }
            }
            return result;
        }
        ToastUtil.showShortMsg("文件不存在");
        return result;
    }

    /**
     * 检查是否已经打开"查看app使用情况"设置
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static boolean isStatsAccessPermissionSet() {
        if (Build.VERSION.SDK_INT > 20) {
            PackageManager pm = MyApplication.getInstance().getContext().getPackageManager();
            try {
                ApplicationInfo info = pm.getApplicationInfo(MyApplication.getInstance().getContext().getPackageName(), 0);
                AppOpsManager appOpsManager = (AppOpsManager) MyApplication.getInstance().getContext().getSystemService(Context.APP_OPS_SERVICE);
                appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName);
                return appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName) == AppOpsManager.MODE_ALLOWED;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean launchApplication(Context context, String packageName) {
        if (context == null) return false;
        Intent intent = getIntentForPackage(context, packageName);
        if (intent == null) {
            ToastUtil.showShortMsg("无法执行打开操作，可能应用已被卸载 !");
            return false;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        return true;
    }

    public static Intent getIntentForPackage(Context context, String packageName) {
        Intent intent = null;
        if (TextUtils.isEmpty(packageName)) return null;
        try {
            intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        } catch (Exception e) {

        }
        return intent;
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = MyApplication.getInstance().getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = MyApplication.getInstance().getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    public static boolean hasSimCard() {
        TelephonyManager telMgr = (TelephonyManager)
                MyApplication.getInstance().getContext().getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
//		Log.d("try", result ? "有SIM卡" : "无SIM卡");
        return result;
    }

    /**
     * 判断字符串是否为URL
     *
     * @param url 链接
     * @return true:是URL、false:不是URL
     */
    public static boolean isHttpUrl(String url) {
        String regex = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(url).matches();
    }


    public static void installApk(File file) {
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkUri = FileProvider.getUriForFile(MyApplication.getInstance().getContext(), "com.qgyyzs.globalcosmetics.fileprovider", file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            MyApplication.getInstance().getContext().startActivity(intent);
        } else
            ToastUtil.showShortMsg("未找到安装文件，请重新下载安装");
    }

    /**
     *      * 将得到的int类型的IP转换为String类型
     *      *
     *      * @param ip
     *      * @return
     *      
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
            ToastUtil.showShortMsg("当前无网络连接,请在设置中打开网络");
        }
        return "";
    }

//    public static String getWifiName(Context context) {
//        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
//        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
//            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//            WifiInfo wifiInfo = wifi.getConnectionInfo();
//            if (info != null && wifiInfo.getMacAddress() != null)
//                return TextUtils.isEmpty(wifiInfo.getSSID()) ? "" : wifiInfo.getSSID().replace("\"", "");
//        }
//        return "";
//    }

    public static String getWifiName(Context context) {
        ConnectivityManager ctm = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ctm.getActiveNetworkInfo();
        if (networkInfo != null)
            return networkInfo.getExtraInfo().replace("\"", "");
        return "";
    }

    public static String readCpuInfo() {
        String result = "";
        try {
            String[] args = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            StringBuffer sb = new StringBuffer();
            String readLine = "";
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }
            responseReader.close();
            result = sb.toString().toLowerCase();
        } catch (IOException ex) {
        }
        return result;
    }

    /**
     * 判断cpu是否为电脑来判断 模拟器
     *
     * @return true 为模拟器
     */
    public static boolean checkIsNotRealPhone() {
        String cpuInfo = readCpuInfo();
        if ((cpuInfo.contains("intel") || cpuInfo.contains("amd"))) {
            return true;
        }
        return false;
    }

    /**
     * 根据部分特征参数设备信息来判断是否为模拟器
     *
     * @return true 为模拟器
     */
    public static boolean isEmulator() {
        String url = "tel:" + "123456";
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_DIAL);
        // 是否可以处理跳转到拨号的 Intent
        boolean canResolveIntent = intent.resolveActivity(MyApplication.getInstance().getContext().getPackageManager()) != null;

        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.SERIAL.equalsIgnoreCase("unknown")
                || Build.SERIAL.equalsIgnoreCase("android")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT)
                || ((TelephonyManager) MyApplication.getInstance().getContext().getSystemService(Context.TELEPHONY_SERVICE))
                .getNetworkOperatorName().toLowerCase().equals("android")
                || !canResolveIntent;
    }

    /**
     * 判断蓝牙是否有效来判断是否为模拟器
     *
     * @return true 为模拟器
     */
    public static boolean notHasBlueTooth() {
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        if (ba == null) {
            return true;
        } else {
            // 如果有蓝牙不一定是有效的。获取蓝牙名称，若为null 则默认为模拟器
            String name = ba.getName();
            if (TextUtils.isEmpty(name)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /****************
     *
     * 发起添加群流程。群号：橙赚粉丝群(297479251) 的 key 为： FbxZwYA-3WYtzXgv7YYwm0d1vxaeGh6U
     * 调用 joinQQGroup(FbxZwYA-3WYtzXgv7YYwm0d1vxaeGh6U) 即可发起手Q客户端申请加群 橙赚粉丝群(297479251)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public static boolean joinQQGroup(Activity activity, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }

    /*
     * 获取下载文件的MD5信息
     * */
    private String getUninstallAPKInfor(String apkPath) { //apkPath为文件路径
        final PackageManager packageManager = MyApplication.getInstance().getContext().getPackageManager();
        PackageInfo packageSign = packageManager.getPackageArchiveInfo(apkPath, GET_SIGNATURES);
        if (packageSign != null) {
//            ApplicationInfo applicationInfo = packageSign.applicationInfo;
//            boolean debug = (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
//            if (debug) {
//                Log.d("log apk", "该文件为测试程序");
//            } else {
//                Log.d("log apk", "该文件为发布程序");
//            }

            try {
                Log.d("log apk", "apk文件签名信息:" + doFingerprint(packageSign.signatures[0].toByteArray(), "MD5"));
                return doFingerprint(packageSign.signatures[0].toByteArray(), "MD5");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * @param certificateBytes 获取到应用的signature值
     * @param algorithm        在上文指定MD5算法
     * @return md5签名
     */
    private String doFingerprint(byte[] certificateBytes, String algorithm) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(certificateBytes);
        byte[] digest = md.digest();

        String toRet = "";
        for (int i = 0; i < digest.length; i++) {
            if (i != 0) {
                toRet += ":";
            }
            int b = digest[i] & 0xff;
            String hex = Integer.toHexString(b);
            if (hex.length() == 1) {
                toRet += "0";
            }
            toRet += hex;
        }
        return toRet;
    }


    public static String getPhoneModel() {
        return Build.MODEL;
    }

    public static String getSystemVer() {
        return Build.VERSION.RELEASE;
    }


    /**
     * MD5加密
     *
     * @param byteStr 需要加密的内容
     * @return 返回 byteStr的md5值
     */
    public static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
//            return Base64.encodeToString(byteArray,Base64.NO_WRAP);
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }


    /**
     * 获取单个文件的MD5值！
     *
     * @param file
     * @return
     */

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }


    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 检测签名
     */
    public static boolean checkSignature() {
        Context context = MyApplication.getInstance().getContext();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;

            if (signatures != null) {
                for (Signature signature : packageInfo.signatures) {
                    //获取MD5或者SHA1
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update(signature.toByteArray());

                    String currentSignature = bytesToHexString(md.digest()).toLowerCase();
                    LogUtils.i("currentSignature=" + currentSignature);
                    if ("YOUR SIGENATURE".equals(currentSignature)) {
                        return true;
                    }
                }
            } else {
                LogUtils.i("signatures ==null");
            }

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

}