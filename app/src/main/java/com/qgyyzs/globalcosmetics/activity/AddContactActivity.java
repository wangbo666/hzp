package com.qgyyzs.globalcosmetics.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.bean.UserListBean;
import com.qgyyzs.globalcosmetics.eventbus.AnyEventFriendList;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.iface.UserListView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.AddFriendPresenter;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.SearchFriendPresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

public class AddContactActivity extends BaseActivity implements UserListView {
    private SearchFriendPresenter searchFriendPresenter = new SearchFriendPresenter(this, this);
    private AddFriendPresenter addFriendPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_note)
    EditText editText;
    @BindView(R.id.ll_user)
    RelativeLayout searchedUserLayout;
    @BindView(R.id.name)
    TextView nameText;
    @BindView(R.id.avatar)
    ImageView ivhead;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.indicator)
    Button mBtnAdd;

    private int fuserid;
    private String headimg, name;

    private String userid;
    private SharedPreferences mSharedPreferences;

    @Override
    protected int getLayout() {
        return R.layout.em_activity_add_contact;
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);
        mSharedPreferences = getSharedPreferences(MyApplication.USERSPINFO, Context.MODE_PRIVATE);
        userid = mSharedPreferences.getString("userid", "未设置");

        addFriendPresenter = new AddFriendPresenter(addView, this);
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
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(fuserid + "").equals(userid)) {
                    addFriendPresenter.addFriend();
                } else {
                    showToast("不能添加自己为好友");
                }
            }
        });
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(editText.getText().toString().trim())) {
                    searchFriendPresenter.getUserList();
                } else {
                    showToast("请输入用户名");
                }
            }
        });
    }

    public void back(View v) {
        finish();
    }

    private StringView addView = new StringView() {
        @Override
        public void showStringResult(String result) {
            if (result == null) return;

            EventBus.getDefault().post(new AnyEventFriendList());
            finish();
            ToastUtil.showToast(AddContactActivity.this, result, true);
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
                jsonObject.put("f_userid", fuserid + "");
                LogUtils.e(jsonObject.toString());
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
            jsonObject.put("accountname", editText.getText().toString().trim());
            LogUtils.e(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToast(this, msg, true);
    }

    @Override
    public void showUserListResult(UserListBean bean) {
        if (bean == null) {
            showToast("未找到该用户");
            return;
        }
        if (bean.getResult() == 1 && bean.getJsonData().size() > 0) {
            headimg = bean.getJsonData().get(0).getHeadImg();
            name = bean.getJsonData().get(0).getRealName();
            fuserid = bean.getJsonData().get(0).getId();
            Glide.with(AddContactActivity.this).load(headimg)
                    .apply(RequestOptions.circleCropTransform()
                            .error(R.drawable.icon_user_defult)).into(ivhead);
            searchedUserLayout.setVisibility(View.VISIBLE);
            nameText.setText(name);
        } else {
            searchedUserLayout.setVisibility(View.GONE);
            ToastUtil.showToast(AddContactActivity.this, bean.getMsg(), true);
        }
    }
}
