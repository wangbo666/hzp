package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;
import com.qgyyzs.globalcosmetics.bean.JobTypeBean;

/**
 * Created by admin on 2017/12/16.
 */

public interface JobTypeView extends IBaseView{
    void showJobTypeResult(JobTypeBean bean);
}
