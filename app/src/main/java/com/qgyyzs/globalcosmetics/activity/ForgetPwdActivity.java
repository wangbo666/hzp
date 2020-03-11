package com.qgyyzs.globalcosmetics.activity;

import android.content.SharedPreferences;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.customview.ClearEditText;
import com.qgyyzs.globalcosmetics.customview.MyCountTimer;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ForgetPwdCodePresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ForgetPwdPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 忘记密码
 */
public class ForgetPwdActivity extends BaseActivity implements StringView{
    private ForgetPwdPresenter forgetPresenter;
    private ForgetPwdCodePresenter sendCodePresenter=new ForgetPwdCodePresenter(this,this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.phone_edit)
    ClearEditText mPhoneEdit;
    @BindView(R.id.pwd_edit)
    ClearEditText mPwdEdit;
    @BindView(R.id.pwd2_edit)
    ClearEditText mPwd2Edit;
    @BindView(R.id.code_edit)
    EditText mCodeEdit;
    @BindView(R.id.getcode_text)
    TextView mGetcodeText;
    @BindView(R.id.resetpwd_btn)
    Button mResetpwdBtn;

    private String codeString;
    private String sendPhone;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected int getLayout() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
    }

    @Override
    public void initData() {
        mSharedPreferences = getSharedPreferences(MyApplication.CONSTACTDATA, MODE_PRIVATE);
        forgetPresenter=new ForgetPwdPresenter(forgetpwdView,this);
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

    @OnClick({R.id.getcode_text, R.id.resetpwd_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getcode_text:
                //电话号码不为空 并且合法
                if (judgePhone()) {
                    //手机号没有被注册过时 执行下面的操作
                    sendPhone = mPhoneEdit.getText().toString().trim();
                    sendCodePresenter.getCode();
                }
                break;
            case R.id.resetpwd_btn:
                if (judgePhone() && judgePwd()) {
                    forgetPresenter.replacePwd();
                }
                break;
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

    private boolean judgePwd() {
//        String regex = "^{6,16}$";
        boolean flog = false;
        if (!TextUtils.isEmpty(mPwdEdit.getText())) {
            if (!TextUtils.isEmpty(mPwd2Edit.getText())) {
                //
                if (mPwd2Edit.getText().toString().equals(mPwdEdit.getText().toString())) {
                    //密码一致
                    if ((mPwdEdit.getText().toString().length() >= 8) && (mPwdEdit.getText().toString().length() <= 16)) {
                        flog = true;
                    } else {
                        ToastUtil.showToast(this, "密码必须是8到16位", true);
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

    private StringView forgetpwdView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if (TextUtils.isEmpty(result))return;

            ToastUtil.showToast(ForgetPwdActivity.this,result,true);
            editor=mSharedPreferences.edit();
            editor.putString("accountname",mPhoneEdit.getText().toString());
            editor.commit();
            ToastUtil.showToast(ForgetPwdActivity.this,"密码重置成功",true);
            finish();
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
                jsonObject.put("mobile", mPhoneEdit.getText().toString().trim());
                jsonObject.put("newpw", mPwdEdit.getText().toString().trim());
                jsonObject.put("yzm",mCodeEdit.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LogUtils.e(jsonObject.toString());
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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", mPhoneEdit.getText().toString().trim());
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
    public void showStringResult(String result) {
        if(TextUtils.isEmpty(result))return;

        codeString = result;
        new MyCountTimer(mGetcodeText).start();
        ToastUtil.showToast(this,"验证码发送成功",true);
    }
}
