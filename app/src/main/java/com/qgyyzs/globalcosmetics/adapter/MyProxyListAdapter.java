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
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.UserDetailActivity;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.bean.ProxyInfoBean;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.DelProxyPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.qgyyzs.globalcosmetics.application.MyApplication.USERSPINFO;

/**
 * Created by Administrator on 2017/4/15.
 */

public class MyProxyListAdapter extends CommonAdapter<ProxyInfoBean.JsonData> implements StringView{
    private String tousername;
    private int dailiId;
    private SharedPreferences mSharedPreferences;
    private DelProxyPresenter delProxyPresenter;

    public MyProxyListAdapter(Activity context, List<ProxyInfoBean.JsonData> buyaBeanList, String tousername) {
        super(context,R.layout.my_publish_listview_item,buyaBeanList);
        this.tousername=tousername;
        mSharedPreferences=context.getSharedPreferences(USERSPINFO,Context.MODE_PRIVATE);
        delProxyPresenter=new DelProxyPresenter(this, (RxFragmentActivity) context);
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
            jsonObject.put("id", dailiId);
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
    public void convert(ViewHolder viewHolder, final ProxyInfoBean.JsonData buyaBean, final int position) {
        TextView mItemDelTv=viewHolder.getView(R.id.item_del_tv);
        TextView Name=viewHolder.getView(R.id.item_proxy_name);
        TextView Content=viewHolder.getView(R.id.item_type_tv);
        TextView Subject=viewHolder.getView(R.id.item_subject_tv);
        TextView address=viewHolder.getView(R.id.item_address_tv);
        View Vdelete=viewHolder.getView(R.id.item_delete_v);

        mItemDelTv.setVisibility(mSharedPreferences.getBoolean("IsPrimary",false)?View.VISIBLE:View.GONE);
        mItemDelTv.setVisibility(TextUtils.isEmpty(tousername)?View.VISIBLE:View.GONE);
        Vdelete.setVisibility(mSharedPreferences.getBoolean("IsPrimary",false)?View.VISIBLE:View.GONE);
        Vdelete.setVisibility(TextUtils.isEmpty(tousername)?View.VISIBLE:View.GONE);
        Name.setText(TextUtils.isEmpty(buyaBean.getProName())?"":Html.fromHtml(buyaBean.getProName()));
        address.setText(TextUtils.isEmpty(buyaBean.getDailiArea())?"":buyaBean.getDailiArea());
        Content.setText(TextUtils.isEmpty(buyaBean.getContent())?"": Html.fromHtml(buyaBean.getContent()));
        Subject.setText(TextUtils.isEmpty(buyaBean.getQudao())?"":buyaBean.getQudao());
        mItemDelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("确定要删除该信息吗？");
                builder.setTitle("系统提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dailiId=buyaBean.getId();
                        delProxyPresenter.delProduct();
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
        viewHolder.getView(R.id.cardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, UserDetailActivity.class);
                intent.putExtra("muser", buyaBean.getPcUsername());
                intent.putExtra("touserid", buyaBean.getAppUserId()+"");
                intent.putExtra("id",buyaBean.getId());
                intent.putExtra("nickname", TextUtils.isEmpty(buyaBean.getProName())?"代理详情":buyaBean.getProName());
                mContext.startActivity(intent);
            }
        });
    }
}
