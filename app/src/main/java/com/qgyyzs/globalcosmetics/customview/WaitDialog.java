package com.qgyyzs.globalcosmetics.customview;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

import com.qgyyzs.globalcosmetics.R;


/**
 * Created by ZFG on 2017/03/21.
 * 描述:
 */

public class WaitDialog extends ProgressDialog {
    public WaitDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setProgressStyle(STYLE_SPINNER);
        setMessage(context.getText(R.string.wait_dialog_title));
    }
}
