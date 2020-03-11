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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.ProxySetInfoBean;
import com.qgyyzs.globalcosmetics.bean.PublishProxyBean;
import com.qgyyzs.globalcosmetics.bean.UserListBean;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventCollect;
import com.qgyyzs.globalcosmetics.http.retrofit.RetrofitUtils;
import com.qgyyzs.globalcosmetics.mvp.iface.ProxySetInfoView;
import com.qgyyzs.globalcosmetics.mvp.iface.PublishProxyView;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.iface.UserListView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.AddVisterPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.MyProxyInfoPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ProductCollectPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ProductIsCollectPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.PublishProxyPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ZsUserListPresenter;
import com.qgyyzs.globalcosmetics.nim.session.SessionHelper;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.MJavascriptInterface;
import com.qgyyzs.globalcosmetics.utils.ScreenUtils;
import com.qgyyzs.globalcosmetics.utils.ShareUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ezy.ui.layout.LoadingLayout;

/**
 * 产品详情页面
 */
public class MedicineDetailActivity extends BaseActivity implements View.OnClickListener,UserListView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_right)
    TextView mRightTextView;
    @BindView(R.id.medicine_detail_webview)
    WebView mWebView;
    @BindView(R.id.bottom)
    LinearLayout mLinearLayout;
    @BindView(R.id.rlPhone)
    RelativeLayout mRlPhone;
    @BindView(R.id.phone_img)
    ImageView mPhoneImage;
    @BindView(R.id.collect_img)
    ImageView mCollectImage;
    @BindView(R.id.send_msg_btn)
    TextView mSendMsgTextView;
    @BindView(R.id.send_daili_tv)
    TextView mSendDailiTextView;
    @BindView(R.id.loading)
    LoadingLayout loadingLayout;

    private int proid;//产品id
    private int flag;//发布者的身份 1代表黄金会员

    private List<String> kefuList = new ArrayList<>();
    private String str[] = null;
    private Dialog mDialog;
    private ListView mListView;
    private ArrayAdapter<String> mArrayAdapter;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String username,userid;
    private String name, imageurl;
    private String linktel,nimid;
    private String muser="";
    private UMWeb web;

    private Dialog dialog;
    private TextView mTvTishi;
    private String danwei;
    private View view1;
    private EditText mNameEdit,mPhoneEdit;
    private Spinner mContentSpinner,mQuanxianSpinner;
    private TextView mTextPlcae;
    private String resplace;
    private LinearLayout rlPlace;
    private Button btnCancel,btnSubmit;
    private String dailiString;
    private int quanxian=1;
    private String[] mItems,mItems2;

    private ZsUserListPresenter zsUserListPresenter=new ZsUserListPresenter(this,this);
    private ProductIsCollectPresenter isCollectPresenter;
    private ProductCollectPresenter collectPresenter;
    private PublishProxyPresenter proxyPresenter;
    private MyProxyInfoPresenter myProxyInfoPresenter;
    private AddVisterPresenter addVisterPresenter;

    @Override
    protected int getLayout() {
        return R.layout.activity_medicine_detail;
    }

    private StringView addvisView=new StringView() {
        @Override
        public void showStringResult(String result) {

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
                jsonObject.put("F_proid", proid);
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

    private StringView isCollectView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(TextUtils.isEmpty(result))return;

            if (result.equals("已收藏")) {
                mCollectImage.setImageResource(R.mipmap.collect_sel);
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
                jsonObject.put("proid", proid);
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

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mSendMsgTextView.setOnClickListener(this);
        mSendDailiTextView.setOnClickListener(this);
        mCollectImage.setOnClickListener(this);
        mRightTextView.setOnClickListener(this);
        mPhoneImage.setOnClickListener(this);
        rlPlace.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                        && ActivityCompat.checkSelfPermission(MedicineDetailActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(Manifest.permission.CALL_PHONE,
                            getString(R.string.permission_call),
                            REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
                }else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + kefuList.get(position)));
                    startActivity(intent);
                }
            }
        });
        mContentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dailiString=mItems[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mQuanxianSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                quanxian=i-1;
                LogUtils.e("点击选中权限"+quanxian);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        resplace = mSharedPreferences.getString("dailiArea", "");
        if (TextUtils.isEmpty(resplace)||resplace.equals("null")) {
            mTextPlcae.setText("未设置");
        } else {
            mTextPlcae.setText(resplace);
        }
    }

    @Override
    public void initData() {
        mWebView.addJavascriptInterface(new MJavascriptInterface(this),"showImage");
        mWebView.loadUrl(RetrofitUtils.BASE_API + "product/productinfo?proid=" + proid);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JavascriptInterface(),"openCompany");
        mWebView.setWebViewClient(new WebViewClient() {

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

        String Name = mSharedPreferences.getString("RealName", "");
        String Tel = mSharedPreferences.getString("linkTel", "");
        if (TextUtils.isEmpty(Name) || Name.equals("null")) {
            mNameEdit.setText("");
        } else {
            mNameEdit.setText(Name);
        }
        if (TextUtils.isEmpty(Tel) || Tel.equals("null")) {
            mPhoneEdit.setText("");
            mPhoneEdit.setFocusable(true);
            mPhoneEdit.setFocusableInTouchMode(true);
        } else {
            mPhoneEdit.setText(Tel);
            mPhoneEdit.setFocusable(false);
            mPhoneEdit.setFocusableInTouchMode(false);
        }

        mItems = getResources().getStringArray(R.array.dailiArrays);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.view_spinner_text, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mContentSpinner.setAdapter(adapter);

        mItems2=getResources().getStringArray(R.array.quanxianArrays);
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,R.layout.view_spinner_text, mItems2);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mQuanxianSpinner.setAdapter(adapter1);
        mQuanxianSpinner.setSelection(2,true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                zsUserListPresenter.getUserList();
                if(!username.equals(muser)) {
                    isCollectPresenter.isCollect();
                    addVisterPresenter.addVister();
                }
                if(TextUtils.isEmpty(resplace)){
                    myProxyInfoPresenter.getMyProxySet();
                }
            }
        }).start();
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        collectPresenter=new ProductCollectPresenter(collectView,this);
        isCollectPresenter=new ProductIsCollectPresenter(isCollectView,this);
        addVisterPresenter=new AddVisterPresenter(addvisView,this);
        proxyPresenter=new PublishProxyPresenter(publishProxyView,this);
        myProxyInfoPresenter=new MyProxyInfoPresenter(proxySetInfoView,this);

        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        username=mSharedPreferences.getString("username","");
        userid=mSharedPreferences.getString("userid","");
        resplace = mSharedPreferences.getString("dailiArea", "");
        Intent intent = getIntent();
        proid = intent.getIntExtra("proid", 0);
        name = intent.getStringExtra("name");
        imageurl = intent.getStringExtra("image");
        muser=intent.getStringExtra("muser");
        danwei=intent.getStringExtra("company");
        toolbar.setTitle(name);
        String share=getIntent().getStringExtra("isshare");
        if(TextUtils.isEmpty(share))mRightTextView.setText("分享");

        view1 = LayoutInflater.from(this).inflate(R.layout.view_sendproxy, null);
        mContentSpinner= (Spinner) view1.findViewById(R.id.content_spinner);
        mQuanxianSpinner=(Spinner)view1.findViewById(R.id.quanxian_spinner);
        mNameEdit= (EditText)view1.findViewById(R.id.name_edit);
        mPhoneEdit= (EditText)view1.findViewById(R.id.phone_edit);
        rlPlace=(LinearLayout) view1.findViewById(R.id.place_rl);
        mTextPlcae=(TextView)view1.findViewById(R.id.place_tv);
        btnCancel=(Button)view1.findViewById(R.id.fliter_reset_btn);
        btnSubmit=(Button)view1.findViewById(R.id.fliter_ok_btn);

        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.kefu_layout);
        Window dialogWindow = mDialog.getWindow();
        mListView = (ListView) dialogWindow.findViewById(R.id.kefu_listview);
        mTvTishi= (TextView) dialogWindow.findViewById(R.id.mTvtishi);
        View Vtishi=dialogWindow.findViewById(R.id.vtishi);
        if(!TextUtils.isEmpty(danwei)) {
            mTvTishi.setVisibility(View.VISIBLE);
            mTvTishi.setText(danwei);
            Vtishi.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lp2 = dialogWindow
                .getAttributes();// 创建布局
        lp2.width = ScreenUtils.getScreenWidth(this);
        lp2.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp2);// 加载布局
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.place_rl:
                Intent intent = new Intent(this, ProvinceSelectActivity.class);
                String area=mTextPlcae.getText().toString();
                intent.putExtra("proxy", "1");
                if (area.equals("未设置")) {
                    intent.putExtra("province", "");
                    intent.putExtra("city", "");
                } else {
                    String[] str = area.split(",");
                    if (str.length == 1) {
                        intent.putExtra("province", str[0]);
                        intent.putExtra("city", "");
                    } else {
                        intent.putExtra("province", str[0]);
                        intent.putExtra("city", area.substring(str[0].length()+1,area.length()));
                    }
                }
                startActivity(intent);
                break;
            case R.id.fliter_reset_btn:
                dialog.dismiss();
                break;
            case R.id.fliter_ok_btn:
                if(!dailiString.equals("请选择留言内容")){
                    if(quanxian!=-1) {
                        if (!TextUtils.isEmpty(resplace) && !resplace.equals("null")) {
                            if (!TextUtils.isEmpty(mNameEdit.getText().toString().trim())) {
                                if (!TextUtils.isEmpty(mPhoneEdit.getText().toString().trim())) {
                                    btnSubmit.setEnabled(false);
                                    proxyPresenter.PublishProxy();
                                } else {
                                    ToastUtil.showToast(this, "请输入联系电话", true);
                                }
                            } else {
                                ToastUtil.showToast(this, "请输入联系人", true);
                            }
                        } else {
                            ToastUtil.showToast(this, "代理区域不能为空", true);
                        }
                    }else{
                        ToastUtil.showToast(this,"请选择查看权限",true);
                    }
                }else{
                    ToastUtil.showToast(this,"请选择留言内容",true);
                }
                break;
            case R.id.send_daili_tv:
                if(dialog==null){
                    dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
                    dialog.show();
                    Window dialogWindow1 = dialog.getWindow();// 获取手机屏幕
                    WindowManager.LayoutParams lp1 = dialogWindow1
                            .getAttributes();// 创建布局
                    dialogWindow1.setGravity(Gravity.BOTTOM);
                    lp1.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    lp1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    dialogWindow1.setAttributes(lp1);// 加载布局
                    dialog.getWindow().setContentView(view1);// 加载dialog
                }else {
                    dialog.show();
                }
                break;
            case R.id.send_msg_btn:
                if (!MyApplication.islogin) {
                    startActivity(new Intent(MedicineDetailActivity.this, LoginActivity.class));
                    return;
                } else {
                    if (mSendMsgTextView.getText().toString().equals("发消息")) {
                        if (!username.equals(muser)) {
                            SessionHelper.startP2PSession(this, nimid);
                        } else {
                            ToastUtil.showToast(this, "不能给自己发消息哦", true);
                        }
                    } else {
                        Intent intent1 = new Intent(this, LinkListActivity.class);
                        intent1.putExtra("username",muser);
                        startActivity(intent1);
                    }
                }
                break;
            case R.id.phone_img:
                if(username.equals(muser)){
                    ToastUtil.showToast(MedicineDetailActivity.this,"不能给自己打电话哦",true);
                }else {
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
                }
                break;
            case R.id.collect_img:
                collectPresenter.getCollect();
                break;
            case R.id.tv_right:
                UMImage image;
                LogUtils.e( "产品图片" + imageurl);
                if (TextUtils.isEmpty(imageurl) || (imageurl.equals("null"))) {
                    image = new UMImage(this, R.mipmap.ic_logo);
                } else {
                    image = new UMImage(this, imageurl);
                }
                web = new UMWeb(RetrofitUtils.BASE_API + "product/productinfo?OpenFromApp=1&proid=" + proid);
                web.setTitle("[正在招商]" + name);//标题
                web.setThumb(image);  //缩略图
                web.setDescription("产品优异，价格实惠。");//描述
                new ShareUtils(this, web, v);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 001 && resultCode == 002) {
            mTextPlcae.setText(data.getStringExtra("province"));
        }
    }

    private StringView collectView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(TextUtils.isEmpty(result))return;

            ToastUtil.showToast(MedicineDetailActivity.this,result,true);
            EventBus.getDefault().post(new AnyEventCollect());
            if (result.equals("收藏产品成功")) {
                mCollectImage.setImageResource(R.mipmap.collect_sel);
            } else {
                mCollectImage.setImageResource(R.mipmap.collect_nor);
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
                jsonObject.put("proid", proid);
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
            jsonObject.put("pcusername", muser);
            LogUtils.e(jsonObject.toString());
        }catch (JSONException e){

        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showUserListResult(UserListBean bean) {
        if(bean!=null){
            if(bean.getJsonData().size()==0){
                return;
            }else{
                if(!TextUtils.isEmpty(bean.getJsonData().get(0).getPcUsername())&&bean.getJsonData().get(0).getPcUsername().equals(username)){
                    mLinearLayout.setVisibility(View.GONE);
                }else{
                    mLinearLayout.setVisibility(View.VISIBLE);
                }
            }
            if(bean.getJsonData().size()==1){
                nimid=bean.getJsonData().get(0).getNimID();
                if(!TextUtils.isEmpty(nimid)) {
                    List<String> list = new ArrayList<>();
                    list.add(nimid);
                    NIMClient.getService(UserService.class).fetchUserInfo(list);
                }
                flag=bean.getJsonData().get(0).getFlag();
                linktel=bean.getJsonData().get(0).getLinkTel();
                mSendMsgTextView.setText("发消息");
                if(TextUtils.isEmpty(nimid)||nimid.equals("null")){
                    mSendMsgTextView.setVisibility(View.GONE);
                }else {
                    mSendMsgTextView.setVisibility(View.VISIBLE);
                }
                if(flag==1){
                    mRlPhone.setVisibility(View.VISIBLE);
                }else{
                    mRlPhone.setVisibility(View.GONE);
                }
            }else if(bean.getJsonData().size()>1){
                mSendMsgTextView.setText("联系招商经理");
                mRlPhone.setVisibility(View.GONE);
            }
        }else{
            mLinearLayout.setVisibility(View.GONE);
        }
    }

    private PublishProxyView publishProxyView=new PublishProxyView() {
        @Override
        public void showPublishResult(PublishProxyBean bean) {
            btnSubmit.setEnabled(true);
            if(null==bean)return;

            if (bean.getResult() == 1) {
                dialog.dismiss();
            }

            ToastUtil.showToast(MedicineDetailActivity.this, bean.getMsg(), true);
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
                jsonObject.put("pcusername",username);
                jsonObject.put("Content", dailiString);
                jsonObject.put("Proid",proid);
                jsonObject.put("ChaKanQuanXian",quanxian);
                jsonObject.put("DailiArea", mTextPlcae.getText().toString().trim());
                jsonObject.put("LinkMan",mNameEdit.getText().toString().trim());
                jsonObject.put("linktel",mPhoneEdit.getText().toString().trim());
                jsonObject.put("ShenFen","个人");
                LogUtils.e(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }

        @Override
        public void showToast(String msg) {

        }
    };

    private ProxySetInfoView proxySetInfoView=new ProxySetInfoView() {
        @Override
        public void showProxyResult(ProxySetInfoBean proxySetInfoBean) {
            if(proxySetInfoBean!=null){
                if(proxySetInfoBean.getJsonData()!=null){
                    resplace = proxySetInfoBean.getJsonData().getCzd();
                    if (TextUtils.isEmpty(resplace)||resplace.equals("null")) {
                        mTextPlcae.setText("未设置");
                    } else {
                        mTextPlcae.setText(resplace);
                    }
                    mEditor = mSharedPreferences.edit();
                    mEditor.putString("dailiArea",resplace);
                    mEditor.commit();
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
        public String getJsonString() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userid", userid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }

        @Override
        public void showToast(String msg) {

        }
    };

    private class JavascriptInterface{
        @android.webkit.JavascriptInterface
        public void getCall(String parmJS){
            LogUtils.e("parmJS"+parmJS);
            try{
                JSONObject jsonObject=new JSONObject(parmJS);
                String PcUsername=jsonObject.getString("PcUsername");
                String company=jsonObject.getString("danwei");
                Intent intent = new Intent(mContext, CompanyDetialActivity.class);
                intent.putExtra("company_username",PcUsername);
                intent.putExtra("title",company);
                mContext.startActivity(intent);
                if(CompanyDetialActivity.instance!=null) CompanyDetialActivity.instance.finish();
            }catch (JSONException e){}

        }
    }

}