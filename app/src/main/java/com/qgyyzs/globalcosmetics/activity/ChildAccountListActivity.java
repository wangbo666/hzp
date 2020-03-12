package com.qgyyzs.globalcosmetics.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.trello.rxlifecycle2.components.RxActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.bean.UserListBean;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.eventbus.AnyChild;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.iface.UserListView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ChildAccountDelPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ChildAccountListPresenter;
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

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class ChildAccountListActivity extends BaseActivity implements UserListView{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;

    private String fusername="";
    private List<UserListBean.JsonData> mChildList;
    private ChildAccountAdapter accountAdapter;

    private ChildAccountListPresenter childAccountPresenter=new ChildAccountListPresenter(this,this);

    @Override
    protected int getLayout() {
        return R.layout.activity_managerchild;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);

        fusername=getIntent().getStringExtra("username");

        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        mChildList=new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        accountAdapter=new ChildAccountAdapter(this,mChildList);
        recyclerView.setAdapter(accountAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {

                refreshLayout.autoRefresh();
            }
        }).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(AnyChild anyKefuArea){
        refreshLayout.autoRefresh();
    }

    @Override
    public void onDestroy() {
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
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChildAccountListActivity.this,ChildAccountAddActivity.class));
            }
        });
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshLayout) {
                childAccountPresenter.getChildList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mChildList.clear();
                childAccountPresenter.getChildList();
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
            jsonObject.put("pcusername", fusername);
        }catch (JSONException e){

        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showUserListResult(UserListBean bean) {
        if(null!=refreshLayout&&refreshLayout.isRefreshing()) refreshLayout.finishRefresh();
        if(null!=refreshLayout&&refreshLayout.isLoading())refreshLayout.finishLoadmore();
        recyclerView.setEmptyView(emptyView);
        if(bean==null||bean.getJsonData()==null)return;
        mChildList.addAll(bean.getJsonData());
        accountAdapter.notifyDataSetChanged();
        List<String> list = new ArrayList<>();
        for(int i=0;i<bean.getJsonData().size();i++){
            if(!TextUtils.isEmpty(bean.getJsonData().get(i).getNimID())) {
                list.add(bean.getJsonData().get(i).getNimID());
            }
        }
        if(list.size()>0) {
            NIMClient.getService(UserService.class).fetchUserInfo(list);
        }

    }

    private class ChildAccountAdapter extends CommonAdapter<UserListBean.JsonData> implements StringView {
        private ChildAccountDelPresenter delPresenter;
        private String accountName;
        private String myusername;
        private SharedPreferences mSharedPreferences;

        public ChildAccountAdapter(Activity context, List<UserListBean.JsonData> biduserBeanList) {
            super(context,R.layout.adapter_childaccount,biduserBeanList);
            delPresenter=new ChildAccountDelPresenter(this, (RxActivity) context);
            mSharedPreferences=context.getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
            myusername=mSharedPreferences.getString("accountname","");
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
                jsonObject.put("accountname",accountName);
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
        public void showStringResult(String result) {
            if(result==null)return;
            ToastUtil.showToast(mContext,result,true);
        }

        @Override
        public void convert(com.qgyyzs.globalcosmetics.base.ViewHolder viewHolder, final UserListBean.JsonData biduserBean, final int position) {
            ImageView mUserHead=viewHolder.getView(R.id.item_userphoto_img);
            TextView mItemUsernameTv=viewHolder.getView(R.id.item_nickname_tv);
            TextView mItemArea=viewHolder.getView(R.id.item_area_tv);
            Button mBtnDel=viewHolder.getView(R.id.btnDel);
            final SwipeMenuLayout swipeMenuLayout=viewHolder.getView(R.id.itemview);

            mItemUsernameTv.setText(TextUtils.isEmpty(biduserBean.getRealName())?"":biduserBean.getRealName());
            mItemArea.setText(TextUtils.isEmpty(biduserBean.getKefuArea())?"":biduserBean.getKefuArea());
            if(TextUtils.isEmpty(biduserBean.getHeadImg())) {
                mUserHead.setImageResource(R.drawable.icon_user_defult);
            }else{
                Glide.with(mContext).load(biduserBean.getHeadImg())
                        .apply(RequestOptions.circleCropTransform()
                        .error(R.drawable.icon_user_defult))
                        .into(mUserHead);
            }
            viewHolder.getView(R.id.content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ChildAccountAreaActivity.class);
                    intent.putExtra("userid", biduserBean.getId()+"");
                    intent.putExtra("area", biduserBean.getKefuArea());
                    startActivityForResult(intent,position);
                }
            });
            mBtnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(mContext)
                            .setMessage("删除此账号?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    accountName=biduserBean.getAccountName();
                                    if(TextUtils.isEmpty(accountName)||accountName.equals(myusername)){
                                        ToastUtil.showToast(mContext,"不能删除自己哦",true);
                                        return;
                                    }
                                    delPresenter.delChild();
                                    swipeMenuLayout.quickClose();
                                    mDatas.remove(position);
                                    notifyDataSetChanged();

                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //取消按钮事件

                                }
                            })
                            .show();
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null&&resultCode==RESULT_OK){
            String area=data.getStringExtra("area");
            if(area==null) return;
            mChildList.get(requestCode).setKefuArea(area.length()>2?area.substring(1,area.length()-1):area);
            accountAdapter.notifyDataSetChanged();
        }
    }
}
