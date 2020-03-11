package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.WebBaseActivity;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.bean.BidBean;
import com.qgyyzs.globalcosmetics.http.retrofit.RetrofitUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public class BidListAdapter extends CommonAdapter<BidBean> {
    private int type;

    public BidListAdapter(Activity context, List<BidBean> bidaBeanList, int type) {
        super(context,R.layout.bida_listview_item,bidaBeanList);
        this.type = type;
    }

    @Override
    public void convert(ViewHolder viewHolder, final BidBean bidaBean, int position) {
        viewHolder.setText(R.id.item_name_tv,bidaBean.getName());
        viewHolder.setText(R.id.item_spec_tv,bidaBean.getSpec());
        viewHolder.setText(R.id.item_price_tv,bidaBean.getPrice());
        viewHolder.setText(R.id.item_agent_tv,bidaBean.getAgent());
        viewHolder.setText(R.id.type_tv,bidaBean.getType());
        TextView mItemVenderTv=viewHolder.getView(R.id.item_vender_tv);
        TextView mItemAreaTv=viewHolder.getView(R.id.item_area_tv);
        TextView mArrow=viewHolder.getView(R.id.arrow);
        if (type == 2) {
            mItemVenderTv.setVisibility(View.GONE);
            mArrow.setCompoundDrawables(null,null,null,null);
        } else {
            mItemAreaTv.setText(bidaBean.getArea());
            mItemVenderTv.setText(bidaBean.getVender());
        }
        viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1){
                    Intent intent = new Intent(mContext, WebBaseActivity.class);
                    intent.putExtra("title", bidaBean.getName());
                    intent.putExtra("type", "bid");
                    intent.putExtra("url", RetrofitUtils.BASE_API + "product/zhongbiaoinfo?id=" + bidaBean.getId());
                    mContext.startActivity(intent);
                }
            }
        });
    }
}
