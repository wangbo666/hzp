package com.qgyyzs.globalcosmetics.fragment;

import android.content.Intent;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.MyFriendsActivity;
import com.qgyyzs.globalcosmetics.base.BaseFragment;
import com.qgyyzs.globalcosmetics.nim.main.fragment.SessionListFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/3/28.
 */

public class MessageFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.news_btn)
    Button mNewsButton;
    @BindView(R.id.tvRight)
    TextView tvRight;
    @BindView(R.id.contect_btn)
    Button mContactButton;

    private FragmentTransaction transaction;
    private SessionFragment chatNewsFragment;

    @Override
    protected int getLayout() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    protected void initListener() {
        mNewsButton.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        mContactButton.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        transaction = getFragmentManager().beginTransaction();
        chatNewsFragment = new ChatNewsFragment();
        transaction.add(R.id.news_container, chatNewsFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRight:
                startActivity(new Intent(getActivity(), MyFriendsActivity.class));
                break;
        }
    }
}
