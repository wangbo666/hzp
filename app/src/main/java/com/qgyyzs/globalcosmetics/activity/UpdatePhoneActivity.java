package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.UpdateUserPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class UpdatePhoneActivity extends BaseActivity implements StringView{
    private UpdateUserPresenter updateUserPresenter=new UpdateUserPresenter(this,this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user_phone_edit)
    EditText mUserPhoneEdit;
    @BindView(R.id.isopen_img)
    ImageView mIsopenImg;
    @BindView(R.id.ok_btn)
    Button mOkBtn;
    @BindView(R.id.open_tv)
    TextView mOpenTv;

    private boolean isdali;
    boolean flog = false;
    private String userid, linktel;
    private boolean isopen;

    private SharedPreferences mSharedPreferences;

    @Override
    protected int getLayout() {
        return R.layout.activity_update_phone;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        userid = mSharedPreferences.getString("userid","");
        Intent intent = getIntent();
        linktel = intent.getStringExtra("linktel");
        isdali=intent.getBooleanExtra("isdailishang",false);
        isopen = intent.getBooleanExtra("isopen", false);
        if(!TextUtils.isEmpty(linktel)) {
            mUserPhoneEdit.setText(linktel);
            mUserPhoneEdit.setSelection(linktel.length());
        }
        if (isopen) {
            mIsopenImg.setBackgroundResource(R.mipmap.open_icon);
            flog = true;
            mOpenTv.setText("公开");
        }
        if(isdali){
            mUserPhoneEdit.setFocusable(false);
            mUserPhoneEdit.setFocusableInTouchMode(false);
        }else{
            mUserPhoneEdit.setFocusableInTouchMode(true);
            mUserPhoneEdit.setFocusable(true);
        }
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
    }

    @OnClick({R.id.isopen_img, R.id.ok_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.isopen_img:
                if (flog) {
                    mIsopenImg.setBackgroundResource(R.mipmap.close_icon);
                    flog = false;
                    mOpenTv.setText("不公开");
                } else {
                    mIsopenImg.setBackgroundResource(R.mipmap.open_icon);
                    flog = true;
                    mOpenTv.setText("公开");
                }
                break;
            case R.id.ok_btn:
                if (!TextUtils.isEmpty(mUserPhoneEdit.getText().toString().trim())) {
                    updateUserPresenter.updateUser();
                } else {
                    ToastUtil.showToast(this, "手机号不能为空", true);
                }
                break;
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
            jsonObject.put("LinkTel", mUserPhoneEdit.getText().toString());
            jsonObject.put("id",userid);
            jsonObject.put("IsOpen", mOpenTv.getText().toString().equals("不公开")?false:true);
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

        Intent intent = new Intent();
        intent.putExtra("linkTel", mUserPhoneEdit.getText().toString());
        intent.putExtra("IsOpen", mOpenTv.getText().toString().equals("不公开")?false:true);
        setResult(RESULT_OK, intent);
        finish();
        ToastUtil.showToast(this,result,true);
    }
}
