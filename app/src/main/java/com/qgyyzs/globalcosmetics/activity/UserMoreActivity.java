package com.qgyyzs.globalcosmetics.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.qgyyzs.globalcosmetics.MainActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventFriendList;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.DelFriendPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.UpdateFriendStatePresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class UserMoreActivity extends BaseActivity implements StringView{
    private DelFriendPresenter delFriendPresenter=new DelFriendPresenter(this,this);
    private UpdateFriendStatePresenter updateFriendStatePresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.user_place)
    TextView mUserPlace;
    @BindView(R.id.user_phone)
    TextView mUserPhone;
    @BindView(R.id.isopen_img)
    ImageView mIsopenImg;
    @BindView(R.id.open_tv)
    TextView mOpenTv;
    @BindView(R.id.del_btn)
    Button mDelBtn;
    @BindView(R.id.other_ll)
    LinearLayout mOtherLl;

    private boolean flog = false;

    private boolean isopen;
    private String nimid, touserid, replace, linktel;
    private int state;

    private String userid;

    @Override
    protected int getLayout() {
        return R.layout.activity_user_more;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        SharedPreferences mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        userid = mSharedPreferences.getString("userid", "未设置");//整个页面要用
        Intent intent = getIntent();
        touserid = intent.getStringExtra("touserid");
        replace = intent.getStringExtra("replace");
        linktel = intent.getStringExtra("linktel");
        isopen = intent.getBooleanExtra("isopen",false);
        nimid=intent.getStringExtra("nimid");
        state = intent.getIntExtra("state", 1);
        LogUtils.e( "状态：" + state);
        if (TextUtils.isEmpty(replace)||replace.equals("null")) {
            mUserPlace.setText("未设置");
        }else{
            mUserPlace.setText(replace);
        }
        if (isopen) {
            if(!TextUtils.isEmpty(linktel)) {
                mUserPhone.setText(linktel);
            }else{
                mUserPhone.setText("未设置");
            }
        } else {
            mUserPhone.setText("未公开");
        }
        if (state == 4) {
            mIsopenImg.setBackgroundResource(R.mipmap.open_icon);
            flog = true;
        }
        if (state == 5) {
            mOtherLl.setVisibility(View.GONE);
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

    @OnClick({R.id.isopen_img, R.id.del_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.isopen_img:
                if (flog) {
                    mIsopenImg.setBackgroundResource(R.mipmap.close_icon);
                    flog = false;
                    state=1;
                    NIMClient.getService(FriendService.class).removeFromBlackList(nimid);
                } else {
                    mIsopenImg.setBackgroundResource(R.mipmap.open_icon);
                    flog = true;
                    state=4;
                    NIMClient.getService(FriendService.class).addToBlackList(nimid);
                }
                updateFriendStatePresenter=new UpdateFriendStatePresenter(updateView,this);
                updateFriendStatePresenter.updateState();
                break;
            case R.id.del_btn:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("删除该好友？");
                builder.setTitle("提示信息");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delFriendPresenter.delFriend();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
        }
    }

    private StringView updateView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(TextUtils.isEmpty(result))return;

            EventBus.getDefault().post(new AnyEventFriendList());
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
                jsonObject.put("userid", userid);//当前页码
                jsonObject.put("f_userid", touserid);//当前页码
                jsonObject.put("state", state);//当前页码
                jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
            jsonObject.put("userid", userid);
            jsonObject.put("f_userid", touserid);
            jsonObject.toString();
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

        ToastUtil.showToast(UserMoreActivity.this, result, true);
        startActivity(new Intent(UserMoreActivity.this, MainActivity.class));
        EventBus.getDefault().post(new AnyEventFriendList());
        finish();
    }
}
