package com.qgyyzs.globalcosmetics.uikit.business.session.fragment;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MemberPushOption;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.robot.model.NimRobotInfo;
import com.netease.nimlib.sdk.robot.model.RobotAttachment;
import com.netease.nimlib.sdk.robot.model.RobotMsgType;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseModel;
import com.qgyyzs.globalcosmetics.mvp.presenter.SendMsgNumPresenter;
import com.qgyyzs.globalcosmetics.mvp.view.MsgDataView;
import com.qgyyzs.globalcosmetics.nim.session.extension.ProductAttachment;
import com.qgyyzs.globalcosmetics.uikit.api.UIKitOptions;
import com.qgyyzs.globalcosmetics.uikit.api.model.main.CustomPushContentProvider;
import com.qgyyzs.globalcosmetics.uikit.api.model.session.SessionCustomization;
import com.qgyyzs.globalcosmetics.uikit.business.ait.AitManager;
import com.qgyyzs.globalcosmetics.uikit.business.session.actions.BaseAction;
import com.qgyyzs.globalcosmetics.uikit.business.session.actions.ChatAction;
import com.qgyyzs.globalcosmetics.uikit.business.session.actions.CompanyAction;
import com.qgyyzs.globalcosmetics.uikit.business.session.actions.ImageAction;
import com.qgyyzs.globalcosmetics.uikit.business.session.actions.ProductAction;
import com.qgyyzs.globalcosmetics.uikit.business.session.actions.VideoAction;
import com.qgyyzs.globalcosmetics.uikit.business.session.constant.Extras;
import com.qgyyzs.globalcosmetics.uikit.business.session.module.Container;
import com.qgyyzs.globalcosmetics.uikit.business.session.module.ModuleProxy;
import com.qgyyzs.globalcosmetics.uikit.business.session.module.input.InputPanel;
import com.qgyyzs.globalcosmetics.uikit.business.session.module.list.MessageListPanelEx;
import com.qgyyzs.globalcosmetics.uikit.common.fragment.TFragment;
import com.qgyyzs.globalcosmetics.uikit.impl.NimUIKitImpl;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;
import com.qgyyzs.globalcosmetics.utils.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * 聊天界面基类
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class MessageFragment extends TFragment implements ModuleProxy, MsgDataView {
    private int AllNum = 1;
    private String nimid;
    private int MyNum = 0;
    private SendMsgNumPresenter presenter = new SendMsgNumPresenter(this);

    private View rootView;

    private SessionCustomization customization;

    protected static final String TAG = "MessageActivity";

    // 聊天对象
    protected String sessionId; // p2p对方Account或者群id

    protected SessionTypeEnum sessionType;

    // modules
    protected InputPanel inputPanel;
    protected MessageListPanelEx messageListPanel;

    protected AitManager aitManager;

    private boolean flog = false;

    private ProductAttachment productAttachment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.nim_message_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parseIntent();
        nimid = "qgyyzs" + UserUtil.getInstance().getUserBean().userid;
//        if(sessionType==SessionTypeEnum.Team) {
//            presenter.getNum();
////            initNum();
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        messageListPanel.onResume();
        NIMClient.getService(MsgService.class).setChattingAccount(sessionId, sessionType);
        getActivity().setVolumeControlStream(AudioManager.STREAM_VOICE_CALL); // 默认使用听筒播放
    }

    /**
     * ***************************** life cycle *******************************
     */

    @Override
    public void onPause() {
        super.onPause();

        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE,
                SessionTypeEnum.None);
        inputPanel.onPause();
        messageListPanel.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        messageListPanel.onDestroy();
        registerObservers(false);
        if (inputPanel != null) {
            inputPanel.onDestroy();
        }
        if (aitManager != null) {
            aitManager.reset();
        }
        if (presenter != null) {
            presenter.detachView();
        }
    }

    public boolean onBackPressed() {
        if (inputPanel.collapse(true)) {
            return true;
        }

        if (messageListPanel.onBackPressed()) {
            return true;
        }
        return false;
    }

    public void refreshMessageList() {
        messageListPanel.refreshMessageList();
    }

    private void parseIntent() {
        sessionId = getArguments().getString(Extras.EXTRA_ACCOUNT);
        sessionType = (SessionTypeEnum) getArguments().getSerializable(Extras.EXTRA_TYPE);
        IMMessage anchor = (IMMessage) getArguments().getSerializable(Extras.EXTRA_ANCHOR);

        productAttachment = (ProductAttachment) getArguments().getSerializable(Extras.EXTRA_PRODUCT);

        customization = (SessionCustomization) getArguments().getSerializable(Extras.EXTRA_CUSTOMIZATION);
        Container container = new Container(getActivity(), sessionId, sessionType, this);

        if (messageListPanel == null) {
            messageListPanel = new MessageListPanelEx(container, rootView, anchor, false, false);
        } else {
            messageListPanel.reload(container, anchor);
        }

        if (inputPanel == null) {
            inputPanel = new InputPanel(container, rootView, getActionList());
            inputPanel.setCustomization(customization);
        } else {
            inputPanel.reload(container, customization);
        }


        initAitManager();

        inputPanel.switchRobotMode(NimUIKitImpl.getRobotInfoProvider().getRobotByAccount(sessionId) != null);

        registerObservers(true);

        if (customization != null) {
            messageListPanel.setChattingBackground(customization.backgroundUri, customization.backgroundColor);
        }

        if (productAttachment != null) {
            IMMessage message =
                    MessageBuilder.createCustomMessage(sessionId,
                            SessionTypeEnum.P2P, productAttachment);
            sendMessage(message);
            presenter.getSendMsg();
            productAttachment = null;
        }
    }

    private void initAitManager() {
        UIKitOptions options = NimUIKitImpl.getOptions();
        if (options.aitEnable) {
            aitManager = new AitManager(getContext(), options.aitTeamMember && sessionType == SessionTypeEnum.Team ? sessionId : null, options.aitIMRobot);
            inputPanel.addAitTextWatcher(aitManager);
            aitManager.setTextChangeListener(inputPanel);
        }
    }

    /**
     * ************************* 消息收发 **********************************
     */
    // 是否允许发送消息
    protected boolean isAllowSendMessage(final IMMessage message) {
        return customization.isAllowSendMessage(message);
    }

    /**
     * ****************** 观察者 **********************
     */

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(incomingMessageObserver, register);
        // 已读回执监听
        if (NimUIKitImpl.getOptions().shouldHandleReceipt) {
            service.observeMessageReceipt(messageReceiptObserver, register);
        }
    }

    /**
     * 消息接收观察者
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }

            messageListPanel.onIncomingMessage(messages);
            sendMsgReceipt(); // 发送已读回执
        }
    };

    private Observer<List<MessageReceipt>> messageReceiptObserver = new Observer<List<MessageReceipt>>() {
        @Override
        public void onEvent(List<MessageReceipt> messageReceipts) {
            receiveReceipt();
        }
    };


    /**
     * ********************** implements ModuleProxy *********************
     */
    @Override
    public boolean sendMessage(IMMessage message) {
        if (!isAllowSendMessage(message)) {
            // 替换成tip
            message = MessageBuilder.createTipMessage(message.getSessionId(), message.getSessionType());
            message.setContent("该消息无法发送");
            message.setStatus(MsgStatusEnum.success);
            NIMClient.getService(MsgService.class).saveMessageToLocal(message, false);
        } else {
            if (sessionType == SessionTypeEnum.Team) {
                presenter.getNum(getJsonString(), message);
            } else {
                flog = true;
                appendTeamMemberPush(message);
                message = changeToRobotMsg(message);
                final IMMessage msg = message;
                appendPushConfig(message);
                // send message to server and save to db
                NIMClient.getService(MsgService.class).sendMessage(message, false).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {

                    }

                    @Override
                    public void onFailed(int code) {
                        sendFailWithBlackList(code, msg);
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
                messageListPanel.onMsgSend(message);

                if (aitManager != null) {
                    aitManager.reset();
                }
            }
        }
        return flog;
    }

    // 被对方拉入黑名单后，发消息失败的交互处理
    private void sendFailWithBlackList(int code, IMMessage msg) {
        if (code == ResponseCode.RES_IN_BLACK_LIST) {
            // 如果被对方拉入黑名单，发送的消息前不显示重发红点
            msg.setStatus(MsgStatusEnum.success);
            NIMClient.getService(MsgService.class).updateIMMessageStatus(msg);
            messageListPanel.refreshMessageList();
            // 同时，本地插入被对方拒收的tip消息
            IMMessage tip = MessageBuilder.createTipMessage(msg.getSessionId(), msg.getSessionType());
            tip.setContent(getActivity().getString(R.string.black_list_send_tip));
            tip.setStatus(MsgStatusEnum.success);
            CustomMessageConfig config = new CustomMessageConfig();
            config.enableUnreadCount = false;
            tip.setConfig(config);
            NIMClient.getService(MsgService.class).saveMessageToLocal(tip, true);
        }
    }

    private void appendTeamMemberPush(IMMessage message) {
        if (aitManager == null) {
            return;
        }
        if (sessionType == SessionTypeEnum.Team) {
            List<String> pushList = aitManager.getAitTeamMember();
            if (pushList == null || pushList.isEmpty()) {
                return;
            }
            MemberPushOption memberPushOption = new MemberPushOption();
            memberPushOption.setForcePush(true);
            memberPushOption.setForcePushContent(message.getContent());
            memberPushOption.setForcePushList(pushList);
            message.setMemberPushOption(memberPushOption);
        }
    }

    private IMMessage changeToRobotMsg(IMMessage message) {
        if (aitManager == null) {
            return message;
        }
        if (message.getMsgType() == MsgTypeEnum.robot) {
            return message;
        }
        if (isChatWithRobot()) {
            if (message.getMsgType() == MsgTypeEnum.text && message.getContent() != null) {
                String content = message.getContent().equals("") ? " " : message.getContent();
                message = MessageBuilder.createRobotMessage(message.getSessionId(), message.getSessionType(), message.getSessionId(), content, RobotMsgType.TEXT, content, null, null);
            }
        } else {
            String robotAccount = aitManager.getAitRobot();
            if (TextUtils.isEmpty(robotAccount)) {
                return message;
            }
            String text = message.getContent();
            String content = aitManager.removeRobotAitString(text, robotAccount);
            content = content.equals("") ? " " : content;
            message = MessageBuilder.createRobotMessage(message.getSessionId(), message.getSessionType(), robotAccount, text, RobotMsgType.TEXT, content, null, null);

        }
        return message;
    }

    private boolean isChatWithRobot() {
        return NimUIKitImpl.getRobotInfoProvider().getRobotByAccount(sessionId) != null;
    }

    private void appendPushConfig(IMMessage message) {
        CustomPushContentProvider customConfig = NimUIKitImpl.getCustomPushContentProvider();
        if (customConfig != null) {
            String content = customConfig.getPushContent(message);
            Map<String, Object> payload = customConfig.getPushPayload(message);
            if (!TextUtils.isEmpty(content)) {
                message.setPushContent(content);
            }
            if (payload != null) {
                message.setPushPayload(payload);
            }
        }
    }

    @Override
    public void onInputPanelExpand() {
        messageListPanel.scrollToBottom();
    }

    @Override
    public void shouldCollapseInputPanel() {
        inputPanel.collapse(false);
    }

    @Override
    public boolean isLongClickEnabled() {
        return !inputPanel.isRecording();
    }

    @Override
    public void onItemFooterClick(IMMessage message) {
        if (aitManager == null) {
            return;
        }
        if (messageListPanel.isSessionMode()) {
            RobotAttachment attachment = (RobotAttachment) message.getAttachment();
            NimRobotInfo robot = NimUIKitImpl.getRobotInfoProvider().getRobotByAccount(attachment.getFromRobotAccount());
            aitManager.insertAitRobot(robot.getAccount(), robot.getName(), inputPanel.getEditSelectionStart());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (aitManager != null) {
            aitManager.onActivityResult(requestCode, resultCode, data);
        }
        inputPanel.onActivityResult(requestCode, resultCode, data);
        messageListPanel.onActivityResult(requestCode, resultCode, data);
    }

    // 操作面板集合
    protected List<BaseAction> getActionList() {
        List<BaseAction> actions = new ArrayList<>();
        actions.add(new ChatAction());
        actions.add(new ProductAction());
        actions.add(new CompanyAction());
        actions.add(new ImageAction());
        actions.add(new VideoAction());
//        actions.add(new LocationAction());

        if (customization != null && customization.actions != null) {
            actions.addAll(customization.actions);
        }
        return actions;
    }

    /**
     * 发送已读回执
     */
    private void sendMsgReceipt() {
        messageListPanel.sendReceipt();
    }

    /**
     * 收到已读回执
     */
    public void receiveReceipt() {
        messageListPanel.receiveReceipt();
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
        try {
            jsonObject.put("tid", sessionId);
            LogUtils.e(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public void showResult(String Msg, int num, IMMessage message) {
        if (num <= 0) {
            ToastUtil.showShortMsg( Msg);
            flog = false;
        } else {
            flog = true;
            appendTeamMemberPush(message);
            message = changeToRobotMsg(message);
            final IMMessage msg = message;
            appendPushConfig(message);
            // send message to server and save to db
            NIMClient.getService(MsgService.class).sendMessage(message, false).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {

                }

                @Override
                public void onFailed(int code) {
                    sendFailWithBlackList(code, msg);
                }

                @Override
                public void onException(Throwable exception) {

                }
            });
            messageListPanel.onMsgSend(message);

            if (aitManager != null) {
                aitManager.reset();
            }
        }
    }

    @Override
    public void showSendMsg(String msg) {
        IMMessage message = MessageBuilder.createTextMessage(sessionId,
                SessionTypeEnum.P2P, msg);
        sendMessage(message);
    }

    private void initNum() {
        long newTime = System.currentTimeMillis();//当前时间毫秒数
        long oldTime = newTime - (newTime + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24);//今天零点零分零秒的毫秒数
        IMMessage anchor = MessageBuilder.createEmptyMessage(sessionId, SessionTypeEnum.Team, newTime);
        NIMClient.getService(MsgService.class)
                .queryMessageListExTime(anchor, oldTime, QueryDirectionEnum.QUERY_OLD, 200)
//                .pullMessageHistoryEx(anchor, oldTime, 200, QueryDirectionEnum.QUERY_OLD, false)
                .setCallback(new RequestCallback<List<IMMessage>>() {
                    @Override
                    public void onSuccess(List<IMMessage> imMessages) {
                        MyNum = 0;
                        for (int i = 0; i < imMessages.size(); i++) {
                            if (imMessages.get(i).getFromAccount().equals(nimid) && imMessages.get(i).getMsgType() != MsgTypeEnum.tip)
                                MyNum++;
                        }
                        LogUtils.e("success：" + MyNum);
                    }

                    @Override
                    public void onFailed(int i) {
                        LogUtils.e("failed" + i);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        LogUtils.e("exception" + throwable.getMessage());
                    }
                });
        LogUtils.e("总共可以发" + AllNum + "我发了" + MyNum);
    }
}
