package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.LoginActivity;
import com.qgyyzs.globalcosmetics.activity.MedicineDetailActivity;
import com.qgyyzs.globalcosmetics.activity.ProductSettingActivity;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.bean.MyProductBean;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.DelProductPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.qgyyzs.globalcosmetics.application.MyApplication.USERSPINFO;

/**
 * Created by Administrator on 2017/4/13.
 */

public class MyMedicineListAdapter extends CommonAdapter<MyProductBean.JsonData> implements StringView{
    private int proid;
    private SharedPreferences mSharedPreferences;
    private DelProductPresenter delProductPresenter;

    public MyMedicineListAdapter(Activity context,List<MyProductBean.JsonData> medicineBeanList) {
        super(context,R.layout.mymedicine_listview_item,medicineBeanList);
        mSharedPreferences=context.getSharedPreferences(USERSPINFO,Context.MODE_PRIVATE);
        delProductPresenter=new DelProductPresenter(this, (RxFragmentActivity) context);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public String getJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", proid);
            LogUtils.e( jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showStringResult(String result) {
        if(result==null)return;

        ToastUtil.showToast(mContext,result,true);
    }

    @Override
    public void convert(ViewHolder viewHolder, final MyProductBean.JsonData medicineBean, final int position) {
        ImageView mItemMedicineLogo=viewHolder.getView(R.id.item_medicine_logo);
        RelativeLayout rlVip=viewHolder.getView(R.id.rl_vip);
        TextView mItemMedicineName=viewHolder.getView(R.id.item_medicine_name);
        TextView mItemMedicineType=viewHolder.getView(R.id.item_medicine_type);
        TextView mItemBrowsenumTv=viewHolder.getView(R.id.item_browsenum_tv);
        TextView mItemMedicineSpec=viewHolder.getView(R.id.item_medicine_spec);
        TextView mItemMedicineVender=viewHolder.getView(R.id.item_medicine_vender);
        TextView mItemMedicineAdvantage=viewHolder.getView(R.id.item_medicine_advantage);
        LinearLayout llvis=viewHolder.getView(R.id.llvis);

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
            Glide.with(mContext).load(str[0]).apply(new RequestOptions().error(R.mipmap.medicin_defult).placeholder(R.mipmap.medicin_defult)).into(mItemMedicineLogo);
        }
        rlVip.setVisibility(medicineBean.getFlag()==1?View.VISIBLE:View.GONE);
        llvis.setVisibility(mSharedPreferences.getBoolean("IsPrimary",false)?View.VISIBLE:View.GONE);

        viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyApplication.islogin) {
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    return;
                } else {
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
            }
        });
        viewHolder.getView(R.id.item_del_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("确定要删除该产品吗？");
                builder.setTitle("系统提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        proid=medicineBean.getId();
                        delProductPresenter.delProduct();
                        mDatas.remove(position);
                        notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        viewHolder.getView(R.id.item_update_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductSettingActivity.class);
                intent.putExtra("proid", medicineBean.getId());
                intent.putExtra("type", "编辑");
                mContext.startActivity(intent);
            }
        });
    }
}
