package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.ProductDetialBean;

/**
 * Created by admin on 2017/12/16.
 */

public interface ProductDetialView extends IBaseView{
    void showProductResult(ProductDetialBean bean);
}
