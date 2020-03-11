package com.qgyyzs.globalcosmetics.activity;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.FragmentAdapter;
import com.qgyyzs.globalcosmetics.base.BaseFragmentActivity;
import com.qgyyzs.globalcosmetics.fragment.MyProxyFragment;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyProxyActivity extends BaseFragmentActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_right)
    TextView mRightTv;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mPublishViewPager;

    private int mCurrentPosition = 0;
    private String NickName;

    @Override
    protected int getLayout() {
        return R.layout.activity_tablayout_viewpage;
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mPublishViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 把当前显示的position传递出去
                mCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {
        List<String> titles = new ArrayList<>();
        titles.add("已审核");
        titles.add("审核中");

        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments = new ArrayList<>();
        MyProxyFragment oneFragment = new MyProxyFragment();
        MyProxyFragment twoFragment=new MyProxyFragment();

        Bundle bundle = new Bundle();
        bundle.putString("touserid", getIntent().getStringExtra("userid"));
        bundle.putString("tousername",getIntent().getStringExtra("username"));
        bundle.putInt("shenhe",1);
        oneFragment.setArguments(bundle);

        Bundle bundle1 = new Bundle();
        bundle1.putString("touserid", getIntent().getStringExtra("userid"));
        bundle1.putString("tousername",getIntent().getStringExtra("username"));
        bundle1.putInt("shenhe",0);
        twoFragment.setArguments(bundle1);

        fragments.add(oneFragment);
        if(TextUtils.isEmpty(NickName)) {
            fragments.add(twoFragment);
        }
        FragmentAdapter mFragmentAdapteradapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mPublishViewPager.setAdapter(mFragmentAdapteradapter);
        mTabLayout.setupWithViewPager(mPublishViewPager);
    }

    @Override
    public void initView(){
        StatusBarUtil.immersive(this);
        NickName=getIntent().getStringExtra("nickname");
        if(getIntent().getStringExtra("nickname")==null){
            toolbar.setTitle("我的代理");
            mRightTv.setText("发布");
        }else{
            toolbar.setTitle(NickName+"发布的代理");
            mRightTv.setVisibility(View.GONE);
            mTabLayout.setVisibility(View.GONE);
        }
        mPublishViewPager.setOffscreenPageLimit(2);
    }

    @OnClick({R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                Log.e("position", "位置：：" + mCurrentPosition);
                Intent intent = new Intent(this, PublishProxyActivity.class);
                intent.putExtra("type", mCurrentPosition);
                startActivity(intent);
                break;
        }
    }
}
