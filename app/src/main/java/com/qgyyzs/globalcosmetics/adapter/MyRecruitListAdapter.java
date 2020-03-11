package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.JobDetailActivity;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.bean.RecruitBean;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.DelJobPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.TimeUtils;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public class MyRecruitListAdapter extends CommonAdapter<RecruitBean.JsonData> implements StringView{
    private int id;
    private SharedPreferences mSharedPreferences;
    private String userid;
    private DelJobPresenter delJobPresenter;

    public MyRecruitListAdapter(Activity context,List<RecruitBean.JsonData> recruitBeanList) {
        super(context,R.layout.my_recruit_listview_item,recruitBeanList);
        mSharedPreferences=context.getSharedPreferences(MyApplication.USERSPINFO,Context.MODE_PRIVATE);
        userid=mSharedPreferences.getString("userid","");
        delJobPresenter=new DelJobPresenter(this, (RxFragmentActivity) context);
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
            jsonObject.put("id", id);
            jsonObject.put("userid", userid);
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
    public void convert(com.qgyyzs.globalcosmetics.base.ViewHolder viewHolder, final RecruitBean.JsonData recruitBean, final int position) {
        TextView mItemJobTv=viewHolder.getView(R.id.item_job_tv);
        TextView mItemRecruitTv=viewHolder.getView(R.id.item_recruit_tv);
        TextView mItemCompanyTv=viewHolder.getView(R.id.item_company_tv);
        TextView mItemOtherTv=viewHolder.getView(R.id.item_other_tv);
        TextView mItemTimeTv=viewHolder.getView(R.id.item_time_tv);
        mItemJobTv.setText(TextUtils.isEmpty(recruitBean.getJobName()) ? "" : recruitBean.getJobName());
        mItemRecruitTv.setText(TextUtils.isEmpty(recruitBean.getNewSalary()) ? "" : recruitBean.getNewSalary());
        mItemCompanyTv.setText(TextUtils.isEmpty(recruitBean.getCompany()) ? "" : recruitBean.getCompany());
        String a = "";
        String b = "";
        String c = "";
        if (!TextUtils.isEmpty(recruitBean.getCity())) {
            a = recruitBean.getCity();
        }
        if (!TextUtils.isEmpty(recruitBean.getNewEducation())) {
            b = recruitBean.getNewEducation();
        }
        if (!TextUtils.isEmpty(recruitBean.getNewJobType())) {
            c = recruitBean.getNewJobType();
        }
        mItemOtherTv.setText(a + " | " + b + " | " + c);
        if (!TextUtils.isEmpty(recruitBean.getAddtime())) {
            long lt = new Long(recruitBean.getAddtime().substring(6, recruitBean.getAddtime().length() - 2));
            Date date = new Date(lt);
            mItemTimeTv.setText(TimeUtils.twoDateDistance(date));
        } else {
            mItemTimeTv.setText("");
        }

        viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, JobDetailActivity.class);
                intent.putExtra("id", recruitBean.getId());
                intent.putExtra("company",recruitBean.getCompany());
                intent.putExtra("puserid", recruitBean.getUserId()+"");
                intent.putExtra("title", recruitBean.getJobName());
                mContext.startActivity(intent);
            }
        });
        viewHolder.getView(R.id.item_del_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("确定要删除该招聘吗？");
                builder.setTitle("系统提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        id=recruitBean.getId();
                        delJobPresenter.delJob();
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
    }
}
