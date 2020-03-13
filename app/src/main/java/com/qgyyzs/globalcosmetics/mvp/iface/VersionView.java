package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.VersionBean;

/**
 * Author Wangbo
 * Date 2020/3/13 12:07
 **/
public interface VersionView extends IBaseView {
    void showVersion(VersionBean bean);
}
