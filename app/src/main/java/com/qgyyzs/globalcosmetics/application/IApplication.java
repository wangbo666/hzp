package com.qgyyzs.globalcosmetics.application;

import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;

public interface IApplication {
    boolean isRelease();
    Handler getHandler();
    Context getContext();
    void loginInvalid();
    String getBASE_URL();

    DisplayMetrics getDisplayMetrics();
    int getWidthPixels();
    int getHeightPixels();
    Float getDensity();
}
