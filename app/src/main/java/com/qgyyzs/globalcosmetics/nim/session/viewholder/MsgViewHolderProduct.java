package com.qgyyzs.globalcosmetics.nim.session.viewholder;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.nim.session.extension.ProductAttachment;
import com.qgyyzs.globalcosmetics.ui.activity.MedicineDetailActivity;
import com.qgyyzs.globalcosmetics.uikit.business.session.viewholder.MsgViewHolderText;
import com.qgyyzs.globalcosmetics.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.qgyyzs.globalcosmetics.utils.LogUtils;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class MsgViewHolderProduct extends MsgViewHolderText {

    private ProductAttachment attachment;
    private ImageView image;
    private TextView tvTitle;
    private LinearLayout mLinearLayout;

    public MsgViewHolderProduct(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.adapter_nim_product;
    }

    @Override
    protected void inflateContentView() {
        image = (ImageView) findViewById(R.id.mIvImage);
        tvTitle = (TextView) findViewById(R.id.mTvTitle);
        mLinearLayout = (LinearLayout) findViewById(R.id.mLinearLayout);
    }

    @Override
    protected void bindContentView() {
        if (!isReceivedMessage()) {
            tvTitle.setTextColor(context.getResources().getColor(R.color.white));
        }
        attachment = (ProductAttachment) message.getAttachment();
        tvTitle.setText("[产品]" + attachment.getSubject());
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.medicin_defult)
                .error(R.mipmap.medicin_defult);
//                .diskCacheStrategy(DiskCacheStrategy.NONE);
        String imgUrl = attachment.getImage().split("\\|")[0];
        LogUtils.e("产品图片：" + imgUrl);
        Glide.with(context)
                .load(imgUrl)
                .apply(options)
                .into(image);

        mLinearLayout.setOnClickListener(v -> {
            MedicineDetailActivity.start(context, attachment.getId(), attachment.getPcUsername(), attachment.getDanwei(),
                    attachment.getSubject(), attachment.getImage() != null ? attachment.getImage().split("\\|")[0] : "");
        });
    }

    @Override
    protected int leftBackground() {
        return R.drawable.nim_message_left_white_bg;
    }

    @Override
    protected int rightBackground() {
        return R.drawable.nim_message_right_blue_bg;
    }

}
