package com.qgyyzs.globalcosmetics.utils;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.customview.SharePopwindow;

/**
 * Created by Administrator on 2017/7/5.
 */

public class ShareUtils {
    private Activity mContext;
    private SharePopwindow mPopwindow;
    private UMWeb web;

    public ShareUtils(Activity context, UMWeb web, View view) {
        mContext = context;
        this.web = web;
        mPopwindow = new SharePopwindow(mContext, itemsOnClick);
        mPopwindow.showAtLocation(view,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            mPopwindow.dismiss();
            mPopwindow.backgroundAlpha(mContext, 1f);
            switch (v.getId()) {
                case R.id.weixinghaoyou:
                    //分享链接
                    share(SHARE_MEDIA.WEIXIN);
//                    Toast.makeText(MedicineDetailActivity.this, "微信好友", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pengyouquan:
                    share(SHARE_MEDIA.WEIXIN_CIRCLE);
                    //分享链接
                 /*   new ShareAction(ShareActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
//                        .withText("hello")
                            .withMedia(web)
                            .setCallback(umShareListener)
                            .share();*/
//                    Toast.makeText(MedicineDetailActivity.this, "朋友圈", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.qqhaoyou:
                    share(SHARE_MEDIA.QQ);
                    //分享链接
                   /* new ShareAction(ShareActivity.this).setPlatform(SHARE_MEDIA.QQ)
//                        .withText("hello")
                            .withMedia(web)
                            .setCallback(umShareListener)
                            .share();*/
//                    Toast.makeText(MedicineDetailActivity.this, "QQ好友", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.qqkongjian:
                    share(SHARE_MEDIA.QZONE);
                    //分享链接
                   /* new ShareAction(ShareActivity.this).setPlatform(SHARE_MEDIA.QZONE)
//                        .withText("hello")
                            .withMedia(web)
                            .setCallback(umShareListener)
                            .share();*/
//                    Toast.makeText(MedicineDetailActivity.this, "QQ空间", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };

    private void share(SHARE_MEDIA share) {
        UMImage image = new UMImage(mContext, R.mipmap.ic_logo);

//        web.setDescription("疗效快，纯中药，价格好");//描述
        new ShareAction(mContext).setPlatform(share)
//                        .withText("hello")
                .withMedia(web)
                .setCallback(umShareListener)
                .share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);

            Toast.makeText(mContext, " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mContext, " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mContext, " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
}
