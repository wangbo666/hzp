package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.MyCollectBean;

/**
 * Created by admin on 2017/12/15.
 */

public interface CollectView extends IBaseView{
    void showCollectResult(MyCollectBean bean);
}
