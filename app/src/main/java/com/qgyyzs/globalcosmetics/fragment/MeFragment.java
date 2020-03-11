package com.qgyyzs.globalcosmetics.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mmin18.widget.RealtimeBlurView;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.activity.ChildAccountListActivity;
import com.qgyyzs.globalcosmetics.activity.FeedBackActivity;
import com.qgyyzs.globalcosmetics.activity.LoginActivity;
import com.qgyyzs.globalcosmetics.activity.MyCollectActivity;
import com.qgyyzs.globalcosmetics.activity.MyFriendsActivity;
import com.qgyyzs.globalcosmetics.activity.MyGuanzhuActivity;
import com.qgyyzs.globalcosmetics.activity.MyInfoActivity;
import com.qgyyzs.globalcosmetics.activity.MyProductListActivity;
import com.qgyyzs.globalcosmetics.activity.MyProxyActivity;
import com.qgyyzs.globalcosmetics.activity.MyRecruitActivity;
import com.qgyyzs.globalcosmetics.activity.ProxySettingActivity;
import com.qgyyzs.globalcosmetics.activity.SettingActivity;
import com.qgyyzs.globalcosmetics.activity.WebBaseActivity;
import com.qgyyzs.globalcosmetics.base.BaseFragment;
import com.qgyyzs.globalcosmetics.http.retrofit.RetrofitUtils;
import com.qgyyzs.globalcosmetics.mvp.iface.CountView;
import com.qgyyzs.globalcosmetics.mvp.iface.ListStringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.KefuPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.MeCountPresenter;
import com.qgyyzs.globalcosmetics.utils.GlideCircleTransform;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.ScreenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/28.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener ,CountView,ListStringView{
    private MeCountPresenter countPresenter=new MeCountPresenter(this, (RxFragmentActivity) getActivity());
    private KefuPresenter kefuPresenter=new KefuPresenter(this, (RxFragmentActivity) getActivity());
    @BindView(R.id.fabu_ll)
    LinearLayout mdailiLl;
    @BindView(R.id.product_ll)
    LinearLayout mProductLl;
    @BindView(R.id.perfect_ll)
    RelativeLayout mPerfectLl;
    @BindView(R.id.recruit_ll)
    LinearLayout mRecruitLl;
    @BindView(R.id.my_circle_rl)
    RelativeLayout mMyCircleRl;
    @BindView(R.id.my_trade_rl)
    RelativeLayout mMyTradeRl;
    @BindView(R.id.my_object_tv)
    TextView mMyObjectTv;
    @BindView(R.id.my_version_tv)
    TextView mMyVersionTv;
    @BindView(R.id.service_tv)
    TextView mServiceTv;
    @BindView(R.id.mjobNumTv)
    TextView mjobNumTv;
    @BindView(R.id.mCollectCount_Tv)
    TextView mCollectNum;
    @BindView(R.id.mGuanzhuCount_Tv)
    TextView mGuanzhuNum;
    @BindView(R.id.my_friends_rl)
    RelativeLayout rlMyfriends;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.user_img)
    ImageView mImageView;
    @BindView(R.id.setting_img)
    ImageView mSettingImageView;
    @BindView(R.id.nickname_tv)
    TextView mNicknameTextView;
    @BindView(R.id.myproduct_tv)
    TextView mNumTextView;
    @BindView(R.id.proxy_tv)
    TextView mProxyTextView;
    @BindView(R.id.manager_ll)
    RelativeLayout rlManager;
    @BindView(R.id.vManager)
    View vManager;
    @BindView(R.id.red)
    ImageView red;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.me_content)
    LinearLayout mlinearContent;
    @BindView(R.id.blurview)
    RealtimeBlurView blurView;

    private String nickname;
    private String headimg;
    private boolean IsPrimary;

    private Dialog mDialog;
    private TextView tv_phone1,tv_phone2,tv_phone3;

    @Override
    protected int getLayout() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView(View view) {
        initDialog();
        IsPrimary = MyApplication.mSharedPreferences.getBoolean("IsPrimary",false);

        if(IsPrimary){
            rlManager.setVisibility(View.VISIBLE);
            vManager.setVisibility(View.VISIBLE);
        }else{
            rlManager.setVisibility(View.GONE);
            vManager.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                countPresenter.getCount();
                kefuPresenter.getkefuTel();
            }
        }).start();
    }

    @Override
    protected void initListener() {
        mImageView.setOnClickListener(this);
        mSettingImageView.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                countPresenter.getCount();
                getServerVersion();
            }
        });
    }

    private void getServerVersion() {
        if (MyApplication.isUpdate==1) {
            red.setVisibility(View.VISIBLE);
        }else{
            red.setVisibility(View.GONE);
        }
    }

    private void setData() {
        headimg = MyApplication.mSharedPreferences.getString("HeadImg", "");//用户头像
        nickname = MyApplication.mSharedPreferences.getString("RealName", "昵称");//整个页面要用
        mNicknameTextView.setText(nickname);
        if (!TextUtils.isEmpty(headimg)) {
            Glide.with(getActivity()).load(headimg).error(R.drawable.icon_user_defult).transform(new GlideCircleTransform(getActivity())).placeholder(R.drawable.icon_user_defult)
                    .into(mImageView);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if(!MyApplication.islogin){
            LoginActivity.start(getActivity());
            return;
        }
        switch (v.getId()) {
            case R.id.user_img:
                startActivity(new Intent(getActivity(), MyInfoActivity.class));
                break;
            case R.id.setting_img:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.tv_phone1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                        && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(Manifest.permission.CALL_PHONE,
                            getString(R.string.permission_call),
                            1);
                }else {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + tv_phone1.getText().toString()));
                    startActivity(intent);
                    mDialog.dismiss();
                }
                break;
            case R.id.tv_phone2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                        && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(Manifest.permission.CALL_PHONE,
                            getString(R.string.permission_call),
                            1);
                }else {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + tv_phone2.getText().toString()));
                    startActivity(intent);
                    mDialog.dismiss();
                }
                break;
            case R.id.tv_phone3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                        && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(Manifest.permission.CALL_PHONE,
                            getString(R.string.permission_call),
                            1);
                }else {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + tv_phone3.getText().toString()));
                    startActivity(intent);
                    mDialog.dismiss();
                }
                break;
        }
    }

    @OnClick({R.id.fabu_ll,R.id.product_ll, R.id.perfect_ll, R.id.recruit_ll, R.id.my_circle_rl, R.id.my_trade_rl, R.id.my_object_tv, R.id.my_version_tv, R.id.service_tv,R.id.manager_ll,R.id.my_friends_rl})
    public void onViewClicked(View view) {
        Intent intent;
        if(!MyApplication.islogin){
            LoginActivity.start(getActivity());
            return;
        }
        switch (view.getId()) {
            case R.id.fabu_ll://我的发布
                startActivity(new Intent(getActivity(), MyProxyActivity.class));
                break;
            case R.id.product_ll://我的产品
                startActivity(new Intent(getActivity(), MyProductListActivity.class));
                break;
            case R.id.perfect_ll://我的代理设置
                startActivity(new Intent(getActivity(), ProxySettingActivity.class));
                break;
            case R.id.recruit_ll://我的招聘
                startActivity(new Intent(getActivity(), MyRecruitActivity.class));
                break;
            case R.id.my_circle_rl://我的关注
                startActivity(new Intent(getActivity(), MyGuanzhuActivity.class));
                break;
            case R.id.my_trade_rl://我的收藏
                startActivity(new Intent(getActivity(), MyCollectActivity.class));
                break;
            case R.id.my_object_tv://意见反馈
                startActivity(new Intent(getActivity(), FeedBackActivity.class));
                break;
            case R.id.my_version_tv://版本介绍
                intent=new Intent();
                intent.putExtra("title","版本介绍");
                intent.putExtra("url", RetrofitUtils.BASE_API+"Home/Version");
                intent.setClass(getActivity(),WebBaseActivity.class);
                startActivity(intent);
                break;
            case R.id.my_friends_rl:
                startActivity(new Intent(getActivity(), MyFriendsActivity.class));
                break;
            case R.id.service_tv://客服热线
                mDialog.show();
                break;
            case R.id.manager_ll:
                intent = new Intent(getActivity(), ChildAccountListActivity.class);
                intent.putExtra("username", MyApplication.username);
                startActivity(intent);
                break;
        }
    }

    private void initDialog() {
        mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.view_kefu);
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp1 = dialogWindow
                .getAttributes();// 创建布局
        lp1.width = ScreenUtils.getScreenWidth(getActivity());
        lp1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
        dialogWindow.setAttributes(lp1);// 加载布局
        tv_phone1= (TextView) dialogWindow.findViewById(R.id.tv_phone1);
        tv_phone2= (TextView) dialogWindow.findViewById(R.id.tv_phone2);
        tv_phone3= (TextView) dialogWindow.findViewById(R.id.tv_phone3);

        tv_phone1.setOnClickListener(this);
        tv_phone2.setOnClickListener(this);
        tv_phone3.setOnClickListener(this);

        getServerVersion();
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
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
            jsonObject.put("userid", MyApplication.userId);
            jsonObject.put("pcusername", MyApplication.username);
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
    public void showCountResult(List<Integer> list) {
        if(null!=refreshLayout)refreshLayout.finishRefresh();
        if(null==list)return;
        mNumTextView.setText(list.get(0)>0?list.get(0)+"":"点击发布");
        mProxyTextView.setText(list.get(1)>0?list.get(1)+"":"点击发布");
        mjobNumTv.setText(list.get(2)>0?list.get(2)+"":"点击发布");
        mGuanzhuNum.setText(list.get(3)>0?list.get(3)+"":"");
        mCollectNum.setText(list.get(4)>0?list.get(4)+"":"");
    }

    @Override
    public void showStringListResult(List<String> klist) {
        if(klist!=null) {
            tv_phone1.setText(klist.get(0));
            if(klist.size()>1) {
                tv_phone2.setVisibility(View.VISIBLE);
                tv_phone2.setText(klist.get(1));
            }else{
                tv_phone2.setVisibility(View.GONE);
            }
            if(klist.size()>2) {
                tv_phone3.setVisibility(View.VISIBLE);
                tv_phone3.setText(klist.get(2));
            }else{
                tv_phone3.setVisibility(View.GONE);
            }
        }else{
            tv_phone1.setText("0571-85885866");
//            tv_phone2.setText("0571-85885082");
//            tv_phone3.setText("4009668868");
        }
    }
}
