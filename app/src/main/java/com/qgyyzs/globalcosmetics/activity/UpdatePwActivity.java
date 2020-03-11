package com.qgyyzs.globalcosmetics.activity;

import android.content.Intent;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.MainActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.customview.ClearEditText;
import com.qgyyzs.globalcosmetics.customview.WaitDialog;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.UpdatePwPresenter;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/20 0020.
 */

public class UpdatePwActivity extends BaseActivity implements StringView{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.pwd_edit)
    ClearEditText mEditPwd;
    @BindView(R.id.pwd2_edit)
    ClearEditText mEditPwd2;
    @BindView(R.id.resetpwd_btn)
    Button mBtnOk;

    private WaitDialog waitDialog;

    private UpdatePwPresenter updatePwPresenter=new UpdatePwPresenter(this,this);

    @Override
    protected int getLayout() {
        return R.layout.activity_update_pw;
    }

    @Override
    protected void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
    }

    @Override
    protected void initData() {
        waitDialog=new WaitDialog(this);
    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdatePwActivity.this, MainActivity.class));
            }
        });
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judgePwd()){
                    updatePwPresenter.updatePw();
                }
            }
        });
    }

    @Override
    public void showLoading() {
        if(waitDialog!=null&&!waitDialog.isShowing())
            waitDialog.show();
    }

    @Override
    public void closeLoading() {
        if(waitDialog!=null&&waitDialog.isShowing())
            waitDialog.dismiss();
    }

    @Override
    public String getJsonString() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("newpw",mEditPwd.getText().toString());
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
        ToastUtil.showToast(this,"密码设置成功",true);
        startActivity(new Intent(this,MainActivity.class));
    }

    private boolean judgePwd() {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        boolean flog = false;
        if (!TextUtils.isEmpty(mEditPwd.getText().toString().trim())) {
            if (!TextUtils.isEmpty(mEditPwd2.getText().toString().trim())) {
                //
                if (mEditPwd2.getText().toString().trim().equals(mEditPwd.getText().toString().trim())) {
                    //密码一致
                    if (mEditPwd.getText().toString().matches(regex)) {
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
}
