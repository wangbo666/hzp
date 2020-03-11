package com.qgyyzs.globalcosmetics.nim.session.viewholder;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.nim.session.adapter.AckMsgDetailAdapter;
import com.qgyyzs.globalcosmetics.uikit.business.team.activity.AdvancedTeamMemberInfoActivity;
import com.qgyyzs.globalcosmetics.uikit.business.team.helper.TeamHelper;
import com.qgyyzs.globalcosmetics.uikit.common.adapter.TViewHolder;
import com.qgyyzs.globalcosmetics.uikit.common.ui.imageview.HeadImageView;

public class AckMsgDetailHolder extends TViewHolder {

    private HeadImageView headImageView;

    private TextView nameTextView;

    private AckMsgDetailAdapter.AckMsgDetailItem memberItem;

    protected AckMsgDetailAdapter getAdapter() {
        return (AckMsgDetailAdapter) super.getAdapter();
    }

    @Override
    protected int getResId() {
        return R.layout.ack_msg_detail_item;
    }

    @Override
    protected void inflate() {
        headImageView = view.findViewById(R.id.imageViewHeader);
        nameTextView = view.findViewById(R.id.textViewName);
    }

    @Override
    protected void refresh(Object item) {
        memberItem = (AckMsgDetailAdapter.AckMsgDetailItem) item;
        headImageView.resetImageView();

        refreshTeamMember(memberItem);
    }

    private void refreshTeamMember(final AckMsgDetailAdapter.AckMsgDetailItem item) {
        nameTextView.setText(TeamHelper.getTeamMemberDisplayName(item.getTid(), item.getAccount()));
        headImageView.loadBuddyAvatar(item.getAccount());
        headImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedTeamMemberInfoActivity.startActivityForResult((Activity) context, item.getAccount(), item.getTid());

            }
        });

    }
}
