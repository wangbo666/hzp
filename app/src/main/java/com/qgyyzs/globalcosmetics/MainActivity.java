package com.qgyyzs.globalcosmetics;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.qgyyzs.globalcosmetics.activity.FreshActivity;
import com.qgyyzs.globalcosmetics.nim.login.LogoutHelper;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.qgyyzs.globalcosmetics.activity.JobDetailActivity;
import com.qgyyzs.globalcosmetics.activity.JobpostActivity;
import com.qgyyzs.globalcosmetics.activity.LoginActivity;
import com.qgyyzs.globalcosmetics.activity.MedicineDetailActivity;
import com.qgyyzs.globalcosmetics.activity.ProxySettingActivity;
import com.qgyyzs.globalcosmetics.activity.PublishProductActivity;
import com.qgyyzs.globalcosmetics.activity.PublishProxyActivity;
import com.qgyyzs.globalcosmetics.activity.UserDetailActivity;
import com.qgyyzs.globalcosmetics.activity.WebBaseActivity;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseFragmentActivity;
import com.qgyyzs.globalcosmetics.customview.ClearEditText;
import com.qgyyzs.globalcosmetics.customview.MyCountTimer;
import com.qgyyzs.globalcosmetics.customview.PublishDialog;
import com.qgyyzs.globalcosmetics.fragment.HomeFragment;
import com.qgyyzs.globalcosmetics.fragment.MeFragment;
import com.qgyyzs.globalcosmetics.fragment.MessageFragment;
import com.qgyyzs.globalcosmetics.fragment.ProxyInfoFragment;
import com.qgyyzs.globalcosmetics.http.retrofit.RetrofitUtils;
import com.qgyyzs.globalcosmetics.jpush.ExampleUtil;
import com.qgyyzs.globalcosmetics.jpush.LocalBroadcastManager;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.BindPhoneCodePresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.BindPhonePresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.FreshProductPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.IsFabuPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.VersionPresenter;
import com.qgyyzs.globalcosmetics.uikit.common.ui.drop.DropFake;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.NoFastClickUtils;
import com.qgyyzs.globalcosmetics.utils.ScreenUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.SystemUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;
import util.UpdateAppUtils;

import static com.qgyyzs.globalcosmetics.application.MyApplication.USERSPINFO;

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener,StringView {
    private VersionPresenter versionPresenter=new VersionPresenter(this, this);
    private IsFabuPresenter fabuPresenter;
    private BindPhoneCodePresenter sendCodePresenter;
    private BindPhonePresenter bindPresenter;
    private FreshProductPresenter freshProductPresenter;
    private ImageView mRedImg;
    private TextView mHomeTextView;
    private TextView mNewsTextView;
    private TextView mBuyTextView;
    private TextView mMeTextView;
    private DropFake unreadLabel;
    private ImageView mImageView;

    private HomeFragment mHomeFragment;
    private MessageFragment mNewsFragment;
    private MeFragment mMeFragment;
    private ProxyInfoFragment dailiFragment;
    private FragmentTransaction transaction;

    private PublishDialog mPublishDialog;
    private SharedPreferences mSharedPreferences;

    public static MainActivity instance;

    private long clickTime = 0;

    private String mobile;
    private Dialog mDialog;
    private Button mBtnCancel,mBtnBind;
    private ClearEditText mPhoneEdit;
    private EditText mCodeEdit;
    private TextView mGetcodeText;
    private String codeString;
    private String sendPhone;
    private int flag;

    private String type;
    private String proid,muser,name,image,company;

    public static boolean isForeground = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    //初始化布局控件
    @Override
    protected void initView() {
        instance = this;
        StatusBarUtil.immersive(this);
        mSharedPreferences=getSharedPreferences(USERSPINFO, Context.MODE_PRIVATE);
        mobile = getIntent().getStringExtra("mobile");
        mRedImg = findViewById(R.id.red_img);
        mHomeTextView = findViewById(R.id.tab_home);
        mNewsTextView = findViewById(R.id.tab_news);
        mBuyTextView = findViewById(R.id.tab_buy);
        mMeTextView = findViewById(R.id.tab_me);
        mImageView = findViewById(R.id.img_free);
        unreadLabel = findViewById(R.id.unread_msg_number);

        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_bindmobile);
        mDialog.setCancelable(false);
        Window dialogWindow = mDialog.getWindow();
        mBtnCancel= (Button) dialogWindow.findViewById(R.id.cancel_btn);
        mBtnBind= (Button) dialogWindow.findViewById(R.id.bind_btn);
        mPhoneEdit= (ClearEditText) dialogWindow.findViewById(R.id.phone_edit);
        mCodeEdit= (EditText) dialogWindow.findViewById(R.id.code_edit);
        mGetcodeText= (TextView) dialogWindow.findViewById(R.id.getcode_text);
        WindowManager.LayoutParams lp1 = dialogWindow.getAttributes();// 创建布局
        lp1.width = ScreenUtils.getScreenWidth(this);
        lp1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp1);// 加载布局

        mBtnCancel.setOnClickListener(this);
        mBtnBind.setOnClickListener(this);
        mGetcodeText.setOnClickListener(this);

        LogUtils.e("mobile"+mobile);
        if(!TextUtils.isEmpty(getIntent().getStringExtra("bindphone"))) {
            if (TextUtils.isEmpty(mobile)) mDialog.show();
        }

        registerMessageReceiver();
