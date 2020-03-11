package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.ReleaseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public interface HomeInfoView extends IBaseView{
    void showHomeInfoResult(List<ReleaseBean> releaseBeanList);
}
