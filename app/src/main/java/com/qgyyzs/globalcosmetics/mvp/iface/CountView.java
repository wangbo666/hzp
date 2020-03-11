package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;

import java.util.List;

/**
 * Created by admin on 2017/12/15.
 */

public interface CountView extends IBaseView{
    void showCountResult(List<Integer>  list);
}
