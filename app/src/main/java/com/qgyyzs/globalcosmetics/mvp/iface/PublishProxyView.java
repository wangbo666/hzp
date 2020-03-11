package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.PublishProxyBean;

/**
 * Created by admin on 2017/12/16.
 */

public interface PublishProxyView extends IBaseView{
    void showPublishResult(PublishProxyBean  bean);
}
