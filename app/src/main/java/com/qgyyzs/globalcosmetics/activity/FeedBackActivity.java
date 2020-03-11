package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.FeedBackPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class FeedBackActivity extends BaseActivity implements StringView{
    private FeedBackPresenter presenter=new FeedBackPresenter(this,this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.suggest_edit)
    EditText mSuggestEdit;
    @BindView(R.id.ok_btn)
    Button mOkBtn;

    private SharedPreferences mSharedPreferences;
    private String userid;

    @Override
    protected int getLayout() {
        return R.layout.activity_suggest;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        userid = mSharedPreferences.getString("userid", "");
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

    @OnClick({R.id.ok_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ok_btn:
                if (!TextUtils.isEmpty(mSuggestEdit.getText().toString().trim())) {
                    presenter.feedBack();
                } else {
                    ToastUtil.showToast(this, "请输入反馈内容",true);
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
            jsonObject.put("userid", userid);
            jsonObject.put("content", mSuggestEdit.getText().toString().trim());
            LogUtils.e("反馈参数"+jsonObject.toString());
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

        ToastUtil.showToast(this,result,true);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        finish();
    }
}
