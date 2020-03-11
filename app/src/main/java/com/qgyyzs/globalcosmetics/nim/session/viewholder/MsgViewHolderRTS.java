package com.qgyyzs.globalcosmetics.nim.session.viewholder;

import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.nim.session.extension.RTSAttachment;
import com.qgyyzs.globalcosmetics.uikit.business.session.viewholder.MsgViewHolderBase;
import com.qgyyzs.globalcosmetics.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

public class MsgViewHolderRTS extends MsgViewHolderBase {

    private TextView textView;

    public MsgViewHolderRTS(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_rts;
    }

    @Override
    protected void inflateContentView() {
        textView = (TextView) view.findViewById(R.id.rts_text);
    }

    @Override
    protected void bindContentView() {
        RTSAttachment attachment = (RTSAttachment) message.getAttachment();
        textView.setText(attachment.getContent());
    }
}

