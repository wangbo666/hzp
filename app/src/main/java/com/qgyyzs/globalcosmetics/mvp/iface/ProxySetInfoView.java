package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.ProxySetInfoBean;

/**
 * Created by admin on 2017/12/16.
 */

public interface ProxySetInfoView extends IBaseView{
    void showProxyResult(ProxySetInfoBean bean);
}
