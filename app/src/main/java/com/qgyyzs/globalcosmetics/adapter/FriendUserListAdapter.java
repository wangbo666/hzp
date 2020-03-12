package com.qgyyzs.globalcosmetics.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.ChatUserdetailActivity;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.bean.UserBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */

public class FriendUserListAdapter extends CommonAdapter<UserBean> {
    private int state;

    public FriendUserListAdapter(Activity context,List<UserBean> biduserBeanList,int state) {
        super(context,R.layout.frienduser_listview_item,biduserBeanList);
        this.state=state;
    }

    @Override
    public void convert(com.qgyyzs.globalcosmetics.base.ViewHolder viewHolder, final UserBean userBean, int position) {
        ImageView mItemUserphotoImg=viewHolder.getView(R.id.item_userphoto_img);
        TextView mItemUsernameTv=viewHolder.getView(R.id.item_username_tv);

        mItemUsernameTv.setText(TextUtils.isEmpty(userBean.getNickname())?"未命名":userBean.getNickname());
        if(TextUtils.isEmpty(userBean.getPhoto())) {
            mItemUserphotoImg.setImageResource(R.drawable.icon_user_defult);
        }else{
            Glide.with(mContext).load(userBean.getPhoto())
                    .apply(RequestOptions.circleCropTransform().error(R.drawable.icon_user_defult).placeholder(R.drawable.icon_user_defult)
                    ).into(mItemUserphotoImg);
        }
        viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatUserdetailActivity.class);
                intent.putExtra("fuserid", userBean.getUsername());
                intent.putExtra("type", state);
                mContext.startActivity(intent);
            }
        });
    }
}
