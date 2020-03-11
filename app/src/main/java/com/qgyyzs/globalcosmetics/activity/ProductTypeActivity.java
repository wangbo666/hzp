package com.qgyyzs.globalcosmetics.activity;

import android.app.Activity;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;
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

public class ProductTypeActivity extends BaseActivity implements SortProductView,View.OnClickListener {
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

    private List<ProductClassBean> bigBeanList;
    private Sort1ProductAdapter BigAdapter;
    private int classId;

    private SelectAdapter SmallAdapter;
    private String type1, type2 = "";
    private String typeId2;
    private List<ProductClassBean> smallBeanList;

    @Override
    protected int getLayout() {
        return R.layout.activity_sort_product;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);

        String type = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(type)) {
            String str[] = type.split(",");
            type1 = str[0];
            type2 = type.substring(type1.length()+1, type.length());
        }
    }

    @Override
    public void initData() {
        mTvRight.setEnabled(false);
        bigBeanList = new ArrayList<>();

        smallBeanList = new ArrayList<>();

        smallPresenter = new SortProductSmallPresenter(sortProductView2, this);

        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        BigAdapter = new Sort1ProductAdapter(this, bigBeanList);
        recyclerView1.setAdapter(BigAdapter);
        SmallAdapter=new SelectAdapter(this,smallBeanList);
        recyclerView2.setAdapter(SmallAdapter);

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
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                if(type2.equals("")){
                    ToastUtil.showToast(this,"请选择一个产品小类",true);
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("type", type1+","+type2);
                intent.putExtra("id",classId+","+typeId2);
                setResult(1, intent);
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

    private SortProductView sortProductView2=new SortProductView() {
        @Override
        public void showSortResult(List<ProductClassBean> list) {
            mTvRight.setEnabled(true);
            if (null==list) return;
            smallBeanList.clear();
            smallBeanList.addAll(list);

            SmallAdapter.setSelectedPosition(-1);
            for (int j = 0; j < smallBeanList.size(); j++) {
                if (smallBeanList.get(j).getType1().equals(type2)) {
                    SmallAdapter.setSelectedPosition(j);
                }
            }
            SmallAdapter.notifyDataSetChanged();
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
                LogUtils.e(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }

        @Override
        public void showToast(String msg) {

        }
    };

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showSortResult(List<ProductClassBean> list) {
        if(null!=refreshLayout)refreshLayout.finishRefresh();
        if (null==list||list.size()==0) return;
        bigBeanList.clear();
        bigBeanList.addAll(list);
        if (TextUtils.isEmpty(type1)) {
            type1 = list.get(0).getType1();
            BigAdapter.setSelectedPosition(0);
            classId = bigBeanList.get(0).getId();
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (type1.equals(list.get(i).getType1())) {
                    classId = bigBeanList.get(i).getId();
                    BigAdapter.setSelectedPosition(i);
                }
            }
        }
        BigAdapter.notifyDataSetChanged();
        smallPresenter.getSmallSort();
    }

    private class Sort1ProductAdapter extends CommonAdapter<ProductClassBean> {

        private int selectedPosition = -1;// 选中的位置

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public Sort1ProductAdapter(Activity context, List<ProductClassBean> biduserBeanList) {
            super(context, R.layout.adapter_sortproduct, biduserBeanList);
        }

        @Override
        public void convert(com.qgyyzs.globalcosmetics.base.ViewHolder viewHolder, final ProductClassBean biduserBean, final int position) {
            TextView mItemProduct1Tv = viewHolder.getView(R.id.mTvSortproductAdapter);
            mItemProduct1Tv.setText(TextUtils.isEmpty(biduserBean.getType1()) ? "" : biduserBean.getType1());
            mItemProduct1Tv.setTextColor(selectedPosition == position ? mContext.getResources().getColor(R.color.main_color) : mContext.getResources().getColor(R.color.item_title));

            viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    classId = biduserBean.getId();
                    type1 = biduserBean.getType1();
                    type2="";
                    mTvRight.setEnabled(false);
                    smallPresenter.getSmallSort();
                    BigAdapter.setSelectedPosition(position);
                    BigAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private class SelectAdapter extends CommonAdapter<ProductClassBean> {

        private int selectedPosition = -1;// 选中的位置

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public SelectAdapter(Activity context, List<ProductClassBean> biduserBeanList) {
            super(context, R.layout.adapter_sortproduct2, biduserBeanList);
        }

        @Override
        public void convert(ViewHolder viewHolder, final ProductClassBean item, final int position) {
            TextView mItemProduct1Tv = viewHolder.getView(R.id.mTvSortproductAdapter);
            mItemProduct1Tv.setText(TextUtils.isEmpty(item.getType1()) ? "" : item.getType1());
            mItemProduct1Tv.setTextColor(selectedPosition == position ? mContext.getResources().getColor(R.color.title_color) : mContext.getResources().getColor(R.color.color_grey_666666));

            viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    type2 = item.getType1();
                    typeId2=item.getId()+"";
                    SmallAdapter.setSelectedPosition(position);
                    SmallAdapter.notifyDataSetChanged();
                }
            });
        }
    }

}
