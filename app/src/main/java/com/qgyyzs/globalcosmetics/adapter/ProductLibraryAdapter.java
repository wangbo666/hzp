package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.LoginActivity;
import com.qgyyzs.globalcosmetics.activity.MedicineDetailActivity;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.bean.MyProductBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */

public class ProductLibraryAdapter extends CommonAdapter<MyProductBean.JsonData> {

    public ProductLibraryAdapter(Activity context, List<MyProductBean.JsonData> medicineBeanList) {
        super(context,R.layout.medicine_listview_item,medicineBeanList);
    }

    @Override
    public void convert(ViewHolder viewHolder, final MyProductBean.JsonData medicineBean, int position) {
        ImageView mItemMedicineLogo=viewHolder.getView(R.id.item_medicine_logo);
        RelativeLayout rlVip=viewHolder.getView(R.id.rl_vip);
        TextView mItemMedicineName=viewHolder.getView(R.id.item_medicine_name);
        TextView mItemMedicineType=viewHolder.getView(R.id.item_medicine_type);
        TextView mItemBrowsenumTv=viewHolder.getView(R.id.item_browsenum_tv);
        TextView mItemMedicineSpec=viewHolder.getView(R.id.item_medicine_spec);
        TextView mItemMedicineVender=viewHolder.getView(R.id.item_medicine_vender);
        TextView mItemMedicineAdvantage=viewHolder.getView(R.id.item_medicine_advantage);

        mItemMedicineName.setText(TextUtils.isEmpty(medicineBean.getSubject())?"": Html.fromHtml(medicineBean.getSubject()));
        mItemMedicineType.setText(TextUtils.isEmpty(medicineBean.getClassName())?"":medicineBean.getClassName());
        mItemBrowsenumTv.setText(medicineBean.getNewhit() + "浏览");
        mItemMedicineSpec.setText(TextUtils.isEmpty(medicineBean.getPpai_name())?"品牌：":"品牌：" + medicineBean.getPpai_name());
        mItemMedicineVender.setText(TextUtils.isEmpty(medicineBean.getX_gg())?"规格：":"规格：" + medicineBean.getX_gg());
        mItemMedicineAdvantage.setText(TextUtils.isEmpty(medicineBean.getDanwei())?"公司：":"公司：" + medicineBean.getDanwei());
        if(TextUtils.isEmpty(medicineBean.getImage())) {
            mItemMedicineLogo.setImageResource(R.mipmap.medicin_defult);
        }else{
            String str[]=medicineBean.getImage().split("\\|");
            Glide.with(mContext).load(str[0]).error(R.mipmap.medicin_defult).placeholder(R.mipmap.medicin_defult).into(mItemMedicineLogo);
        }
        rlVip.setVisibility(medicineBean.getFlag()==1?View.VISIBLE:View.GONE);

        viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyApplication.islogin) {
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    return;
                }
                Intent intent = new Intent(mContext, MedicineDetailActivity.class);
                intent.putExtra("proid", medicineBean.getId());
                intent.putExtra("muser", medicineBean.getPcUsername());
                intent.putExtra("company",medicineBean.getDanwei());
                intent.putExtra("name", medicineBean.getSubject());
                if(medicineBean.getImage()!=null){
                    String str[]=medicineBean.getImage().split("\\|");
                    intent.putExtra("image", str[0]);
                }
                mContext.startActivity(intent);

            }
        });
    }

}
