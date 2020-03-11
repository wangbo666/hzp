package com.qgyyzs.globalcosmetics.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;

public class CityDialog extends Dialog {

    public static final int NO = 0;// 取消，设置监听事件的时候用
    public static final int YES = 1;// 确认，设置监听事件的时候用

    private View.OnClickListener commitListener;
    private View.OnClickListener cancelListener;
    public static TextView province;
    public static TextView city;
    private RelativeLayout rlpro,rlcity;

    public CityDialog(Context context) {
        super(context,R.style.DialogNoBg);
        initView(context);
    }

    private void initView(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(context, R.layout.fliter_area_layout, null);
        setContentView(view);
        province = (TextView) view.findViewById(R.id.province_tv);
        city = (TextView) view.findViewById(R.id.city_tv);
        rlcity=(RelativeLayout) view.findViewById(R.id.city_rl);
        rlpro=(RelativeLayout)view.findViewById(R.id.province_rl);

        rlpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commitListener != null) {
                    commitListener.onClick(v);
                }
                CityDialog.this.dismiss();
            }
        });
        rlcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null) {
                    cancelListener.onClick(v);
                }
                CityDialog.this.dismiss();
            }
        });
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        lp.width = (int) (dm.widthPixels * 0.9); // 设置宽度
        lp.alpha = 1.0f;// 设置透明度
        dialogWindow.setAttributes(lp);

        // 点击边缘是否关闭对话框
        setCanceledOnTouchOutside(true);
    }

    /**
     * 设置监听事件，自带销毁，只要点击了按钮就会销毁了。
     *
     * @param listener
     * @param type
     */
    public CityDialog setOnClickListener(
            android.view.View.OnClickListener listener, int type) {
        if (type == 1) {
            this.commitListener = listener;
        } else if (type == 0) {
            this.cancelListener = listener;
        }
        return this;
    }
}
