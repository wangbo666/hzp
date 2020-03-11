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
import com.qgyyzs.globalcosmetics.adapter.RecruitListAdapter;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.RecruitBean;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.mvp.iface.JobView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.JobPresenter;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 人才招聘页面
 */
public class RecruitActivity extends BaseActivity implements View.OnClickListener,JobView {
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
    private List<RecruitBean.JsonData> mRecruitBeanList;
    private RecruitListAdapter mRecruitListAdapter;

    private JobPresenter presenter=new JobPresenter(this,this);
    @Override
    protected int getLayout() {
        return R.layout.activity_recruit;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        Intent intent=getIntent();
        searchText = TextUtils.isEmpty(intent.getStringExtra("searchStr"))?"":intent.getStringExtra("searchStr");

    }
    @Override
    public void initData() {
        mRecruitBeanList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecruitListAdapter = new RecruitListAdapter(this,mRecruitBeanList);
        recyclerView.setAdapter(mRecruitListAdapter);

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
                presenter.getJobList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                cur = 1;
                presenter.getJobList();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right_tv://发布
                if (!MyApplication.islogin) {
                    startActivity(new Intent(RecruitActivity.this, LoginActivity.class));
                    return;
                } else {
                    startActivity(new Intent(this, JobpostActivity.class));
                }

                break;
            case R.id.LinearSearch:
                Intent intent=new Intent(this,SearchActivity.class);
                intent.putExtra("searchType","job_history");
                intent.putExtra("searchContent",searchText);
                startActivityForResult(intent,001);
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
            jsonObject.put("curpage", cur);
            jsonObject.put("pagesize", 10);
            jsonObject.put("keyword", searchText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showJobResult(RecruitBean bean) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing()) refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading()) refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if(bean==null||bean.getJsonData()==null) return;
        if(cur==1)mRecruitBeanList.clear();
        mRecruitBeanList.addAll(bean.getJsonData());
        mRecruitListAdapter.notifyDataSetChanged();
        if(bean.getMsg()==null)return;
        refreshLayout.setLoadmoreFinished(cur >= Integer.parseInt(bean.getMsg())?true:false);
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
}
