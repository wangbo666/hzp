package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.CompanyBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public interface HomeCompanyView extends IBaseView{
    void showCompanyResult(List<CompanyBean> mCompanyBeanList,int totalpage);
}
