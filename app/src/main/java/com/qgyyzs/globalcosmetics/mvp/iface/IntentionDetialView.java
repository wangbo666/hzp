package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.IntentionDetialBean;

/**
 * Created by admin on 2017/12/15.
 */

public interface IntentionDetialView extends IBaseView{
    void showDetialResult(IntentionDetialBean bean);
}
