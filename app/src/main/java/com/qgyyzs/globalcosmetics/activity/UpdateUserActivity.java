package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.customview.WaitDialog;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.UpdateUserPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class UpdateUserActivity extends BaseActivity implements StringView{
    private UpdateUserPresenter updateUserPresenter=new UpdateUserPresenter(this,this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.info_edit)
    EditText mInfoEdit;
    @BindView(R.id.num_tv)
    TextView mNumTv;
    @BindView(R.id.text1)
    TextView mText1;
    @BindView(R.id.tishi_rl)
    RelativeLayout mTishiRl;
    @BindView(R.id.ok_btn)
    Button mOkBtn;

    private String title;
    private String hint;
    private CharSequence temp;

    private SharedPreferences mSharedPreferences;
    private String userid,titleKey;
    private WaitDialog waitDialog;

    @Override
    protected int getLayout() {
        return R.layout.activity_update_user;
    }

    @Override
    public void initData() {
        if (!title.equals("个人介绍")) {
            mTishiRl.setVisibility(View.GONE);
        } else {
            mTishiRl.setVisibility(View.VISIBLE);
            mInfoEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});  //其中100最大输入字数
            mInfoEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    temp = s;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    mNumTv.setText("" + (20 - temp.length()));
                    if (temp.length() >= 20) {
                        ToastUtil.showToast(UpdateUserActivity.this, "你输入的字数已经超过了限制！", true);
                    }
                }
            });
        }
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
        waitDialog=new WaitDialog(this);
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        userid=mSharedPreferences.getString("userid","");
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        hint = intent.getStringExtra("hint");
        toolbar.setTitle(title);
        if (!TextUtils.isEmpty(hint)) {
            mInfoEdit.setText(hint);
            mInfoEdit.setSelection(hint.length());
            mNumTv.setText("" + (20 - hint.length()));
        }
    }

    @OnClick({R.id.ok_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ok_btn:
                switch (title) {
                    case "真实姓名":
                        if(mInfoEdit.getText().toString().length()>0) {
                            sendRequest("RealName");
                        }else{
                            ToastUtil.showToast(this,"名称不能为空",true);
                        }
                        break;
                    case "所在公司":
                        sendRequest("Company");
                        break;
                    case "个人介绍":
                        sendRequest("JieShao");
                        break;
                }
                break;
        }
    }

    private void sendRequest(String title) {
        titleKey=title;
        updateUserPresenter.updateUser();
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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(titleKey, mInfoEdit.getText().toString());
            jsonObject.put("id",userid);
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
        intent.putExtra(titleKey, mInfoEdit.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
        ToastUtil.showToast(this,result,true);
    }
}
