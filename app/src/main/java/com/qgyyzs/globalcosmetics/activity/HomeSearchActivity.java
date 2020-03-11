package com.qgyyzs.globalcosmetics.activity;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.FragmentAdapter;
import com.qgyyzs.globalcosmetics.base.BaseFragmentActivity;
import com.qgyyzs.globalcosmetics.fragment.SearchBidFragment;
import com.qgyyzs.globalcosmetics.fragment.SearchProductFragment;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeSearchActivity extends BaseFragmentActivity {
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mNewsViewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.shadow)
    View shadow;

    @Override
    protected int getLayout() {
        return R.layout.activity_tablayout_viewpage;
    }

    @Override
    public void initView() {
        toolbar.setVisibility(View.GONE);
        shadow.setVisibility(View.GONE);
    }

    @Override
    public void initListener(){
        StatusBarUtil.immersive(this);
    }

    @Override
    public void initData() {
        List<String> titles = new ArrayList<>();
        titles.add("找产品");
        titles.add("找公司");

        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments = new ArrayList<>();
        SearchProductFragment oneFragment = new SearchProductFragment();
        SearchBidFragment twoFragment = new SearchBidFragment();
        fragments.add(oneFragment);
        fragments.add(twoFragment);
        FragmentAdapter mFragmentAdapteradapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        //给ViewPager设置适配器
        mNewsViewPager.setAdapter(mFragmentAdapteradapter);
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mNewsViewPager);
        //给TabLayout设置适配器
        mTabLayout.setTabsFromPagerAdapter(mFragmentAdapteradapter);
    }
}
