package com.qgyyzs.globalcosmetics.customview;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;

import static com.qgyyzs.globalcosmetics.application.MyApplication.USERSPINFO;

/**
 * Created by Administrator on 2017/3/29.
 * 自定义dialog 实现项目中间的弹出框
 */

public class PublishDialog extends Dialog {
    private int flag;
    private SharedPreferences mSharedPreferences;
    private LinearLayout rlMain;

    private Context context;

    private LinearLayout llBtnArticle, llBtnMiniBlog, llBtnLetter, llBtnPhoto, llBtnBid, llBtnProduct, llBtnMenu;

    private Handler handler;

    private ImageView ivMenu;

    private TextView mTvFresh;

    public PublishDialog(Context context) {
        this(context, R.style.main_publishdialog_style);
    }

    private PublishDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        init();
    }
    /**
     * 初始化
     */
    private void init() {
        handler = new Handler();
        setContentView(R.layout.middle_dialog);
        mSharedPreferences=context.getSharedPreferences(USERSPINFO, Context.MODE_PRIVATE);
        flag=mSharedPreferences.getInt("flag",0);
        mTvFresh=findViewById(R.id.mTvFresh);
        mTvFresh.setText(flag==1?"提升排名":"刷新产品");
        rlMain = (LinearLayout) findViewById(R.id.mainPublish_dialog_rlMain);

        llBtnArticle = (LinearLayout) findViewById(R.id.mainPublish_dialog_llBtnArticle);
        llBtnMiniBlog = (LinearLayout) findViewById(R.id.mainPublish_dialog_llBtnMiniBlog);
        llBtnLetter = (LinearLayout) findViewById(R.id.mainPublish_dialog_llBtnLetter);
        llBtnPhoto = (LinearLayout) findViewById(R.id.mainPublish_dialog_llBtnPhoto);
        llBtnBid = (LinearLayout) findViewById(R.id.dialog_liner5);
        llBtnProduct = (LinearLayout) findViewById(R.id.dialog_liner6);

        llBtnMenu = (LinearLayout) findViewById(R.id.mainPublish_dialog_llBtnMenu);
        ivMenu = (ImageView) findViewById(R.id.mainPublish_dialog_ivMenu);


        llBtnMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                outputDialog();
            }
        });
        rlMain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                outputDialog();
            }
        });
    }
    /**
     * 进入对话框（带动画）
     */
    private void inputDialog() {
        llBtnArticle.setVisibility(View.INVISIBLE);
        llBtnMiniBlog.setVisibility(View.INVISIBLE);
        llBtnLetter.setVisibility(View.INVISIBLE);
        llBtnPhoto.setVisibility(View.INVISIBLE);
        llBtnBid.setVisibility(View.INVISIBLE);
        llBtnProduct.setVisibility(View.INVISIBLE);
        //背景动画
        rlMain.startAnimation(AnimationUtils.loadAnimation(context, R.anim.main_fade_in));
        //菜单按钮动画
        ivMenu.startAnimation(AnimationUtils.loadAnimation(context, R.anim.main_rotate_right));
        //选项动画
        llBtnArticle.setVisibility(View.VISIBLE);
        llBtnArticle.startAnimation(AnimationUtils.loadAnimation(context, R.anim.main_push_bottom_in));
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                llBtnMiniBlog.setVisibility(View.VISIBLE);
                llBtnMiniBlog.startAnimation(AnimationUtils.loadAnimation(context, R.anim.main_push_bottom_in));
            }
        }, 100);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                llBtnLetter.setVisibility(View.VISIBLE);
                llBtnLetter.startAnimation(AnimationUtils.loadAnimation(context, R.anim.main_push_bottom_in));
            }
        }, 200);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                llBtnPhoto.setVisibility(View.VISIBLE);
                llBtnPhoto.startAnimation(AnimationUtils.loadAnimation(context, R.anim.main_push_bottom_in));
            }
        }, 300);

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                llBtnBid.setVisibility(View.VISIBLE);
                llBtnBid.startAnimation(AnimationUtils.loadAnimation(context, R.anim.main_push_bottom_in));
            }
        }, 150);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                llBtnProduct.setVisibility(View.VISIBLE);
                llBtnProduct.startAnimation(AnimationUtils.loadAnimation(context, R.anim.main_push_bottom_in));
            }
        }, 250);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isShowing()) {
            outputDialog();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    /**
     * 取消对话框（带动画）
     */
    private void outputDialog() {
        //退出动画
        rlMain.startAnimation(AnimationUtils.loadAnimation(context, R.anim.main_fade_out));
        ivMenu.startAnimation(AnimationUtils.loadAnimation(context, R.anim.main_rotate_left));
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                dismiss();
            }
        }, 400);
        llBtnArticle.startAnimation(AnimationUtils.loadAnimation(context, R.anim.main_push_bottom_out));
        llBtnArticle.setVisibility(View.INVISIBLE);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                llBtnMiniBlog.startAnimation(AnimationUtils.loadAnimation(context, R.anim.main_push_bottom_out));
                llBtnMiniBlog.setVisibility(View.INVISIBLE);
            }
        }, 50);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                llBtnLetter.startAnimation(AnimationUtils.loadAnimation(context, R.anim.main_push_bottom_out));
                llBtnLetter.setVisibility(View.INVISIBLE);
            }
        }, 100);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                llBtnPhoto.startAnimation(AnimationUtils.loadAnimation(context, R.anim.main_push_bottom_out));
                llBtnPhoto.setVisibility(View.INVISIBLE);
            }
        }, 150);

    }


    @Override
    public void show() {
        super.show();
        inputDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }


    public PublishDialog setArticleBtnClickListener(android.view.View.OnClickListener clickListener) {
        llBtnArticle.setOnClickListener(clickListener);
        return this;
    }

    public PublishDialog setMiniBlogBtnClickListener(android.view.View.OnClickListener clickListener) {
        llBtnMiniBlog.setOnClickListener(clickListener);
        return this;
    }

    public PublishDialog setLetterBtnClickListener(android.view.View.OnClickListener clickListener) {
        llBtnLetter.setOnClickListener(clickListener);
        return this;
    }

    public PublishDialog setPhotoBtnClickListener(android.view.View.OnClickListener clickListener) {
        llBtnPhoto.setOnClickListener(clickListener);
        return this;
    }

    public PublishDialog setBidBtnClickListener(android.view.View.OnClickListener clickListener) {
        llBtnBid.setOnClickListener(clickListener);
        return this;
    }

    public PublishDialog setProductBtnClickListener(android.view.View.OnClickListener clickListener) {
        llBtnProduct.setOnClickListener(clickListener);
        return this;
    }
}
