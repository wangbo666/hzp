package com.qgyyzs.globalcosmetics.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.NewsAdapter;
import com.qgyyzs.globalcosmetics.base.BaseFragment;
import com.qgyyzs.globalcosmetics.bean.MedicalNewsBean;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.mvp.iface.NewsView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.NewsAllPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/3/28.
 */

public class NewsAllFragment extends BaseFragment implements NewsView{
    private NewsAllPresenter presenter;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;

    private List<MedicalNewsBean> mMedicalNewsBeanList;
    private NewsAdapter mMedicinenewsListAdapter;
    private int cur = 1;

    @Override
    protected int getLayout() {
        return R.layout.fragment_newsa;
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                cur++;
                presenter.getNewsList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                cur = 1;
                presenter.getNewsList();
            }
        });
    }

    @Override
    protected void initData() {
        mMedicalNewsBeanList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMedicinenewsListAdapter = new NewsAdapter(getActivity(),mMedicalNewsBeanList);
        recyclerView.setAdapter(mMedicinenewsListAdapter);

        presenter=new NewsAllPresenter(this, (RxFragmentActivity) getActivity());

        refreshLayout.autoRefresh();

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
    public void showNewsResult(List<MedicalNewsBean> newsLsit, String Msg) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing()) refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading())refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if(newsLsit==null)  return;
        if(cur==1)mMedicalNewsBeanList.clear();
        mMedicalNewsBeanList.addAll(newsLsit);
        mMedicinenewsListAdapter.notifyDataSetChanged();
        if(null==Msg||TextUtils.isEmpty(Msg))return;
        refreshLayout.setLoadmoreFinished(cur >= Integer.parseInt(Msg)?true:false);

    }
}
