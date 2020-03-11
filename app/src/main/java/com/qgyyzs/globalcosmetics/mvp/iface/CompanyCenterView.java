package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.CompanyCenterBean;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public interface CompanyCenterView extends IBaseView{
    void showCompanyListResult(CompanyCenterBean bean);
}
