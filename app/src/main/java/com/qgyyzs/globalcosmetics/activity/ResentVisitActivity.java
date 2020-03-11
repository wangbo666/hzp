package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.adapter.VisitUserListAdapter;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.VisitBean;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.mvp.iface.ResentVisView;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ResentVisPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.VisLookAllPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 最近来访页面
 */
public class ResentVisitActivity extends BaseActivity implements ResentVisView{
    private ResentVisPresenter resentVisitPresenter=new ResentVisPresenter(this,this);
    private VisLookAllPresenter lookAllPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_right)
    TextView mTvbiaoji;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;

    private List<VisitBean.JsonData> mMedicineBeanList;
    private VisitUserListAdapter mVisitUserListAdapter;
    private SharedPreferences mSharedPreferences;
    private String username;
    private int cur=1;

    @Override
    protected int getLayout() {
        return R.layout.activity_resent_visit;
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
                resentVisitPresenter.getResentVisList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                cur=1;
                resentVisitPresenter.getResentVisList();
            }
        });
        mTvbiaoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lookAllPresenter.getLookAll();
            }
        });
    }

    @Override
    public void initData() {
        mMedicineBeanList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mVisitUserListAdapter = new VisitUserListAdapter(this,mMedicineBeanList);
        recyclerView.setAdapter(mVisitUserListAdapter);

        lookAllPresenter=new VisLookAllPresenter(lookAllView,this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.autoRefresh();
            }
        }).start();
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        username=mSharedPreferences.getString("username","");
        mTvbiaoji.setVisibility(View.GONE);
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
            jsonObject.put("curpage", cur);//当前页码
            jsonObject.put("pagesize", 10);
            jsonObject.put("pcusername", username);
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
    public void showResentVisResult(VisitBean bean) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing()) refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading())refreshLayout.finishLoadmore();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if (bean==null||bean.getJsonData()==null) return;
        if(cur==1)mMedicineBeanList.clear();
        mMedicineBeanList.addAll(bean.getJsonData());
        mVisitUserListAdapter.notifyDataSetChanged();
        mTvbiaoji.setVisibility(mMedicineBeanList.size()>0?View.VISIBLE:View.GONE);
        if(TextUtils.isEmpty(bean.getMsg()))return;
        refreshLayout.setLoadmoreFinished(cur >= Integer.parseInt(bean.getMsg())?true:false);
    }

    private StringView lookAllView=new StringView() {
        @Override
        public void showStringResult(String result) {
            for(int i=0;i<mMedicineBeanList.size();i++){
                mMedicineBeanList.get(i).setIsLook(true);
            }
            mVisitUserListAdapter.notifyDataSetChanged();
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
                jsonObject.put("pcusername",username);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LogUtils.e(jsonObject.toString());
            return jsonObject.toString();
        }

        @Override
        public void showToast(String msg) {

        }
    };
}
