package com.qgyyzs.globalcosmetics.fragment;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.ProvinceActivity;
import com.qgyyzs.globalcosmetics.adapter.NewsAdapter;
import com.qgyyzs.globalcosmetics.base.BaseFragment;
import com.qgyyzs.globalcosmetics.bean.MedicalNewsBean;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventBid;
import com.qgyyzs.globalcosmetics.mvp.iface.NewsView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.NewsBidPresenter;
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

public class NewsBidFragment extends BaseFragment implements NewsView{
    private NewsBidPresenter presenter;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.mLinearSx)
    LinearLayout mLinearSx;
    @BindView(R.id.fliter4_tv)
    TextView mTvProvince;

    private List<MedicalNewsBean> mMedicalNewsBeanList;
    private NewsAdapter mMedicinenewsListAdapter;
    private int cur = 1;
    private String SearchString;

    private String isShowArea;
    private String province="全国";

    @Override
    protected int getLayout() {
        return R.layout.fragment_newsa;
    }

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
        isShowArea=getArguments().getString("isSx");

        if(TextUtils.isEmpty(isShowArea)){
            return;
        }
        mLinearSx.setVisibility(View.VISIBLE);
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

        presenter=new NewsBidPresenter(this, (RxFragmentActivity) getActivity());

        refreshLayout.autoRefresh();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(AnyEventBid anyEventBid){
        SearchString= anyEventBid.getSearchString();
        cur=1;
        mMedicinenewsListAdapter.notifyDataSetChanged();
        presenter.getNewsList();
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
            mMedicinenewsListAdapter.notifyDataSetChanged();
            presenter.getNewsList();
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
            if(!TextUtils.isEmpty(isShowArea)){
                jsonObject.put("keyword", SearchString);
                if (!province.equals("全国")) {
                    jsonObject.put("province", province);
                }
            }
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
        if(null!=refreshLayout&&refreshLayout.isRefreshing())refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading()) refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if(newsLsit==null)  return;
        if(cur==1)mMedicalNewsBeanList.clear();
        mMedicalNewsBeanList.addAll(newsLsit);
        mMedicinenewsListAdapter.notifyDataSetChanged();
        if(null==Msg||TextUtils.isEmpty(Msg))return;
        refreshLayout.setLoadmoreFinished(cur >= Integer.parseInt(Msg)?true:false);
    }
}
