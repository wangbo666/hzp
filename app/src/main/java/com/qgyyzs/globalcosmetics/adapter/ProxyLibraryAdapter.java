package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.UserDetailActivity;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.bean.ProxyLibraryBean;
import com.qgyyzs.globalcosmetics.utils.GlideCircleTransform;
import com.qgyyzs.globalcosmetics.utils.TimeUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/12/17.
 */

public class ProxyLibraryAdapter extends CommonAdapter<ProxyLibraryBean.JsonData>{

    public ProxyLibraryAdapter(Activity activity, List<ProxyLibraryBean.JsonData> proxyBeanList){
        super(activity, R.layout.proxy_listview_item,proxyBeanList);
    }
    @Override
    public void convert(ViewHolder viewHolder, final ProxyLibraryBean.JsonData proxyBean, int position) {
        TextView mItemProxyName=viewHolder.getView(R.id.item_proxy_name);
        TextView mItemTypeTv=viewHolder.getView(R.id.item_type_tv);
        TextView mItemSubjectTv=viewHolder.getView(R.id.item_subject_tv);
        TextView  mItemAddressTv=viewHolder.getView(R.id.item_address_tv);
        TextView mItemDepartTv=viewHolder.getView(R.id.item_depart_tv);
        RelativeLayout rlGuanzhu=viewHolder.getView(R.id.rl_guanzhu);
        ImageView mItemUserphotoImg=viewHolder.getView(R.id.item_userphoto_img);
        TextView mItemLastTime=viewHolder.getView(R.id.item_lasttime_tv);
        mItemProxyName.setText(TextUtils.isEmpty(proxyBean.getRealName())?"暂未设置":proxyBean.getRealName());
        mItemTypeTv.setText(TextUtils.isEmpty(proxyBean.getDaili_type())?"暂未设置":proxyBean.getDaili_type());
        mItemSubjectTv.setText(TextUtils.isEmpty(proxyBean.getQudao())?"暂未设置":proxyBean.getQudao());
        mItemAddressTv.setText(TextUtils.isEmpty(proxyBean.getCzd())?"暂未设置":proxyBean.getCzd());
        mItemDepartTv.setText(TextUtils.isEmpty(proxyBean.getKeshi())?"":proxyBean.getKeshi());
        rlGuanzhu.setVisibility(TextUtils.isEmpty(proxyBean.getKeshi())? View.GONE:View.VISIBLE);
        if(TextUtils.isEmpty(proxyBean.getHeadImg())) {
            mItemUserphotoImg.setImageResource(R.mipmap.icon_user_defult);
        }else{
            Glide.with(mContext).load(proxyBean.getHeadImg()).error(R.drawable.icon_user_defult).placeholder(R.drawable.icon_user_defult).transform(new GlideCircleTransform(mContext)).into(mItemUserphotoImg);
        }
        if (TextUtils.isEmpty(proxyBean.getLastRequestTime())) {
            mItemLastTime.setText("");
        } else {
            long lt = new Long(proxyBean.getLastRequestTime().substring(6, proxyBean.getLastRequestTime().length() - 2));
            Date date = new Date(lt);
            if(TimeUtils.twoDateDistance(date).equals("刚刚")){
                mItemLastTime.setText("刚刚在线");
            }else {
                mItemLastTime.setText(TimeUtils.twoDateDistance(date));
            }
        }
        viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserDetailActivity.class);
                intent.putExtra("info", 10);//代理商库
                intent.putExtra("touserid", proxyBean.getUserId() + "");
                intent.putExtra("nickname", proxyBean.getRealName());
                mContext.startActivity(intent);
            }
        });
    }
}
