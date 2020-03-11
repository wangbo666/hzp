package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.JobDetailActivity;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.bean.RecruitBean;
import com.qgyyzs.globalcosmetics.utils.TimeUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public class RecruitListAdapter extends CommonAdapter<RecruitBean.JsonData> {

    public RecruitListAdapter(Activity context,List<RecruitBean.JsonData> recruitBeanList) {
        super(context,R.layout.recruit_listview_item,recruitBeanList);
    }

    @Override
    public void convert(ViewHolder viewHolder, final RecruitBean.JsonData recruitBean, int position) {
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
    }
}
