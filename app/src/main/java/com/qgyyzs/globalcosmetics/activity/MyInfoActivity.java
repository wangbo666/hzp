package com.qgyyzs.globalcosmetics.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jaiky.imagespickers.ImageConfig;
import com.jaiky.imagespickers.ImageSelector;
import com.jaiky.imagespickers.ImageSelectorActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.UserDetialBean;
import com.qgyyzs.globalcosmetics.customview.ClearEditText;
import com.qgyyzs.globalcosmetics.customview.GlideLoader;
import com.qgyyzs.globalcosmetics.customview.MyCountTimer;
import com.qgyyzs.globalcosmetics.customview.WaitDialog;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventZizhi;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.iface.UserDetialView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.BindPhoneCodePresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.BindPhonePresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.CancelBindPhonePresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.UpdateAvtarPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.UserDetialPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.ZizhiStatePresenter;
import com.qgyyzs.globalcosmetics.utils.AesUtils;
import com.qgyyzs.globalcosmetics.utils.FileUtils;
import com.qgyyzs.globalcosmetics.utils.GlideCircleTransform;
import com.qgyyzs.globalcosmetics.utils.ImageUtils;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.ScreenUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener,UserDetialView {
    private UserDetialPresenter userDetialPresenter=new UserDetialPresenter(this,this);
    private UpdateAvtarPresenter updateAvatarPresenter;
    private BindPhoneCodePresenter sendCodePresenter;
    private BindPhonePresenter bindPresenter;
    private CancelBindPhonePresenter cancelPresenter;
    private ZizhiStatePresenter zizhiStatePresenter;
    @BindView(R.id.my_photo_img)
    ImageView mMyPhotoImg;
    @BindView(R.id.my_photo_rl)
    RelativeLayout mMyPhotoRl;
    @BindView(R.id.my_realname_tv)
    TextView mMyRealnameTv;
    @BindView(R.id.my_realname_rl)
    RelativeLayout mMyRealnameRl;
    @BindView(R.id.my_phone_tv)
    TextView mMyPhoneTv;
    @BindView(R.id.my_phone_rl)
    RelativeLayout mMyPhoneRl;
    @BindView(R.id.my_place_tv)
    TextView mMyPlaceTv;
    @BindView(R.id.my_place_rl)
    RelativeLayout mMyPlaceRl;
    @BindView(R.id.my_company_tv)
    TextView mMyCompanyTv;
    @BindView(R.id.my_company_rl)
    RelativeLayout mMyCompanyRl;
    @BindView(R.id.update_pwd)
    RelativeLayout mUpdatePwd;
    @BindView(R.id.my_jieshao_tv)
    TextView mMyJieshaoTv;
    @BindView(R.id.my_jieshao_rl)
    RelativeLayout mMyJieshaoRl;
    @BindView(R.id.activity_my_info)
    LinearLayout mActivityMyInfo;
    @BindView(R.id.mTvAccountName)
    TextView mTvAccountName;
    @BindView(R.id.mTvBind)
    TextView mTvBind;
    @BindView(R.id.mTvCancelBind)
    TextView mTvCancelBind;
    @BindView(R.id.my_zizhi_rl)
    RelativeLayout rlZizhi;
    @BindView(R.id.mTvzizhi_state)
    TextView mTvzizhiState;
    @BindView(R.id.update_bind)
    RelativeLayout rlBind;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String accountname;
    private String userid;
    private String headimg;
    private String realname = "";//真实姓名
    private String linktel = "";//联系电话
    private String resplace = "";//常住地
    private String company = "";//所在公司
    private String jiashao = "";//介绍
    private String mobile = "";
    private boolean isopen;//联系方式是否公开
    private boolean isDailishang;

    private Dialog mDialog;
    private Button mBtnCancel,mBtnBind;
    private TextView mTvUpdateBind;
    private ClearEditText mPhoneEdit;
    private EditText mCodeEdit;
    private TextView mGetcodeText;
    private String codeString;
    private String sendPhone;

    private String remake;

    private ImageConfig imageConfig;

    private static final int REQUEST_CODE = 1001;
    private static final int REQUEST_NAME_CODE = 1002;
    private static final int REQUEST_COMPANY_CODE = 1003;
    private static final int REQUEST_JIESHAO_CODE = 1004;
    private static final int REQUEST_PHONE_CODE = 1005;

    private WaitDialog waitDialog;

    @Override
    protected int getLayout() {
        return R.layout.activity_my_info;
    }
    @Override
    public void initData(){
        sendCodePresenter=new BindPhoneCodePresenter(codeView,this);
        bindPresenter=new BindPhonePresenter(bindView,this);
        cancelPresenter=new CancelBindPhonePresenter(cancelbindView,this);

        zizhiStatePresenter=new ZizhiStatePresenter(zizhiView,this);

        updateAvatarPresenter=new UpdateAvtarPresenter(updateView,this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                userDetialPresenter.getUserInfo();
                zizhiStatePresenter.getState();
            }
        }).start();

        mTvAccountName.setText(accountname);
        mTvBind.setText(TextUtils.isEmpty(mobile)?"未绑定":mobile);
        mTvCancelBind.setText(TextUtils.isEmpty(mobile)?"":"(解除绑定)");
        mTvCancelBind.setVisibility(accountname.equals(mobile)?View.GONE:View.VISIBLE);
        mMyRealnameTv.setText(TextUtils.isEmpty(realname)?"未设置":realname);
        mMyPlaceTv.setText(TextUtils.isEmpty(resplace)?"未设置":resplace);
        mMyCompanyTv.setText(TextUtils.isEmpty(company)?"未设置":company);
        mMyJieshaoTv.setText(TextUtils.isEmpty(jiashao)?"未设置":jiashao);
        mMyPhoneTv.setText(TextUtils.isEmpty(linktel)?"未设置":linktel.split("\\|")[0]);
        Glide.with(this).load(headimg).error(R.drawable.icon_user_defult).transform(new GlideCircleTransform(MyInfoActivity.this)).placeholder(R.drawable.icon_user_defult).into(mMyPhotoImg);

    }

    @Override
    protected void onResume() {
        super.onResume();
        resplace = mSharedPreferences.getString("ResPlace", "未设置");
        if (TextUtils.isEmpty(resplace) || resplace.equals("null")) {
            mMyPlaceTv.setText("未设置");
        } else {
            mMyPlaceTv.setText(resplace);
        }
    }

    private StringView zizhiView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(TextUtils.isEmpty(result))return;

            remake=result;
            if(result.equals("0")) {
                mTvzizhiState.setText("审核中");
            }else if(result.equals("1")){
                mTvzizhiState.setText("已审核");
            }else{
                mTvzizhiState.setText("未认证");
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

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rlZizhi.setOnClickListener(this);
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        EventBus.getDefault().register(this);
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        mEditor=mSharedPreferences.edit();
        userid = mSharedPreferences.getString("userid","");
        accountname= mSharedPreferences.getString("accountname","");
        headimg = mSharedPreferences.getString("HeadImg", "");
        realname = mSharedPreferences.getString("RealName", "");
        company = mSharedPreferences.getString("Company", "");
        jiashao = mSharedPreferences.getString("JieShao", "");
        resplace = mSharedPreferences.getString("ResPlace", "");
        linktel = mSharedPreferences.getString("linkTel", "");
        mobile = mSharedPreferences.getString("mobile","");
        isopen = mSharedPreferences.getBoolean("IsOpen",false);

        waitDialog=new WaitDialog(this);
    }

    private void initDialog(){
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_bindmobile);
        mDialog.setCancelable(false);
        Window dialogWindow = mDialog.getWindow();
        mBtnCancel= (Button) dialogWindow.findViewById(R.id.cancel_btn);
        mBtnBind= (Button) dialogWindow.findViewById(R.id.bind_btn);
        mPhoneEdit= (ClearEditText) dialogWindow.findViewById(R.id.phone_edit);
        mCodeEdit= (EditText) dialogWindow.findViewById(R.id.code_edit);
        mTvUpdateBind=(TextView)dialogWindow.findViewById(R.id.mTvUpdateBind);
        mGetcodeText= (TextView) dialogWindow.findViewById(R.id.getcode_text);
        WindowManager.LayoutParams lp1 = dialogWindow.getAttributes();// 创建布局
        lp1.width = ScreenUtils.getScreenWidth(this);
        lp1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp1);// 加载布局

        mBtnCancel.setOnClickListener(this);//手机绑定
        mBtnBind.setOnClickListener(this);
        mGetcodeText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.cancel_btn:
                if(mDialog!=null)
                    mDialog.dismiss();
                break;
            case R.id.getcode_text:
                if (judgePhone()) {
                    sendPhone = mPhoneEdit.getText().toString().trim();
                    sendCodePresenter.BindPhone();
                }
                break;
            case R.id.bind_btn:
                if (judgePhone()) {
                    if(mTvCancelBind.getText().toString().equals("")) {
                        bindPresenter.BindPhone();
                    }
                }
                break;
            case R.id.my_zizhi_rl:
                intent=new Intent(this,ZizhiPostActivity.class);
                intent.putExtra("remake",remake);
                startActivity(intent);
                break;
        }
    }

    @OnClick({R.id.update_bind,R.id.my_photo_rl, R.id.my_realname_rl, R.id.my_phone_rl, R.id.my_place_rl, R.id.my_company_rl, R.id.update_pwd, R.id.my_jieshao_rl,R.id.mTvCancelBind})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mTvCancelBind:
                new AlertDialog.Builder(this)
                        .setMessage("是否取消绑定?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                setResult(RESULT_OK);//确定按钮事
                                cancelPresenter.CancelBindPhone();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //取消按钮事件

                            }
                        })
                        .show();
                break;
            case R.id.my_photo_rl:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                        && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            "是否允许读取您的相册",
                            1);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                            && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermission(Manifest.permission.CAMERA,
                                "请允许权限打开摄像头",
                                1);
                    } else {
                        imageConfig = new ImageConfig.Builder(
                                new GlideLoader())
                                .steepToolBarColor(getResources().getColor(R.color.main_color))
                                .titleBgColor(getResources().getColor(R.color.main_color))
                                .titleSubmitTextColor(getResources().getColor(R.color.white))
                                .titleTextColor(getResources().getColor(R.color.white))
                                // 开启单选   （默认为多选）
                                .singleSelect()
                                // 裁剪 (只有单选可裁剪)
                                .crop()
                                // 开启拍照功能 （默认关闭）
                                .showCamera()
                                .requestCode(REQUEST_CODE)
                                .build();
                        ImageSelector.open(this, imageConfig);
                    }
                }
                break;
            case R.id.update_bind:
                if(TextUtils.isEmpty(mobile)||mobile.equals("null")){
                    initDialog();
                    mTvUpdateBind.setText("绑定手机后可用作找回密码");
                    mBtnBind.setText("确定绑定");
                    if(mDialog!=null)
                        mDialog.show();
                }else{
                    new AlertDialog.Builder(this)
                            .setMessage("是否取消绑定?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    setResult(RESULT_OK);//确定按钮事
                                    cancelPresenter.CancelBindPhone();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //取消按钮事件

                                }
                            })
                            .show();
                }
                break;
            case R.id.update_pwd:
                if(TextUtils.isEmpty(mobile)||mobile.equals("null")){
                    ToastUtil.showToast(this,"请先绑定手机号",true);
                }else {
                    startActivity(new Intent(this, UpdatePwdActivity.class));
                }
                break;
            case R.id.my_phone_rl:
                intent = new Intent(this, UpdatePhoneActivity.class);
                intent.putExtra("linktel", TextUtils.isEmpty(linktel)?"":linktel.split("\\|")[0]);
                intent.putExtra("isopen", isopen);
                intent.putExtra("isdailishang",isDailishang);
                startActivityForResult(intent, REQUEST_PHONE_CODE);
                break;
            case R.id.my_place_rl:
                intent = new Intent(this, ProvinceSelectActivity.class);
                intent.putExtra("proxy", "0");
                if (TextUtils.isEmpty(resplace)) {
                    intent.putExtra("province", "");
                    intent.putExtra("city", "");
                } else {
                    String[] str = resplace.split(",");
                    if (str.length == 2) {
                        intent.putExtra("province", str[0]);
                        intent.putExtra("city", str[1]);
                    } else {
                        intent.putExtra("province", str[0]);
                        intent.putExtra("city", "");
                    }
                }
                startActivity(intent);
                break;
            case R.id.my_realname_rl:
                intent = new Intent(this, UpdateUserActivity.class);
                intent.putExtra("title", "真实姓名");
                intent.putExtra("hint", TextUtils.isEmpty(realname)?"":realname);
                startActivityForResult(intent, REQUEST_NAME_CODE);
                break;
            case R.id.my_company_rl://公司
                intent = new Intent(this, UpdateUserActivity.class);
                intent.putExtra("title", "所在公司");
                intent.putExtra("hint", TextUtils.isEmpty(company)?"":company);
                startActivityForResult(intent, REQUEST_COMPANY_CODE);
                break;
            case R.id.my_jieshao_rl:
                intent = new Intent(this, UpdateUserActivity.class);
                intent.putExtra("title", "个人介绍");
                intent.putExtra("hint", TextUtils.isEmpty(jiashao)?"":jiashao);
                startActivityForResult(intent, REQUEST_JIESHAO_CODE);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(AnyEventZizhi anyEventZizhi){
        zizhiStatePresenter.getState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode){
                case REQUEST_CODE:
                    List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

                    File file = FileUtils.compressImage(ImageUtils.getimage(pathList.get(0)));

                    MultipartBody.Builder builder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)//表单类型
                            .addFormDataPart("jsonstr", updateView.getJsonString());
                    RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    builder.addFormDataPart("headimg", file.getName(), imageBody);//imgfile 后台接收图片流的参数名

                    List<MultipartBody.Part> parts = builder.build().parts();
                    updateAvatarPresenter.updateAvtar(parts);
                    break;
                case REQUEST_NAME_CODE:
                    realname=data.getStringExtra("RealName");
                    mMyRealnameTv.setText(realname);
                    break;
                case REQUEST_COMPANY_CODE:
                    company=data.getStringExtra("Company");
                    mMyCompanyTv.setText(company);
                    break;
                case REQUEST_JIESHAO_CODE:
                    jiashao=data.getStringExtra("JieShao");
                    mMyJieshaoTv.setText(jiashao);
                    break;
                case REQUEST_PHONE_CODE:
                    isopen=data.getBooleanExtra("IsOpen",false);
                    linktel=data.getStringExtra("linkTel");
                    mMyPhoneTv.setText(linktel);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private StringView updateView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(TextUtils.isEmpty(result))return;

            ToastUtil.showToast(MyInfoActivity.this,"头像上传成功",true);
            mEditor.putString("HeadImg",result);
            mEditor.commit();
            Glide.with(MyInfoActivity.this).load(result).error(R.drawable.icon_user_defult).transform(new GlideCircleTransform(MyInfoActivity.this)).placeholder(R.drawable.icon_user_defult).into(mMyPhotoImg);
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
                jsonObject.put("primarykeyvalue", userid);
                LogUtils.e( jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            };
            return AesUtils.AesString(jsonObject.toString());
        }

        @Override
        public void showToast(String msg) {

        }
    };

    private boolean judgePhone() {
        String telRegex = "[1][34578]\\d{9}";
        if (TextUtils.isEmpty(mPhoneEdit.getText().toString().trim())) {
            ToastUtil.showToast(this, "手机号不能为空", true);
            return false;
        } else if (mPhoneEdit.getText().toString().matches(telRegex)) {
            return mPhoneEdit.getText().toString().matches(telRegex);
        } else {
            ToastUtil.showToast(this, "手机号输入有误，请重新输入", true);
            return mPhoneEdit.getText().toString().matches(telRegex);
        }
    }
    private boolean judgeTrue() {
        boolean flog = false;
        if (mPhoneEdit.getText().toString().equals(sendPhone)) {
            flog = true;
        } else {
            ToastUtil.showToast(this, "手机号不一致", true);
        }
        return flog;
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
    private StringView bindView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(TextUtils.isEmpty(result))return;

            ToastUtil.showToast(MyInfoActivity.this,"手机号绑定成功",true);
            mEditor.putString("mobile",mPhoneEdit.getText().toString().trim());
            mEditor.commit();
            mobile = mPhoneEdit.getText().toString().trim();
            mTvBind.setText(mobile);
            mTvCancelBind.setText("(解除绑定)");
            if(mDialog!=null)
                mDialog.dismiss();
        }

        @Override
        public void showLoading() {

        }

        @Override
        public void closeLoading() {

        }

        @Override
        public String getJsonString() {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("id", MyApplication.userId);
                jsonObject.put("mobile",mPhoneEdit.getText().toString().trim());
                jsonObject.put("yzm",mCodeEdit.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }

        @Override
        public void showToast(String msg) {

        }
    };
    private StringView codeView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(TextUtils.isEmpty(result))return;

            ToastUtil.showToast(MyInfoActivity.this,"验证码发送成功",true);
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
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("mobile",mPhoneEdit.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }

        @Override
        public void showToast(String msg) {

        }
    };

    private StringView cancelbindView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(TextUtils.isEmpty(result))return;

            if(mDialog!=null&&mDialog.isShowing())mDialog.dismiss();

            mTvBind.setText("未绑定");
            mobile="";
            mTvCancelBind.setText(mobile);
            mEditor.putString("mobile","");
            mEditor.commit();
        }

        @Override
        public void showLoading() {
            if(waitDialog!=null&&!waitDialog.isShowing())waitDialog.show();
        }

        @Override
        public void closeLoading() {
            if(waitDialog!=null&&waitDialog.isShowing())waitDialog.dismiss();
        }

        @Override
        public String getJsonString() {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("id",userid);
                jsonObject.put("mobile",mobile);
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
            jsonObject.put("AccountName", accountname);//当前页码
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
    public void showUserResult(UserDetialBean bean) {
        if(bean==null) {
            return;
        }

        isDailishang=bean.getJsonData().getIsDaiLiShang();
        accountname=bean.getJsonData().getAccountName();
        headimg=bean.getJsonData().getHeadImg();
        realname=bean.getJsonData().getRealName();
        linktel=bean.getJsonData().getLinkTel();
        mobile=bean.getJsonData().getMobile();
        isopen=bean.getJsonData().getIsOpen();
        resplace=bean.getJsonData().getResPlace();
        company=bean.getJsonData().getCompany();
        jiashao=bean.getJsonData().getJieShao();
        mEditor.putString("accountname",accountname);
        mEditor.putString("HeadImg", headimg);
        mEditor.putString("RealName", realname);
        mEditor.putString("linkTel",linktel);
        mEditor.putString("mobile",mobile);
        mEditor.putBoolean("IsOpen", isopen);
        mEditor.putString("ResPlace", bean.getJsonData().getProvince()+(TextUtils.isEmpty(bean.getJsonData().getCity())?"":","+bean.getJsonData().getCity()));
        mEditor.putString("Company", company);
        mEditor.putString("JieShao", jiashao);
        mEditor.putBoolean("IsPrimary",bean.getJsonData().getIsPrimary());
        mEditor.putInt("flag",bean.getJsonData().getFlag());
        mEditor.commit();

    }
}
