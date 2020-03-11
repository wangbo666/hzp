package com.qgyyzs.globalcosmetics.nim.session.viewholder;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.nim.session.extension.CompanyAttachment;
import com.qgyyzs.globalcosmetics.ui.activity.CompanyProductListActivity;
import com.qgyyzs.globalcosmetics.uikit.business.session.viewholder.MsgViewHolderText;
import com.qgyyzs.globalcosmetics.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class MsgViewHolderCompany extends MsgViewHolderText {

    private CompanyAttachment attachment;
    private ImageView image;
    private TextView tvTitle;
    private LinearLayout mLinearLayout;

    public MsgViewHolderCompany(BaseMultiItemFetchLoadAdapter adapter) {
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
        attachment = (CompanyAttachment) message.getAttachment();
        tvTitle.setText("[商铺]" + attachment.getDanwei());
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.medicin_defult)
                .error(R.mipmap.medicin_defult);
//                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context)
                .load(attachment.getComp_logo().split("\\|")[0])
                .apply(options)
                .into(image);

        mLinearLayout.setOnClickListener(v -> {
            CompanyProductListActivity.start(context, attachment.getPcUsername(), attachment.getDanwei());
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
