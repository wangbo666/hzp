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
import com.qgyyzs.globalcosmetics.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.MultiSelectAdapter;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.customview.WaitDialog;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ProductAddAreaPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ProvincePresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/28 0028.
 */

public class ProductAreaActivity extends BaseActivity implements StringView,View.OnClickListener{
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
    private String resultstrs="";

    private SharedPreferences mSharedPreferences;
    private String provinceString;
    private String area;
    private int proid;

    private WaitDialog waitDialog;
    private ProductAddAreaPresenter addAreaPresenter=new ProductAddAreaPresenter(this,this);
    private ProvincePresenter provincePresenter;

    @Override
    protected int getLayout() {
        return R.layout.activity_recycleview_select;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        area=getIntent().getStringExtra("province");
        proid=getIntent().getIntExtra("proid",0);
        mSharedPreferences=getSharedPreferences(MyApplication.CONSTACTDATA,MODE_PRIVATE);
        provinceString=mSharedPreferences.getString("province","");
        provinceString="全国," + provinceString;

        toolbar.setTitle("招商区域设置");
        mRithtTextView.setText("保存");
        refreshLayout.setEnableLoadmore(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        waitDialog=new WaitDialog(this);
    }

    @Override
    public void initData() {
        mStringList=new ArrayList<>();

        mMultiSelectAdapter = new MultiSelectAdapter(this, mStringList,true);
        recyclerView.setAdapter(mMultiSelectAdapter);
        provincePresenter=new ProvincePresenter(provinceView,this);

        if(!TextUtils.isEmpty(provinceString)) {
            strs = provinceString.split(",");
            for (int j = 0; j < strs.length; j++) {
                mStringList.add(strs[j]);
            }
            mMultiSelectAdapter.addData(mStringList);
            mMultiSelectAdapter.getIsSelected().put(0, resultstrs.equals("全国")?true:false);
            if (!TextUtils.isEmpty(area)) {
                strsResult = area.split(",");
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
            provincePresenter.getProvince();
        }
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mRithtTextView.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if(mStringList.size()==0) {
                    provincePresenter.getProvince();
                }else{
                    refreshlayout.finishRefresh(1000);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                break;
            case R.id.tv_right:
                if (mStringList.size() == 0)return;
                resultstrs = "";
                for (int i = 0; i < mMultiSelectAdapter.getIsSelected().size(); i++) {
                    boolean flog = mMultiSelectAdapter.getIsSelected().get(i);
                    if (flog) {
                        resultstrs += mStringList.get(i) + ",";
                    }
                }
                resultstrs = ","+resultstrs;
                if(resultstrs.trim().equals(",")){
                    ToastUtil.showToast(this,"请至少选择一个区域",true);
                    return;
                }
                addAreaPresenter.addArea();
                break;
        }
    }

    @Override
    public void showLoading() {
        if(waitDialog!=null&&!waitDialog.isShowing())
            waitDialog.isShowing();
    }

    @Override
    public void closeLoading() {
        if(waitDialog!=null&&waitDialog.isShowing())
            waitDialog.dismiss();
    }

    @Override
    public String getJsonString() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("id",proid);
            jsonObject.put("zsarea",resultstrs);
            LogUtils.e(resultstrs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showStringResult(String result) {
        if(TextUtils.isEmpty(result))return;

        Intent intent=new Intent();
        intent.putExtra("area",resultstrs);
        setResult(RESULT_OK,intent);
        finish();
        ToastUtil.showToast(this,result,true);
    }

    private StringView provinceView=new StringView() {
        @Override
        public void showStringResult(String result) {
            refreshLayout.finishRefresh();
            if(TextUtils.isEmpty(result))return;
            provinceString=result;
            mStringList.clear();
            mMultiSelectAdapter.getIsSelected().clear();
            strs = provinceString.split(",");
            for (int j = 0; j < strs.length; j++) {
                mStringList.add(strs[j]);
            }
            mMultiSelectAdapter.addData(mStringList);
            mMultiSelectAdapter.getIsSelected().put(0, resultstrs.equals("全国")?true:false);
            if (!TextUtils.isEmpty(area)) {
                strsResult = area.split(",");
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
    };
}
