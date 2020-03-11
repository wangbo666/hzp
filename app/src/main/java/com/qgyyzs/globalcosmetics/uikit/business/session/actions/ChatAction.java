package com.qgyyzs.globalcosmetics.uikit.business.session.actions;

import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.ui.customview.ChatBottomDialog;

/**
 * Created by Administrator on 2018/4/16 0016.
 */

public class ChatAction extends BaseAction implements ChatBottomDialog.SettingAreaListener{
    private ChatBottomDialog chatBottomDialog;

    public ChatAction() {
        super(R.drawable.icon_nim_chat, R.string.input_panel_chat);
    }
    @Override
    public void onClick() {
        chatBottomDialog=new ChatBottomDialog(getActivity());
        chatBottomDialog.setOnSettingAreaListener(this);
        chatBottomDialog.show();
    }

    @Override
    public void onSettingArea(String word) {
        IMMessage message =
                MessageBuilder.createTextMessage(getAccount(),
                        getSessionType(), word);
        sendMessage(message);
    }
}
