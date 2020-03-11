package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.MyBaseAdapter;

import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/18.
 */

public class GridviewFliterAdapter extends MyBaseAdapter {
    private int lastPosition = -1;   //lastPosition 记录上一次选中的图片位置，-1表示未选中
    private Vector<Boolean> vector = new Vector<Boolean>();// 定义一个向量作为选中与否容器

    private boolean multiChoose;                //表示当前适配器是否允许多选

    public GridviewFliterAdapter(Activity context, List<String> stringList, boolean isMulti) {
        super(context,stringList);
        multiChoose = isMulti;
        for (int i = 0; i < stringList.size(); i++) {
            vector.add(false);
        }
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.fliter_gridview_item;
    }

    @Override
    public Object getViewHolder(View rootView) {
        return new ViewHolder(rootView);
    }

    @Override
    public void setItemData(int position, Object dataItem, Object viewHolder) {
        String str = (String) dataItem;
        ViewHolder myholder= (ViewHolder) viewHolder;
        if (!str.equals("")) {
            myholder.mItemFliterTv.setText(str);
        }
        if (vector.elementAt(position) == true) {
            myholder.mItemFliterTv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.fliter_bg_p));
            myholder.mItemFliterTv.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            myholder.mItemFliterTv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.fliter_bg_n));
            myholder.mItemFliterTv.setTextColor(context.getResources().getColor(R.color.item_title2));
        }
    }


    static class ViewHolder {
        @BindView(R.id.item_fliter_tv)
        TextView mItemFliterTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 修改选中时的状态
     *
     * @param position
     */
    public void changeState(int position) {
       /* // 多选时
        if (multiChoose == true) {
            vector.setElementAt(!vector.elementAt(position), position);
            //直接取反即可
        } else {

            //通知适配器进行更新
        }*/
        if (multiChoose == true) {
            vector.setElementAt(!vector.elementAt(position), position);

            notifyDataSetChanged();
        } else {
            if (lastPosition != -1) {
                vector.setElementAt(false, lastPosition);                   //取消上一次的选中状态
            }
            vector.setElementAt(!vector.elementAt(position), position);     //直接取反即可
            lastPosition = position; //记录本次选中的位置
            notifyDataSetChanged();
        }
    }
}
