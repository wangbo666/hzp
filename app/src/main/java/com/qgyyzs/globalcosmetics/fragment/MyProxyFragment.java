package com.qgyyzs.globalcosmetics.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.MyProxyListAdapter;
import com.qgyyzs.globalcosmetics.base.BaseFragment;
import com.qgyyzs.globalcosmetics.bean.ProxyInfoBean;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventMyProxyList;
import com.qgyyzs.globalcosmetics.mvp.iface.ProxyInfoView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ProxyInfoPresenter;
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
 * Created by Administrator on 2017/3/28.
 */

public class MyProxyFragment extends BaseFragment implements ProxyInfoView{
    private ProxyInfoPresenter presenter=new ProxyInfoPresenter(this, (RxFragmentActivity) getActivity());
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;


    private List<ProxyInfoBean.JsonData> mBuyaBeanList;
    private MyProxyListAdapter mPublishListAdapter;
    private String userid,touserid="";
    private SharedPreferences mSharedPreferences;
    private int cur = 1;
    private int shenhe;
    private int flag;

    @Override
    protected int getLayout() {
        return R.layout.fragment_myproxy;
    }

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        mBuyaBeanList = new ArrayList<>();
        mSharedPreferences = getActivity().getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        userid = mSharedPreferences.getString("userid", "");
        flag=mSharedPreferences.getInt("flag",5);
        touserid=getArguments().getString("touserid");
        shenhe=getArguments().getInt("shenhe");

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPublishListAdapter = new MyProxyListAdapter(getActivity(),mBuyaBeanList,touserid);
        recyclerView.setAdapter(mPublishListAdapter);

        refreshLayout.autoRefresh();
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                cur++;
                presenter.getProxyList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                cur = 1;
                mBuyaBeanList.clear();
                presenter.getProxyList();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(AnyEventMyProxyList messageEvent){
        cur = 1;
        presenter.getProxyList();
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
            if(!TextUtils.isEmpty(touserid)){
                jsonObject.put("userid", touserid);
            }else {
                jsonObject.put("userid", userid);
            }
            jsonObject.put("shenhe",shenhe);
            jsonObject.put("curpage", cur);
            jsonObject.put("pagesize", 10);

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
    public void showProxyInfoResult(ProxyInfoBean bean) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing()) refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading()) refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if(bean==null||bean.getJsonData()==null) return;
        if(cur==1)mBuyaBeanList.clear();
        mBuyaBeanList.addAll(bean.getJsonData());
        mPublishListAdapter.notifyDataSetChanged();
        if(null==bean.getMsg()||TextUtils.isEmpty(bean.getMsg()))return;
        refreshLayout.setLoadmoreFinished(cur >= Integer.parseInt(bean.getMsg())?true:false);
    }
}

