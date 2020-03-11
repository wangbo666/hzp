package com.qgyyzs.globalcosmetics.nim.session.viewholder;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.nim.session.extension.StickerAttachment;
import com.qgyyzs.globalcosmetics.uikit.business.session.emoji.StickerManager;
import com.qgyyzs.globalcosmetics.uikit.business.session.viewholder.MsgViewHolderBase;
import com.qgyyzs.globalcosmetics.uikit.business.session.viewholder.MsgViewHolderThumbBase;
import com.qgyyzs.globalcosmetics.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

/**
 * Created by zhoujianghua on 2015/8/7.
 */
public class MsgViewHolderSticker extends MsgViewHolderBase {

    private ImageView imageView;

    public MsgViewHolderSticker(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_sticker;
    }

    @Override
    protected void inflateContentView() {
        imageView = findViewById(R.id.message_item_sticker_image);
        imageView.setMaxWidth(MsgViewHolderThumbBase.getImageMaxEdge());
    }

    @Override
    protected void bindContentView() {
        StickerAttachment attachment = (StickerAttachment) message.getAttachment();
        if (attachment == null) {
            return;
        }

        Glide.with(context)
                .load(StickerManager.getInstance().getStickerUri(attachment.getCatalog(), attachment.getChartlet()))
                .apply(new RequestOptions()
                        .error(com.qgyyzs.globalcosmetics.R.drawable.nim_default_img_failed))
//                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(imageView);
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }
}
