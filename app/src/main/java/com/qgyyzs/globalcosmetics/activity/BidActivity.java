package com.qgyyzs.globalcosmetics.activity;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.FragmentAdapter;
import com.qgyyzs.globalcosmetics.base.BaseFragmentActivity;
import com.qgyyzs.globalcosmetics.fragment.NewsExpFragment;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BidActivity extends BaseFragmentActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mNewsViewPager;

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

        mTabLayout.setVisibility(View.GONE);
    }

    @Override
    protected void initView() {
        StatusBarUtil.immersive(this);
        toolbar.setTitle("美容展会");
        mNewsViewPager.setOffscreenPageLimit(1);
    }

    @Override
    protected void initData() {
        List<String> titles = new ArrayList<>();
        titles.add("展会");

        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments = new ArrayList<>();
        NewsExpFragment fourFragment = new NewsExpFragment();

        fragments.add(fourFragment);

        FragmentAdapter mFragmentAdapteradapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mNewsViewPager.setAdapter(mFragmentAdapteradapter);
        mTabLayout.setupWithViewPager(mNewsViewPager);
    }
}