package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.MedicalNewsBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public interface NewsView extends IBaseView{
    void showNewsResult(List<MedicalNewsBean> mMedicalNewsBeanList, String Msg);
}
