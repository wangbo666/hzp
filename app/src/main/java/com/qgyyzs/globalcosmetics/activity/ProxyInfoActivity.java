package com.qgyyzs.globalcosmetics.activity;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseFragmentActivity;
import com.qgyyzs.globalcosmetics.fragment.ProxyInfoFragment;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import butterknife.BindView;

public class ProxyInfoActivity extends BaseFragmentActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FragmentTransaction transaction;
    private ProxyInfoFragment proxyInfoFragment;

    @Override
    protected int getLayout() {
        return R.layout.activity_proxyinfo;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        transaction = getSupportFragmentManager().beginTransaction();
        proxyInfoFragment = new ProxyInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hide","hideBar");
        proxyInfoFragment.setArguments(bundle);
        transaction.add(R.id.fragment_container, proxyInfoFragment);
        transaction.commit();
    }



    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
    }
}
