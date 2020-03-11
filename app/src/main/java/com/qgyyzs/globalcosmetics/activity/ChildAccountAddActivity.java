package com.qgyyzs.globalcosmetics.activity;

import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.customview.WaitDialog;
import com.qgyyzs.globalcosmetics.eventbus.AnyChild;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ChildAccountAddPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/12 0012.
 */

public class ChildAccountAddActivity extends BaseActivity implements StringView{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.login_phone_edit)
    EditText mEditPhone;
    @BindView(R.id.login_pwd_edit)
    EditText mEditPwd;
    @BindView(R.id.login_btn)
    Button mBtnAdd;

    private WaitDialog waitDialog;

    private ChildAccountAddPresenter addPresenter=new ChildAccountAddPresenter(this,this);

    @Override
    protected int getLayout() {
        return R.layout.activity_addaccount;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);

        waitDialog=new WaitDialog(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(mEditPhone.getText().toString())){
                    showToast("账号不能为空");
                    return;
                }
                if(TextUtils.isEmpty(mEditPwd.getText().toString())){
                    showToast("密码不能为空");
                    return;
                }
                addPresenter.addChild();
            }
        });
    }

    @Override
    public void showLoading() {
        waitDialog.show();
    }

    @Override
    public void closeLoading() {
        waitDialog.dismiss();
    }

    @Override
    public String getJsonString() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("accountname",mEditPhone.getText().toString());
            jsonObject.put("accountpw",mEditPwd.getText().toString());
            LogUtils.e(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToast(this,msg,true);
    }

    @Override
    public void showStringResult(String result) {
        if(null==result)return;

        EventBus.getDefault().post(new AnyChild());
        finish();
        showToast(result);
    }
}
