package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.UserDetailActivity;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.bean.ProxyInfoBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public class ProxyInfoAdapter extends CommonAdapter<ProxyInfoBean.JsonData> {

    public ProxyInfoAdapter(Activity context,List<ProxyInfoBean.JsonData> proxyBeanList) {
        super(context,R.layout.proxyinfo_listview_item,proxyBeanList);
    }

    @Override
    public void convert(ViewHolder viewHolder, final ProxyInfoBean.JsonData proxyBean, int position) {
        ImageView mItemUserphotoImg=viewHolder.getView(R.id.item_userphoto_img);
        TextView mItemProxyName=viewHolder.getView(R.id.item_proxy_name);
        TextView mItemTimeTv=viewHolder.getView(R.id.item_time_tv);
        TextView mItemAddressTv=viewHolder.getView(R.id.item_address_tv);
        TextView mItemTypeTv=viewHolder.getView(R.id.item_type_tv);

        mItemProxyName.setText(TextUtils.isEmpty(proxyBean.getProName())?"暂未设置": Html.fromHtml(proxyBean.getProName()));
        mItemTypeTv.setText(TextUtils.isEmpty(proxyBean.getContent())?"暂未设置":Html.fromHtml(proxyBean.getContent()));
        viewHolder.setText(R.id.item_subject_tv,proxyBean.getDailiType());
        mItemAddressTv.setText(TextUtils.isEmpty(proxyBean.getDailiArea())?"暂未设置":proxyBean.getDailiArea());

        viewHolder.getView(R.id.cardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserDetailActivity.class);
                intent.putExtra("id",proxyBean.getId());
                intent.putExtra("touserid",proxyBean.getAppUserId()+"");
                intent.putExtra("nickname", TextUtils.isEmpty(proxyBean.getProName())?"代理详情":proxyBean.getProName());
                intent.putExtra("muser", proxyBean.getZsUsername());
                mContext.startActivity(intent);
            }
        });
    }
}
