package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.CompanyDetialActivity;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.bean.CompanyGuanBean;
import com.qgyyzs.globalcosmetics.utils.GlideCircleTransform;

import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public class CompanyGuanListAdapter extends CommonAdapter<CompanyGuanBean> {

    public CompanyGuanListAdapter(Activity context,List<CompanyGuanBean> biduserBeanList) {
        super(context,R.layout.adapter_guanzhu,biduserBeanList);
    }

    @Override
    public void convert(ViewHolder viewHolder, final CompanyGuanBean biduserBean, int position) {
        ImageView mItemUserphotoImg=viewHolder.getView(R.id.item_userphoto_img);
        TextView mItemUsernameTv=viewHolder.getView(R.id.item_nickname_tv);
        TextView mItemTimeTv=viewHolder.getView(R.id.item_time_tv);

        mItemUsernameTv.setText(TextUtils.isEmpty(biduserBean.getCompanyname())?"":biduserBean.getCompanyname());
        mItemTimeTv.setText(TextUtils.isEmpty(biduserBean.getLastesttime())?"":biduserBean.getLastesttime());
        if (TextUtils.isEmpty(biduserBean.getCompanylogo())) {
            mItemUserphotoImg.setImageResource(R.drawable.icon_user_defult);
        }else{
            Glide.with(mContext).load(biduserBean.getCompanylogo()).error(R.drawable.icon_user_defult).placeholder(R.drawable.icon_user_defult).transform(new GlideCircleTransform(mContext))
                    .into(mItemUserphotoImg);
        }

        viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CompanyDetialActivity.class);
                intent.putExtra("company_username",biduserBean.getUsername());
                intent.putExtra("title",biduserBean.getCompanyname());
                mContext.startActivity(intent);
            }
        });
    }
}
