package com.qgyyzs.globalcosmetics.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.MyRecruitListAdapter;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseFragment;
import com.qgyyzs.globalcosmetics.bean.RecruitBean;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventJobList;
import com.qgyyzs.globalcosmetics.mvp.iface.JobView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.MyJobPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/25.
 */

public class MyRecruitFragment extends BaseFragment implements JobView{
    private MyJobPresenter presenter=new MyJobPresenter(this, (RxFragmentActivity) getActivity());
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;

    private List<RecruitBean.JsonData> mRecruitBeanList;
    private MyRecruitListAdapter mRecruitListAdapter;
    private int cur = 1;
    private String userid;
    private SharedPreferences mSharedPreferences;

    @Override
    protected int getLayout() {
        return R.layout.fragment_myrecriuti;
    }


    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                cur++;
                presenter.getJobList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mRecruitBeanList.clear();
                cur = 1;
                presenter.getJobList();
            }
        });
    }

    @Override
    protected void initView(View view) {
        mSharedPreferences=getActivity().getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        userid=mSharedPreferences.getString("userid","");
    }

    @Override
    protected void initData() {
        mRecruitBeanList = new ArrayList<>();
        EventBus.getDefault().register(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecruitListAdapter = new MyRecruitListAdapter(getActivity(),mRecruitBeanList);
        recyclerView.setAdapter(mRecruitListAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.autoRefresh();
            }
        }).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(AnyEventJobList messageEvent){
        cur = 1;
        presenter.getJobList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("curpage", cur);
            jsonObject.put("pagesize", 10);
            jsonObject.put("keyword", "");
            jsonObject.put("userid", userid);
            jsonObject.put("shenhe",1);
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
    public void showJobResult(RecruitBean bean) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing())  refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading()) refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if (bean == null||bean.getJsonData()==null) return;
        mRecruitBeanList.addAll(bean.getJsonData());
        mRecruitListAdapter.notifyDataSetChanged();
        if(null==bean.getMsg()||TextUtils.isEmpty(bean.getMsg()))return;
        refreshLayout.setLoadmoreFinished(cur >= Integer.parseInt(bean.getMsg())?true:false);

    }
}
