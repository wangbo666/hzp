package com.qgyyzs.globalcosmetics.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.MultiSelectAdapter;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.mvp.iface.ListStringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.CityPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CityMultActivity extends BaseActivity implements ListStringView{
    private CityPresenter cityPresenter=new CityPresenter(this,this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_right)
    TextView mRightTv;

    private String province, type, city = "", resultCity = "";
    private List<String> mStringList, mResultStringList;
    private String[] strsResult;

    private MultiSelectAdapter mMultiSelectAdapter;
    private SharedPreferences mSharedPreferences;
    private String userid;
    private String setType;
    SharedPreferences.Editor mEditor;

    @Override
    protected int getLayout() {
        return R.layout.activity_recycleview_select;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        userid=mSharedPreferences.getString("userid","");
        Intent intent = getIntent();
        province = intent.getStringExtra("province");
        setType=intent.getStringExtra("proxyset");
        type = intent.getStringExtra("type");
        city = intent.getStringExtra("city");
        mRightTv.setEnabled(false);

        mRightTv.setText("完成");
        toolbar.setTitle("选择城市");
        refreshLayout.setRefreshHeader(new MaterialHeader(this)).setEnableHeaderTranslationContent(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void initData() {
        mStringList=new ArrayList<>();

        mMultiSelectAdapter = new MultiSelectAdapter(CityMultActivity.this, mStringList, true);
        recyclerView.setAdapter(mMultiSelectAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.autoRefresh();
            }
        }).start();
    }

    @Override
    public void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                cityPresenter.getCity();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                mResultStringList = new ArrayList<>();

                if(mStringList.size()<mMultiSelectAdapter.getIsSelected().size()){
                    return;
                }
                for (int i = 0; i < mMultiSelectAdapter.getIsSelected().size(); i++) {
                    boolean flog = mMultiSelectAdapter.getIsSelected().get(i);
                    if (flog) {
                        mResultStringList.add(mStringList.get(i));
                    }
                }
                if (mResultStringList.size() == 0) {
                    ToastUtil.showToast(this, "城市选择不能为空", true);
                    return;
                }
                for (int i = 0; i < mResultStringList.size(); i++) {
                    resultCity = resultCity + mResultStringList.get(i).toString() + ",";
                }
                resultCity=resultCity.substring(0,resultCity.length()-1);
                if(!TextUtils.isEmpty(resultCity)&&resultCity.equals("全省"))
                    mEditor.putString("dailiArea", province);
                else
                    mEditor.putString("dailiArea", province+","+resultCity);
                mEditor.commit();
                if(TextUtils.isEmpty(setType)||setType.equals("null")) {
                    finish();
                }else{
                    startActivity(new Intent(this, ProxySettingActivity.class));
                }
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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("province", province);
            LogUtils.e( jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showStringListResult(List<String> cityList) {
        refreshLayout.finishRefresh();
        mRightTv.setEnabled(true);
        if (null==cityList||cityList.size()==0) {
            mEditor.putString("dailiArea", province);
            mEditor.commit();
            finish();
            return;
        }
        mStringList.clear();
        mStringList.add("全省");
        mStringList.addAll(cityList);
        mMultiSelectAdapter.addData(mStringList);
        if (!city.equals("") && !TextUtils.isEmpty(city)) {
            mMultiSelectAdapter.getIsSelected().put(0, false);
        }
        if (city.equals("null") || (city.equals("")) || (city.equals("全省"))) {
            mMultiSelectAdapter.getIsSelected().put(0, true);
        }
        strsResult = city.split(",");
        for (int j = 1; j < cityList.size(); j++) {
            mMultiSelectAdapter.getIsSelected().put(j, false);
            for (int k = 0; k < strsResult.length; k++) {
                if (cityList.get(j).equals(strsResult[k])) {
                    mMultiSelectAdapter.getIsSelected().put(j, true);
                }
            }
        }
        mMultiSelectAdapter.notifyDataSetChanged();
    }
}
