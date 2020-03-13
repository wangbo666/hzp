package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.MainActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.RegisterBean;
import com.qgyyzs.globalcosmetics.bean.UserDetialBean;
import com.qgyyzs.globalcosmetics.customview.ClearEditText;
import com.qgyyzs.globalcosmetics.customview.MyCountTimer;
import com.qgyyzs.globalcosmetics.customview.WaitDialog;
import com.qgyyzs.globalcosmetics.http.retrofit.RetrofitUtils;
import com.qgyyzs.globalcosmetics.mvp.iface.RegisterView;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.iface.UserDetialView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.LoginPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.RegisterCodePresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.RegisterPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.RegisterPresenter2;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ShowRegisterPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.SystemUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Set;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

public class RegisterActivity extends BaseActivity implements View.OnClickListener ,UserDetialView ,RegisterView{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.getcode_text)
    TextView mgetCodeTextView;
    @BindView(R.id.code_edit)
    ClearEditText mCodeEditText;
    @BindView(R.id.xieyi_tv)
    TextView mTextView;
    @BindView(R.id.my_place_tv)
    TextView mTvPlace;
    @BindView(R.id.my_place_rl)
    RelativeLayout rlPlace;
    @BindView(R.id.register_btn)
    Button mRegisterButton;
    @BindView(R.id.changjia_checkbox)
    CheckBox mCbChangshang;
    @BindView(R.id.daili_checkbox)
    CheckBox mCbDaili;
    @BindView(R.id.my_checkbox)
    CheckBox mCheckBox;
    @BindView(R.id.phone_edit)
    ClearEditText mPhoneEditText;
    @BindView(R.id.pwd_edit)
    ClearEditText mPwdEditText;
    @BindView(R.id.pwd2_edit)
    ClearEditText mPwd2EditText;
    @BindView(R.id.realname_edit)
    ClearEditText mRealNameEditText;
    @BindView(R.id.company_edit)
    ClearEditText mCompanyEditText;
    @BindView(R.id.ivShow)
    ImageView mIvShow;
    @BindView(R.id.mLinearShow)
    LinearLayout mLinearShow;
    @BindView(R.id.mLinear_tjcode)
    LinearLayout mLinearTjCode;
    @BindView(R.id.tjcode_edit)
    ClearEditText mTjcodeEdit;
    @BindView(R.id.mLinearNick)
    LinearLayout mLinearNick;
    @BindView(R.id.mLinearName)
    LinearLayout mLinearName;
    @BindView(R.id.mLinearPwd)
    LinearLayout mLinearPwd;
    @BindView(R.id.mLinearPwd2)
    LinearLayout mLinearPwd2;
    @BindView(R.id.mLinearCompany)
    LinearLayout mLinearCompany;
    @BindView(R.id.mLinearType)
    LinearLayout mLinearType;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.view5)
    View view5;

    private String codeString="";
    private String sendPhone;

    private SharedPreferences mSharedPreferences,mSharedPreferences1;
    private SharedPreferences.Editor mEditor,mEditor1;

    private WaitDialog mWaitDialog;
    private RegisterPresenter mRegisterPresenter;
    private RegisterPresenter2 mRegisterPresenter2;
    private RegisterCodePresenter codePresenter;
    private LoginPresenter mLoginPresenter=new LoginPresenter(this,this);
    private ShowRegisterPresenter showRegisterPresenter=new ShowRegisterPresenter(this,this);

    private int isShowTj=0;
    private String showRegister="0";

    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        mCheckBox.setChecked(true);
        mWaitDialog=new WaitDialog(this);
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, MODE_PRIVATE);
        mSharedPreferences1=getSharedPreferences(MyApplication.CONSTACTDATA,MODE_PRIVATE);
        mEditor=mSharedPreferences.edit();
        mEditor1=mSharedPreferences1.edit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences mSharedPreferences=getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        String resplace = mSharedPreferences.getString("ResPlace", "请选择");
        if (TextUtils.isEmpty(resplace) || resplace.equals("null")) {
            mTvPlace.setText("请选择");
        } else {
            mTvPlace.setText(resplace);
        }
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
            jsonObject.put("logintype","yzmlogin");
            jsonObject.put("yzm", mCodeEditText.getText().toString());
            jsonObject.put("loginmobileguid", SystemUtil.getIMEI(this));
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

        if(showRegister.equals("0")) {
            if (mCbDaili.isChecked()) {
                Intent intent = new Intent(this, RegisterSettingActivity.class);
                mEditor = mSharedPreferences.edit();
                mEditor.putString("dailiArea", mTvPlace.getText().toString());
                mEditor.putString("mobile", bean.getJsonData().getMobile());
                mEditor.commit();
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }else {
            startActivity(new Intent(this, UpdatePwActivity.class));
        }

        if(LoginActivity.instance!=null)LoginActivity.instance.finish();
        finish();
        ToastUtil.showToast(this,"登陆成功",true);

        Set<String> tagSet = new LinkedHashSet<String>();
        if(!TextUtils.isEmpty(bean.getJsonData().getDaili_sort()))tagSet.add(bean.getJsonData().getDaili_sort());
        if(!TextUtils.isEmpty(bean.getJsonData().getProvince()))tagSet.add(bean.getJsonData().getProvince());
        if(!TextUtils.isEmpty(bean.getJsonData().getCity())) tagSet.add(bean.getJsonData().getCity());
        tagSet.add("Flag"+bean.getJsonData().getFlag());
        tagSet.add("Android");

        String alias="qgyyzs_"+ bean.getJsonData().getAccountName();

        JPushInterface.setTags(MyApplication.getInstance().getContext(), 1, tagSet);
        JPushInterface.setAlias(MyApplication.getInstance().getContext(), 1, alias);
    }

    @Override
    public void initData(){
        mRegisterPresenter=new RegisterPresenter(registerView,this);
        mRegisterPresenter2=new RegisterPresenter2(registerView,this);
        codePresenter=new RegisterCodePresenter(codeView,this);
        showRegisterPresenter.showRegister();
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mgetCodeTextView.setOnClickListener(this);
        mRegisterButton.setOnClickListener(this);
        mTextView.setOnClickListener(this);
        rlPlace.setOnClickListener(this);
        mIvShow.setOnClickListener(this);
        mPwdEditText.addTextChangedListener(new TextWatcher() {
            String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!mPwdEditText.getText().toString().matches(regex)) {
                    mPwdEditText.setError("密码必须是8到16位且为数字字母组合");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mPwdEditText.getText().toString().matches(regex)) {
                    mPwdEditText.setError("密码必须是8到16位且为数字字母组合");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mPwdEditText.getText().toString().matches(regex)) {
                    mPwdEditText.setError("密码必须是8到16位且为数字字母组合");
                }
            }
        });
        mPhoneEditText.addTextChangedListener(new TextWatcher() {
            String telRegex = "[1][34578]\\d{9}";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if(!mPhoneEditText.getText().toString().matches(telRegex)){
//                    mPhoneEditText.setError("请输入正确的手机号码");
//                }
            }
        });
        mPwd2EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!mPwdEditText.getText().toString().equals(mPwd2EditText.getText().toString())){
                    mPwd2EditText.setError("两次密码不一致");
                }
            }
        });
    }

    private String getDaili_sort(){
        String daili_sort;
        if(mCbDaili.isChecked()&&mCbChangshang.isChecked()){
            daili_sort = mCbDaili.getText().toString() + "," + mCbChangshang.getText().toString();
        }else if(mCbDaili.isChecked()&&!mCbChangshang.isChecked()){
            daili_sort = mCbDaili.getText().toString();
        }else if(mCbChangshang.isChecked()&&!mCbDaili.isChecked()){
            daili_sort =  mCbChangshang.getText().toString();
        }else{
            daili_sort="";
        }
        return daili_sort;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ivShow:
                if(isShowTj==0) {
                    mLinearTjCode.setVisibility(View.VISIBLE);
                    isShowTj = 1;
                }else {
                    mLinearTjCode.setVisibility(View.INVISIBLE);
                    isShowTj = 0;
                }
                break;
            case R.id.getcode_text:
                if (judgePhone()) {
                    sendPhone = mPhoneEditText.getText().toString().trim();
                    codePresenter.getCode();
                }
                break;
            case R.id.register_btn:
                LogUtils.e("sort"+getDaili_sort());
                if (judgePhone()&& judgeCheckBox()) {
                    if(TextUtils.isEmpty(mCodeEditText.getText().toString())){
                        ToastUtil.showToast(this,"验证码不能为空",true);
                        return;
                    }
                    if(showRegister.equals("0")) {
                        if(judgePwd()&&judgePwdtrue()) {
                            if(TextUtils.isEmpty(getDaili_sort())){
                                ToastUtil.showToast(this,"请选择一个身份",true);
                                return;
                            }
                            mRegisterPresenter.Register();
                        }
                    }else {
                        mRegisterPresenter2.Register();
                    }
                }
                break;
            case R.id.xieyi_tv:
                intent=new Intent();
                intent.putExtra("title","环球化妆品网使用协议");
                intent.putExtra("url", RetrofitUtils.BASE_API+"Home/Agreement");
                intent.setClass(this,WebBaseActivity.class);
                startActivity(intent);
                break;
            case R.id.my_place_rl:
                intent = new Intent(this, ProvinceSelectActivity.class);
                intent.putExtra("proxy", "5");
                if (mTvPlace.getText().toString().equals("未设置")) {
                    intent.putExtra("province", "");
                    intent.putExtra("city", "");
                } else {
                    String[] str = mTvPlace.getText().toString().split(",");
                    if (str.length == 2) {
                        intent.putExtra("province", str[0]);
                        intent.putExtra("city", str[1]);
                    } else {
                        intent.putExtra("province", str[0]);
                        intent.putExtra("city", "");
                    }
                }
                startActivity(intent);
                break;
        }
    }

    private boolean isCbCheck(){
        boolean flog = false;
        if (!TextUtils.isEmpty(getDaili_sort())) {
            flog = true;
        } else {
            ToastUtil.showToast(this, "请选择一个身份类型", true);
        }
        return flog;
    }

    public boolean judgePhone() {
        String telRegex = "[1][345789]\\d{9}";
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

    private boolean judgePwdtrue() {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        boolean flog = false;
        if (!TextUtils.isEmpty(mPwdEditText.getText().toString().trim())) {
            if (mPwdEditText.getText().toString().matches(regex)) {
                flog = true;
            } else {
                ToastUtil.showToast(this, "密码必须是8到16位且为数字字母组合", true);
            }
        } else {
            ToastUtil.showToast(this, "密码不能为空", true);
        }
        return flog;
    }


    private boolean judgePwd() {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        boolean flog = false;
        if (!TextUtils.isEmpty(mPwdEditText.getText().toString().trim())) {
            if (!TextUtils.isEmpty(mPwd2EditText.getText().toString().trim())) {
                //
                if (mPwd2EditText.getText().toString().trim().equals(mPwdEditText.getText().toString().trim())) {
                    //密码一致
                    if (mPwdEditText.getText().toString().matches(regex)) {
                        flog = true;
                    } else {
                        ToastUtil.showToast(this, "密码必须是8到16位且为数字字母组合", true);
                    }

                } else {
                    ToastUtil.showToast(this, "两次密码不一致", true);
                }
            } else {
                ToastUtil.showToast(this, "确认密码不能为空", true);
            }
        } else {
            ToastUtil.showToast(this, "密码不能为空", true);
        }
        return flog;
    }

    private boolean judgeCode() {
        boolean flog = false;
        //验证码为空
        if (!TextUtils.isEmpty(mCodeEditText.getText())) {
            if (mCodeEditText.getText().toString().trim().equals(codeString)) {
                Log.e("验证码信息", "用户输入" + mCodeEditText.getText().toString() + ";真实" + codeString);
                flog = true;
            } else {
                ToastUtil.showToast(this, "验证码输入有误", true);
            }
        } else {
            ToastUtil.showToast(this, "验证码不能为空", true);
        }
        return flog;
    }

    private boolean judgeNickName() {
        boolean flog = false;
        //验证码为空
        if (!TextUtils.isEmpty(mRealNameEditText.getText())) {
            flog = true;
        } else {
            ToastUtil.showToast(this, "姓名不能为空", true);
        }
        return flog;
    }
    private boolean judgeCompany() {
        boolean flog = false;
        //验证码为空
        if (!TextUtils.isEmpty(mCompanyEditText.getText())) {
            flog = true;
        } else {
            ToastUtil.showToast(this, "所在公司不能为空", true);
        }
        return flog;
    }

    private boolean judgeCheckBox() {
        boolean flog = false;
        //验证码为空
        //复选框没有选
        if (mCheckBox.isChecked()) {
            flog = true;
        } else {
            ToastUtil.showToast(this, "请同意协议", true);
        }
        return flog;
    }
    /* 注册 同步方法*/
    private boolean judgeTrue() {
        boolean flog = false;
        if (mPhoneEditText.getText().toString().equals(sendPhone)) {
            flog = true;
        } else {
            ToastUtil.showToast(this, "手机号不一致", true);
        }
        return flog;
    }

    private StringView registerView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(result==null)return;
            ToastUtil.showToast(RegisterActivity.this, "注册成功，正在自动登录", true);
            mLoginPresenter.Login();
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
                jsonObject.put("yzm",mCodeEditText.getText().toString());
                jsonObject.put("accountpw", mPwdEditText.getText().toString());
                if(!TextUtils.isEmpty(mRealNameEditText.getText().toString().trim()))jsonObject.put("realname", mRealNameEditText.getText().toString().trim());
                jsonObject.put("loginmobileguid", SystemUtil.getIMEI(RegisterActivity.this));
                if(!TextUtils.isEmpty(getDaili_sort()))jsonObject.put("daili_sort", getDaili_sort());
                jsonObject.put("tjcode",mTjcodeEdit.getText().toString());
                if(!mTvPlace.getText().toString().equals("请选择"))jsonObject.put("ResPlace",mTvPlace.getText().toString());
                if(!TextUtils.isEmpty(mCompanyEditText.getText().toString().trim()))jsonObject.put("Company", mCompanyEditText.getText().toString().trim());
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

    private StringView codeView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(result==null)return;
            codeString = result;
            new MyCountTimer(mgetCodeTextView).start();
            ToastUtil.showToast(RegisterActivity.this,"验证码发送成功",true);
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
    public void showRegisterView(RegisterBean bean) {
        if(bean==null)return;
        mLinearShow.setVisibility(bean.getJsonData().getTjcode().equals("1")?View.VISIBLE:View.GONE);
        showRegister=bean.getJsonData().getRegister();
        mLinearNick.setVisibility(showRegister.equals("0")?View.VISIBLE:View.GONE);
        mLinearName.setVisibility(showRegister.equals("0")?View.VISIBLE:View.GONE);
        mLinearPwd.setVisibility(showRegister.equals("0")?View.VISIBLE:View.GONE);
        mLinearPwd2.setVisibility(showRegister.equals("0")?View.VISIBLE:View.GONE);
        mLinearCompany.setVisibility(showRegister.equals("0")?View.VISIBLE:View.GONE);
        mLinearType.setVisibility(showRegister.equals("0")?View.VISIBLE:View.GONE);
        view1.setVisibility(showRegister.equals("0")?View.VISIBLE:View.GONE);
        view2.setVisibility(showRegister.equals("0")?View.VISIBLE:View.GONE);
        view3.setVisibility(showRegister.equals("0")?View.VISIBLE:View.GONE);
        view4.setVisibility(showRegister.equals("0")?View.VISIBLE:View.GONE);
        view5.setVisibility(showRegister.equals("0")?View.VISIBLE:View.GONE);
    }
}
