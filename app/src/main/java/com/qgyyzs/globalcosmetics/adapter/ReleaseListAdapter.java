package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.MyBaseAdapter;
import com.qgyyzs.globalcosmetics.bean.ReleaseBean;
import com.qgyyzs.globalcosmetics.utils.TimeUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/11.
 */

public class ReleaseListAdapter extends MyBaseAdapter {
    public ReleaseListAdapter(Activity context,List<ReleaseBean> releaseBeanList) {
        super(context,releaseBeanList);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.release_listview_item;
    }

    @Override
    public Object getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }

    @Override
    public void setItemData(int position, Object dataItem, Object viewHolder) {
        ViewHolder myholder= (ViewHolder) viewHolder;
        final ReleaseBean releaseBean = (ReleaseBean) dataItem;
        if (releaseBean.getType().equals("招商")) {
            try {
                String str[]=releaseBean.getPhoto().split("\\|");
                Glide.with(context).load(str[0])
                        .apply(RequestOptions.circleCropTransform().error(R.mipmap.new_pub_sku).placeholder(R.mipmap.new_pub_sku)).into(myholder.mItemTypeImage);
            }catch (Exception e){
                myholder.mItemTypeImage.setImageResource(R.mipmap.new_pub_sku);
            }
        }else{
            try {
                Glide.with(context).load(releaseBean.getPhoto()).apply(RequestOptions.circleCropTransform()
                        .error(R.mipmap.new_pub_proxy).placeholder(R.mipmap.new_pub_proxy)).into(myholder.mItemTypeImage);
            }catch (Exception e){
                myholder.mItemTypeImage.setImageResource(R.mipmap.new_pub_proxy);
            }
        }
        myholder.mItemTitleTv.setText(TextUtils.isEmpty(releaseBean.getTitle())?"":releaseBean.getTitle());
        myholder.mItemRemarkTv.setText(TextUtils.isEmpty(releaseBean.getRemark())?"":releaseBean.getRemark());
        if (TextUtils.isEmpty(releaseBean.getTime())||releaseBean.getTime().equals("null")) {
            myholder.mItemTimeTv.setText("");
        } else {
            long lt = new Long(releaseBean.getTime().substring(6, releaseBean.getTime().length() - 2));
            Date date = new Date(lt);
            myholder.mItemTimeTv.setText(TimeUtils.twoDateDistance(date));
            if(releaseBean.getType().equals("代理")){
                if(TimeUtils.twoDateDistance(date).equals("刚刚")){
                    myholder.mItemTimeTv.setText("刚刚在线");
                }
            }
        }
    }


    static class ViewHolder {
        @BindView(R.id.item_type_image)
        ImageView mItemTypeImage;
        @BindView(R.id.item_title_tv)
        TextView mItemTitleTv;
        @BindView(R.id.item_remark_tv)
        TextView mItemRemarkTv;
        @BindView(R.id.item_time_tv)
        TextView mItemTimeTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