//        Set<Integer> days = new HashSet<Integer>();
//        days.add(1);
//        days.add(2);
//        days.add(3);
//        days.add(4);
//        days.add(5);
//        JPushInterface.setPushTime(getApplicationContext(), days, 10, 23);
    }

    //添加监听事件
    @Override
    protected void initListener() {
        mHomeTextView.setOnClickListener(this);
        mNewsTextView.setOnClickListener(this);
        mBuyTextView.setOnClickListener(this);
        mMeTextView.setOnClickListener(this);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPublishDialog == null) {
                    mPublishDialog = new PublishDialog(MainActivity.this);

                    mPublishDialog.setArticleBtnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!MyApplication.islogin) {
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                return;
                            } else {
                                Intent intent = new Intent(MainActivity.this, ProxySettingActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                    mPublishDialog.setBidBtnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ToastDialog toastDialog = new ToastDialog(MainActivity.this);
                            if (!MyApplication.islogin) {
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                return;
                            } else {
//                                ToastUtil.showToast(MainActivity.this, "代理", true);
                                Intent intent = new Intent(MainActivity.this, PublishProxyActivity.class);
                                intent.putExtra("type", 0);
                                startActivity(intent);
                            }
                        }
                    });
                    mPublishDialog.setProductBtnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!MyApplication.islogin) {
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                return;
                            } else {
                                if(mSharedPreferences.getBoolean("IsPrimary",false)) {
                                    if (!NoFastClickUtils.isFastClick()) {
                                        fabuPresenter.isFabu();
                                    }
                                }else{
                                    ToastUtil.showToast(MainActivity.this,"请使用主账号发布产品",true);
                                }
                            }
                        }
                    });
                }
                mPublishDialog.setLetterBtnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!MyApplication.islogin) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            return;
                        } else {
//                                ToastUtil.showToast(MainActivity.this, "发布招聘", true);
                            Intent intent = new Intent(MainActivity.this, JobpostActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                mPublishDialog.setPhotoBtnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!MyApplication.islogin) {
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            return;
                        } else {
                            if(mSharedPreferences.getBoolean("IsPrimary",false)) {
                                if(flag==1) {
                                    startActivity(new Intent(MainActivity.this, FreshActivity.class));
                                }else {
                                    freshProductPresenter.getFresh();
                                }
                            }else{
                                ToastUtil.showToast(MainActivity.this,"请使用主账号刷新产品",true);
                            }
                        }
                    }
                });
                mPublishDialog.setMiniBlogBtnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, WebBaseActivity.class);
                        intent.putExtra("title", "关于我们");
                        intent.putExtra("url", RetrofitUtils.BASE_API + "Home/About");
                        startActivity(intent);
                    }
                });
                mPublishDialog.show();
            }
        });
    }


    //初始化数据
    @Override
    protected void initData() {
        freshProductPresenter=new FreshProductPresenter(freshView,this);
        Intent intent=getIntent();
        type=intent.getStringExtra("type");
        proid=intent.getStringExtra("proid");
        muser=intent.getStringExtra("muser");
        name=intent.getStringExtra("name");
        image=intent.getStringExtra("image");
        company=intent.getStringExtra("company");
        if(!TextUtils.isEmpty(type)) {
            Intent intent1;
            if (!MyApplication.islogin) {
                startActivity(new Intent(this, LoginActivity.class));
                return;
            } else {
                if (type.equals("1")) {
                    intent1 = new Intent(this, MedicineDetailActivity.class);
                    intent1.putExtra("proid", Integer.parseInt(proid));
                    intent1.putExtra("name", name);
                    intent1.putExtra("image", image);
                    intent1.putExtra("muser", muser);
                    intent1.putExtra("company",company);
                    startActivity(intent1);
                }else if(type.equals("2")){
                    intent1 = new Intent(this, UserDetailActivity.class);
                    intent1.putExtra("id",Integer.parseInt(intent.getStringExtra("dlid")));
                    intent1.putExtra("touserid",intent.getStringExtra("userid"));
                    intent1.putExtra("nickname", intent.getStringExtra("pname"));
                    intent1.putExtra("muser", intent.getStringExtra("muser"));
                    startActivity(intent1);
                }else if(type.equals("3")){
                    intent1 = new Intent(this, UserDetailActivity.class);
                    intent1.putExtra("info", 10);//代理商库
                    intent1.putExtra("touserid", intent.getStringExtra("userid"));
                    intent1.putExtra("nickname", intent.getStringExtra("rname"));
                    startActivity(intent1);
                }else if(type.equals("4")){
                    intent1 = new Intent(this, WebBaseActivity.class);
                    intent1.putExtra("title", intent.getStringExtra("title"));
                    intent1.putExtra("type","medical");
                    intent1.putExtra("url", RetrofitUtils.BASE_API + "artcle/artcleinfo?Type=" + type + "&id=" + intent.getStringExtra("id"));
                    startActivity(intent1);
                }else if(type.equals("5")){
                    intent1 = new Intent(this, WebBaseActivity.class);
                    intent1.putExtra("title", intent.getStringExtra("title"));
                    intent1.putExtra("type","bid");
                    intent1.putExtra("url", RetrofitUtils.BASE_API + "product/zhongbiaoinfo?id=" + intent.getStringExtra("id"));
                    startActivity(intent1);
                }else if(type.equals("6")){
                    intent1 = new Intent(this, JobDetailActivity.class);
                    intent1.putExtra("id", Integer.parseInt(intent.getStringExtra("id")));
                    intent1.putExtra("company",intent.getStringExtra("company"));
                    intent1.putExtra("puserid", intent.getStringExtra("userid"));
                    intent1.putExtra("title", intent.getStringExtra("jobname"));
                    startActivity(intent1);
                }
            }
        }

        mHomeTextView.setSelected(true);
        //开始先显示第一个界面
        transaction = getSupportFragmentManager().beginTransaction();
        mHomeFragment = new HomeFragment();
        transaction.add(R.id.fragment_container, mHomeFragment);
        transaction.commit();

        MobclickAgent.setDebugMode(true);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setCatchUncaughtExceptions(true);

        if(MyApplication.islogin) {
            NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, true);
        }

        versionPresenter.getVersion();

        fabuPresenter = new IsFabuPresenter(fabuView, this);

        sendCodePresenter=new BindPhoneCodePresenter(codeView,this);
        bindPresenter=new BindPhonePresenter(bindView,this);
    }

    private StringView bindView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(TextUtils.isEmpty(result))return;

            ToastUtil.showToast(MainActivity.this,"手机号绑定成功",true);
            if(mDialog!=null)
                mDialog.dismiss();
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
                jsonObject.put("id", MyApplication.userId);
                jsonObject.put("mobile",mPhoneEdit.getText().toString().trim());
                jsonObject.put("yzm",mCodeEdit.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }

        @Override
        public void showToast(String msg) {

        }
    };


    //重置所有文本的选中状态
    public void selected() {
        mHomeTextView.setSelected(false);
        mNewsTextView.setSelected(false);
        mBuyTextView.setSelected(false);
        mMeTextView.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mNewsFragment != null) {
            transaction.hide(mNewsFragment);
        }
        if (dailiFragment != null) {
            transaction.hide(dailiFragment);
        }
        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tab_home:
                transaction = getSupportFragmentManager().beginTransaction();
                hideAllFragment(transaction);
                selected();
                mHomeTextView.setSelected(true);
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    transaction.add(R.id.fragment_container, mHomeFragment);
                } else {
                    transaction.show(mHomeFragment);
                }
                transaction.commit();
                break;

            case R.id.tab_news:
                transaction = getSupportFragmentManager().beginTransaction();
                hideAllFragment(transaction);
                if (!MyApplication.islogin) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    return;
                } else {
                    selected();
                    mNewsTextView.setSelected(true);
                    if (mNewsFragment == null) {
                        mNewsFragment = new MessageFragment();
                        transaction.add(R.id.fragment_container, mNewsFragment);
                    } else {
                        transaction.show(mNewsFragment);
                    }
                }
                transaction.commit();
                break;

            case R.id.tab_buy:
                transaction = getSupportFragmentManager().beginTransaction();
                hideAllFragment(transaction);
                if (!MyApplication.islogin) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    return;
                } else {
                    selected();
                    mBuyTextView.setSelected(true);
                    if (dailiFragment == null) {
                        dailiFragment = new ProxyInfoFragment();
                        transaction.add(R.id.fragment_container, dailiFragment);
                    } else {
                        transaction.show(dailiFragment);
                    }
                }
                transaction.commit();
                break;

            case R.id.tab_me:
                transaction = getSupportFragmentManager().beginTransaction();
                hideAllFragment(transaction);
                if (!MyApplication.islogin) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    return;
                } else {
                    selected();
                    mMeTextView.setSelected(true);
                    if (mMeFragment == null) {
                        mMeFragment = new MeFragment();
                        transaction.add(R.id.fragment_container, mMeFragment);
                    } else {
                        transaction.show(mMeFragment);
                    }
                }
                transaction.commit();
                break;
            case R.id.cancel_btn:
                if(mDialog!=null)
                    mDialog.dismiss();
                break;
            case R.id.getcode_text:
                //电话号码不为空 并且合法
                if (judgePhone()) {
                    //手机号没有被注册过时 执行下面的操作
                    sendPhone = mPhoneEdit.getText().toString().trim();
                    sendCodePresenter.BindPhone();
                }
                break;
            case R.id.bind_btn:
                if (judgePhone()) {
                    bindPresenter.BindPhone();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        flag=mSharedPreferences.getInt("flag",0);
        isForeground=true;
        MobclickAgent.onResume(this);

        if(MyApplication.islogin) {
            NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, true);
            registerSystemMessageObservers(true);

            int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();
            queryUnreadCount(unreadNum);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, false);
        registerSystemMessageObservers(false);
    }

    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                // 清理缓存&注销监听&清除状态
                LogoutHelper.logout();
                MyApplication.islogin = false;
                MyApplication.TOKEN = "";
                MyApplication.userId="";
                MyApplication.username="";
                MyApplication.mSharedPreferences.edit().clear().commit();
                LoginActivity.start(MainActivity.this, true);
                finish();
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
        MobclickAgent.onPause(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (SystemClock.uptimeMillis() - clickTime <= 1500) {
                // 如果两次的时间差＜1s，就不执行操作

            } else {
                // 当前系统时间的毫秒值
                clickTime = SystemClock.uptimeMillis();
                ToastUtil.showToast(this, "再次点击退出", true);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private StringView fabuView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if (TextUtils.isEmpty(result)) {
                ToastUtil.showToast(MainActivity.this, "账号正在审核中，审核后可发布产品", true);
                return;
            }
            Intent intent = new Intent(MainActivity.this, PublishProductActivity.class);
            intent.putExtra("set_type", "add");
            startActivity(intent);

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
                LogUtils.e(jsonObject.toString());
            } catch (JSONException e) {
            }
            return jsonObject.toString();
        }

        @Override
        public void showToast(String msg) {

        }
    };
    /**
     * 注册/注销系统消息未读数变化
     *
     * @param register
     */
    private void registerSystemMessageObservers(boolean register) {
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, register);

    }

    //  创建观察者对象
    Observer<List<RecentContact>> messageObserver = (Observer<List<RecentContact>>) recentContacts -> {
        int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();
        queryUnreadCount(unreadNum);
    };

    private void queryUnreadCount(int unreadNum) {
        LogUtils.e("unreadNum=" + unreadNum);
        if (unreadNum > 0) {
            unreadLabel.setText(unreadNum > 99 ? "99" : String.valueOf(unreadNum));
            unreadLabel.setVisibility(View.VISIBLE);
            try {
                ShortcutBadger.applyCount(MainActivity.this, unreadNum > 99 ? 99 : unreadNum);
            } catch (Exception e) {
            }
        } else {
            try {
                ShortcutBadger.removeCount(MainActivity.this);
            } catch (Exception e) {
            }
            unreadLabel.setVisibility(View.GONE);
        }
    }

    private boolean judgePhone() {
        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mPhoneEdit.getText().toString().trim())) {
            ToastUtil.showToast(this, "手机号不能为空", true);
            return false;
        } else if (mPhoneEdit.getText().toString().matches(telRegex)) {
            return mPhoneEdit.getText().toString().matches(telRegex);
        } else {
            ToastUtil.showToast(this, "手机号输入有误，请重新输入", true);
            return mPhoneEdit.getText().toString().matches(telRegex);
        }
    }

    private boolean judgeTrue() {
        boolean flog = false;
        if (mPhoneEdit.getText().toString().equals(sendPhone)) {
            flog = true;
        } else {
            ToastUtil.showToast(this, "手机号不一致", true);
        }
        return flog;
    }

    private boolean judgeCode() {
        boolean flog = false;
        //验证码为空
        if (!TextUtils.isEmpty(mCodeEdit.getText())) {
            if (mCodeEdit.getText().toString().trim().equals(codeString)) {
                Log.e("验证码信息", "用户输入" + mCodeEdit.getText().toString() + ";真实" + codeString);
                flog = true;
            } else {
                ToastUtil.showToast(this, "验证码输入有误", true);
            }
        } else {
            ToastUtil.showToast(this, "验证码不能为空", true);
        }
        return flog;
    }

    private StringView codeView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(TextUtils.isEmpty(result))return;

            ToastUtil.showToast(MainActivity.this,"验证码发送成功",true);
            codeString = result;
            new MyCountTimer(mGetcodeText).start();
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
                jsonObject.put("mobile",mPhoneEdit.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }

        @Override
        public void showToast(String msg) {

        }
    };


    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.qgyyzs.globalcosmetics.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public String getJsonString() {
        JSONObject  jsonObject=new JSONObject();
        try {
            jsonObject.put("ver", SystemUtil.getVersion());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showStringResult(String result) {
        if(TextUtils.isEmpty(result))return;

        if (MyApplication.isUpdate==1) {
            mRedImg.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                    && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        getString(R.string.permission_download),
                        1);
            }else {
                UpdateAppUtils.from(this)
                        .serverVersionCode(SystemUtil.getVersionCode(this)+1)
                        .serverVersionName(MyApplication.server_version)
                        .apkPath(MyApplication.server_apkurl)
                        .updateInfo(MyApplication.server_apktitle)
                        .isForce(MyApplication.ForceUpdate==1?true:false)
                        .update();
            }
        }else {
            mRedImg.setVisibility(View.GONE);
        }
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    LogUtils.e("push="+showMsg.toString());
                }
            } catch (Exception e){
            }
        }
    }

    private StringView freshView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if (!TextUtils.isEmpty(result)) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage(result)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                setResult(RESULT_OK);//确定按钮事件
                            }
                        })
                        .show();
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
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("userid", MyApplication.userId);
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
