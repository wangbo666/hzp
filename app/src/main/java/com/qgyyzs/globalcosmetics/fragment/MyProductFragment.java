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
import com.qgyyzs.globalcosmetics.adapter.MyMedicineListAdapter;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseFragment;
import com.qgyyzs.globalcosmetics.bean.MyProductBean;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventMyPdtList;
import com.qgyyzs.globalcosmetics.mvp.iface.ProductListView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.MyProductPresenter;
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

public class MyProductFragment extends BaseFragment implements ProductListView{
    private MyProductPresenter productPresenter;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;

    private int cur = 1;
    private SharedPreferences mSharedPreferences;
    private String userid;
    private int shenhe;
    private List<MyProductBean.JsonData> mMedicineBeanList;
    private MyMedicineListAdapter mMedicineListAdapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_myproduct;
    }

    @Override
    protected void initListener() {
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                cur++;
                productPresenter.getProductList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mMedicineBeanList.clear();
                cur = 1;
                productPresenter.getProductList();
            }
        });
    }

    @Override
    protected void initData() {
        mMedicineBeanList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMedicineListAdapter = new MyMedicineListAdapter(getActivity(),mMedicineBeanList);
        recyclerView.setAdapter(mMedicineListAdapter);

        productPresenter=new MyProductPresenter(this, (RxFragmentActivity) getActivity());

        refreshLayout.autoRefresh();
    }

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
        shenhe=getArguments().getInt("shenhe");
        mSharedPreferences = getActivity().getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        userid = mSharedPreferences.getString("userid", "");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(AnyEventMyPdtList messageEvent){
        cur = 1;
        productPresenter.getProductList();
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
            jsonObject.put("userid", userid);
            jsonObject.put("shenhe",shenhe);
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
    public void showProductListResult(MyProductBean bean) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing())  refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading())refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if(bean==null||bean.getJsonData()==null) return;
        if(cur==1)mMedicineBeanList.clear();
        mMedicineBeanList.addAll(bean.getJsonData());
        mMedicineListAdapter.notifyDataSetChanged();
        if(null==bean.getMsg()||TextUtils.isEmpty(bean.getMsg()))return;
        refreshLayout.setLoadmoreFinished(cur >= Integer.parseInt(bean.getMsg())?true:false);
    }
}
