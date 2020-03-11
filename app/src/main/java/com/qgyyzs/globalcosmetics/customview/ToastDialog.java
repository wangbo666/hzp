package com.qgyyzs.globalcosmetics.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Administrator on 2017/5/6.
 */

public class ToastDialog {
    Context mContext;

    public ToastDialog(Context context) {
        mContext = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("该功能尚未上线，敬请期待！");
        builder.setTitle("系统提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

//                Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_LONG).show();
            }
        });

        /*builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/
        builder.show();
    }


}
