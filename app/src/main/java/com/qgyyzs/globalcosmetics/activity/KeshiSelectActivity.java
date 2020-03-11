package com.qgyyzs.globalcosmetics.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.MultiSelectAdapter;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.KeshiPresenter;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class KeshiSelectActivity extends BaseActivity implements View.OnClickListener,StringView{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_right)
    TextView mRithtTextView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private MultiSelectAdapter mMultiSelectAdapter;
    private String[] strs, strsResult;
    private List<String> mStringList;
    //熟悉的科室
    private String resultstrs = "";

    private SharedPreferences mSharedPreferences,mSharedPreferences1;
    private SharedPreferences.Editor mEditor;

    private String keshi = "";

    private KeshiPresenter keshiPresenter=new KeshiPresenter(this,this);

    @Override
    protected int getLayout() {
        return R.layout.activity_recycleview_select;
    }

    @Override
    public void initData() {
        mStringList = new ArrayList<>();

        mMultiSelectAdapter = new MultiSelectAdapter(KeshiSelectActivity.this, mStringList, false);
        recyclerView.setAdapter(mMultiSelectAdapter);

        String keshiString=mSharedPreferences1.getString("keshi","");
        if(!TextUtils.isEmpty(keshiString)) {
            strs = keshiString.split(",");
            for (int j = 0; j < strs.length; j++) {
                mStringList.add(strs[j]);
            }
            mMultiSelectAdapter.addData(mStringList);
            if (!keshi.equals("")) {
                strsResult = keshi.split(",");
                for (int j = 0; j < strs.length; j++) {
                    mMultiSelectAdapter.getIsSelected().put(j, false);
                    for (int k = 0; k < strsResult.length; k++) {
                        if (strs[j].equals(strsResult[k])) {
                            mMultiSelectAdapter.getIsSelected().put(j, true);
                        }
                    }

                }
            }
            mMultiSelectAdapter.notifyDataSetChanged();
        }else{
            keshiPresenter.getKeshi();
        }

    }

    @Override
    public void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if(mStringList.size()==0) {
                    keshiPresenter.getKeshi();
                }else{
                    refreshlayout.finishRefresh(1000);
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mRithtTextView.setOnClickListener(this);
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        Intent intent = getIntent();
        keshi = intent.getStringExtra("keshi");

        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO,MODE_PRIVATE);
        mSharedPreferences1 = getSharedPreferences(MyApplication.CONSTACTDATA,MODE_PRIVATE);
        mRithtTextView.setText("保存");
        toolbar.setTitle("我熟悉的科室");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                if (mStringList.size() == 0) return;
                resultstrs = "";
                for (int i = 0; i < mMultiSelectAdapter.getIsSelected().size(); i++) {
                    boolean flog = mMultiSelectAdapter.getIsSelected().get(i);
                    if (flog) {
                        resultstrs += mStringList.get(i) + ",";
                    }
                }
                if(!TextUtils.isEmpty(resultstrs)) {
                    resultstrs = resultstrs.substring(0, resultstrs.length() - 1);
                }
                Intent intent=new Intent();
                intent.putExtra("keshi",resultstrs);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public String getJsonString() {
        return null;
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showStringResult(String result) {
        refreshLayout.finishRefresh();
        if(TextUtils.isEmpty(result))return;
        mStringList.clear();
        if(mMultiSelectAdapter!=null)mMultiSelectAdapter.getIsSelected().clear();
        strs = result.split(",");
        for (int j = 0; j < strs.length; j++) {
            mStringList.add(strs[j]);
        }
        mMultiSelectAdapter.addData(mStringList);
        if (!keshi.equals("")) {
            strsResult = keshi.split(",");
            for (int j = 0; j < strs.length; j++) {
                mMultiSelectAdapter.getIsSelected().put(j, false);
                for (int k = 0; k < strsResult.length; k++) {
                    if (strs[j].equals(strsResult[k])) {
                        mMultiSelectAdapter.getIsSelected().put(j, true);
                    }
                }

            }
        }
        mMultiSelectAdapter.notifyDataSetChanged();
    }
}
