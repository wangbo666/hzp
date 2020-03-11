# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\androidstudio\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
##环信 混淆
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-verbose
#-dontwarn
#
## Optimization is turned off by default. Dex does not like code run
## through the ProGuard optimize and preverify steps (and performs some
## of these optimizations on its own).
#-dontoptimize
#-dontpreverify
## Note that if you want to enable optimization, you cannot just
## include optimization flags in your own project configuration file;
## instead you will need to point to the
## "proguard-android-optimize.txt" file instead of this one from your
## project.properties file.
#
#-keepattributes *Annotation*
#-keep public class com.google.vending.licensing.ILicensingService
#-keep public class com.android.vending.licensing.ILicensingService
#
## For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#
## keep setters in Views so that animations can still work.
## see http://proguard.sourceforge.net/manual/examples.html#beans
#-keepclassmembers public class * extends android.view.View {
#   void set*(***);
#   *** get*();
#}
#
## We want to keep methods in Activity that could be used in the XML attribute onClick
#-keepclassmembers class * extends android.app.Activity {
#   public void *(android.view.View);
#}
#
## For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#
#-keepclassmembers class **.R$* {
#    public static <fields>;
#}
#
#-keep class android.support.v4.** {*;}
#
#-keep class org.xmlpull.** {*;}
#-keep class com.baidu.** {*;}
#-keep public class * extends com.umeng.**
#-keep class com.umeng.** { *; }
#-keep class com.squareup.picasso.* {*;}
#
#-keep class com.hyphenate.* {*;}
#-keep class com.hyphenate.chat.** {*;}
#-keep class org.jivesoftware.** {*;}
#-keep class org.apache.** {*;}
##另外，demo中发送表情的时候使用到反射，需要keep SmileUtils,注意前面的包名，
##不要SmileUtils复制到自己的项目下keep的时候还是写的demo里的包名
#-keep class com.hyphenate.easeui.utils.SmileUtils {*;}
#
##2.0.9后加入语音通话功能，如需使用此功能的api，加入以下keep
#-keep class net.java.sip.** {*;}
#-keep class org.webrtc.voiceengine.** {*;}
#-keep class org.bitlet.** {*;}
#-keep class org.slf4j.** {*;}
#-keep class ch.imvs.** {*;}
#
##-------------------------------------------定制化区域----------------------------------------------
##---------------------------------1.实体类---------------------------------
#
#-keep class com.qgyyzs.globalcosmetics.bean.** { *;}
#-keep class com.qgyyzs.globalcosmetics.chat.domain.** {*;}
#-keep class com.hyphenate.easeui.domain.** {*;}
##-------------------------------------------------------------------------
#
##---------------------------------2.第三方包-------------------------------
##百度地图
#-libraryjars libs/BaiduLBS_Android.jar
#-keep class com.baidu.** { *; }
#-keep class vi.com.gdi.bgl.android.**{*;}
#
##谷歌
#-libraryjars libs/google-play-services.jar
#-keep class com.google.android.gms.** { *; }
#
##华为
#-libraryjars libs/HwPush_SDK_V2705_nomap.jar
#-keep class com.huawei.android.** { *; }
#-keep class com.hianalytics.android.** { *; }
#
##环信
#-libraryjars libs/hyphenatechat_3.3.0.jar
#-keep class com.hyphenate.** { *; }
#-keep class com.superrtc.** { *; }
#-keep class com.superrtc.** { *; }
#
###eventBus
##-keepattributes *Annotation*
##-keepclassmembers class ** {
##    @org.greenrobot.eventbus.Subscribe <methods>;
##}
##-keep enum org.greenrobot.eventbus.ThreadMode { *; }
##-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
##    <init>(java.lang.Throwable);
##}
#
##glide
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
#  **[] $VALUES;
#  public *;
#}
#
###log4j
##-libraryjars log4j-1.2.17.jar
##-dontwarn org.apache.log4j.**
##-keep class  org.apache.log4j.** { *;}
#
##-------------------------------------------------------------------------
#
##---------------------------------3.与js互相调用的类------------------------
##
##-keepclasseswithmembers class com.demo.login.bean.ui.MainActivity$JSInterface {
##      <methods>;
##}
#
##-------------------------------------------------------------------------
#
##---------------------------------4.反射相关的类和方法-----------------------
#
##TODO 我的工程里没有。。。
#
##----------------------------------------------------------------------------
##---------------------------------------------------------------------------------------------------
#
##-------------------------------------------基本不用动区域--------------------------------------------
##---------------------------------基本指令区----------------------------------
#-optimizationpasses 5
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-dontskipnonpubliclibraryclassmembers
#-dontpreverify
#-verbose
#-printmapping proguardMapping.txt
#-optimizations !code/simplification/cast,!field/*,!class/merging/*
#-keepattributes *Annotation*,InnerClasses
#-keepattributes Signature
#-keepattributes SourceFile,LineNumberTable
##----------------------------------------------------------------------------
#
##---------------------------------默认保留区---------------------------------
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class * extends android.view.View
#-keep public class com.android.vending.licensing.ILicensingService
#-keep class android.support.** {*;}
#
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#-keepclassmembers class * extends android.app.Activity{
#    public void *(android.view.View);
#}
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#-keep public class * extends android.view.View{
#    *** get*();
#    void set*(***);
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#-keep class **.R$* {
# *;
#}
#-keepclassmembers class * {
#    void *(**On*Event);
#}
##----------------------------------------------------------------------------
#
##---------------------------------webview------------------------------------
#-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
#   public *;
#}
#-keepclassmembers class * extends android.webkit.WebViewClient {
#    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
#    public boolean *(android.webkit.WebView, java.lang.String);
#}
#-keepclassmembers class * extends android.webkit.WebViewClient {
#    public void *(android.webkit.WebView, jav.lang.String);
#}
##----------------------------------------------------------------------------
##---------------------------------------------------------------------------------------------------