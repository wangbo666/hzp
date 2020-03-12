package com.qgyyzs.globalcosmetics.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.base.CommonAdapter;
import com.qgyyzs.globalcosmetics.bean.LinkBean;
import com.qgyyzs.globalcosmetics.customview.EmptyRecyclerView;
import com.qgyyzs.globalcosmetics.mvp.iface.LinkView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.LinkManPresenter;
import com.qgyyzs.globalcosmetics.nim.session.SessionHelper;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/29 0029.
 */

public class LinkListActivity extends BaseActivity implements LinkView{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private String username;
    private List<LinkBean.JsonData> mChildList=new ArrayList<>();
    private LinkAdapter accountAdapter;

    private LinkManPresenter linkPresenter=new LinkManPresenter(this,this);

    @Override
    protected int getLayout() {
        return R.layout.activity_linklist;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        username=getIntent().getStringExtra("username");
    }

    @Override
    public void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        accountAdapter=new LinkAdapter(this,mChildList);
        recyclerView.setAdapter(accountAdapter);

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
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                linkPresenter.getLinkList();
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
            jsonObject.put("pcusername",username);
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
    public void showLinkResult(LinkBean bean) {
        if(null!=refreshLayout)refreshLayout.finishRefresh();
        if(null!=recyclerView)recyclerView.setEmptyView(emptyView);
        if(null==bean||bean.getJsonData()==null)return;
        mChildList.clear();
        mChildList.addAll(bean.getJsonData());
        accountAdapter.notifyDataSetChanged();
    }

    private class LinkAdapter extends CommonAdapter<LinkBean.JsonData> {

        public LinkAdapter(Activity context, List<LinkBean.JsonData> biduserBeanList) {
            super(context,R.layout.adapter_link,biduserBeanList);
        }

        @Override
        public void convert(com.qgyyzs.globalcosmetics.base.ViewHolder viewHolder, final LinkBean.JsonData biduserBean, int position) {
            ImageView mUserHead=viewHolder.getView(R.id.item_userphoto_img);
            TextView mItemUsernameTv=viewHolder.getView(R.id.item_nickname_tv);
            TextView mContactTv=viewHolder.getView(R.id.item_contact_tv);
            TextView mProvinceTv=viewHolder.getView(R.id.item_province_tv);
            TextView mTvPhone=viewHolder.getView(R.id.item_phone_tv);
            mItemUsernameTv.setText(TextUtils.isEmpty(biduserBean.getRealName())?"":biduserBean.getRealName());
            mProvinceTv.setText(TextUtils.isEmpty(biduserBean.getKefuArea())?"":biduserBean.getKefuArea());
            mTvPhone.setVisibility(biduserBean.getFlag()==1?View.VISIBLE:View.GONE);
            if(TextUtils.isEmpty(biduserBean.getHeadImg())) {
                mUserHead.setImageResource(R.drawable.icon_user_defult);
            }else{
                Glide.with(mContext).load(biduserBean.getHeadImg()).apply(RequestOptions.circleCropTransform()
                        .error(R.drawable.icon_user_defult))
                        .into(mUserHead);
            }

            mContactTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!TextUtils.isEmpty(biduserBean.getNimID())) {
                        SessionHelper.startP2PSession(mContext, biduserBean.getNimID());
                    }
                }
            });
            mTvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!TextUtils.isEmpty(biduserBean.getLinkTel())){
                        new AlertDialog.Builder(mContext)
                                .setMessage("拨打电话："+biduserBean.getLinkTel())
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                                                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
                                                != PackageManager.PERMISSION_GRANTED) {
                                            requestPermission(Manifest.permission.CALL_PHONE,
                                                    getString(R.string.permission_call),
                                                    1);
                                        }else {
                                            Intent intent = new Intent();
                                            intent.setAction(Intent.ACTION_CALL);
                                            intent.setData(Uri.parse("tel:" + biduserBean.getLinkTel()));
                                            mContext.startActivity(intent);
                                        }
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                    }
                                })
                                .show();
                    }else{
                        ToastUtil.showToast(mContext,"该用户暂未公开联系方式",true);
                    }
                }
            });
        }
    }
}
