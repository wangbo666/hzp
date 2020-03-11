package com.qgyyzs.globalcosmetics.uikit.business.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseModel;
import com.qgyyzs.globalcosmetics.mvp.model.UserBean;
import com.qgyyzs.globalcosmetics.mvp.presenter.JobDetailPresenter;
import com.qgyyzs.globalcosmetics.mvp.view.JobDetailView;
import com.qgyyzs.globalcosmetics.nim.session.extension.ProductAttachment;
import com.qgyyzs.globalcosmetics.uikit.api.NimUIKit;
import com.qgyyzs.globalcosmetics.uikit.api.model.contact.ContactChangedObserver;
import com.qgyyzs.globalcosmetics.uikit.api.model.main.OnlineStateChangeObserver;
import com.qgyyzs.globalcosmetics.uikit.api.model.session.SessionCustomization;
import com.qgyyzs.globalcosmetics.uikit.api.model.user.UserInfoObserver;
import com.qgyyzs.globalcosmetics.uikit.api.wrapper.NimToolBarOptions;
import com.qgyyzs.globalcosmetics.uikit.business.session.constant.Extras;
import com.qgyyzs.globalcosmetics.uikit.business.session.fragment.MessageFragment;
import com.qgyyzs.globalcosmetics.uikit.business.session.helper.MessageListPanelHelper;
import com.qgyyzs.globalcosmetics.uikit.business.uinfo.UserInfoHelper;
import com.qgyyzs.globalcosmetics.uikit.common.activity.ToolBarOptions;
import com.qgyyzs.globalcosmetics.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.qgyyzs.globalcosmetics.uikit.impl.NimUIKitImpl;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

import java.util.List;
import java.util.Set;


/**
 * 点对点聊天界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class P2PMessageActivity extends BaseMessageActivity implements JobDetailView {
    private JobDetailPresenter jobDetailPresenter = new JobDetailPresenter(this);
    private TextView TvFlag;
    private RelativeLayout titlebar;
    private TextView mTvTile;

    private boolean isResume = false;

    private static String Id;

    public static void start(Context context, String contactId, SessionCustomization customization, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        Id = contactId;
        intent.setClass(context, P2PMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }

    public static void start(Context context, String contactId, SessionCustomization customization, ProductAttachment anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_PRODUCT, anchor);
        }
        Id = contactId;
        intent.setClass(context, P2PMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 单聊特例话数据，包括个人信息，
        requestBuddyInfo();
        displayOnlineState();
        registerObservers(true);
        registerOnlineStateChangeListener(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerOnlineStateChangeListener(false);
        if (jobDetailPresenter != null)
            jobDetailPresenter.detachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
        mTvTile.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;
    }

    private void requestBuddyInfo() {
        setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
    }

    private void registerObservers(boolean register) {
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
    }

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }
    };

    private UserInfoObserver uinfoObserver;

    OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            // 更新 toolbar
            if (accounts.contains(sessionId)) {
                // 按照交互来展示
                displayOnlineState();
            }
        }
    };

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
    }

    private void displayOnlineState() {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(sessionId);
        setSubTitle(detailContent);
    }

    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {
                        requestBuddyInfo();
                    }
                }
            };
        }
        NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, true);
    }

    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, false);
        }
    }

    /**
     * 命令消息接收观察者
     */
    Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
                return;
            }
            showCommandMessage(message);
        }
    };

    protected void showCommandMessage(CustomNotification message) {
        if (!isResume) {
            return;
        }

        String content = message.getContent();
        try {
            JSONObject json = JSON.parseObject(content);
            int id = json.getIntValue("id");
            if (id == 1) {
                // 正在输入
                Toast.makeText(P2PMessageActivity.this, "对方正在输入...", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(P2PMessageActivity.this, "command: " + content, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

    @Override
    protected MessageFragment fragment() {
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_message_activity;
    }

    @Override
    protected void initToolBar() {
        ToolBarOptions options = new NimToolBarOptions();
        setToolBar(R.id.toolbar, options);
        ImageView iback = (ImageView) findViewById(R.id.back);
        mTvTile = (TextView) findViewById(R.id.title_text);
        TvFlag = (TextView) findViewById(R.id.flag_text);
        titlebar = findView(R.id.titlebar);
//        StatusBarUtil.immersive(this);
        LogUtils.e("sessionId" + Id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(Id)) return;
                jobDetailPresenter.getJobDetail(getJsonString());
            }
        }).start();


        ImageView iright = (ImageView) findViewById(R.id.right_img);
        iback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyAlertDialogHelper.createOkCancelDiolag(P2PMessageActivity.this, null, "是否刪除聊天记录？", true, new EasyAlertDialogHelper.OnDialogActionListener() {
                    @Override
                    public void doCancelAction() {

                    }

                    @Override
                    public void doOkAction() {
                        NIMClient.getService(MsgService.class).clearChattingHistory(sessionId, SessionTypeEnum.P2P);
                        MessageListPanelHelper.getInstance().notifyClearMessages(sessionId);
                    }
                }).show();
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void closeLoading() {

    }

    @Override
    public void onErrorCode(BaseModel model) {

    }

    public String getJsonString() {
        JSONObject jsonObject = new JSONObject();
        if (!TextUtils.isEmpty(Id)) jsonObject.put("userid", Id.substring(6, Id.length()));
        return jsonObject.toString();
    }

    @Override
    public void showUserResult(UserBean bean) {
        if (null == bean) return;

        TvFlag.setVisibility(View.VISIBLE);
        if (bean.PrimaryFlag == 1) {
            TvFlag.setText("VIP用户");
        } else {
            TvFlag.setText("未认证用户");
        }
    }


}
