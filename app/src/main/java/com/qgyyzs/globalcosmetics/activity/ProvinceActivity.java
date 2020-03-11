package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.base.BaseRecyclerAdapter;
import com.qgyyzs.globalcosmetics.base.SmartViewHolder;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ProvincePresenter;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ProvinceActivity extends BaseActivity implements StringView,AdapterView.OnItemClickListener{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<String> mStringList;

    private String provinceString;
    private String type;

    private SharedPreferences mSharedPreferences;

    private ProvincePresenter presenter=new ProvincePresenter(this,this);
    private String[] provinces;
    private BaseRecyclerAdapter adapter;


    @Override
    protected int getLayout() {
        return R.layout.activity_province;
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter.setOnItemClickListener(this);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if(mStringList.size()==0) {
                    presenter.getProvince();
                }else {
                    refreshlayout.finishRefresh(1000);
                }
            }
        });
    }

    @Override
    public void initData() {
        mStringList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        adapter =new BaseRecyclerAdapter<String>(mStringList,android.R.layout.simple_list_item_1) {
            @Override
            protected void onBindViewHolder(SmartViewHolder holder, String model, int position) {
                holder.text(android.R.id.text1, model);
            }
        };
        recyclerView.setAdapter(adapter);


        if(!TextUtils.isEmpty(provinceString)) {
            if(!TextUtils.isEmpty(getIntent().getStringExtra("all"))){
                mStringList.add("全国");
            }

            provinces = provinceString.split(",");
            for (int j = 0; j < provinces.length; j++) {
                mStringList.add(provinces[j]);
            }
        }
        adapter.refresh(mStringList);
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        mSharedPreferences = getSharedPreferences(MyApplication.CONSTACTDATA, Context.MODE_PRIVATE);
        provinceString=mSharedPreferences.getString("province","");
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
        if(result==null)return;

        mStringList.clear();
        if(!TextUtils.isEmpty(getIntent().getStringExtra("all"))){
            mStringList.add("全国");
        }

        provinces = result.split(",");
        for (int j = 0; j < provinces.length; j++) {
            mStringList.add(provinces[j]);
        }
        adapter.refresh(mStringList);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (type.equals("shengfen")) {
            Intent intent = new Intent();
            intent.putExtra("province", mStringList.get(position).toString());
            //记录下要返回给那个父亲，可能还有什么继父什么的
            setResult(002, intent);
            finish();
        }
        if (type.equals("shengcity")) {
            Intent intent1 = new Intent(ProvinceActivity.this, CitySelectActivity.class);
            intent1.putExtra("province", mStringList.get(position).toString());
            //记录下要返回给那个父亲，可能还有什么继父什么的
            intent1.putExtra("proxy", "4");
            startActivity(intent1);
            finish();
        }
    }
}
