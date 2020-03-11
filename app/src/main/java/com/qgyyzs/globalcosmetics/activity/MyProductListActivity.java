package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.FragmentAdapter;
import com.qgyyzs.globalcosmetics.base.BaseFragmentActivity;
import com.qgyyzs.globalcosmetics.customview.WaitDialog;
import com.qgyyzs.globalcosmetics.fragment.MyProductFragment;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.IsFabuPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.NoFastClickUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.qgyyzs.globalcosmetics.application.MyApplication.USERSPINFO;

public class MyProductListActivity extends BaseFragmentActivity implements StringView{
    private IsFabuPresenter fabuPresenter=new IsFabuPresenter(this,this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_right)
    TextView mRightTv;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mPublishViewPager;

    private SharedPreferences mSharedPreferences;
    private String userId;
    private boolean IsPrimary;

    private WaitDialog waitDialog;

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
        MyProductFragment oneFragment = new MyProductFragment();
        MyProductFragment twoFragment=new MyProductFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("shenhe",1);
        oneFragment.setArguments(bundle);

        Bundle bundle1 = new Bundle();
        bundle1.putInt("shenhe",0);
        twoFragment.setArguments(bundle1);

        fragments.add(oneFragment);
        fragments.add(twoFragment);
        FragmentAdapter mFragmentAdapteradapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mPublishViewPager.setAdapter(mFragmentAdapteradapter);
        mTabLayout.setupWithViewPager(mPublishViewPager);
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        waitDialog=new WaitDialog(this);
        mSharedPreferences=getSharedPreferences(USERSPINFO,Context.MODE_PRIVATE);
        userId=mSharedPreferences.getString("userid","");
        IsPrimary=mSharedPreferences.getBoolean("IsPrimary",false);
        toolbar.setTitle("我的产品");
        mRightTv.setText("发布");
        mPublishViewPager.setOffscreenPageLimit(2);
    }

    @OnClick({R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                if(IsPrimary) {
                    if (!NoFastClickUtils.isFastClick()) {
                        fabuPresenter.isFabu();
                    }
                }else{
                    ToastUtil.showToast(this,"请使用主账号发布产品",true);
                }
                break;
        }
    }

    @Override
    public void showLoading() {
        waitDialog.show();
    }

    @Override
    public void closeLoading() {
        waitDialog.dismiss();
    }

    @Override
    public String getJsonString() {
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("userid",userId);
            LogUtils.e(jsonObject.toString());
        }catch (JSONException e){}
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showStringResult(String result) {
        if (TextUtils.isEmpty(result)){
            ToastUtil.showToast(this, "账号正在审核中，审核后可发布产品", true);
            return;
        }
        Intent intent = new Intent(this, PublishProductActivity.class);
        intent.putExtra("set_type", "add");
        startActivity(intent);
    }
}
