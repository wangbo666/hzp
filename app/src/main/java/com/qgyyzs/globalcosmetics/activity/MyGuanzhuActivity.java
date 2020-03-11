package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.CompanyGuanListAdapter;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.CompanyGuanBean;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventGuanzhu;
import com.qgyyzs.globalcosmetics.mvp.iface.GuanzhuView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.MyGuanzhuPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyGuanzhuActivity extends BaseActivity implements GuanzhuView{
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private List<CompanyGuanBean> mCompanyBeanList;
    private CompanyGuanListAdapter mCompanyListAdapter;
    private String userid;
    private SharedPreferences mSharedPreferences;
    private int cur=1;

    private MyGuanzhuPresenter presenter=new MyGuanzhuPresenter(this,this);

    @Override
    protected int getLayout() {
        return R.layout.activity_smartrefresh_recycleview;
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                cur++;
                presenter.getGuanzhuList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                cur=1;
                presenter.getGuanzhuList();
            }
        });
    }

    @Override
    public void initData() {
        mCompanyBeanList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCompanyListAdapter = new CompanyGuanListAdapter(this,mCompanyBeanList);
        recyclerView.setAdapter(mCompanyListAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.autoRefresh();
            }
        }).start();
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        toolbar.setTitle("我的关注");
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        userid=mSharedPreferences.getString("userid","");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(AnyEventGuanzhu messageEvent){
        mCompanyBeanList.clear();
        cur=1;
        presenter.getGuanzhuList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public String getJsonString() {
        JSONObject jsonObject = new org.json.JSONObject();
        try {
            jsonObject.put("curpage", cur);
            jsonObject.put("pagesize", 10);
            jsonObject.put("userid", userid);
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
    public void showGuanzhuResult(List<CompanyGuanBean> mlist, String Msg) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing())  refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading())  refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if(mlist==null)  return;
        if(cur==1)mCompanyBeanList.clear();
        mCompanyBeanList.addAll(mlist);
        mCompanyListAdapter.notifyDataSetChanged();
        if(TextUtils.isEmpty(Msg))return;
        refreshLayout.setLoadmoreFinished(cur >= Integer.parseInt(Msg)?true:false);
    }
}
