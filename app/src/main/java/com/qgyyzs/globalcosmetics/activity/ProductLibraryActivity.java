package com.qgyyzs.globalcosmetics.activity;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.ProductLibraryAdapter;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.MyProductBean;
import com.qgyyzs.globalcosmetics.customview.AreaPopWindow;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.customview.SinglePopWindow;
import com.qgyyzs.globalcosmetics.customview.SortProductPopWindow;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventProduct;
import com.qgyyzs.globalcosmetics.mvp.iface.ProductListView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ProductLiraryPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.qgyyzs.globalcosmetics.R.id.type5_tv;

public class ProductLibraryActivity extends BaseActivity implements ProductListView,AreaPopWindow.SettingAreaListener,SortProductPopWindow.SettingSortListener,SinglePopWindow.SettingSingleListener{
    private ProductLiraryPresenter productLiraryPresenter=new ProductLiraryPresenter(this,this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.type1_tv)
    TextView mType1Tv;
    @BindView(type5_tv)
    TextView mType5Tv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.ll_sx)
    LinearLayout ll_sx;
    @BindView(R.id.province_tv)
    TextView mProvinceTv;
    @BindView(R.id.LinearType)
    LinearLayout LinearType;
    @BindView(R.id.LinearProvince)
    LinearLayout LinearProvince;
    @BindView(R.id.LinearFilter)
    LinearLayout LinearFilter;
    @BindView(R.id.LinearSearch)
    LinearLayout mLinearSearch;

    private String searchText="";
    private int cur = 1;
    private List<MyProductBean.JsonData> mMedicineBeanList;
    private ProductLibraryAdapter mMedicineListAdapter;

    private int positon_1 = 1;
    private String pro_type1 = "", pro_type2 = "";
    private int typeId1,typeId2;

    private AreaPopWindow areaWindow;
    private SortProductPopWindow sortProductWindow;
    private SinglePopWindow singlePopWindow;
    private List<String> type1List;
    private String province = "", city = "";

    @Override
    protected int getLayout() {
        return R.layout.activity_productlibrary;
    }

    @Override
    protected void initListener() {
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
//                productPresenter.getProductList();
                productLiraryPresenter.getProductList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                cur=1;
                productLiraryPresenter.getProductList();
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent=getIntent();
        pro_type1 = intent.getStringExtra("type1");
        pro_type2 = intent.getStringExtra("type2");
        typeId1 = intent.getIntExtra("typeId1",0);
        typeId2 = intent.getIntExtra("typeId2",0);
        if ((!TextUtils.isEmpty(pro_type1)) && (!TextUtils.isEmpty(pro_type2))) {
            if(pro_type2.equals("全部")){
                mType5Tv.setText(pro_type1);
            }else {
                mType5Tv.setText(pro_type2);
            }
        }else{
            mType5Tv.setText("筛选");
        }

        searchText = TextUtils.isEmpty(intent.getStringExtra("searchStr"))?"":intent.getStringExtra("searchStr");

        type1List=new ArrayList<>();
        type1List.add("为我推荐");
        type1List.add("最新发布");

        mMedicineBeanList = new ArrayList<>();//产品

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
    protected void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        EventBus.getDefault().register(this);

//        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
    }

    @OnClick({R.id.LinearType, R.id.LinearFilter,R.id.LinearProvince,R.id.LinearSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.LinearSearch:
                Intent intent=new Intent(this,SearchActivity.class);
                intent.putExtra("searchType","product_history");
                intent.putExtra("searchContent",searchText);
                startActivityForResult(intent,001);
                break;
            case R.id.LinearProvince:
                if(areaWindow==null) {
                    areaWindow = new AreaPopWindow(this);
                    areaWindow.setOnSettingAreaListener(this);
                    areaWindow.showPopupWindow(LinearProvince);
                }else{
                    areaWindow.setOnSettingAreaListener(this);
                    areaWindow.showPopupWindow(LinearProvince);
                }
                break;
            case R.id.LinearType:
                if(singlePopWindow==null) {
                    singlePopWindow = new SinglePopWindow(this,type1List);
                    singlePopWindow.setOnSettingSingleListener(this);
                    singlePopWindow.showAsDropDown(LinearFilter);
                }else{
                    singlePopWindow.setOnSettingSingleListener(this);
                    singlePopWindow.showAsDropDown(LinearFilter);
                }
                break;
            case R.id.LinearFilter:
                if(sortProductWindow==null) {
                    sortProductWindow = new SortProductPopWindow(this);
                    sortProductWindow.setOnSettingSortListener(this);
                    sortProductWindow.showPopupWindow(LinearFilter);
                }else{
                    sortProductWindow.setOnSettingSortListener(this);
                    sortProductWindow.showPopupWindow(LinearFilter);
                }
                break;
        }
    }

    private void eventData(){
        cur=1;
        mMedicineListAdapter.notifyDataSetChanged();
        productLiraryPresenter.getProductList();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AnyEventProduct event){
        eventData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSettingArea(String Province,String City) {
        province = Province;
        city = City;
        if (city.equals("全省")||city.equals("")) {
            mProvinceTv.setText(province);
        }else{
            mProvinceTv.setText(city);
        }
        cur=1;
        refreshLayout.autoRefresh();
    }

    @Override
    public void onSettingSort(String proType1,String proType2,int id1,int id2) {
        pro_type1=proType1;
        pro_type2=proType2;
        typeId1=id1;
        typeId2=id2;
        LogUtils.e("type1="+proType1+"type2"+proType2);
        if ("全部".equals(pro_type2)) {
            mType5Tv.setText(pro_type1);
        } else {
            mType5Tv.setText(pro_type2);
        }
        cur=1;
        refreshLayout.autoRefresh();
    }

    @Override
    public void onSettingSingle(int position) {
        positon_1=position;
        mType1Tv.setText(type1List.get(position));
        cur=1;
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
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("curpage", cur);
            jsonObject.put("pagesize", 10);
            jsonObject.put("subject", searchText);
            jsonObject.put("leixin", positon_1 + 2);
            if ("全部".equals(pro_type2)) {
                if(typeId1!=-1) {
                    jsonObject.put("classid", typeId1);
                }
            } else {
                jsonObject.put("nclassid", typeId2);
            }
            if (!province.equals("全国")) {
                jsonObject.put("province", province);
            } else {
                jsonObject.put("province", "");
            }
            if (!city.equals("全省")) {
                jsonObject.put("city", city);
            } else {
                jsonObject.put("city", "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.e(jsonObject.toString());
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToast(this,msg,true);
    }

    @Override
    public void showProductListResult(MyProductBean bean) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing())refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading())refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if(bean==null||bean.getJsonData()==null) return;
        if(cur==1)mMedicineBeanList.clear();
        mMedicineBeanList.addAll(bean.getJsonData());
        mMedicineListAdapter.notifyDataSetChanged();
        if(TextUtils.isEmpty(bean.getMsg()))return;
        refreshLayout.setLoadmoreFinished(cur >= Integer.parseInt(bean.getMsg())?true:false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && data!=null){
            searchText=data.getStringExtra("searchStr");
            cur=1;
            refreshLayout.autoRefresh();
        }
    }
}
