package com.qgyyzs.globalcosmetics.activity;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.CompanyCenterAdapter;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.CompanyCenterBean;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.mvp.iface.CompanyCenterView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.CompanyCenterPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CompanyCenterActivity extends BaseActivity implements View.OnClickListener ,CompanyCenterView{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.LinearSearch)
    LinearLayout mLinearSearch;

    private String searchText="";

    private int cur = 1;
    private List<CompanyCenterBean.JsonData> mCompanyBeanList=new ArrayList<>();
    private CompanyCenterAdapter mCompanyListAdapter;
    private CompanyCenterPresenter centerPresenter=new CompanyCenterPresenter(this, this);

    @Override
    protected int getLayout() {
        return R.layout.activity_company_center;
    }

    @Override
    public void initData() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mCompanyListAdapter = new CompanyCenterAdapter(this,mCompanyBeanList);
        recyclerView.setAdapter(mCompanyListAdapter);

        centerPresenter=new CompanyCenterPresenter(this,this);

        new Thread(new Runnable() {
            @Override
            public void run() {

                refreshLayout.autoRefresh();
            }
        }).start();

    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mLinearSearch.setOnClickListener(this);
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                cur++;
                centerPresenter.getCompanyList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                cur = 1;
                centerPresenter.getCompanyList();
            }
        });
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
//        refreshLayout.setRefreshFooter(new ClassicsFooter(this));


        searchText = TextUtils.isEmpty(getIntent().getStringExtra("searchStr"))?"":getIntent().getStringExtra("searchStr");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.LinearSearch:
                Intent intent=new Intent(this,SearchActivity.class);
                intent.putExtra("searchType","company_history");
                intent.putExtra("searchContent",searchText);
                startActivityForResult(intent,001);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && data!=null){
            searchText=data.getStringExtra("searchStr");
            cur=1;
            refreshLayout.autoRefresh();
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
            jsonObject.put("curpage", cur);//当前页码
            jsonObject.put("pagesize", 10);//当前显示几条数据
            jsonObject.put("company", searchText);
            LogUtils.e(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showCompanyListResult(CompanyCenterBean bean) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing())  refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading())   refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if (bean == null||bean.getJsonData()==null) return;
        if (cur == 1) mCompanyBeanList.clear();
        for(int i=0;i<bean.getJsonData().size();i++) {
            if(bean.getJsonData().get(i).getProlist()!=null&&bean.getJsonData().get(i).getProlist().size()>1) {
                mCompanyBeanList.add(bean.getJsonData().get(i));
            }
        }
        mCompanyListAdapter.notifyDataSetChanged();
        if(TextUtils.isEmpty(bean.getMsg()))return;
        refreshLayout.setLoadmoreFinished(cur >= Integer.parseInt(bean.getMsg()) ? true : false);
    }
}
