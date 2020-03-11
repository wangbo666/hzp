package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.UserDetialBean;

/**
 * Created by admin on 2017/12/15.
 */

public interface UserDetialView extends IBaseView{
    void showUserResult(UserDetialBean bean);
}
