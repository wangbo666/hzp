package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.MedicalNewsBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public interface MedicalNewsView extends IBaseView{
    void showHeadLineResult(List<MedicalNewsBean> mMedicalNewsBeanList);
}
