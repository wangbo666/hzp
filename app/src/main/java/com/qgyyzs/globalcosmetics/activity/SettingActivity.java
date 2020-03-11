package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qgyyzs.globalcosmetics.MainActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.customview.CustomDialog;
import com.qgyyzs.globalcosmetics.http.retrofit.RetrofitUtils;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.LogoutPresenter;
import com.qgyyzs.globalcosmetics.nim.login.LogoutHelper;
import com.qgyyzs.globalcosmetics.service.UpdateService;
import com.qgyyzs.globalcosmetics.utils.DataCleanManager;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.SystemUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import util.UpdateAppUtils;

public class SettingActivity extends BaseActivity implements View.OnClickListener,StringView{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.signout_tv)
    TextView mSignOutTextView;
    @BindView(R.id.tvCache)
    TextView tvCache;
    @BindView(R.id.red_img)
    ImageView imgRed;
    @BindView(R.id.tvVersion)
    TextView mTvVersion;
    @BindView(R.id.version_rl)
    RelativeLayout rlVersion;
    @BindView(R.id.push_rl)
    RelativeLayout mPushRelativeLayout;
    @BindView(R.id.secret_rl)
    RelativeLayout mSceretRelativeLayout;
    @BindView(R.id.aboutour_rl)
    RelativeLayout mAboutRelativeLayout;
    @BindView(R.id.clearCache_rl)
    RelativeLayout mClearCache;

    private SharedPreferences mSharedPreferences;
    private String userid;

    private static final int REQUEST_WRITE_STORAGE = 111;
    private CustomDialog mDownDialog = null;
    private LogoutPresenter presenter=new LogoutPresenter(this,this);

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
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
        mSignOutTextView.setOnClickListener(this);
        mPushRelativeLayout.setOnClickListener(this);
        mSceretRelativeLayout.setOnClickListener(this);
        mAboutRelativeLayout.setOnClickListener(this);
        mClearCache.setOnClickListener(this);
        rlVersion.setOnClickListener(this);

        if (MyApplication.isUpdate==1) {
            imgRed.setVisibility(View.VISIBLE);
            mTvVersion.setText("有新版本了");
        }else{
            imgRed.setVisibility(View.GONE);
            mTvVersion.setText("已是最新版本("+ SystemUtil.getVersion()+")");
        }
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        userid=mSharedPreferences.getString("userid","");

        try {
            tvCache.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.signout_tv:
                new AlertDialog.Builder(this)
                        .setMessage("退出登录?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                setResult(RESULT_OK);//确定按钮事件
                                presenter.Logout();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //取消按钮事件

                            }
                        })
                        .show();
                break;
            case R.id.push_rl:
                break;
            case R.id.version_rl:
                if (MyApplication.isUpdate==1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                            && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                getString(R.string.permission_download),
                                1);
                    } else {
                        UpdateAppUtils.from(this)
                                .serverVersionCode(SystemUtil.getVersionCode(this) + 1)
                                .serverVersionName(MyApplication.server_version)
                                .apkPath(MyApplication.server_apkurl)
                                .updateInfo(MyApplication.server_apktitle)
                                .isForce(false)
                                .update();
                    }
                }
                break;
            case R.id.secret_rl:
                intent=new Intent();
                intent.putExtra("title","法律声明");
                intent.putExtra("url", RetrofitUtils.BASE_API+"home/Declaration");
                intent.setClass(this,WebBaseActivity.class);
                startActivity(intent);
                break;
            case R.id.aboutour_rl:
                intent = new Intent(this, WebBaseActivity.class);
                intent.putExtra("title", "关于我们");
                intent.putExtra("url", RetrofitUtils.BASE_API + "Home/About");
                startActivity(intent);
                break;
            case R.id.clearCache_rl:
                new AlertDialog.Builder(this)
                        .setMessage("清除缓存?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                setResult(RESULT_OK);//确定按钮事件
                                DataCleanManager.clearAllCache(SettingActivity.this);
                                try {
                                    tvCache.setText(DataCleanManager
                                            .getTotalCacheSize(SettingActivity.this));
                                    ToastUtil.showToast(SettingActivity.this,"清除成功",true);
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //取消按钮事件

                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //获取到存储权限,进行下载
                    startDownload();
                } else {
                    Toast.makeText(SettingActivity.this, "不授予存储权限将无法进行下载!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    /**
     * 启动下载
     */
    private void startDownload() {
        Intent it = new Intent(SettingActivity.this, UpdateService.class);
        //下载地址
        it.putExtra("apkUrl", MyApplication.server_apkurl);
        SettingActivity.this.startService(it);
        if(MyApplication.ForceUpdate!=1)
            mDownDialog.dismiss();
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
            jsonObject.put("id", userid);
            LogUtils.e(jsonObject.toString());
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

        MyApplication.islogin=false;
        MyApplication.TOKEN="";
        MyApplication.userId="";
        MyApplication.username="";
        mSharedPreferences.edit().clear().commit();
        MainActivity.instance.finish();
        LogoutHelper.logout();
        JPushInterface.setTags(this,1,null);
        JPushInterface.setAlias(this,1,null);
        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
        finish();
        ToastUtil.showToast(this,result,true);
    }
}
