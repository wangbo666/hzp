package com.qgyyzs.globalcosmetics.activity;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.bean.ProductClassBean;
import com.qgyyzs.globalcosmetics.mvp.iface.SortProductView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.SortProductBigPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.SortProductSmallPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SortProductActivity extends BaseActivity implements SortProductView,View.OnClickListener{
    private SortProductBigPresenter bigPresenter = new SortProductBigPresenter(this, this);
    private SortProductSmallPresenter smallPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView1;
    @BindView(R.id.recyclerView2)
    RecyclerView recyclerView2;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<ProductClassBean> mProductClassBeenList;
    private Sort1ProductAdapter sort1Adapter;
    private List<ProductClassBean> mSort2List;
    private Sort2ProductAdapter sort2Adapter;
    private int classId;

    private String g_type1="", g_type2="";
    private int typeId1,typeId2;

    @Override
    protected int getLayout() {
        return R.layout.activity_sort_product;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);

        mTvRight.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        mProductClassBeenList=new ArrayList<>();
        mSort2List=new ArrayList<>();

        smallPresenter = new SortProductSmallPresenter(sortProductView2, this);

        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        sort1Adapter=new Sort1ProductAdapter(this,mProductClassBeenList);
        recyclerView1.setAdapter(sort1Adapter);
        sort2Adapter=new Sort2ProductAdapter(this,mSort2List);
        recyclerView2.setAdapter(sort2Adapter);
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
        mTvRight.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                bigPresenter.getBigSort();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                Intent intent=new Intent(this,ProductLibraryActivity.class);
                intent.putExtra("type1",g_type1);
                intent.putExtra("type2",g_type2);
                intent.putExtra("typeId1",typeId1);
                intent.putExtra("typeId2",typeId2);
                startActivity(intent);
                finish();
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
        return null;
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showSortResult(List<ProductClassBean> list) {
        if(null!=refreshLayout)refreshLayout.finishRefresh();
        if(null==list||list.size()==0)return;
        mProductClassBeenList.clear();
        mProductClassBeenList.addAll(list);
        classId=mProductClassBeenList.get(0).getId();
        g_type1=mProductClassBeenList.get(0).getType1();
        sort1Adapter.setSelectedPosition(0);
        sort1Adapter.notifyDataSetChanged();
        smallPresenter.getSmallSort();
    }

    private SortProductView sortProductView2=new SortProductView() {
        @Override
        public void showSortResult(List<ProductClassBean> list) {
            if(null==list||list.size()==0)return;
            mSort2List.clear();
            mSort2List.addAll(list);
            g_type2=mSort2List.get(0).getType1();
            sort2Adapter.setSelectedPosition(0);
            sort2Adapter.notifyDataSetChanged();
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
                jsonObject.put("ClassID", "" + classId);
                LogUtils.e( jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }

        @Override
        public void showToast(String msg) {

        }
    };

    private class Sort1ProductAdapter extends CommonAdapter<ProductClassBean> {

        private int selectedPosition = -1;// 选中的位置
        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public Sort1ProductAdapter(Activity context, List<ProductClassBean> biduserBeanList) {
            super(context,R.layout.adapter_sortproduct,biduserBeanList);
        }

        @Override
        public void convert(com.qgyyzs.globalcosmetics.base.ViewHolder viewHolder, final ProductClassBean biduserBean, final int position) {
            TextView mItemProduct1Tv=viewHolder.getView(R.id.mTvSortproductAdapter);
            mItemProduct1Tv.setText(TextUtils.isEmpty(biduserBean.getType1())?"":biduserBean.getType1());
            mItemProduct1Tv.setTextColor(selectedPosition==position?mContext.getResources().getColor(R.color.main_color):mContext.getResources().getColor(R.color.item_title));

            viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    classId=biduserBean.getId();
                    g_type1=biduserBean.getType1();
                    smallPresenter.getSmallSort();
                    sort1Adapter.setSelectedPosition(position);
                    sort1Adapter.notifyDataSetChanged();
                    typeId1=classId;
                }
            });
        }
    }

    private class Sort2ProductAdapter extends CommonAdapter<ProductClassBean>{

        private int selectedPosition = -1;// 选中的位置
        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public Sort2ProductAdapter(Activity context,List<ProductClassBean> biduserBeanList) {
            super(context,R.layout.adapter_sortproduct2,biduserBeanList);
        }

        @Override
        public void convert(com.qgyyzs.globalcosmetics.base.ViewHolder viewHolder, final ProductClassBean item, final int position) {
            TextView mItemProduct1Tv=viewHolder.getView(R.id.mTvSortproductAdapter);
            mItemProduct1Tv.setText(TextUtils.isEmpty(item.getType1())?"":item.getType1());
            mItemProduct1Tv.setTextColor(selectedPosition==position?mContext.getResources().getColor(R.color.title_color):mContext.getResources().getColor(R.color.color_grey_666666));

            viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    g_type2 = item.getType1();
                    sort2Adapter.setSelectedPosition(position);
                    sort2Adapter.notifyDataSetChanged();
                    typeId2=item.getId();
                    Intent intent=new Intent(SortProductActivity.this,ProductLibraryActivity.class);
                    intent.putExtra("type1",g_type1);
                    intent.putExtra("type2",g_type2);
                    intent.putExtra("typeId1",typeId1);
                    intent.putExtra("typeId2",typeId2);
                    startActivity(intent);
                }
            });
        }
    }
}
