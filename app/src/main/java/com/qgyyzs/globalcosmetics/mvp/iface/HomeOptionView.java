package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.HomeOptionBean;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public interface HomeOptionView extends IBaseView{
    void showOptionResult(HomeOptionBean bean);
}
