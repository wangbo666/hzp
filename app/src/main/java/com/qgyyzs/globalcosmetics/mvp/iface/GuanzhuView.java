package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.CompanyGuanBean;

import java.util.List;

/**
 * Created by admin on 2017/12/15.
 */

public interface GuanzhuView extends IBaseView{
    void  showGuanzhuResult(List<CompanyGuanBean> mlist, String Msg);
}
