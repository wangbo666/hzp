package com.qgyyzs.globalcosmetics.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;

import com.qgyyzs.globalcosmetics.MainActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ChannelPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ProvincePresenter;

import cn.jpush.android.api.JPushInterface;

public class SplashActivity extends BaseActivity implements StringView{
    private ProvincePresenter provincePresenter;
    private ChannelPresenter channelPresenter;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private String type;
    private String proid,muser,name,image,company;
    private String dailiid,userid,pname;

    private String needPermission[] = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        JPushInterface.init(getApplicationContext());
        Intent intent=getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            type = uri.getQueryParameter("genre");
            if(type.equals("1")) {
                proid = uri.getQueryParameter("proid");
                muser = uri.getQueryParameter("pu");
                name = uri.getQueryParameter("pname");
                image = uri.getQueryParameter("image");
                company = uri.getQueryParameter("comp");
            }else if(type.equals("2")){
                dailiid=uri.getQueryParameter("dlid");
                userid=uri.getQueryParameter("userid");
                muser=uri.getQueryParameter("pu");
                pname=uri.getQueryParameter("pname");
            }else if(type.equals("3")){
                userid=uri.getQueryParameter("userid");
                pname=uri.getQueryParameter("rname");
            }else if(type.equals("4")){
                proid=uri.getQueryParameter("id");
                pname=uri.getQueryParameter("title");
            }else if(type.equals("5")){
                proid=uri.getQueryParameter("id");
                pname=uri.getQueryParameter("pname");
            }else if(type.equals("6")){
                proid=uri.getQueryParameter("id");
                userid=uri.getQueryParameter("userid");
                company=uri.getQueryParameter("comp");
                name=uri.getQueryParameter("jname");
            }
        }

        mSharedPreferences = getSharedPreferences(MyApplication.CONSTACTDATA, MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    @Override
    public void initData() {
        provincePresenter=new ProvincePresenter(provinceView,this);
        channelPresenter=new ChannelPresenter(channelView,this);

//        RxPermissions permissions = new RxPermissions(this);
//        permissions.request(needPermission)
//                .subscribe(b -> {
//                    if (b) {
//                        MyApplication.getInstance().getHandler().postDelayed(() -> {
//                            presenter.getChannel();
//                            presenter.getProvince();
//                            presenter.getType();
//                            presenter.getKeshi();
//                        }, 1000);
//                    } else {
//                        ToastUtil.showLongMsg("请在设置-应用-环球医药网-权限管理中开启存储、电话、相机权限，以正常使用环球医药网");
//                        finish();
//                    }
//                });
        new Thread(new Runnable() {
            @Override
            public void run() {
                provincePresenter.getProvince();
                channelPresenter.getChannel();
            }
        }).start();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public String getJsonString() {
        return null;
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showStringResult(String ksString) {
        if(!TextUtils.isEmpty(ksString)) {
            mEditor.putString("keshi", ksString);
            mEditor.commit();
        }


    }

    private StringView provinceView=new StringView() {
        @Override
        public void showStringResult(String provinceString) {
            if(!TextUtils.isEmpty(provinceString)) {
                mEditor.putString("province", provinceString);
                mEditor.commit();
            }
//            if(MyApplication.mSharedPreferences.getInt("isdaili",1)==0){
//                Intent intent=new Intent(SplashActivity.this,RegisterSettingActivity.class);
//                startActivity(intent);
//            }else {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                if(!TextUtils.isEmpty(type)) {
                    intent.putExtra("type", type);
                    if(type.equals("1")) {
                        intent.putExtra("proid", proid);
                        intent.putExtra("muser", muser);
                        intent.putExtra("name", name);
                        intent.putExtra("image", image);
                        intent.putExtra("company", company);
                    }else if(type.equals("2")){
                        intent.putExtra("dlid",dailiid);
                        intent.putExtra("userid",userid);
                        intent.putExtra("muser",muser);
                        intent.putExtra("pname",pname);
                    }else if(type.equals("3")){
                        intent.putExtra("userid",userid);
                        intent.putExtra("rname",pname);
                    }else if(type.equals("4")){
                        intent.putExtra("id",proid);
                        intent.putExtra("title",pname);
                    }else if(type.equals("5")){
                        intent.putExtra("id",proid);
                        intent.putExtra("title",pname);
                    }else if(type.equals("6")){
                        intent.putExtra("id",proid);
                        intent.putExtra("userid",userid);
                        intent.putExtra("company",company);
                        intent.putExtra("jobname",name);
                    }
                }
                startActivity(intent);
//            }
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
            return null;
        }

        @Override
        public void showToast(String msg) {

        }
    };

    private StringView channelView=new StringView() {
        @Override
        public void showStringResult(String channelString) {
            if(!TextUtils.isEmpty(channelString)) {
                mEditor.putString("channel", channelString);
                mEditor.commit();
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
            return null;
        }

        @Override
        public void showToast(String msg) {

        }
    };

    private StringView typeView=new StringView() {
        @Override
        public void showStringResult(String yaopinString) {
            if (!TextUtils.isEmpty(yaopinString)){
                mEditor.putString("yptype", yaopinString);
                mEditor.commit();
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
            return null;
        }

        @Override
        public void showToast(String msg) {

        }
    };
}
