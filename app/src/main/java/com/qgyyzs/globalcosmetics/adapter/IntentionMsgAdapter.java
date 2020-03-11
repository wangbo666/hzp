package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.IntentionMsgDetialActivity;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.bean.IntentionBean;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.DelIntenionMsgPresenter;
import com.qgyyzs.globalcosmetics.utils.TimeUtils;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/10/12 0012.
 */

public class IntentionMsgAdapter extends CommonAdapter<IntentionBean.JsonData> implements StringView{
    private int type;
    private int id;
    private DelIntenionMsgPresenter delIntenionMsgPresenter;

    public IntentionMsgAdapter(Activity context,List<IntentionBean.JsonData> biduserBeanList,int type) {
        super(context,R.layout.visituser_listview_item,biduserBeanList);
        this.type=type;
        delIntenionMsgPresenter=new DelIntenionMsgPresenter(this, (RxFragmentActivity) context);
    }

    @Override
    public void convert(final ViewHolder viewHolder, final IntentionBean.JsonData biduserBean, final int position) {
        ImageView mItemUserphotoImg=viewHolder.getView(R.id.item_userphoto_img);
        TextView mItemUsernameTv=viewHolder.getView(R.id.item_nickname_tv);
        TextView mItemTimeTv=viewHolder.getView(R.id.item_time_tv);
        TextView mItemName=viewHolder.getView(R.id.productName);
        ImageView IvNolook=viewHolder.getView(R.id.red);
        Button btnDel=viewHolder.getView(R.id.btnDel);

        IvNolook.setVisibility(biduserBean.getIsLook()==1? View.INVISIBLE:View.VISIBLE);
        mItemUsernameTv.setText(TextUtils.isEmpty(biduserBean.getTitle())?"":biduserBean.getTitle());
        if(TextUtils.isEmpty(biduserBean.getContent())) {
            mItemName.setText("");
        }else{
            String str[]=biduserBean.getContent().split("<br>");
            mItemName.setText(str[0]);
        }
        String res="",addtime=biduserBean.getCreateTime();
        if (!TextUtils.isEmpty(addtime)) {
            addtime = addtime.substring(6, addtime.length() - 2);
            long lt = new Long(addtime);
            Date date = new Date(lt);
            res = TimeUtils.twoDateDistance(date);
        }
        mItemTimeTv.setText(res);
        viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biduserBean.setIsLook(1);
                notifyDataSetChanged();
                Intent intent=new Intent(mContext,IntentionMsgDetialActivity.class);
                intent.putExtra("id",biduserBean.getId());
                intent.putExtra("title",biduserBean.getTitle());
                mContext.startActivity(intent);
            }
        });
        if(type!=3) {
            btnDel.setVisibility(View.VISIBLE);
        }
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SwipeMenuLayout) viewHolder.itemView).quickClose();
                mDatas.remove(position);
                notifyDataSetChanged();
                id=biduserBean.getId();
                delIntenionMsgPresenter.delMsg();
            }
        });
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
            jsonObject.put("id",id);
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
}
