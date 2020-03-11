package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.UserinfoBean;

/**
 * Created by admin on 2017/12/16.
 */

public interface UserInfoView extends IBaseView{
    void showUserResult(UserinfoBean  bean);
}
