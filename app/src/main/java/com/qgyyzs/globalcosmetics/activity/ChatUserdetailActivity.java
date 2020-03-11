package com.qgyyzs.globalcosmetics.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.UserinfoBean;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventFriendList;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.iface.UserInfoView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.AddFriendPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.SimpleUserinfoPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.UpdateFriendStatePresenter;
import com.qgyyzs.globalcosmetics.nim.session.SessionHelper;
import com.qgyyzs.globalcosmetics.utils.GlideCircleTransform;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.ScreenUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChatUserdetailActivity extends BaseActivity implements UserInfoView{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_right)
    TextView mRightTv;
    @BindView(R.id.user_img)
    ImageView mUserImg;
    @BindView(R.id.user_place)
    TextView mUserPlace;
    @BindView(R.id.user_realname)
    TextView mUserRealname;
    @BindView(R.id.user_company)
    TextView mUserCompany;
    @BindView(R.id.user_jieshao)
    TextView mUserJieshao;
    @BindView(R.id.my_circle_rl)
    LinearLayout mMyCircleRl;
    @BindView(R.id.product_rl)
    LinearLayout mProductRl;
    @BindView(R.id.isopen_img)
    ImageView mIsopenImg;
    @BindView(R.id.kehu_rl)
    LinearLayout mKehuRl;
    @BindView(R.id.send_call_tv)
    TextView mSendCallTv;
    @BindView(R.id.send_msg_tv)
    TextView mSendMsgTv;
    @BindView(R.id.add_friend_tv)
    TextView mAddFriendTv;
    @BindView(R.id.bottom)
    LinearLayout mBottom;
    @BindView(R.id.mTvRemake)
    TextView mTvRemake;
    @BindView(R.id.remake_ll)
    LinearLayout mLinearRemake;

    private int type;//好友的类型 是好友还是客户
    private String nickname = "";
    private String headimg;
    private String realname = "";//真实姓名
    private String linktel = "";//联系电话
    private String resplace = "";//常住地
    private String company = "";//所在公司
    private String shenfen = "";//介绍
    private boolean isopen;//联系方式是否公开
    private String userid;
    private String nimid;
    private String tousername;
    private int state;
    private String touserid;
    private String alias;

    private boolean flog = false;
    private SharedPreferences mSharedPreferences;

    private List<String> kefuList = new ArrayList<>();
    private String str[] = null;

    private Dialog mDialog;
    private ListView mListView;
    private ArrayAdapter<String> mArrayAdapter;


    private SimpleUserinfoPresenter userInfoPresenter=new SimpleUserinfoPresenter(this,this);
    private AddFriendPresenter addFriendPresenter;
    private UpdateFriendStatePresenter updateFriendStatePresenter;

    @Override
    protected int getLayout() {
        return R.layout.activity_chat_userdetail;
    }

    @Override
    public void initData() {
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        userid=mSharedPreferences.getString("userid","");
        touserid = getIntent().getStringExtra("fuserid");

        addFriendPresenter=new AddFriendPresenter(addView,this);
        updateFriendStatePresenter=new UpdateFriendStatePresenter(updateView,this);
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
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.kefu_layout);
        Window dialogWindow = mDialog.getWindow();
        mListView = (ListView) dialogWindow.findViewById(R.id.kefu_listview);
        WindowManager.LayoutParams lp1 = dialogWindow
                .getAttributes();// 创建布局
        lp1.width = ScreenUtils.getScreenWidth(this);
        lp1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp1);// 加载布局

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                        && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(Manifest.permission.CALL_PHONE,
                            getString(R.string.permission_call),
                            1);
                }else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + kefuList.get(position)));
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick({R.id.tv_right, R.id.my_circle_rl, R.id.product_rl, R.id.isopen_img, R.id.kehu_rl, R.id.send_call_tv, R.id.send_msg_tv, R.id.add_friend_tv,R.id.remake_ll})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_right:
                intent = new Intent(this, UserMoreActivity.class);
                intent.putExtra("touserid", touserid);//主键
                intent.putExtra("replace", resplace);//常驻地
                intent.putExtra("linktel", linktel);//联系电话
                intent.putExtra("isopen", isopen);// 是否公开
                intent.putExtra("state", type);//黑名单
                intent.putExtra("nimid",nimid);
                startActivity(intent);
                break;
            case R.id.remake_ll:
                intent=new Intent(this,UpdateRemakeActivity.class);
                intent.putExtra("userid",touserid);
                intent.putExtra("nimid",nimid);
                intent.putExtra("alias",alias);
                startActivityForResult(intent, 001);
                break;
            case R.id.my_circle_rl:
                intent = new Intent(this, MyProxyActivity.class);
                intent.putExtra("userid", touserid);
                intent.putExtra("username",tousername);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                break;
            case R.id.product_rl:
                intent = new Intent(this, FriendProductActivity.class);
                intent.putExtra("userid", touserid);
                intent.putExtra("username",tousername);
                intent.putExtra("nickname", nickname);
                startActivity(intent);
                break;
            case R.id.isopen_img:
                if (flog) {
                    mIsopenImg.setBackgroundResource(R.mipmap.close_icon);
                    flog = false;//不是我的客户
                    state=1;
                } else {
                    mIsopenImg.setBackgroundResource(R.mipmap.open_icon);
                    flog = true;//我的客户
                    state=2;
                }
                updateFriendStatePresenter.updateState();
                break;
            case R.id.kehu_rl:
                break;
            case R.id.send_call_tv:
                LogUtils.e("Tel"+linktel);
                if(!userid.equals(touserid)) {
                    if (isopen) {
                        str = null;
                        kefuList.clear();
                        if (!TextUtils.isEmpty(linktel)) {
                            str = linktel.split("\\|");
                            for (int j = 0; j < str.length; j++) {
                                if (!str[j].equals(null)) {
                                    kefuList.add(str[j]);
                                }
                            }
                        }
                        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, kefuList);
                        mListView.setAdapter(mArrayAdapter);
                        mDialog.show();
                    } else {
                        ToastUtil.showToast(this, "对方未公开联系方式", true);
                    }
                }else{
                    ToastUtil.showToast(this, "不能给自己打电话哦", true);
                }
                break;
            case R.id.send_msg_tv:
                if (!MyApplication.islogin) {
                    startActivity(new Intent(ChatUserdetailActivity.this, LoginActivity.class));
                    return;
                } else {
                    if(nimid!=null) {
                        if(!userid.equals(touserid)) {
                            SessionHelper.startP2PSession(ChatUserdetailActivity.this, nimid);
                        }else{
                            ToastUtil.showToast(this,"不能给自己发消息哦",true);
                        }
                    }else{
                        ToastUtil.showToast(this,"该用户未公开联系方式",true);
                    }
                }
                break;
            case R.id.add_friend_tv:
                if (mAddFriendTv.getText().toString().equals("已加入")) {
                    ToastUtil.showToast(this, "已经是好友了", true);
                } else {
                    addFriendPresenter.addFriend();
                }
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
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                userInfoPresenter.getUserInfo();
            }
        }).start();
    }

    @Override
    public void showUserResult(UserinfoBean bean) {
        if(bean==null) {
            mAddFriendTv.setText("加入通讯录");
            type = 5;
            return;
        }
        headimg = bean.getJsonData().getHeadImg();
        nickname = bean.getJsonData().getRealName();
        realname = bean.getJsonData().getRealName();
        tousername=bean.getJsonData().getPcUsername();
        linktel = bean.getJsonData().getLinkTel();
        isopen = bean.getJsonData().getIsOpen();
        resplace=bean.getJsonData().getProvince()+(TextUtils.isEmpty(bean.getJsonData().getCity())?"":","+bean.getJsonData().getCity());
        company = bean.getJsonData().getCompany();
        shenfen = bean.getJsonData().getShenFen();
        nimid=bean.getJsonData().getNimID();
        alias=bean.getJsonData().getAlias();
        if(!TextUtils.isEmpty(nimid)) {
            List<String> list = new ArrayList<>();
            list.add(nimid);
            NIMClient.getService(UserService.class).fetchUserInfo(list);
        }
        type = bean.getJsonData().getState();
        mTvRemake.setText(TextUtils.isEmpty(bean.getJsonData().getAlias())?"":bean.getJsonData().getAlias());
        mKehuRl.setVisibility(type==1?View.VISIBLE:View.GONE);
        mLinearRemake.setVisibility(type==1?View.VISIBLE:View.GONE);
        if (TextUtils.isEmpty(realname)||nickname.equals("null")) {
            mUserRealname.setText("用户"+bean.getJsonData().getId());
            toolbar.setTitle("用户"+bean.getJsonData().getId());
        }else{
            mUserRealname.setText(realname);
            toolbar.setTitle(nickname);
        }
        if (!TextUtils.isEmpty(resplace)) {
            mUserPlace.setText(resplace);
        }
        if (!TextUtils.isEmpty(company)) {
            mUserCompany.setText(company);
        }
        if (!TextUtils.isEmpty(shenfen)) {
            mUserJieshao.setText(shenfen);
        }
        if (!TextUtils.isEmpty(headimg)) {
            try {
                Glide.with(ChatUserdetailActivity.this).load(headimg).error(R.drawable.icon_user_defult).placeholder(R.drawable.icon_user_defult).transform(new GlideCircleTransform(ChatUserdetailActivity.this)).into(mUserImg);
            }catch (Exception e){}
        } else {
            mUserImg.setImageResource(R.mipmap.icon_user_defult);
        }
        if (type == 2) {
            mKehuRl.setVisibility(View.VISIBLE);
            mIsopenImg.setBackgroundResource(R.mipmap.open_icon);
            flog = true;
        } else if (type == 4) {
            mKehuRl.setVisibility(View.GONE);
            mLinearRemake.setVisibility(View.GONE);
        }else if(type==1) {
            mKehuRl.setVisibility(View.VISIBLE);
            mLinearRemake.setVisibility(View.VISIBLE);
            mAddFriendTv.setText("已加入");
        }
        if (userid.equals(touserid)) {
            mKehuRl.setVisibility(View.GONE);
            mBottom.setVisibility(View.GONE);
            mRightTv.setVisibility(View.GONE);
        } else {
            mKehuRl.setVisibility(View.VISIBLE);
            mBottom.setVisibility(View.VISIBLE);
        }
    }

    private StringView addView=new StringView() {
        @Override
        public void showStringResult(String result) {
            if(TextUtils.isEmpty(result))return;

            mAddFriendTv.setText("已加入");
            ToastUtil.showToast(ChatUserdetailActivity.this,result,true);
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
            jsonObject.put("userid", touserid);//当前页码
            jsonObject.put("f_userid",userid);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 001 && resultCode == 002) {
            mTvRemake.setText(data.getStringExtra("remake"));
            EventBus.getDefault().post(new AnyEventFriendList());
        }
    }
}
