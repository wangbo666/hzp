package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.mvp.iface.ListStringView;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.CityPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.UpdateUserPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CitySelectActivity extends BaseActivity implements ListStringView{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.city_listview)
    ListView mListView;

    private List<String> mStringList;
    private ArrayAdapter<String> mArrayAdapter;

    private Intent mIntent;
    private String province="",city="";
    private String type = "";
    private String setType;
    private String userid;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private CityPresenter cityPresenter=new CityPresenter(this,this);
    private UpdateUserPresenter updateUserPresenter;

    @Override
    protected int getLayout() {
        return R.layout.activity_city_select;
    }

    @Override
    public void initListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if(mStringList.size()==0) {
                    cityPresenter.getCity();
                }else{
                    refreshlayout.finishRefresh(1000);
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city=mStringList.get(position);
                mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, MODE_PRIVATE);
                //向偏好设置文件中保存数据
                mEditor = mSharedPreferences.edit();
                mEditor.putString("ResPlace", province + "," + city);
                mEditor.commit();
                if (type.equals("0")) {
                    updateUserPresenter.updateUser();
                } else if (type.equals("4")) {
                    Intent intent = new Intent(CitySelectActivity.this, JobpostActivity.class);
                    intent.putExtra("province", province);
                    intent.putExtra("city", mStringList.get(position).toString());
                    //记录下要返回给那个父亲，可能还有什么继父什么的
                    startActivity(intent);
                    //向偏好设置文件中保存数据
                    mEditor = mSharedPreferences.edit();
                    mEditor.putString("sprovince", province);
                    mEditor.putString("scity", mStringList.get(position).toString());
                    mEditor.commit();
                    finish();
                } else if (type.equals("1")) {
                    if(TextUtils.isEmpty(setType)||setType.equals("null")) {
                        finish();
                    }else{
                        startActivity(new Intent(CitySelectActivity.this, ProxySettingActivity.class));
                    }
                }else if(type.equals("5")){
                    finish();
                }
            }
        });
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        mIntent = getIntent();
        province = mIntent.getStringExtra("province");
        type = mIntent.getStringExtra("proxy");
        city = mIntent.getStringExtra("city");
        setType=mIntent.getStringExtra("proxyset");
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        userid=mSharedPreferences.getString("userid","");
        toolbar.setTitle("选择城市");
        refreshLayout.setRefreshHeader(new MaterialHeader(this)).setEnableHeaderTranslationContent(false);
    }

    @Override
    public void initData() {
        mStringList = new ArrayList<>();

        updateUserPresenter=new UpdateUserPresenter(updateView,this);

        if(province.equals("全国")||province.equals("天津")||province.equals("北京")||province.equals("上海")||province.equals("重庆")||province.equals("香港")||province.equals("澳门")){
            if (type.equals("4")) {
                //向偏好设置文件中保存数据
                mEditor = mSharedPreferences.edit();
                mEditor.putString("sprovince", province);
                mEditor.putString("scity", province);
                mEditor.commit();
                finish();
            } else if(type.equals("5")){
                mEditor = mSharedPreferences.edit();
                mEditor.putString("ResPlace", province);
                mEditor.commit();
                finish();
            }else {
                mEditor = mSharedPreferences.edit();
                mEditor.putString("ResPlace", province);
                mEditor.commit();
                city="";
                updateUserPresenter.updateUser();
            }
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.autoRefresh();
                }
            }).start();
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
    public void showStringListResult(List<String> list) {
        refreshLayout.finishRefresh();
        if(list==null||list.size()==0)return;
        mStringList.clear();
        mStringList.addAll(list);
        if (mStringList.size() == 0) {
            if (type.equals("4")) {
                //向偏好设置文件中保存数据
                mEditor = mSharedPreferences.edit();
                mEditor.putString("sprovince", province);
                mEditor.putString("scity", province);
                mEditor.commit();
                finish();
            } else if(type.equals("5")){
                mEditor = mSharedPreferences.edit();
                mEditor.putString("ResPlace", province);
                mEditor.commit();
                finish();
            }else {
                mEditor = mSharedPreferences.edit();
                mEditor.putString("ResPlace", province);
                mEditor.commit();
                city="";
                updateUserPresenter.updateUser();
            }
            return;
        }
        mArrayAdapter = new ArrayAdapter<String>(CitySelectActivity.this, android.R.layout.simple_list_item_single_choice, mStringList);
        mListView.setAdapter(mArrayAdapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        if (!TextUtils.isEmpty(city)) {
            for (int j = 0; j < mStringList.size(); j++) {
                if (city.equals(mStringList.get(j).toString())) {
                    mListView.setItemChecked(j, true);
                }
            }
        }
    }

    private StringView updateView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(TextUtils.isEmpty(result))return;

            if (type.equals("0")) {
                startActivity(new Intent(CitySelectActivity.this, MyInfoActivity.class));
            }
            if (type.equals("1")) {
                if(TextUtils.isEmpty(setType)||setType.equals("null")) {
                }else{
                    startActivity(new Intent(CitySelectActivity.this, ProxySettingActivity.class));
                }
            }
            if (type.equals("4")) {
                startActivity(new Intent(CitySelectActivity.this, JobpostActivity.class));
            }
            finish();
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
                jsonObject.put("id", userid);
                jsonObject.put("province",province);
                if(!TextUtils.isEmpty(city))
                    jsonObject.put("city",city);
                LogUtils.e( jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }

        @Override
        public void showToast(String msg) {

        }
    };
}
