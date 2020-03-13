package com.qgyyzs.globalcosmetics.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.ClientType;
import com.qgyyzs.globalcosmetics.MainActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.UserDetialBean;
import com.qgyyzs.globalcosmetics.customview.ClearEditText;
import com.qgyyzs.globalcosmetics.customview.MyCountTimer;
import com.qgyyzs.globalcosmetics.customview.WaitDialog;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.iface.UserDetialView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.LoginCodePresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.LoginPresenter;
import com.qgyyzs.globalcosmetics.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Set;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends BaseActivity implements View.OnClickListener,UserDetialView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_right)
    TextView mRegisterTextView;
    @BindView(R.id.login_phone_edit)
    ClearEditText mPhoneEditText;
    @BindView(R.id.login_pwd_edit)
    ClearEditText mPwdEditText;
    @BindView(R.id.login_btn)
    Button mLoginButton;
    @BindView(R.id.help_text)
    TextView mHelpTextView;
    @BindView(R.id.forget_text)
    TextView mForgetTextView;
    @BindView(R.id.getcode_text)
    TextView mTvGetCode;
    @BindView(R.id.mTvLoginType)
    TextView mTvLoginType;
    @BindView(R.id.login_code_edit)
    ClearEditText mEditCode;

    private int LoginType=1;

    public static LoginActivity instance;
    private String username;

    private SharedPreferences mSharedPreferences,mSharedPreferences1;
    private SharedPreferences.Editor mEditor,mEditor1;

    private WaitDialog mWaitDialog;
    private LoginPresenter loginPresenter=new LoginPresenter(this,this);

    private LoginCodePresenter codePresenter;

    private static final String KICK_OUT = "KICK_OUT";

    public static void start(Context context) {
        start(context, false);
    }

    public static void start(Context context, boolean kickOut) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(KICK_OUT, kickOut);
        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void showLoading() {
        if (mWaitDialog != null && !mWaitDialog.isShowing())
            mWaitDialog.show();
    }

    @Override
    public void closeLoading() {
        if (mWaitDialog != null && mWaitDialog.isShowing())
            mWaitDialog.dismiss();
    }

    @Override
    public String getJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accountname", mPhoneEditText.getText().toString());
            jsonObject.put("logintype",LoginType==1?"pwlogin":"yzmlogin");
            if(LoginType==1) {
                jsonObject.put("accountpw", mPwdEditText.getText().toString());
            }else{
                jsonObject.put("yzm",mEditCode.getText().toString());
            }
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
    public void showUserResult(UserDetialBean bean) {
        if(null==bean)return;

        mEditor.putString("userid", bean.getJsonData().getId()+"");
        mEditor.putString("username", bean.getJsonData().getPcUsername());
        mEditor.putString("accountname",bean.getJsonData().getAccountName());
        mEditor.putString("HeadImg", bean.getJsonData().getHeadImg());
        mEditor.putString("RealName", bean.getJsonData().getRealName());
        mEditor.putString("linkTel",bean.getJsonData().getLinkTel());
        mEditor.putString("mobile",bean.getJsonData().getMobile());
        mEditor.putBoolean("IsOpen", bean.getJsonData().getIsOpen());
        mEditor.putString("ResPlace", bean.getJsonData().getResPlace());
        mEditor.putString("Company", bean.getJsonData().getCompany());
        mEditor.putString("JieShao", bean.getJsonData().getJieShao());
        mEditor.putString("token",bean.getJsonData().getToken());
        mEditor.putBoolean("islogin",true);
        mEditor.putString("nimid",bean.getJsonData().getNimID());
        mEditor.putBoolean("IsPrimary",bean.getJsonData().getIsPrimary());
        mEditor.putInt("isdaili",bean.getJsonData().getIsDaiLi());
        mEditor.putInt("flag",bean.getJsonData().getFlag());
        mEditor.putString("daili_sort",bean.getJsonData().getDaili_sort());
        mEditor.commit();

        mEditor1.putString("accountname",bean.getJsonData().getAccountName());
        mEditor1.commit();

        MyApplication.TOKEN=bean.getJsonData().getToken();
        MyApplication.islogin=true;
        MyApplication.userId=bean.getJsonData().getId()+"";
        MyApplication.username=bean.getJsonData().getPcUsername();

//        if(bean.getJsonData().getIsDaiLi()==1) {
        Intent intent=new Intent(this, MainActivity.class );
//            intent.putExtra("bindphone","bindphone");
//            intent.putExtra("mobile",bean.getJsonData().getMobile());
        startActivity(intent);
//        }else{
//            Intent intent=new Intent(this,ProxySettingActivity.class);
//            intent.putExtra("login","firstlogin");
//            intent.putExtra("mobile",bean.getJsonData().getMobile());
//            startActivity(intent);
//        }
        finish();
        ToastUtil.showToast(this,"登陆成功",true);

        Set<String> tagSet = new LinkedHashSet<String>();
        if(!TextUtils.isEmpty(bean.getJsonData().getDaili_sort()))tagSet.add(bean.getJsonData().getDaili_sort());
        if(!TextUtils.isEmpty(bean.getJsonData().getProvince()))tagSet.add(bean.getJsonData().getProvince());
        if(!TextUtils.isEmpty(bean.getJsonData().getCity())) tagSet.add(bean.getJsonData().getCity());
        tagSet.add("Flag"+bean.getJsonData().getFlag());
        tagSet.add("Android");

        String alias="hzpzs_"+ bean.getJsonData().getAccountName();

        JPushInterface.setTags(MyApplication.getInstance().getContext(), 1, tagSet);
        JPushInterface.setAlias(MyApplication.getInstance().getContext(), 1, alias);
    }

    private void onParseIntent() {
        if (getIntent().getBooleanExtra(KICK_OUT, false)) {
            int type = NIMClient.getService(AuthService.class).getKickedClientType();
            String client;
            switch (type) {
                case ClientType.Web:
                    client = "网页端";
                    break;
                case ClientType.Windows:
                    client = "电脑端";
                    break;
                case ClientType.REST:
                    client = "服务端";
                    break;
                default:
                    client = "移动端";
                    break;
            }
            EasyAlertDialogHelper.showOneButtonDiolag(LoginActivity.this, getString(R.string.kickout_notify),
                    String.format(getString(R.string.kickout_content), client), getString(R.string.ok), true, null);
        }
    }

    @Override
    public void initData() {
        codePresenter=new LoginCodePresenter(codeView,this);
        onParseIntent();
        mPhoneEditText.setText(username);
        if (!TextUtils.isEmpty(username)) {
            mPhoneEditText.setSelection(username.length());
        }
    }

    private StringView codeView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(result==null)return;
            new MyCountTimer(mTvGetCode).start();
            ToastUtil.showToast(LoginActivity.this,"验证码发送成功",true);
        }

        @Override
        public void showLoading() {
            if(mWaitDialog!=null&&!mWaitDialog.isShowing())
                mWaitDialog.show();
        }

        @Override
        public void closeLoading() {
            if(mWaitDialog!=null&&mWaitDialog.isShowing())
                mWaitDialog.dismiss();
        }

        @Override
        public String getJsonString() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("mobile", mPhoneEditText.getText().toString().trim());
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
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        instance = this;
        mWaitDialog = new WaitDialog(this);

        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, MODE_PRIVATE);
        mSharedPreferences1=getSharedPreferences(MyApplication.CONSTACTDATA,MODE_PRIVATE);
        mEditor=mSharedPreferences.edit();
        mEditor1=mSharedPreferences1.edit();
        username = mSharedPreferences1.getString("accountname","");


        JPushInterface.setTags(MyApplication.getInstance().getContext(), 1, null);
        JPushInterface.setAlias(MyApplication.getInstance().getContext(), 1, null);
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mLoginButton.setOnClickListener(this);
        mForgetTextView.setOnClickListener(this);
        mRegisterTextView.setOnClickListener(this);
        mHelpTextView.setOnClickListener(this);
        mTvLoginType.setOnClickListener(this);
        mTvGetCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getcode_text:
                if(judgePhone())codePresenter.getCode();
                break;
            case R.id.mTvLoginType:
                if(LoginType==1){
                    LoginType=2;
                    mPwdEditText.setText("");
                    mEditCode.setText("");
                    mPwdEditText.setVisibility(View.GONE);
                    mTvGetCode.setVisibility(View.VISIBLE);
                    mEditCode.setVisibility(View.VISIBLE);
                    mTvLoginType.setText("密码登陆");
                }else {
                    LoginType=1;
                    mPwdEditText.setText("");
                    mEditCode.setText("");
                    mPwdEditText.setVisibility(View.VISIBLE);
                    mTvGetCode.setVisibility(View.GONE);
                    mEditCode.setVisibility(View.GONE);
                    mTvLoginType.setText("验证码登陆");
                }
                break;
            case R.id.login_btn:
                if (judgeAccount()) {
                    if(LoginType==1){
                        if(TextUtils.isEmpty(mPwdEditText.getText().toString().trim())){
                            ToastUtil.showToast(this,"请输入密码",true);
                            return;
                        }
                    }else{
                        if(TextUtils.isEmpty(mEditCode.getText().toString().trim())){
                            ToastUtil.showToast(this,"请输入验证码",true);
                            return;
                        }
                    }
                    loginPresenter.Login();
                }
                break;
            case R.id.forget_text:
                startActivity(new Intent(this, ForgetPwdActivity.class));
                break;
            case R.id.tv_right:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.help_text:
                new AlertDialog.Builder(this)
                        .setMessage("需要帮助："+"0571-85885866")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                setResult(RESULT_OK);//确定按钮事件
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                                        && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    requestPermission(Manifest.permission.CALL_PHONE,
                                            "是否获取打电话权限",
                                            1);
                                }else {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse("tel:" + "0571-85885866"));
                                    startActivity(intent);
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //取消按钮事件

                            }
                        })
                        .show();
                break;
        }
    }

    private boolean judgeAccount() {
        if (TextUtils.isEmpty(mPhoneEditText.getText().toString().trim())) {
            ToastUtil.showToast(this, "账号不能为空", true);
            return false;
        }
        return true;
    }

    public boolean judgePhone() {
        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mPhoneEditText.getText())) {
            ToastUtil.showToast(this, "手机号不能为空", true);
            return false;
        } else if (mPhoneEditText.getText().toString().matches(telRegex)) {
            return mPhoneEditText.getText().toString().matches(telRegex);
        } else {
            ToastUtil.showToast(this, "手机号输入有误，请重新输入", true);
            return mPhoneEditText.getText().toString().matches(telRegex);
        }
    }
}
