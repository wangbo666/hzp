package com.qgyyzs.globalcosmetics.fragment;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.ProductLibraryAdapter;
import com.qgyyzs.globalcosmetics.base.BaseFragment;
import com.qgyyzs.globalcosmetics.bean.MyProductBean;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.mvp.iface.ProductListView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.CompanyProductPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/30 0030.
 */

public class CompanyProductFragment extends BaseFragment implements ProductListView {
    private CompanyProductPresenter productLiraryPresenter=new CompanyProductPresenter(this, (RxFragmentActivity) getActivity());

    @BindView(R.id.product_edit)
    EditText mProductEdit;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.empty_view)
    View  emptyView;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.search_btn)
    TextView mTvSearch;

    private String companyUsername;

    private int cur = 1;
    private List<MyProductBean.JsonData> mMedicineBeanList;
    private ProductLibraryAdapter mMedicineListAdapter;
    @Override
    protected int getLayout() {
        return R.layout.fragment_companyproduct;
    }

    @Override
    protected void initView(View view) {
        companyUsername=getArguments().getString("username");
        mProductEdit.setHint("请输入产品名称");
//        refreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
    }

    @Override
    protected void initData() {
        mMedicineBeanList = new ArrayList<>();//产品

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mMedicineListAdapter = new ProductLibraryAdapter(getActivity(),mMedicineBeanList);
        recyclerView.setAdapter(mMedicineListAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {

                refreshLayout.autoRefresh();
            }
        }).start();
    }

    @Override
    protected void initListener() {
        mProductEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    cur=1;
                    refreshLayout.autoRefresh();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                cur++;
                productLiraryPresenter.getProductList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                cur=1;
                productLiraryPresenter.getProductList();
            }
        });
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur=1;
                refreshLayout.autoRefresh();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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
            jsonObject.put("curpage", cur);
            jsonObject.put("pagesize", 10);
            jsonObject.put("pcusername",companyUsername);
            jsonObject.put("subject", mProductEdit.getText().toString().trim());
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
    public void showProductListResult(MyProductBean bean) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing())refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading())refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if(bean==null||bean.getJsonData()==null) return;
        if(cur==1)mMedicineBeanList.clear();
        mMedicineBeanList.addAll(bean.getJsonData());
        mMedicineListAdapter.notifyDataSetChanged();
        if(TextUtils.isEmpty(bean.getMsg()))return;
        if(null!=refreshLayout)refreshLayout.setLoadmoreFinished(cur >= Integer.parseInt(bean.getMsg())?true:false);
    }
}
