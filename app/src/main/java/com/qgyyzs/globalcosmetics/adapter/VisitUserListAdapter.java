package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trello.rxlifecycle2.components.RxActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.ChatUserdetailActivity;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.bean.VisitBean;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.VisLookOnePresenter;
import com.qgyyzs.globalcosmetics.utils.GlideCircleTransform;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public class VisitUserListAdapter extends CommonAdapter<VisitBean.JsonData> {
    private VisLookOnePresenter lookOnePresenter;;
    private int Lookid;

    public VisitUserListAdapter(Activity context,List<VisitBean.JsonData> biduserBeanList) {
        super(context,R.layout.visituser_listview_item,biduserBeanList);
        lookOnePresenter=new VisLookOnePresenter(lookoneView, (RxActivity) context);
    }

    @Override
    public void convert(ViewHolder viewHolder, final VisitBean.JsonData biduserBean, int position) {
        ImageView mItemUserphotoImg=viewHolder.getView(R.id.item_userphoto_img);
        TextView mItemUsernameTv=viewHolder.getView(R.id.item_nickname_tv);
        TextView mItemTimeTv=viewHolder.getView(R.id.item_time_tv);
        TextView mItemName=viewHolder.getView(R.id.productName);
        ImageView IvNolook=viewHolder.getView(R.id.red);

        IvNolook.setVisibility(biduserBean.getIsLook()?View.INVISIBLE:View.VISIBLE);
        mItemUsernameTv.setText(TextUtils.isEmpty(biduserBean.getUserInfo().getRealName())?"":biduserBean.getUserInfo().getRealName());
        mItemName.setText(TextUtils.isEmpty(biduserBean.getF_proname())?"":"访问了: "+biduserBean.getF_proname());

        String res="",addtime=biduserBean.getAddTime();
        if (!TextUtils.isEmpty(addtime)) {
            addtime = addtime.substring(6, addtime.length() - 2);
            long lt = new Long(addtime);
            Date date = new Date(lt);
            res = TimeUtils.twoDateDistance(date);
        }
        mItemTimeTv.setText(res);
        if(!TextUtils.isEmpty(biduserBean.getUserInfo().getHeadImg())) {
            Glide.with(mContext).load(biduserBean.getUserInfo().getHeadImg()).error(R.drawable.icon_user_defult).placeholder(R.drawable.icon_user_defult).transform(new GlideCircleTransform(mContext))
                    .into(mItemUserphotoImg);
        }else{
            mItemUserphotoImg.setImageResource(R.drawable.icon_user_defult);
        }
        viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lookid=biduserBean.getId();
                lookOnePresenter.getLookOne();
                biduserBean.setIsLook(true);
                notifyDataSetChanged();
                Intent intent = new Intent(mContext, ChatUserdetailActivity.class);
                intent.putExtra("fuserid", biduserBean.getUserId()+"");
                mContext.startActivity(intent);
            }
        });
    }

    private StringView lookoneView=new StringView() {
        @Override
        public void showStringResult(String result) {

        }

        @Override
        public void showLoading() {

        }

        @Override
        public void closeLoading() {

        }

        @Override
        public String getJsonString() {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("id",Lookid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LogUtils.e(jsonObject.toString());
            return jsonObject.toString();
        }

        @Override
        public void showToast(String msg) {

        }
    };
}
