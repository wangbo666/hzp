package com.qgyyzs.globalcosmetics.activity;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.ProductLibraryAdapter;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.MyProductBean;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.mvp.iface.ProductListView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.FriendProductPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FriendProductActivity extends BaseActivity implements ProductListView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;

    private int cur = 1;
    private String nickname,touserid;
    private ProductLibraryAdapter mMedicineListAdapter;
    private List<MyProductBean.JsonData> mMedicineBeanList;

    private FriendProductPresenter productPresenter=new FriendProductPresenter(this,this);

    @Override
    protected int getLayout() {
        return R.layout.activity_friend_product;
    }


    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        Intent intent = getIntent();
        nickname = intent.getStringExtra("nickname");
        touserid=intent.getStringExtra("userid");
        toolbar.setTitle(nickname + "的产品");
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
    public void initData() {
        mMedicineBeanList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMedicineListAdapter = new ProductLibraryAdapter(this,mMedicineBeanList);
        recyclerView.setAdapter(mMedicineListAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.autoRefresh();
            }
        }).start();
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
            jsonObject.put("userid", touserid);
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
    public void showProductListResult(MyProductBean bean) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing()) refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading()) refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if(bean==null||bean.getJsonData()==null) return;
        mMedicineBeanList.addAll(bean.getJsonData());
        mMedicineListAdapter.notifyDataSetChanged();
        if(TextUtils.isEmpty(bean.getMsg()))return;
        refreshLayout.setLoadmoreFinished(cur >= Integer.parseInt(bean.getMsg())?true:false);
    }
}
