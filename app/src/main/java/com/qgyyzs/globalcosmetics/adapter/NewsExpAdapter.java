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
import com.qgyyzs.globalcosmetics.bean.NewsExpBean;
import com.qgyyzs.globalcosmetics.http.retrofit.RetrofitUtils;
import com.qgyyzs.globalcosmetics.utils.TimeUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public class NewsExpAdapter extends CommonAdapter<NewsExpBean.JsonData> {

    public NewsExpAdapter(Activity context, List<NewsExpBean.JsonData> medicalNewsBeanList) {
        super(context,R.layout.medicinenews_listview_item2,medicalNewsBeanList);
    }

    @Override
    public void convert(ViewHolder viewHolder, final NewsExpBean.JsonData medicalNewsBean, int position) {
        TextView mItemTitleTv=viewHolder.getView(R.id.item_title_tv);
        TextView mItemLooknumTv=viewHolder.getView(R.id.item_looknum_tv);
        TextView mItemTimeTv=viewHolder.getView(R.id.item_time_tv);
        ImageView mImageView=viewHolder.getView(R.id.item_image);
        mItemTitleTv.setText(TextUtils.isEmpty(medicalNewsBean.getArtcle_title())?"":medicalNewsBean.getArtcle_title());
        mItemLooknumTv.setText(TextUtils.isEmpty(medicalNewsBean.getDiDian())?"":"地址：" + medicalNewsBean.getDiDian());

        String res="",res1="";
        if(!TextUtils.isEmpty(medicalNewsBean.getStartTime())) {
            String starttime = medicalNewsBean.getStartTime().substring(6, medicalNewsBean.getStartTime().length() - 2);
            long lt = new Long(starttime);
            Date date = new Date(lt);
            res = TimeUtils.DataTime(date);
        }
        if(!TextUtils.isEmpty(medicalNewsBean.getEndTime())) {
            String endtime = medicalNewsBean.getEndTime().substring(6, medicalNewsBean.getEndTime().length() - 2);
            long lt1 = new Long(endtime);
            Date date1 = new Date(lt1);
            res1 = TimeUtils.DataTime(date1);
        }
        mItemTimeTv.setText("时间："+res+"至"+res1);
        viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebBaseActivity.class);
                intent.putExtra("title", medicalNewsBean.getArtcle_title());
                intent.putExtra("type","zhanhui");
                intent.putExtra("url", RetrofitUtils.BASE_API + "artcle/artcleinfo?Type=" + 3 + "&id=" + medicalNewsBean.getArtcle_id());
                mContext.startActivity(intent);
            }
        });
    }
}
