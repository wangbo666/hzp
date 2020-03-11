package com.qgyyzs.globalcosmetics.activity;

import android.content.Intent;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.FriendFieldEnum;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.customview.WaitDialog;
import com.qgyyzs.globalcosmetics.mvp.iface.StringView;
import com.qgyyzs.globalcosmetics.mvp.ipresenter.UpdateFriendRemakePresenter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/20 0020.
 */

public class UpdateRemakeActivity extends BaseActivity implements StringView{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.info_edit)
    EditText mInfoEdit;
    @BindView(R.id.ok_btn)
    Button mOkBtn;

    private String nimid;
    private String userid;
    private String alias;
    private WaitDialog waitDialog;

    private UpdateFriendRemakePresenter updatePresenter=new UpdateFriendRemakePresenter(this,this);


    @Override
    protected int getLayout() {
        return R.layout.activity_update_user;
    }

    @Override
    protected void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        toolbar.setTitle("修改备注");
        waitDialog=new WaitDialog(this);
    }

    @Override
    protected void initData() {
        userid=getIntent().getStringExtra("userid");
        nimid=getIntent().getStringExtra("nimid");
        alias=getIntent().getStringExtra("alias");

        if(!TextUtils.isEmpty(alias)){
            mInfoEdit.setText(alias);
            mInfoEdit.setSelection(alias.length());
        }
    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePresenter.updateRemake();
            }
        });
    }

    @Override
    public void showLoading() {
        waitDialog.show();
    }

    @Override
    public void closeLoading() {
        waitDialog.dismiss();
    }

    @Override
    public String getJsonString() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("f_userid",userid);
            jsonObject.put("alias",mInfoEdit.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.e(jsonObject.toString());
        return jsonObject.toString();
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showStringResult(String result) {
        if(result==null)return;

        Map<FriendFieldEnum, Object> map = new HashMap<>();
        map.put(FriendFieldEnum.ALIAS, mInfoEdit.getText().toString());
        NIMClient.getService(FriendService.class).updateFriendFields(nimid,map);
        Intent intent = new Intent();
        intent.putExtra("remake", mInfoEdit.getText().toString());
        setResult(002, intent);
        finish();
    }
}
