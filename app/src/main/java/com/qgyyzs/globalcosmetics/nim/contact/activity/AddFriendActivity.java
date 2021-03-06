package com.qgyyzs.globalcosmetics.nim.contact.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.nim.DemoCache;
import com.qgyyzs.globalcosmetics.uikit.api.NimUIKit;
import com.qgyyzs.globalcosmetics.uikit.api.model.SimpleCallback;
import com.qgyyzs.globalcosmetics.uikit.api.wrapper.NimToolBarOptions;
import com.qgyyzs.globalcosmetics.uikit.common.activity.ToolBarOptions;
import com.qgyyzs.globalcosmetics.uikit.common.activity.UI;
import com.qgyyzs.globalcosmetics.uikit.common.ui.dialog.DialogMaker;
import com.qgyyzs.globalcosmetics.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.qgyyzs.globalcosmetics.uikit.common.ui.widget.ClearableEditTextWithIcon;

/**
 * 添加好友页面
 * Created by huangjun on 2015/8/11.
 */
public class AddFriendActivity extends UI {

    private ClearableEditTextWithIcon searchEdit;

    public static final void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AddFriendActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend_activity);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.add_buddy;
        setToolBar(R.id.toolbar, options);

        findViews();
        initActionbar();
    }

    private void findViews() {
        searchEdit = findView(R.id.search_friend_edit);
        searchEdit.setDeleteImage(R.drawable.nim_grey_delete_icon);
    }

    private void initActionbar() {
        TextView toolbarView = findView(R.id.action_bar_right_clickable_textview);
        toolbarView.setText(R.string.search);
        toolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(searchEdit.getText().toString())) {
                    Toast.makeText(AddFriendActivity.this, R.string.not_allow_empty, Toast.LENGTH_SHORT).show();
                } else if (searchEdit.getText().toString().equals(DemoCache.getAccount())) {
                    Toast.makeText(AddFriendActivity.this, R.string.add_friend_self_tip, Toast.LENGTH_SHORT).show();
                } else {
                    query();
                }
            }
        });
    }

    private void query() {
        DialogMaker.showProgressDialog(this, null, false);
        final String account = searchEdit.getText().toString().toLowerCase();
        NimUIKit.getUserInfoProvider().getUserInfoAsync(account, new SimpleCallback<NimUserInfo>() {
            @Override
            public void onResult(boolean success, NimUserInfo result, int code) {
                DialogMaker.dismissProgressDialog();
                if (success) {
                    if (result == null) {
                        EasyAlertDialogHelper.showOneButtonDiolag(AddFriendActivity.this, R.string.user_not_exsit,
                                R.string.user_tips, R.string.ok, false, null);
                    } else {
                        UserProfileActivity.start(AddFriendActivity.this, account);
                    }
                } else if (code == 408) {
                    Toast.makeText(AddFriendActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                } else if (code == ResponseCode.RES_EXCEPTION) {
                    Toast.makeText(AddFriendActivity.this, "on exception", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddFriendActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
