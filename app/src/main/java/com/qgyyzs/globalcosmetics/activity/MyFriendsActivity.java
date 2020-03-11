package com.qgyyzs.globalcosmetics.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.utils.IndexBar.widget.IndexBar;
import com.qgyyzs.globalcosmetics.utils.suspension.SuspensionDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.base.ViewHolder;
import com.qgyyzs.globalcosmetics.bean.MyFriend;
import com.qgyyzs.globalcosmetics.bean.MyFriendBean;
import com.qgyyzs.globalcosmetics.customview.DividerItemDecoration;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventFriendList;
import com.qgyyzs.globalcosmetics.mvp.iface.FriendView;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.DelFriendPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.MyFriendPresenter;
import com.qgyyzs.globalcosmetics.nim.activity.AddContactActivity;
import com.qgyyzs.globalcosmetics.utils.GlideCircleTransform;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/22 0022.
 */

public class MyFriendsActivity extends BaseActivity implements FriendView{
    private MyFriendPresenter presenter=new MyFriendPresenter(this,this);
    private DelFriendPresenter delFriendPresenter;
    private static final String INDEX_STRING_TOP = "↑";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.indexBar)
    IndexBar mIndexBar;
    @BindView(R.id.tvSideBarHint)
    TextView mTvSideBarHint;

    private LinearLayoutManager mManager;
    private SuspensionDecoration mDecoration;
    private FriendAdapter mAdapter;
    private List<MyFriendBean> friendBeanList=new ArrayList<>();
    private String userid,f_userid;
    private SharedPreferences mSharedPreferences;

    @Override
    protected int getLayout() {
        return R.layout.activity_myfriendlist;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        EventBus.getDefault().register(this);
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        userid = mSharedPreferences.getString("userid", "");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                friendBeanList.clear();
                friendBeanList.add((MyFriendBean) new MyFriendBean("","","","添加好友").setTop(true).setBaseIndexTag(INDEX_STRING_TOP));
                friendBeanList.add((MyFriendBean) new MyFriendBean("","","","我的客户").setTop(true).setBaseIndexTag(INDEX_STRING_TOP));
                friendBeanList.add((MyFriendBean) new MyFriendBean("","","","黑名单").setTop(true).setBaseIndexTag(INDEX_STRING_TOP));
                mAdapter.notifyDataSetChanged();
                presenter.getFriendList();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(AnyEventFriendList friendList){
     new Thread(new Runnable() {
         @Override
         public void run() {
             refreshLayout.autoRefresh();
         }
     }) .start();
    }

    @Override
    public void initData() {
        delFriendPresenter=new DelFriendPresenter(delView,this);
        recyclerView.setLayoutManager(mManager = new LinearLayoutManager(this));

        mAdapter = new FriendAdapter(this, friendBeanList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(mDecoration = new SuspensionDecoration(this, friendBeanList));
        //如果add两个，那么按照先后顺序，依次渲染。
        //mRv.addItemDecoration(new TitleItemDecoration2(this,mDatas));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

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
            jsonObject.put("userid", userid);
            jsonObject.put("state", "1");
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
        if(refreshLayout!=null)refreshLayout.finishRefresh();
        mIndexBar.setVisibility(View.VISIBLE);
        if(bean==null||bean.getJsonData()==null)return;

        for (int i=0;i<bean.getJsonData().size();i++) {
            friendBeanList.add(new MyFriendBean(bean.getJsonData().get(i).getF_UserId()+"",bean.getJsonData().get(i).getUserInfo().getNimID(),bean.getJsonData().get(i).getUserInfo().getHeadImg(),
                    TextUtils.isEmpty(bean.getJsonData().get(i).getAlias())?bean.getJsonData().get(i).getUserInfo().getRealName():bean.getJsonData().get(i).getAlias()));
        }
        mIndexBar.setmSourceDatas(friendBeanList)
                .invalidate();
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setmSourceDatas(friendBeanList)//设置数据
                .invalidate();
        mDecoration.setmDatas(friendBeanList);
        mAdapter.notifyDataSetChanged();
    }

    private class FriendAdapter extends CommonAdapter<MyFriendBean> {

        public FriendAdapter(Activity activity, List<MyFriendBean> list) {
            super(activity, R.layout.adapter_myfriend, list);
        }

        @Override
        public void convert(final ViewHolder viewHolder, final MyFriendBean item, final int position) {
            TextView tvName = viewHolder.getView(R.id.tvName);
            ImageView imageView = viewHolder.getView(R.id.ivAvatar);
            LinearLayout content = viewHolder.getView(R.id.content);
            Button benDel=viewHolder.getView(R.id.btnDel);
            tvName.setText(TextUtils.isEmpty(item.getNickName()) ? "未命名" : item.getNickName());
            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(position==0) {
                        startActivity(new Intent(MyFriendsActivity.this,AddContactActivity.class));
                    }else if(position==1){
                        Intent intent = new Intent(mContext, UserKehuActivity.class);
                        intent.putExtra("title", "我的客户");
                        intent.putExtra("state", 2);
                        startActivity(intent);
                    }else if(position==2){
                        Intent intent = new Intent(mContext, UserKehuActivity.class);
                        intent.putExtra("title", "黑名单");
                        intent.putExtra("state", 4);
                        startActivity(intent);
                    }else{
                        Intent intent=new Intent(MyFriendsActivity.this,ChatUserdetailActivity.class);
                        intent.putExtra("type",1);
                        intent.putExtra("fuserid",item.getUserid()+"");
                        startActivity(intent);
                    }
                }
            });
            benDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SwipeMenuLayout) viewHolder.itemView).quickClose();
                    friendBeanList.remove(position);
                    mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                            .setNeedRealIndex(true)//设置需要真实的索引
                            .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                            .setmSourceDatas(mDatas)//设置数据
                            .invalidate();
                    notifyDataSetChanged();
                    f_userid=item.getUserid();
                    delFriendPresenter.delFriend();
                }
            });
            if(position>2) {
                benDel.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(item.getHeadUrl())) {
                    imageView.setImageResource(R.mipmap.icon_user_defult);
                } else {
                    Glide.with(mContext).load(item.getHeadUrl()).error(R.mipmap.icon_user_defult).transform(new GlideCircleTransform(mContext)).placeholder(R.mipmap.icon_user_defult).into(imageView);
                }
            }else{
                benDel.setVisibility(View.GONE);
                if(position==0) {
                    imageView.setImageResource(R.mipmap.ic_addfriend);
                }else if(position==1){
                    imageView.setImageResource(R.mipmap.ic_mykehu);
                }else if(position==2){
                    imageView.setImageResource(R.mipmap.ic_myblack);
                }
            }
        }
    }

    private StringView delView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if (!TextUtils.isEmpty(result)) {
                ToastUtil.showToast(MyFriendsActivity.this, result, true);
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
                jsonObject.put("userid", userid);
                jsonObject.put("f_userid", f_userid);
                jsonObject.toString();
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
}
