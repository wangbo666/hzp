package com.qgyyzs.globalcosmetics.uikit.business.session.actions;

import android.content.Intent;

import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.nim.session.extension.ProductAttachment;
import com.qgyyzs.globalcosmetics.ui.activity.CompanyProductActivity;
import com.qgyyzs.globalcosmetics.uikit.business.session.constant.RequestCode;

/**
 * Created by Administrator on 2018/4/16 0016.
 */

public class ProductAction extends BaseAction{

    public ProductAction() {
        super(R.drawable.icon_nim_product, R.string.input_panel_product);
    }
    @Override
    public void onClick() {
        Intent intent=new Intent(getActivity(),CompanyProductActivity.class);
        getActivity().startActivityForResult(intent,makeRequestCode(RequestCode.GET_PRODUCT));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){
            String Subject=data.getStringExtra("name");
            int id=data.getIntExtra("proid",0);
            String danwei=data.getStringExtra("company");
            String PcUsername=data.getStringExtra("muser");
            String image=data.getStringExtra("image");

            ProductAttachment attachment = new ProductAttachment();
            attachment.setSubject(Subject);
            attachment.setDanwei(danwei);
            attachment.setPcUsername(PcUsername);
            attachment.setId(id);
            attachment.setImage(image);
            attachment.setMaiDian("");
            IMMessage message =
                    MessageBuilder.createCustomMessage(getAccount(),
                            getSessionType(), attachment);
            sendMessage(message);
        }
    }
}
