package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.Intent;
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
import com.qgyyzs.globalcosmetics.adapter.FriendUserListAdapter;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.MyFriend;
import com.qgyyzs.globalcosmetics.bean.UserBean;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.mvp.iface.FriendView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.MyFriendPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class UserKehuActivity extends BaseActivity implements View.OnClickListener,FriendView {
    private MyFriendPresenter friendListPresenter=new MyFriendPresenter(this,this);
    @BindView(R.id.tv_right)
    TextView mRightTextView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;


    private int cur = 1;
    private List<UserBean> mMedicineBeanList;
    private FriendUserListAdapter mFriendUserListAdapter;

    private String userid;
    private SharedPreferences mSharedPreferences;
    private int get_state;

    @Override
    protected int getLayout() {
        return R.layout.activity_smartrefresh_recycleview;
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mRightTextView.setOnClickListener(this);
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                cur++;
                friendListPresenter.getFriendList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                cur = 1;
                friendListPresenter.getFriendList();
            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        get_state = intent.getIntExtra("state", 1);
        if (get_state == 2) {
            toolbar.setTitle("我的客户");
        }
        if (get_state == 4) {
            toolbar.setTitle("黑名单");
        }
        mMedicineBeanList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFriendUserListAdapter = new FriendUserListAdapter(this,mMedicineBeanList,get_state);
        recyclerView.setAdapter(mFriendUserListAdapter);

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
        userid = mSharedPreferences.getString("userid", "未设置");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                Intent intent = new Intent(this, PublishProductActivity.class);
                intent.putExtra("set_type", "add");
                startActivity(intent);
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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("state", get_state);
            jsonObject.put("userid", userid);
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
    public void showFriendResult(MyFriend bean) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing()) refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading()) refreshLayout.finishLoadmore();
        recyclerView.setEmptyView(emptyView);
        if(bean==null||bean.getJsonData()==null)  return;
        if(cur==1)mMedicineBeanList.clear();
        for (int i = 0; i < bean.getJsonData().size(); i++) {
            mMedicineBeanList.add(new UserBean(bean.getJsonData().get(i).getF_UserId()+"", TextUtils.isEmpty(bean.getJsonData().get(i).getAlias())?bean.getJsonData().get(i).getUserInfo().getRealName():bean.getJsonData().get(i).getAlias(), bean.getJsonData().get(i).getUserInfo().getHeadImg()));
        }
        mFriendUserListAdapter.notifyDataSetChanged();

    }
}
