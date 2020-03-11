package com.qgyyzs.globalcosmetics.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.adapter.NewsExpAdapter;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.bean.NewsExpBean;
import com.qgyyzs.globalcosmetics.bean.ProductClassBean;
import com.qgyyzs.globalcosmetics.mvp.iface.NewsExpView;
import com.qgyyzs.globalcosmetics.mvp.iface.SortProductView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ZhanhuiTypePresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseFragment;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.NewsExpPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/3/28.
 */

public class NewsExpFragment extends BaseFragment implements NewsExpView,View.OnClickListener,SortProductView{
    private ZhanhuiTypePresenter typePresenter=new ZhanhuiTypePresenter(this, (RxFragmentActivity) getActivity());
    private NewsExpPresenter presenter;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.LinearYear)
    LinearLayout mLinearYear;
    @BindView(R.id.year_tv)
    TextView mTvYear;
    @BindView(R.id.LinearType)
    LinearLayout mLinearType;
    @BindView(R.id.type_tv)
    TextView mTvType;
    @BindView(R.id.LinearProvince)
    LinearLayout mLinearProvince;
    @BindView(R.id.province_tv)
    TextView mTvProvince;

    private List<NewsExpBean.JsonData> mMedicalNewsBeanList;
    private NewsExpAdapter mMedicinenewsListAdapter;
    private int cur = 1;

    private SharedPreferences mSharedPreferences;
    private String provinceString;
    private String[] provinceStrs;
    private String province="全部";
    private int province_position=0;

    private String year="全部";
    private int year_position=0;
    private String[] yearStrs;

    private List<ProductClassBean> typeList=new ArrayList<>();
    private int typeId;
    private int type_position = 0;
    private String[] typeStrs;

    @Override
    protected int getLayout() {
        return R.layout.fragment_news_exp;
    }

    @Override
    protected void initView(View view) {
        mSharedPreferences=mContext.getSharedPreferences(MyApplication.CONSTACTDATA, Context.MODE_PRIVATE);
        provinceString=mSharedPreferences.getString("province","");
        provinceString="全部,"+provinceString+"海外";
        if(!TextUtils.isEmpty(provinceString)) provinceStrs=provinceString.split(",");

        Calendar a=Calendar.getInstance();
        int years=a.get(Calendar.YEAR);
        yearStrs=new String[]{"全部",years-3+"",years-2+"",years-1+"",years+"",years+1+""};
    }

    @Override
    protected void initListener() {
        mLinearProvince.setOnClickListener(this);
        mLinearType.setOnClickListener(this);
        mLinearYear.setOnClickListener(this);
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
        mMedicinenewsListAdapter = new NewsExpAdapter(getActivity(),mMedicalNewsBeanList);
        recyclerView.setAdapter(mMedicinenewsListAdapter);

        presenter=new NewsExpPresenter(this, (RxFragmentActivity) getActivity());

        refreshLayout.autoRefresh();
        typePresenter.getZhanhuiType();
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
            if(!province.equals("全部"))
                jsonObject.put("province",province);
            if(!year.equals("全部"))
                jsonObject.put("year",year);
            if(type_position!=0)
                jsonObject.put("type",typeId);
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
    public void showNewsResult(NewsExpBean bean) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing()) refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading())refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if(bean==null||bean.getJsonData()==null) return;
        if(cur==1)mMedicalNewsBeanList.clear();
        mMedicalNewsBeanList.addAll(bean.getJsonData());
        mMedicinenewsListAdapter.notifyDataSetChanged();
        if(null==bean.getMsg()||TextUtils.isEmpty(bean.getMsg()))return;
        refreshLayout.setLoadmoreFinished(cur >= Integer.parseInt(bean.getMsg())?true:false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.LinearYear:
                if(yearStrs==null)return;
                new AlertDialog.Builder(mContext)
                        .setSingleChoiceItems(yearStrs, year_position,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        year = yearStrs[which];
                                        mTvYear.setText(year);
                                        year_position = which;
                                        dialog.dismiss();
                                        cur=1;
                                        refreshLayout.autoRefresh();
                                    }
                                }
                        )
                        .show();
                break;
            case R.id.LinearType:
                if(typeStrs==null)return;
                new AlertDialog.Builder(mContext)
                        .setSingleChoiceItems(typeStrs, type_position,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        typeId = typeList.get(which).getId();
                                        mTvType.setText(typeStrs[which]);
                                        type_position = which;
                                        dialog.dismiss();
                                        cur=1;
                                        refreshLayout.autoRefresh();
                                    }
                                }
                        )
                        .show();
                break;
            case R.id.LinearProvince:
                if(provinceStrs==null)return;
                new AlertDialog.Builder(mContext)
                        .setSingleChoiceItems(provinceStrs, province_position,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        province = provinceStrs[which];
                                        mTvProvince.setText(province);
                                        province_position = which;
                                        dialog.dismiss();
                                        cur=1;
                                        refreshLayout.autoRefresh();
                                    }
                                }
                        )
                        .show();
                break;
        }
    }

    @Override
    public void showSortResult(List<ProductClassBean> list) {
        if(list==null)return;
        typeList.clear();
        typeList.add(new ProductClassBean(-1,"全部"));
        typeList.addAll(list);
        typeStrs=new String[typeList.size()];
        for(int i=0;i<typeList.size();i++){
            typeStrs[i]=typeList.get(i).getType1();
        }
    }
}
