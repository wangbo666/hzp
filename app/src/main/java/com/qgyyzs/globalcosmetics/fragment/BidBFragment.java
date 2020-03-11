package com.qgyyzs.globalcosmetics.fragment;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.ProvinceActivity;
import com.qgyyzs.globalcosmetics.adapter.BidListAdapter;
import com.qgyyzs.globalcosmetics.base.BaseFragment;
import com.qgyyzs.globalcosmetics.bean.BidBean;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventBid;
import com.qgyyzs.globalcosmetics.mvp.iface.BidView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.YibaoPresenter;
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

public class BidBFragment extends BaseFragment implements BidView{
    private YibaoPresenter presenter;
    @BindView(R.id.mTvProvince)
    TextView mTvProvince;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;

    private List<BidBean> mBidaBeanList;
    private BidListAdapter mBidaListAdapter;
    private int cur = 1;
    private String SearchString;
    private String province="全国";

    @Override
    protected int getLayout() {
        return R.layout.fragment_bid;
    }

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
        SearchString=getArguments().getString("searchStr");
    }

    @Override
    protected void initData() {
        mBidaBeanList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBidaListAdapter = new BidListAdapter( getActivity(), mBidaBeanList,2);
        recyclerView.setAdapter(mBidaListAdapter);

        presenter=new YibaoPresenter(this, (RxFragmentActivity) getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.autoRefresh();
            }
        }).start();
    }

    @Override
    protected void initListener() {
        mTvProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProvinceActivity.class);
                intent.putExtra("type", "shengfen");
                intent.putExtra("all", "quanguo");
                startActivityForResult(intent, 001);
            }
        });
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                cur++;
                presenter.getYibaoList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                cur = 1;
                presenter.getYibaoList();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(AnyEventBid anyEventBid){
        SearchString= anyEventBid.getSearchString();
        cur=1;
        mBidaListAdapter.notifyDataSetChanged();
        presenter.getYibaoList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 001 && resultCode == 002) {
            province=data.getStringExtra("province");
            mTvProvince.setText(province);
            cur=1;
            refreshLayout.setVisibility(View.VISIBLE);
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
            jsonObject.put("curpage", cur);
            jsonObject.put("pagesize", 10);
            jsonObject.put("keyword", SearchString);
            if (!province.equals("全国")) {
                jsonObject.put("province", province);
            }
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
    public void showBidResult(List<BidBean> bidList, int totalpage) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing()) refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading())refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if(bidList==null) return;
        if(cur==1)mBidaBeanList.clear();
        mBidaBeanList.addAll(bidList);
        mBidaListAdapter.notifyDataSetChanged();
        refreshLayout.setLoadmoreFinished(cur >= totalpage?true:false);
    }
}
