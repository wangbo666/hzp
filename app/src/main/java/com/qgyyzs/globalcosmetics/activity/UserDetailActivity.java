package com.qgyyzs.globalcosmetics.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.UserinfoBean;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventFriendList;
import com.qgyyzs.globalcosmetics.http.retrofit.RetrofitUtils;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.iface.UserInfoView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.AddFriendPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.SimpleUserinfoPresenter;
import com.qgyyzs.globalcosmetics.nim.session.SessionHelper;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.ScreenUtils;
import com.qgyyzs.globalcosmetics.utils.ShareUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ezy.ui.layout.LoadingLayout;

public class UserDetailActivity extends BaseActivity implements View.OnClickListener,UserInfoView{
    private SimpleUserinfoPresenter userInfoPresenter=new SimpleUserinfoPresenter(this,this);
    private AddFriendPresenter addFriendPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_right)
    TextView mRightTextview;
    @BindView(R.id.user_webview)
    WebView mWebView;
    @BindView(R.id.bottom)
    LinearLayout mLinearLayout;
    @BindView(R.id.send_msg_tv)
    TextView mMsgTextView;
    @BindView(R.id.send_call_tv)
    TextView mCallTextView;
    @BindView(R.id.add_friend_tv)
    TextView mAddfriendTextView;
    @BindView(R.id.loading)
    LoadingLayout loadingLayout;

    private List<String> kefuList = new ArrayList<>();
    private String str[] = null;
    private Dialog mDialog;
    private ListView mListView;
    private ArrayAdapter<String> mArrayAdapter;

    private String myuserId, touserid,nickname,photo,linktel,nimid;
    private String shareName;
    private int infoId;
    private boolean isopen;

    private String Weburl;
    private int tag;
    private String muser;

    private SharedPreferences mSharedPreferences;

    @Override
    protected int getLayout() {
        return R.layout.activity_user_detail;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        muser = intent.getStringExtra("muser");
        touserid=intent.getStringExtra("touserid");
        tag=intent.getIntExtra("info",0);
        infoId=intent.getIntExtra("id",0);
        nickname = intent.getStringExtra("nickname");
        shareName=intent.getStringExtra("nickname");
        photo = intent.getStringExtra("image");
        toolbar.setTitle(nickname);
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        myuserId=mSharedPreferences.getString("userid","");

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        if(tag==10){
            Weburl= RetrofitUtils.BASE_API+"user/supplierinfo?userid="+touserid;
        }else {
            Weburl = RetrofitUtils.BASE_API + "Caigou/DaiLiInfo?dlid=" + infoId + "&token=" + MyApplication.TOKEN;
        }
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public WebResourceResponse  shouldInterceptRequest(WebView view, String url) {
//                if(url.contains("qgyyzs.net")){
                    return super.shouldInterceptRequest(view, url);
//                }else{//去掉广告
//                    return new WebResourceResponse(null,null,null);
//                }
            }

            @Override
            public void onPageFinished(WebView view, String url){
                super.onPageFinished(view, url);
                if(loadingLayout!=null)
                    loadingLayout.showContent();
            }
        });
        mWebView.loadUrl(Weburl);

        addFriendPresenter=new AddFriendPresenter(addFriendView,this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(touserid)||touserid.equals("0")) {
                    if(TextUtils.isEmpty(muser))return;
                }
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
        mMsgTextView.setOnClickListener(this);
        mCallTextView.setOnClickListener(this);
        mAddfriendTextView.setOnClickListener(this);
        mRightTextview.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                        && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(Manifest.permission.CALL_PHONE,
                            getString(R.string.permission_call),
                            1);
                }else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + kefuList.get(position)));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        String isshare=getIntent().getStringExtra("isshare");
        if(TextUtils.isEmpty(isshare))
            mRightTextview.setText("分享");

        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.kefu_layout);
        Window dialogWindow = mDialog.getWindow();
        mListView = (ListView) dialogWindow.findViewById(R.id.kefu_listview);
        WindowManager.LayoutParams lp1 = dialogWindow
                .getAttributes();// 创建布局
        lp1.width = ScreenUtils.getScreenWidth(this);
        lp1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp1);// 加载布局
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_friend_tv:
                if(!mAddfriendTextView.getText().toString().equals("已加入通讯录")){
                    if (myuserId.equals(touserid)) {
                        ToastUtil.showToast(this, "不能添加自己为好友", true);
                    } else {
                        addFriendPresenter.addFriend();
                    }
                }else{
                    ToastUtil.showToast(this,"该好友已存在",true);
                }
                break;
            case R.id.send_msg_tv:
                LogUtils.e("nimid"+nimid);
                if (!MyApplication.islogin) {
                    startActivity(new Intent(UserDetailActivity.this, LoginActivity.class));
                    return;
                } else {
                    if(nimid!=null) {
                        if(!myuserId.equals(touserid)) {
                            SessionHelper.startP2PSession(this, nimid);
                        }else{
                            ToastUtil.showToast(this,"不能给自己发消息哦",true);
                        }
                    }else{
                        ToastUtil.showToast(this,"该用户未公开联系方式",true);
                    }
                }
                break;
            case R.id.send_call_tv:
                LogUtils.e("Tel"+linktel);
                if(!myuserId.equals(touserid)) {
                    if (isopen) {
                        str = null;
                        kefuList.clear();
                        if (!TextUtils.isEmpty(linktel)) {
                            str = linktel.split("\\|");
                            for (int j = 0; j < str.length; j++) {
                                if (!str[j].equals(null)) {
                                    kefuList.add(str[j]);
                                }
                            }
                        }
                        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, kefuList);
                        mListView.setAdapter(mArrayAdapter);
                        mDialog.show();
                    } else {
                        ToastUtil.showToast(this, "对方未公开联系方式", true);
                    }
                }else{
                    ToastUtil.showToast(this, "不能给自己打电话哦", true);
                }
                break;
            case R.id.tv_right:
                if(tag==10){
                    Weburl=RetrofitUtils.BASE_API+"user/supplierinfo?OpenFromApp=1&userid="+touserid;
                }else {
                    Weburl = RetrofitUtils.BASE_API + "Caigou/DaiLiInfo?OpenFromApp=1&dlid=" + infoId;
                }
                UMImage image;
                LogUtils.e( "产品图片" + photo);

                if (TextUtils.isEmpty(photo) || (photo.equals("null"))) {
                    image = new UMImage(this, R.mipmap.ic_logo);
                } else {
                    image = new UMImage(this, photo);
                }
                UMWeb web = new UMWeb(Weburl);
                if(tag==10){
                    web.setTitle("[代理商]"+nickname);//标题
                }else{
                    web.setTitle("[代理信息]"+shareName);
                }
                web.setThumb(image);  //缩略图
                web.setDescription(" ");//描述
                new ShareUtils(this, web, v);
                break;
        }
    }

    @Override
    public String getJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            if(TextUtils.isEmpty(touserid)||touserid.equals("null")||touserid.equals("0")) {
                jsonObject.put("accountname", muser);//当前页码
            }else{
                jsonObject.put("userid",touserid);
            }
            LogUtils.e(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showUserResult(UserinfoBean bean) {
        if(bean!=null){
            nickname = bean.getJsonData().getRealName();
            linktel = bean.getJsonData().getLinkTel();
            isopen = bean.getJsonData().getIsOpen();
            nimid=bean.getJsonData().getNimID();

            if(!TextUtils.isEmpty(nimid)) {
                List<String> list = new ArrayList<>();
                list.add(nimid);
                NIMClient.getService(UserService.class).fetchUserInfo(list);
            }

            touserid=bean.getJsonData().getId()+"";
            if (bean.getResult() == 1) {
                if (TextUtils.isEmpty(nimid)||nimid.equals("null")) {
                    mLinearLayout.setVisibility(View.GONE);
                } else {
                    if(touserid.equals(myuserId)){
                        mLinearLayout.setVisibility(View.GONE);
                    }else {
                        mLinearLayout.setVisibility(View.VISIBLE);
                    }
                }
            }else{
                mLinearLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public void showToast(String msg) {

    }

    private StringView addFriendView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(TextUtils.isEmpty(result))return;

            mAddfriendTextView.setText("已加入通讯录");
            EventBus.getDefault().post(new AnyEventFriendList());
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
                jsonObject.put("userid", myuserId);
                jsonObject.put("f_userid", touserid);
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
