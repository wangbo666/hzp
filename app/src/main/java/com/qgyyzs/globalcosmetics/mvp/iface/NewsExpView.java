package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.NewsExpBean;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public interface NewsExpView extends IBaseView{
    void showNewsResult(NewsExpBean bean);
}
