package com.qgyyzs.globalcosmetics.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.bean.FriendBean;
import com.qgyyzs.globalcosmetics.utils.GlideCircleTransform;

import java.util.List;

/**
 * Created by AMing on 16/1/14.
 * Company RongCloud
 */
public class FriendAdapter extends BaseAdapter implements SectionIndexer {

    private Context context;

    private List<FriendBean> list;

    public FriendAdapter(Context context, List<FriendBean> list) {
        this.context = context;
        this.list = list;
    }


    /**
     * 传入新的数据 刷新UI的方法
     */
    public void updateListView(List<FriendBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final FriendBean mContent = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.view_friend_item, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.friendname);
            viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.frienduri);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(list.get(position).getNickName())) {
            viewHolder.tvTitle.setText(list.get(position).getNickName());
        }else{
            viewHolder.tvTitle.setText("未命名");
        }
        if(!TextUtils.isEmpty(list.get(position).getHeadImg())) {
            Glide.with(context).load(list.get(position).getHeadImg()).error(R.drawable.icon_user_defult).placeholder(R.drawable.icon_user_defult).transform(new GlideCircleTransform(context)).into(viewHolder.mImageView);
        }else{
            viewHolder.mImageView.setImageResource(R.drawable.icon_user_defult);
        }
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getLetters().charAt(0);
    }


    final static class ViewHolder {
        /**
         * 首字母
         */
        TextView tvLetter;
        /**
         * 昵称
         */
        TextView tvTitle;
        /**
         * 头像
         */
        ImageView mImageView;
        /**
         * userid
         */
//        TextView tvUserId;
    }
}
