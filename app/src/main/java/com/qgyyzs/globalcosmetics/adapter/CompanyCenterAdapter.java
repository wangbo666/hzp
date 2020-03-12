package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.CompanyDetialActivity;
import com.qgyyzs.globalcosmetics.activity.MedicineDetailActivity;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.bean.CompanyCenterBean;

import java.util.List;

/**
 * Created by admin on 2017/12/17.
 */

public class CompanyCenterAdapter extends CommonAdapter<CompanyCenterBean.JsonData>{
    public CompanyCenterAdapter(Activity activity, List<CompanyCenterBean.JsonData> list){
        super(activity, R.layout.company_center_item,list);
    }

    @Override
    public void convert(ViewHolder viewHolder, final CompanyCenterBean.JsonData companyBean, int position) {
        ImageView mItemCompanylogoImg=viewHolder.getView(R.id.item_companylogo_img);
        FrameLayout frameLayout1=viewHolder.getView(R.id.contentview1);
        FrameLayout frameLayout2=viewHolder.getView(R.id.contentview2);
        FrameLayout frameLayout3=viewHolder.getView(R.id.contentview3);
        ImageView Image1=viewHolder.getView(R.id.item_medicine_img);
        ImageView Image2=viewHolder.getView(R.id.item_medicine_img2);
        ImageView Image3=viewHolder.getView(R.id.item_medicine_img3);
        TextView name1=viewHolder.getView(R.id.item_medicinename_tv);
        TextView name2=viewHolder.getView(R.id.item_medicinename_tv2);
        TextView name3=viewHolder.getView(R.id.item_medicinename_tv3);

        if (TextUtils.isEmpty(companyBean.getProlist().get(0).getImage().split("\\|")[0])) {
            Image1.setImageResource(R.mipmap.medicin_defult);
        } else {
            Glide.with(mContext).load(companyBean.getProlist().get(0).getImage().split("\\|")[0])
                    .apply(new RequestOptions()
                    .error(R.mipmap.medicin_defult)
                    .placeholder(R.mipmap.medicin_defult)).into(Image1);
        }
        name1.setText(TextUtils.isEmpty(companyBean.getProlist().get(0).getSubject())?"":companyBean.getProlist().get(0).getSubject());
        if (TextUtils.isEmpty(companyBean.getProlist().get(1).getImage().split("\\|")[0])) {
            Image2.setImageResource(R.mipmap.medicin_defult);
        } else {
            Glide.with(mContext).load(companyBean.getProlist().get(1).getImage().split("\\|")[0])
                    .apply(new RequestOptions().error(R.mipmap.medicin_defult).placeholder(R.mipmap.medicin_defult)).into(Image2);
        }
        name2.setText(TextUtils.isEmpty(companyBean.getProlist().get(1).getSubject())?"":companyBean.getProlist().get(1).getSubject());

        if(companyBean.getProlist().size()>=3){
            frameLayout3.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(companyBean.getProlist().get(2).getImage().split("\\|")[0])) {
                Image3.setImageResource(R.mipmap.medicin_defult);
            } else {
                Glide.with(mContext).load(companyBean.getProlist().get(2).getImage().split("\\|")[0])
                        .apply(new RequestOptions().error(R.mipmap.medicin_defult).placeholder(R.mipmap.medicin_defult)).into(Image3);
            }
            name3.setText(TextUtils.isEmpty(companyBean.getProlist().get(2).getSubject())?"":companyBean.getProlist().get(2).getSubject());
        }else{
            frameLayout3.setVisibility(View.GONE);
        }

        LinearLayout lltext=viewHolder.getView(R.id.lltext);
        if (TextUtils.isEmpty(companyBean.getComp_logo())||companyBean.getComp_logo().equals("")) {
            mItemCompanylogoImg.setVisibility(View.GONE);
        }else{
            mItemCompanylogoImg.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(companyBean.getComp_logo()).into(mItemCompanylogoImg);
        }
        viewHolder.setText(R.id.item_companyname_tv,companyBean.getCompany());
        lltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CompanyDetialActivity.class);
                intent.putExtra("company_username",companyBean.getPcUsername());
                intent.putExtra("title",companyBean.getCompany());
                mContext.startActivity(intent);
            }
        });
        frameLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MedicineDetailActivity.class);
                intent.putExtra("proid", companyBean.getProlist().get(0).getId());
                intent.putExtra("muser", companyBean.getPcUsername());
                intent.putExtra("company",companyBean.getCompany());
                intent.putExtra("name", companyBean.getProlist().get(0).getSubject());
                intent.putExtra("image", companyBean.getProlist().get(0).getImage().split("\\|")[0]);
                mContext.startActivity(intent);
            }
        });
        frameLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MedicineDetailActivity.class);
                intent.putExtra("proid", companyBean.getProlist().get(1).getId());
                intent.putExtra("muser", companyBean.getPcUsername());
                intent.putExtra("company",companyBean.getCompany());
                intent.putExtra("name", companyBean.getProlist().get(1).getSubject());
                intent.putExtra("image", companyBean.getProlist().get(1).getImage().split("\\|")[0]);
                mContext.startActivity(intent);
            }
        });
        frameLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MedicineDetailActivity.class);
                intent.putExtra("proid", companyBean.getProlist().get(2).getId());
                intent.putExtra("muser", companyBean.getPcUsername());
                intent.putExtra("company",companyBean.getCompany());
                intent.putExtra("name", companyBean.getProlist().get(2).getSubject());
                intent.putExtra("image", companyBean.getProlist().get(2).getImage().split("\\|")[0]);
                mContext.startActivity(intent);
            }
        });
    }
}
