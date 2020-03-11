package com.qgyyzs.globalcosmetics.nim.session.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.TeamMsgAckInfo;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.nim.session.activity.AckMsgInfoActivity;
import com.qgyyzs.globalcosmetics.nim.session.adapter.AckMsgDetailAdapter;
import com.qgyyzs.globalcosmetics.nim.session.fragment.tab.AckMsgTabFragment;
import com.qgyyzs.globalcosmetics.nim.session.model.AckMsgViewModel;
import com.qgyyzs.globalcosmetics.nim.session.viewholder.AckMsgDetailHolder;
import com.qgyyzs.globalcosmetics.uikit.business.team.ui.TeamInfoGridView;
import com.qgyyzs.globalcosmetics.uikit.common.adapter.TAdapterDelegate;
import com.qgyyzs.globalcosmetics.uikit.common.adapter.TViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 群已读人员界面
 * Created by winnie on 2018/3/15.
 */

public class ReadAckMsgFragment extends AckMsgTabFragment implements TAdapterDelegate {
    private AckMsgViewModel viewModel;
    private AckMsgDetailAdapter adapter;
    private List<AckMsgDetailAdapter.AckMsgDetailItem> dataSource;
    private View rootView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAdapter();
        findViews();
        IMMessage message = (IMMessage) getActivity().getIntent().getSerializableExtra(AckMsgInfoActivity.EXTRA_MESSAGE);
        viewModel = ViewModelProviders.of(getActivity()).get(AckMsgViewModel.class);
        viewModel.init(message);
        viewModel.getTeamMsgAckInfo().observe(getActivity(), new Observer<TeamMsgAckInfo>() {
            @Override
            public void onChanged(@Nullable TeamMsgAckInfo teamMsgAckInfo) {
                for (String account : teamMsgAckInfo.getAckAccountList()) {
                    dataSource.add(new AckMsgDetailAdapter.AckMsgDetailItem(teamMsgAckInfo.getTeamId(), account));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onInit() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.unread_ack_msg_fragment, container, false);
        return rootView;
    }

    private void initAdapter() {
        dataSource = new ArrayList<>();
        adapter = new AckMsgDetailAdapter(getActivity(), dataSource, this);
    }

    private void findViews() {
        TeamInfoGridView teamInfoGridView = rootView.findViewById(R.id.team_member_grid);
        teamInfoGridView.setSelector(R.color.transparent);
        teamInfoGridView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        teamInfoGridView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    adapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
        teamInfoGridView.setAdapter(adapter);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
        return AckMsgDetailHolder.class;
    }

    @Override
    public boolean enabled(int position) {
        return false;
    }
}
