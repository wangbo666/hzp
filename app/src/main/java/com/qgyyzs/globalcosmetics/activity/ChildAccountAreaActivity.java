package com.qgyyzs.globalcosmetics.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.utils.ToastUtil;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.MultiSelectAdapter;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.customview.WaitDialog;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.GetKefuAreaPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.SetKefuAreaPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class ChildAccountAreaActivity extends BaseActivity implements View.OnClickListener,StringView{
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

    private String userid,username,area;
    private SharedPreferences mSharedPreferences;

    private GetKefuAreaPresenter kefuAreaPresenter=new GetKefuAreaPresenter(this,this);
    private SetKefuAreaPresenter setKefuAreaPresenter;

    private WaitDialog waitDialog;

    @Override
    protected int getLayout() {
        return R.layout.activity_recycleview_select;
    }

    @Override
    public void initView() {
        waitDialog=new WaitDialog(this);
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        mSharedPreferences =getSharedPreferences(MyApplication.USERSPINFO,MODE_PRIVATE);
        username=mSharedPreferences.getString("username","");
        userid=getIntent().getStringExtra("userid");
        area=getIntent().getStringExtra("area");
        toolbar.setTitle("招商区域设置");
        mRithtTextView.setText("保存");

        refreshLayout.setRefreshHeader(new MaterialHeader(this)).setEnableHeaderTranslationContent(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void initData() {
        mStringList=new ArrayList<>();

        mMultiSelectAdapter = new MultiSelectAdapter(ChildAccountAreaActivity.this, mStringList, false);
        recyclerView.setAdapter(mMultiSelectAdapter);

        setKefuAreaPresenter=new SetKefuAreaPresenter(setView,this);
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
                if(mStringList.size()==0) {
                    kefuAreaPresenter.getArea();
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_right:
                if (mStringList.size() == 0) return;
                resultstrs = ",";
                for (int i = 0; i < mMultiSelectAdapter.getIsSelected().size(); i++) {
                    boolean flog = mMultiSelectAdapter.getIsSelected().get(i);
                    if (flog) {
                        resultstrs += mStringList.get(i) + ",";
                    }
                }
                if(resultstrs.trim().equals(",")){
                    ToastUtil.showToast(this,"请至少选择一个区域",true);
                    return;
                }
                LogUtils.e("resultstrs"+resultstrs);
                setKefuAreaPresenter.setArea();
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
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("pcusername", username);
            jsonObject.put("userid",userid);
            LogUtils.e(jsonObject.toString());
        }catch (JSONException e){

        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showStringResult(String areaString) {
        if(refreshLayout!=null)refreshLayout.finishRefresh();
        if(TextUtils.isEmpty(areaString))return;
        mStringList.clear();
        if(mMultiSelectAdapter!=null)mMultiSelectAdapter.getIsSelected().clear();
        strs = areaString.split(",");
        for (int j = 0; j < strs.length; j++) {
            mStringList.add(strs[j]);
        }
        mMultiSelectAdapter.addData(mStringList);
        if (!area.equals("")) {
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

    private StringView setView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(TextUtils.isEmpty(result))return;

            Intent intent=new Intent();
            intent.putExtra("area",resultstrs);
            setResult(RESULT_OK,intent);
            finish();
            ToastUtil.showToast(ChildAccountAreaActivity.this,result,true);
        }

        @Override
        public void showLoading() {
            if(waitDialog!=null&&!waitDialog.isShowing())
                waitDialog.show();
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
                jsonObject.put("kefuarea", resultstrs);
                jsonObject.put("userid",userid);
                LogUtils.e(jsonObject.toString());
            }catch (JSONException e){

            }
            return jsonObject.toString();
        }

        @Override
        public void showToast(String msg) {

        }
    };
}
