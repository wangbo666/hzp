package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.MainActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.customview.ClearEditText;
import com.qgyyzs.globalcosmetics.customview.MyCountTimer;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.UpdatePwdCodePresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.UpdatePwdPresenter;
import com.qgyyzs.globalcosmetics.nim.login.LogoutHelper;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.qgyyzs.globalcosmetics.R.id.getcode_text;
import static com.qgyyzs.globalcosmetics.application.MyApplication.USERSPINFO;

public class UpdatePwdActivity extends BaseActivity implements StringView{
    private UpdatePwdPresenter updatePwdPresenter=new UpdatePwdPresenter(this,this);
    private UpdatePwdCodePresenter codePresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.oldpwd_edit)
    ClearEditText mOldpwdEdit;
    @BindView(R.id.newpwd_edit)
    ClearEditText mNewpwdEdit;
    @BindView(R.id.new2pwd_edit)
    ClearEditText mNew2pwdEdit;
    @BindView(R.id.ok_btn)
    Button mOkBtn;
    @BindView(R.id.code_edit)
    EditText mCodeEdit;
    @BindView(getcode_text)
    TextView mGetcodeText;
    String codeString;
    @BindView(R.id.textView6)
    TextView mTextView6;

    private String mobile,userid;
    private SharedPreferences mSharedPreferences;
    @Override
    protected int getLayout() {
        return R.layout.activity_update_pwd;
    }

    @Override
    public void initData() {
        mSharedPreferences = getSharedPreferences(USERSPINFO, Context.MODE_PRIVATE);
        mobile = mSharedPreferences.getString("mobile", "");
        userid=mSharedPreferences.getString("userid","");
        if (!TextUtils.isEmpty(mobile)&&mobile.length()>10) {
            mTextView6.setText("发送验证码给手机" + mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4, mobile.length()));
        }
        codePresenter=new UpdatePwdCodePresenter(codeView,this);
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
    }

    private boolean judgeoldPwd() {
        boolean flog = false;
        if (!TextUtils.isEmpty(mNewpwdEdit.getText())) {
            flog = true;
        } else {
            ToastUtil.showToast(this, "旧密码不能为空", true);
        }
        return flog;
    }

    private boolean judgePwd() {
        boolean flog = false;
        if (!TextUtils.isEmpty(mNewpwdEdit.getText().toString().trim())) {
            if (!TextUtils.isEmpty(mNew2pwdEdit.getText().toString().trim())) {
                //
                if (mNew2pwdEdit.getText().toString().trim().equals(mNewpwdEdit.getText().toString().trim())) {
                    //密码一致
                    if ((mNewpwdEdit.getText().toString().length() >= 8) && (mNewpwdEdit.getText().toString().length() <= 16)) {
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

    @OnClick({R.id.ok_btn, getcode_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ok_btn:
                if (judgePwd() && judgeoldPwd()) {
                    updatePwdPresenter.updatePwd();
                }
                break;
            case R.id.getcode_text:
                codePresenter.getCode();
                break;
        }
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

            ToastUtil.showToast(UpdatePwdActivity.this,"验证码已发送",true);
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
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("mobile", mobile);
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
            jsonObject.put("id", userid);
            jsonObject.put("oldpw", mOldpwdEdit.getText().toString());
            jsonObject.put("newpw", mNewpwdEdit.getText().toString());
            jsonObject.put("yzm",mCodeEdit.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.e( jsonObject.toString());
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showStringResult(String result) {
        if(TextUtils.isEmpty(result))return;

        ToastUtil.showToast(this,"密码修改成功",true);
        MyApplication.islogin=false;
        MyApplication.TOKEN="";
        MyApplication.userId="";
        MyApplication.username="";
        mSharedPreferences.edit().clear().commit();
        MainActivity.instance.finish();
        LogoutHelper.logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
