package com.qgyyzs.globalcosmetics.application;

import com.netease.nimlib.sdk.ServerAddresses;

/**
 * 网易云信私有化配置项
 */
class PrivatizationConfig {

    static ServerAddresses getServerAddresses() {
        return null;
    }

    static String getAppKey() {
        return "6a1776372c8831d9e3bc2b1da052028b";
    }

    private static ServerAddresses get() {
        ServerAddresses addresses = new ServerAddresses();
        addresses.nosDownload = "nos.netease.com";
        addresses.nosAccess = "{bucket}.nosdn.127.net/{object}";
        return addresses;
    }
}
