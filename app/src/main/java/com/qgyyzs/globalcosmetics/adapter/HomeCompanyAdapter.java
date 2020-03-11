package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.CompanyDetialActivity;
import com.qgyyzs.globalcosmetics.activity.LoginActivity;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.MyBaseAdapter;
import com.qgyyzs.globalcosmetics.bean.CompanyBean;
import com.qgyyzs.globalcosmetics.utils.ScreenUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class HomeCompanyAdapter extends MyBaseAdapter<CompanyBean>{

    public HomeCompanyAdapter(Activity context, List<CompanyBean> list){
        super(context,list);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.adapter_homecompany;
    }

    @Override
    public Object getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }

    @Override
    public void setItemData(int position, final CompanyBean companyBean, Object viewHolder) {
        ViewHolder holder= (ViewHolder) viewHolder;

//        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        if(position%2==0){
//            layoutParams.setMargins(0,0,5,0);
//        }else{
//            layoutParams.setMargins(5,0,0,0);
//        }
//        holder.contetView.setLayoutParams(layoutParams);
        if(TextUtils.isEmpty(companyBean.getCompanylogo())||companyBean.getCompanylogo().equals("null")){
            holder.imageView.setImageResource(R.drawable.global_img_default);
        }else{
            Glide.with(context).load(companyBean.getCompanylogo()).error(R.drawable.global_img_default).placeholder(R.drawable.global_img_default).into(holder.imageView);
        }
        holder.imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenWidth(context) / 5));
        holder.txtname.setText(TextUtils.isEmpty(companyBean.getCompanyname())?"":companyBean.getCompanyname());
        String[] str=companyBean.getLinkText().split("\\|");
        holder.mTvProduct.setText(TextUtils.isEmpty(str[0])?"主营：":str[0]);
        if(str.length>1){
            holder.mTvYoushi.setText(TextUtils.isEmpty(str[1])?"优势：":str[1]);
        }
        holder.contetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MyApplication.islogin){
                    context.startActivity(new Intent(context,LoginActivity.class));
                    return;
                }
                Intent companyIntent = new Intent(context, CompanyDetialActivity.class);
                companyIntent.putExtra("company_username", companyBean.getUsername());
                companyIntent.putExtra("title", companyBean.getCompanyname());
                context.startActivity(companyIntent);
            }
        });
    }

    static class ViewHolder{
        @BindView(R.id.company_img)
        ImageView imageView;
        @BindView(R.id.company_name)
        TextView txtname;
        @BindView(R.id.company_product)
        TextView mTvProduct;
        @BindView(R.id.company_youshi)
        TextView mTvYoushi;
        @BindView(R.id.content)
        LinearLayout contetView;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
