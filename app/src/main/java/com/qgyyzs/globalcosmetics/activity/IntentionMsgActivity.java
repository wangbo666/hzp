package com.qgyyzs.globalcosmetics.activity;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.FragmentAdapter;
import com.qgyyzs.globalcosmetics.base.BaseFragmentActivity;
import com.qgyyzs.globalcosmetics.fragment.IntentionMsgFragment;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/12 0012.
 */

public class IntentionMsgActivity extends BaseFragmentActivity{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    @Override
    protected int getLayout() {
        return R.layout.activity_tablayout_viewpage;
    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initView() {
        StatusBarUtil.immersive(this);
        toolbar.setTitle("意向留言");
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    protected void initData() {
        List<String> titles = new ArrayList<>();
        titles.add("全部");
        titles.add("未读");
        titles.add("已删除");

        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments = new ArrayList<>();
        IntentionMsgFragment oneFragment = new IntentionMsgFragment();
        IntentionMsgFragment twoFragment = new IntentionMsgFragment();
        IntentionMsgFragment threeFragment = new IntentionMsgFragment();

        Bundle bundle1 = new Bundle();
        bundle1.putInt("type",1);
        oneFragment.setArguments(bundle1);
        fragments.add(oneFragment);

        Bundle bundle2 = new Bundle();
        bundle2.putInt("type",2);
        twoFragment.setArguments(bundle2);
        fragments.add(twoFragment);

        Bundle bundle3 = new Bundle();
        bundle3.putInt("type",3);
        threeFragment.setArguments(bundle3);
        fragments.add(threeFragment);


        FragmentAdapter mFragmentAdapteradapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mFragmentAdapteradapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
