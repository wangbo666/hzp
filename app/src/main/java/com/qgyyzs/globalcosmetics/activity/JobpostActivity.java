package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventJobList;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.AddJobPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class JobpostActivity extends BaseActivity implements StringView{
    private AddJobPresenter addJobPresenter=new AddJobPresenter(this,this);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_01)
    TextView mTv01;
    @BindView(R.id.company_name_edit)
    EditText mCompanyNameEdit;
    @BindView(R.id.tv_02)
    TextView mTv02;
    @BindView(R.id.type_tv)
    TextView mTypeTv;
    @BindView(R.id.type_rl)
    RelativeLayout mTypeRl;
    @BindView(R.id.tv_03)
    TextView mTv03;
    @BindView(R.id.place_tv)
    TextView mPlaceTv;
    @BindView(R.id.place_rl)
    RelativeLayout mPlaceRl;
    @BindView(R.id.tv_04)
    TextView mTv04;
    @BindView(R.id.gangwei_edit)
    EditText mGangweiEdit;
    @BindView(R.id.tv_05)
    TextView mTv05;
    @BindView(R.id.jingyan_tv)
    TextView mJingyanTv;
    @BindView(R.id.jingyan_rl)
    RelativeLayout mJingyanRl;
    @BindView(R.id.tv_06)
    TextView mTv06;
    @BindView(R.id.xinzi_tv)
    TextView mXinziTv;
    @BindView(R.id.xinzi_rl)
    RelativeLayout mXinziRl;
    @BindView(R.id.tv_07)
    TextView mTv07;
    @BindView(R.id.xueli_tv)
    TextView mXueliTv;
    @BindView(R.id.xueli_rl)
    RelativeLayout mXueliRl;
    @BindView(R.id.tv_08)
    TextView mTv08;
    @BindView(R.id.email_edit)
    EditText mEmailEdit;
    @BindView(R.id.tv_09)
    TextView mTv09;
    @BindView(R.id.describ_tv)
    TextView mDescribTv;
    @BindView(R.id.describ_rl)
    RelativeLayout mDescribRl;
    @BindView(R.id.jobpost_btn)
    Button mJobpostBtn;
    @BindView(R.id.activity_jobpost)
    LinearLayout mActivityJobpost;

    private String type;//类型
    private String jingyan;//工作年限
    private String xinzi;//薪资范围
    private String xueli;//学历
    private SharedPreferences mSharedPreferences;
    private String sProvince = "", sCity = "";
    private String userid;
    private String company;

    @Override
    protected int getLayout() {
        return R.layout.activity_jobpost;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        userid=mSharedPreferences.getString("userid","");
        company=mSharedPreferences.getString("Company","");
        mCompanyNameEdit.setText(company);
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

    @OnClick({R.id.type_rl, R.id.place_rl, R.id.jingyan_rl, R.id.xinzi_rl, R.id.xueli_rl, R.id.describ_rl, R.id.jobpost_btn})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.type_rl:
                intent = new Intent(this, SelectJobtypeActivity.class);
                intent.putExtra("type", "1");
                startActivityForResult(intent, 001);
                break;
            case R.id.place_rl:
                intent = new Intent(JobpostActivity.this, ProvinceActivity.class);
                intent.putExtra("type", "shengcity");
                startActivity(intent);
                break;
            case R.id.jingyan_rl:
                intent = new Intent(this, SelectJobtypeActivity.class);
                intent.putExtra("type", "2");
                startActivityForResult(intent, 002);
                break;
            case R.id.xinzi_rl:
                intent = new Intent(this, SelectJobtypeActivity.class);
                intent.putExtra("type", "3");
                startActivityForResult(intent, 003);
                break;
            case R.id.xueli_rl:
                intent = new Intent(this, SelectJobtypeActivity.class);
                intent.putExtra("type", "4");
                startActivityForResult(intent, 004);
                break;
            case R.id.describ_rl:
                intent = new Intent(this, JobDescribActivity.class);
                startActivityForResult(intent, 001);
                break;
            case R.id.jobpost_btn:

                if (mCompanyNameEdit.getText().toString().trim().equals("")) {
                    ToastUtil.showToast(this, "公司名称不能为空", true);
                    return;
                }
                if (mTypeTv.getText().toString().trim().equals("")) {
                    ToastUtil.showToast(this, "岗位类型不能为空", true);
                    return;
                }
                if (sCity.equals("")) {
                    ToastUtil.showToast(this, "工作地区不能为空", true);
                    return;
                }
                if (mGangweiEdit.getText().toString().trim().equals("")) {
                    ToastUtil.showToast(this, "岗位名称不能为空", true);
                    return;
                }

                if (mJingyanTv.getText().toString().trim().equals("")) {
                    ToastUtil.showToast(this, "工作经验不能为空", true);
                    return;
                }
                if (mXinziTv.getText().toString().trim().equals("")) {
                    ToastUtil.showToast(this, "薪资范围不能为空", true);
                    return;
                }
                if (mXueliTv.getText().toString().trim().equals("")) {
                    ToastUtil.showToast(this, "学历不能为空", true);
                    return;
                }
                if (mEmailEdit.getText().toString().trim().equals("")) {
                    ToastUtil.showToast(this, "邮箱不能为空", true);
                    return;
                }
                if (mDescribTv.getText().toString().trim().equals("")) {
                    ToastUtil.showToast(this, "岗位描述不能为空", true);
                    return;
                }
                addJobPresenter.PublishJob();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sProvince = mSharedPreferences.getString("sprovince", "");//整个页面要用
        sCity = mSharedPreferences.getString("scity", "");//整个页面要用
        if (sProvince.equals(sCity)) {
            mPlaceTv.setText(sProvince);
        } else {
            mPlaceTv.setText(sProvince + " " + sCity);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 001 && resultCode == 002) {
            mDescribTv.setText(data.getStringExtra("jobDescrib"));
        }
        if (requestCode == 001 && resultCode == 001) {
            mTypeTv.setText(data.getStringExtra("value"));
            type = data.getStringExtra("key");
            LogUtils.e( "传过来了：" + data.getStringExtra("value") + data.getStringExtra("key"));
        }
        if (requestCode == 002 && resultCode == 001) {
            mJingyanTv.setText(data.getStringExtra("value"));
            LogUtils.e( "传过来了：" + data.getStringExtra("value"));
            jingyan = data.getStringExtra("key");
        }
        if (requestCode == 003 && resultCode == 001) {
            mXinziTv.setText(data.getStringExtra("value"));
            LogUtils.e( "传过来了：" + data.getStringExtra("value"));
            xinzi = data.getStringExtra("key");
        }
        if (requestCode == 004 && resultCode == 001) {
            mXueliTv.setText(data.getStringExtra("value"));
            LogUtils.e( "传过来了：" + data.getStringExtra("value"));
            xueli = data.getStringExtra("key");
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
            jsonObject.put("company", mCompanyNameEdit.getText().toString().trim());
            jsonObject.put("jobtype", type);
            jsonObject.put("province", sProvince);
            jsonObject.put("city", sCity);
            jsonObject.put("jobName", mGangweiEdit.getText().toString().trim());
            jsonObject.put("workYear", jingyan);
            jsonObject.put("salary", xinzi);
            jsonObject.put("education", xueli);
            jsonObject.put("email", mEmailEdit.getText().toString().trim());
            jsonObject.put("jobdes", mDescribTv.getText().toString().trim());
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

        EventBus.getDefault().post(new AnyEventJobList());
        ToastUtil.showToast(this, result, true);
        finish();
    }
}
