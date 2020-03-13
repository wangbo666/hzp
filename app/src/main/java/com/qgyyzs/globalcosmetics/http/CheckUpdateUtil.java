package com.qgyyzs.globalcosmetics.http;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.bean.VersionBean;
import com.qgyyzs.globalcosmetics.http.download.DownloadManager;
import com.qgyyzs.globalcosmetics.http.download.DownloadObserver;
import com.qgyyzs.globalcosmetics.utils.NoFastClickUtils;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;
import com.qgyyzs.globalcosmetics.utils.ToolUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckUpdateUtil {
    private boolean hasView;
    private Activity mContext;
    private String server_apkurl;
    public static boolean shouldShowNewVersionFlag = false;

    public CheckUpdateUtil(boolean hasView, Activity mContext) {
        this.hasView = hasView;
        this.mContext = mContext;
    }

    public void CheckVersion(final Activity mContext, VersionBean bean) {
        if (bean == null) {
            return;
        }
        server_apkurl = bean.httppath;
//        server_apkurl = "http://www.qgyyzs.net/gg/qgyyzs_android_3.2.0.apk";
        String type = "1";
        if (bean.IsUpdate == 0) {
            type = "0";
        } else {
            type = bean.ForceUpdate == 0 ? "1" : "2";
        }
        if ("1".equals(type)) {
            shouldShowNewVersionFlag = true;
            alertUpdateDialog(mContext, bean.msg, type, bean.verdesc);
        } else if ("2".equals(type)) {
            shouldShowNewVersionFlag = true;
            alertUpdateDialog(mContext, bean.msg, type, bean.verdesc);
        } else {
            shouldShowNewVersionFlag = false;
            if (hasView) {
                ToastUtil.showLongMsg("已是最新版本");
            }
        }
    }

    public ProgressBar download_progress;
    public TextView download_btn;

    public void alertUpdateDialog(final Activity mContext, final String title,
                                  final String type, String desc) {
        final AlertDialog ad = new AlertDialog.Builder(mContext, R.style.TransDialog).create();
        ad.setCancelable(false);
        ad.setCanceledOnTouchOutside(false);
        View view = View.inflate(mContext, R.layout.dialog_version_update, null);
        if (ad != null && !ToolUtils.isActivityDestroyed(mContext) && !ad.isShowing()) {
            ad.show();
        }
        TextView msg_tv = view.findViewById(R.id.tv_sub_title);
        TextView title_tv = view.findViewById(R.id.tv_title);
        title_tv.setText(title);
        download_progress = view.findViewById(R.id.download_progress);
        download_btn = view.findViewById(R.id.download_btn);
        msg_tv.setText(desc);
        ImageView btn_close = view.findViewById(R.id.btn_close);
        btn_close.setVisibility(type.equals("1") ? View.VISIBLE : View.GONE);
        btn_close.setOnClickListener(v -> {
            if (ad != null && !ToolUtils.isActivityDestroyed(mContext) && ad.isShowing())
                ad.dismiss();
        });
        download_btn.setOnClickListener(v -> {
            if (NoFastClickUtils.isFastClick()) {
                return;
            }
            DownloadManager.getInstance().download(server_apkurl, new DownloadObserver()
                    .setButtonStatus(download_btn));

        });
        ad.setContentView(view);
    }

    /**
     * 获取重定向地址
     *
     * @param path
     * @return
     * @throws Exception
     */
    private String getRedirectUrl(String path) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(path)
                    .openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(5000);
        return conn.getHeaderField("Location");
    }
}
