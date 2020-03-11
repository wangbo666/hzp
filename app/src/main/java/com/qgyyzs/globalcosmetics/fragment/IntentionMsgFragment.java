package com.qgyyzs.globalcosmetics.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.IntentionMsgAdapter;
import com.qgyyzs.globalcosmetics.base.BaseFragment;
import com.qgyyzs.globalcosmetics.bean.IntentionBean;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.mvp.iface.IntentionMsgView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.IntentionMsgPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/19 0019.
 */

public class IntentionMsgFragment extends BaseFragment implements IntentionMsgView {
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;


    private int cur=1;
    private List<IntentionBean.JsonData> mIntentionList;
    private IntentionMsgAdapter intentionMsgAdapter;

    private IntentionMsgPresenter presenter=new IntentionMsgPresenter(this, (RxFragmentActivity) getActivity());

    private int type = 1;

    @Override
    protected int getLayout() {
        return R.layout.fragment_intentionmsg;
    }

    @Override
    protected void initView(View view) {
        type=getArguments().getInt("type");
    }

    @Override
    protected void initData() {
        mIntentionList=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        intentionMsgAdapter=new IntentionMsgAdapter(getActivity(),mIntentionList,type);
        recyclerView.setAdapter(intentionMsgAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.autoRefresh();
            }
        }).start();
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                cur++;
                presenter.getMsgList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                cur=1;
                presenter.getMsgList();
            }
        });
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
            jsonObject.put("curpage",cur);
            jsonObject.put("pagesize",10);
            if(type==2){
                jsonObject.put("nolook",1);
            }else if(type==3){
                jsonObject.put("isdel",1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.e(jsonObject.toString());
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showIntentionResult(IntentionBean bean) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing())  refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading()) refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if (bean==null||bean.getJsonData()==null) return;
        if(cur==1)mIntentionList.clear();
        mIntentionList.addAll(bean.getJsonData());
        intentionMsgAdapter.notifyDataSetChanged();
        if(TextUtils.isEmpty(bean.getMsg()))return;
        refreshLayout.setLoadmoreFinished(cur >= Integer.parseInt(bean.getMsg())?true:false);
    }
}
