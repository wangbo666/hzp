package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.UpdateProductKeshiPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class ProductPolicyActivity extends BaseActivity implements StringView{
    private UpdateProductKeshiPresenter updateProductKeshiPresenter=new UpdateProductKeshiPresenter(this,this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_right)
    TextView mRightTv;
    @BindView(R.id.zhengce_edit)
    EditText mZhengceEdit;

    private int proid;
    private String zc;

    private SharedPreferences mSharedPreferences;
    private String userid;

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
    protected int getLayout() {
        return R.layout.activity_product_policy;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        Intent intent = getIntent();
        proid = intent.getIntExtra("proid", 0);
        zc = intent.getStringExtra("zc");
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        userid=mSharedPreferences.getString("userid","");
        mZhengceEdit.setText(zc);
        if(!TextUtils.isEmpty(zc)){
            mZhengceEdit.setSelection(zc.length());
        }
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                if (!TextUtils.isEmpty(mZhengceEdit.getText().toString())) {
                    updateProductKeshiPresenter.updateKeshi();
                }else{
                    ToastUtil.showToast(this,"政策不能为空",true);
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
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("id", proid);
            jsonObject1.put("zc", mZhengceEdit.getText().toString().trim());
            jsonObject1.put("userid",userid);
            jsonObject1.toString();
            LogUtils.e( jsonObject1.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject1.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showStringResult(String result) {
        if(TextUtils.isEmpty(result))return;

        Intent intent=new Intent();
        intent.putExtra("zc",mZhengceEdit.getText().toString());
        setResult(RESULT_OK, intent);

        finish();
        ToastUtil.showToast(this,result,true);
    }
}
