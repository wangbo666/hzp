package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.IntentionBean;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public interface IntentionMsgView extends IBaseView{
    void showIntentionResult(IntentionBean bean);
}
