package com.qgyyzs.globalcosmetics.mvp.iface;

import com.qgyyzs.globalcosmetics.base.IBaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public interface BannerView extends IBaseView {
    void showBannerResult(List<String> imageUrlList, List<String> imgLinkList,List<String> jsonString,List<String> linkText);
}
