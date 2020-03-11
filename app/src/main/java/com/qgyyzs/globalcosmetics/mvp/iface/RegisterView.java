package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.RegisterBean;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public interface RegisterView extends IBaseView{
    void showRegisterView(RegisterBean bean);
}
