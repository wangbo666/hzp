package com.qgyyzs.globalcosmetics.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ProvincePresenter;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ProvinceSelectActivity extends BaseActivity implements StringView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.mListView)
    ListView mListView;
    private List<String> mStringList;
    private ArrayAdapter<String> mArrayAdapter;
    private String provinceString;
    private String[] strs;
    private String type, cityString, province = "";
    private int proid;
    private SharedPreferences mSharedPreferences;
    private String set;

    private ProvincePresenter presenter=new ProvincePresenter(this,this);

    @Override
    protected int getLayout() {
        return R.layout.activity_listview_select;
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent;
                if(type.equals("1"))
                    intent = new Intent(ProvinceSelectActivity.this, CityMultActivity.class);
                else
                    intent =new Intent(ProvinceSelectActivity.this,CitySelectActivity.class);
                intent.putExtra("province", mStringList.get(position).toString());
                intent.putExtra("proxyset",set);
                intent.putExtra("proxy", type);
                intent.putExtra("city", cityString);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void initData() {

        mStringList = new ArrayList<>();

        provinceString=mSharedPreferences.getString("province","");


        mArrayAdapter = new ArrayAdapter<String>(ProvinceSelectActivity.this, android.R.layout.simple_list_item_single_choice, mStringList);
        mListView.setAdapter(mArrayAdapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        if(TextUtils.isEmpty(provinceString)||provinceString.equals("null")){
            presenter.getProvince();
        }else {
            strs = provinceString.split(",");
            if (type.equals("1")) {
                mStringList.add("全国");
            }
            for (int j = 0; j < strs.length; j++) {
                mStringList.add(strs[j]);
            }
            if (!TextUtils.isEmpty(province)) {
                for (int j = 0; j < mStringList.size(); j++) {
                    if (province.equals(mStringList.get(j).toString())) {
                        mListView.setItemChecked(j, true);
                    }
                }
            }
            mArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        Intent intent = getIntent();
        mSharedPreferences=getSharedPreferences(MyApplication.CONSTACTDATA,MODE_PRIVATE);
        type = intent.getStringExtra("proxy");
        proid = intent.getIntExtra("proid", 0);
        set=intent.getStringExtra("proxyset");
        cityString = intent.getStringExtra("city");
        province = intent.getStringExtra("province");
        toolbar.setTitle("选择地区");
    }

    @Override
    public void showStringResult(String provinceString) {
        refreshLayout.finishRefresh();
        if(provinceString==null)return;

        this.provinceString = provinceString;
        mStringList.clear();

        strs = provinceString.split(",");
        if (type.equals("1")) {
            mStringList.add("全国");
        }
        for (int j = 0; j < strs.length; j++) {
            mStringList.add(strs[j]);
        }
        if (!TextUtils.isEmpty(province)) {
            for (int j = 0; j < mStringList.size(); j++) {
                if (province.equals(mStringList.get(j).toString())) {
                    mListView.setItemChecked(j, true);
                }
            }
        }
        mArrayAdapter.notifyDataSetChanged();
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
}
