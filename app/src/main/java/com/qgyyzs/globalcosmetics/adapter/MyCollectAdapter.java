package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.MedicineDetailActivity;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.bean.MyCollectBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */

public class MyCollectAdapter extends CommonAdapter<MyCollectBean.JsonData> {
    public MyCollectAdapter(Activity context,List<MyCollectBean.JsonData> medicineBeanList) {
        super(context,R.layout.medicine_listview_item,medicineBeanList);
    }

    @Override
    public void convert(ViewHolder viewHolder, final MyCollectBean.JsonData medicineBean, int position) {
        ImageView mItemMedicineLogo=viewHolder.getView(R.id.item_medicine_logo);
        TextView mItemMedicineName=viewHolder.getView(R.id.item_medicine_name);
        TextView mItemMedicineType=viewHolder.getView(R.id.item_medicine_type);
        TextView mItemBrowsenumTv=viewHolder.getView(R.id.item_browsenum_tv);
        TextView mItemMedicineSpec=viewHolder.getView(R.id.item_medicine_spec);
        TextView mItemMedicineVender=viewHolder.getView(R.id.item_medicine_vender);
        TextView mItemMedicineAdvantage=viewHolder.getView(R.id.item_medicine_advantage);

        mItemMedicineName.setText(TextUtils.isEmpty(medicineBean.getEqinfoz().getSubject())?"":medicineBean.getEqinfoz().getSubject());
        mItemMedicineType.setText(TextUtils.isEmpty(medicineBean.getEqinfoz().getClassName())?"":medicineBean.getEqinfoz().getClassName());
        mItemBrowsenumTv.setText(medicineBean.getEqinfoz().getNewhit() + "浏览");
        mItemMedicineSpec.setText(TextUtils.isEmpty(medicineBean.getEqinfoz().getPpai_name())?"品牌：":"品牌：" + medicineBean.getEqinfoz().getPpai_name());
        mItemMedicineVender.setText(TextUtils.isEmpty(medicineBean.getEqinfoz().getX_gg())?"规格：":"规格：" + medicineBean.getEqinfoz().getX_gg());
        mItemMedicineAdvantage.setText(TextUtils.isEmpty(medicineBean.getEqinfoz().getDanwei())?"公司：":"公司：" + medicineBean.getEqinfoz().getDanwei());
        if(TextUtils.isEmpty(medicineBean.getEqinfoz().getImage())){
            mItemMedicineLogo.setImageResource(R.mipmap.medicin_defult);
        }else{
            Glide.with(mContext).load(medicineBean.getEqinfoz().getImage()).apply(new RequestOptions()
                    .error(R.mipmap.medicin_defult).placeholder(R.mipmap.medicin_defult)).into(mItemMedicineLogo);
        }

        viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MedicineDetailActivity.class);
                intent.putExtra("proid", medicineBean.getProId());
                intent.putExtra("name", medicineBean.getEqinfoz().getSubject());
                intent.putExtra("company",medicineBean.getEqinfoz().getDanwei());
                intent.putExtra("muser",medicineBean.getEqinfoz().getPcUsername());
                if(medicineBean.getEqinfoz().getImage()!=null){
                    intent.putExtra("image",medicineBean.getEqinfoz().getImage());
                }
                mContext.startActivity(intent);
            }
        });
    }

}
