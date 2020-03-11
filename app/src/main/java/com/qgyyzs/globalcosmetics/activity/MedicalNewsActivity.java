package com.qgyyzs.globalcosmetics.activity;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.FragmentAdapter;
import com.qgyyzs.globalcosmetics.base.BaseFragmentActivity;
import com.qgyyzs.globalcosmetics.fragment.NewsAllFragment;
import com.qgyyzs.globalcosmetics.fragment.NewsHangyeFragment;
import com.qgyyzs.globalcosmetics.fragment.NewsHeadLineFragment;
import com.qgyyzs.globalcosmetics.fragment.NewsMeirongFragment;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 医药资讯
 */
public class MedicalNewsActivity extends BaseFragmentActivity {
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
    }

    @Override
    protected void initView() {
        StatusBarUtil.immersive(this);
        toolbar.setTitle("日化资讯");
        mNewsViewPager.setOffscreenPageLimit(4);
    }

    @Override
    protected void initData() {
        List<String> titles = new ArrayList<>();
        titles.add("头条");
        titles.add("综合");
        titles.add("行业");
        titles.add("美容");

        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments = new ArrayList<>();
        NewsHeadLineFragment oneFragment = new NewsHeadLineFragment();
        NewsAllFragment twoFragment = new NewsAllFragment();
        NewsHangyeFragment threeFragment=new NewsHangyeFragment();
        NewsMeirongFragment fourFragment=new NewsMeirongFragment();

        fragments.add(oneFragment);
        fragments.add(twoFragment);
        fragments.add(threeFragment);
        fragments.add(fourFragment);

        FragmentAdapter mFragmentAdapteradapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mNewsViewPager.setAdapter(mFragmentAdapteradapter);
        mTabLayout.setupWithViewPager(mNewsViewPager);
    }
}
