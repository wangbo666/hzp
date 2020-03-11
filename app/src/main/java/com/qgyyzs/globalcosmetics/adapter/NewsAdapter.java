package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.WebBaseActivity;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.bean.MedicalNewsBean;
import com.qgyyzs.globalcosmetics.http.retrofit.RetrofitUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public class NewsAdapter extends CommonAdapter<MedicalNewsBean> {

    public NewsAdapter(Activity context, List<MedicalNewsBean> medicalNewsBeanList) {
        super(context,R.layout.medicinenews_listview_item,medicalNewsBeanList);
    }

    @Override
    public void convert(ViewHolder viewHolder, final MedicalNewsBean medicalNewsBean, int position) {
        TextView mItemTitleTv=viewHolder.getView(R.id.item_title_tv);
        TextView mItemLooknumTv=viewHolder.getView(R.id.item_looknum_tv);
        TextView mItemTimeTv=viewHolder.getView(R.id.item_time_tv);
        ImageView mImageView=viewHolder.getView(R.id.item_image);
        mItemTitleTv.setText(TextUtils.isEmpty(medicalNewsBean.getTitle())?"":medicalNewsBean.getTitle());
        mItemLooknumTv.setText(TextUtils.isEmpty(medicalNewsBean.getLooknum())?"":"浏览：" + medicalNewsBean.getLooknum());
        mItemTimeTv.setText(TextUtils.isEmpty(medicalNewsBean.getTime())?"":medicalNewsBean.getTime());
        if(TextUtils.isEmpty(medicalNewsBean.getImg())||medicalNewsBean.getImg().equals("null")){
            mImageView.setVisibility(View.GONE);
        }else{
            mImageView.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(medicalNewsBean.getImg()).error(R.drawable.global_img_default).placeholder(R.drawable.global_img_default).into(mImageView);
        }
        viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebBaseActivity.class);
                intent.putExtra("title", medicalNewsBean.getTitle());
                intent.putExtra("type","medical");
                intent.putExtra("url", RetrofitUtils.BASE_API + "artcle/artcleinfo?Type=" + 1 + "&id=" + medicalNewsBean.getId());
                mContext.startActivity(intent);
            }
        });
    }
}
