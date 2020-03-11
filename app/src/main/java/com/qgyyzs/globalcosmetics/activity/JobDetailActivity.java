package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.UserinfoBean;
import com.qgyyzs.globalcosmetics.http.retrofit.RetrofitUtils;
import com.qgyyzs.globalcosmetics.mvp.iface.UserInfoView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.SimpleUserinfoPresenter;
import com.qgyyzs.globalcosmetics.nim.session.SessionHelper;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.ShareUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

public class JobDetailActivity extends BaseActivity implements UserInfoView{
    private SimpleUserinfoPresenter userInfoPresenter=new SimpleUserinfoPresenter(this,this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_right)
    TextView mRightTv;
    @BindView(R.id.job_webview)
    WebView mJobWebview;
    @BindView(R.id.send_msg_tv)
    TextView mSendMsgTv;
    @BindView(R.id.bottom)
    LinearLayout mBottom;
    @BindView(R.id.loading)
    LoadingLayout loadingLayout;

    private String userid, touserid, title,company;
    private SharedPreferences mSharedPreferences;
    private String nimid;

    private int job_id = 0;

    @Override
    protected int getLayout() {
        return R.layout.activity_job_detail;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            job_id = intent.getIntExtra("id", 0);
            touserid = intent.getStringExtra("puserid");
            title = intent.getStringExtra("title");
            company=intent.getStringExtra("company");
        }

        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        userid = mSharedPreferences.getString("userid", "未设置");//整个页面要用
        if (userid.equals(touserid)) {
            mBottom.setVisibility(View.GONE);
        } else {
            mBottom.setVisibility(View.VISIBLE);
        }
        WebSettings settings = mJobWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        mJobWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//                if(url.contains("hzpzs.net")){
                    return super.shouldInterceptRequest(view, url);
//                }else{//去掉广告
//                    return new WebResourceResponse(null,null,null);
//                }
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(loadingLayout!=null)
                    loadingLayout.showContent();
            }
        });

        mJobWebview.loadUrl(RetrofitUtils.BASE_API + "job/jobinfo?id=" + job_id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                userInfoPresenter.getUserInfo();
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
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        toolbar.setTitle("招聘详情");
        mRightTv.setText("分享");
    }

    @OnClick({R.id.send_msg_tv, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_msg_tv:
                if (!MyApplication.islogin) {
                    startActivity(new Intent(JobDetailActivity.this, LoginActivity.class));
                    return;
                } else {
                    if(nimid!=null) {
                        if(!userid.equals(touserid)) {
                            SessionHelper.startP2PSession(this, nimid);
                        }else{
                            ToastUtil.showToast(this,"不能给自己发消息哦",true);
                        }
                    }else{
                        ToastUtil.showToast(this,"该用户未公开联系方式",true);
                    }
                }
                break;
            case R.id.tv_right:
                UMWeb web;
                UMImage image;
                image = new UMImage(this, R.mipmap.ic_logo);
                web = new UMWeb(RetrofitUtils.BASE_API + "job/jobinfo?OpenFromApp=1&id=" + job_id);
                web.setTitle("[招聘]"+company+"正在招聘：" + title);//标题
                web.setThumb(image);  //缩略图
                web.setDescription(" ");//描述
                new ShareUtils(this, web, view);
                break;
        }
    }

    @Override
    public void showUserResult(UserinfoBean bean) {
        if(bean==null) {
            mSendMsgTv.setVisibility(View.GONE);
            return;
        }
        nimid = bean.getJsonData().getNimID();
        if(!TextUtils.isEmpty(nimid)) {
            List<String> list = new ArrayList<>();
            list.add(nimid);
            NIMClient.getService(UserService.class).fetchUserInfo(list);
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
            jsonObject.put("userid", touserid);//当前页码
            LogUtils.e(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }
}
