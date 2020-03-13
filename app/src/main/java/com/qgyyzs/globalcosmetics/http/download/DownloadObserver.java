package com.qgyyzs.globalcosmetics.http.download;

import android.util.Log;
import android.widget.TextView;

import com.qgyyzs.globalcosmetics.utils.ToolUtils;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DownloadObserver implements Observer<DownloadInfo> {

    public Disposable d;//可以用于取消注册的监听者
    public DownloadInfo downloadInfo;

    private OnDownloadListener listener;
    private TextView download_btn;

    public DownloadObserver setDownloadListener(OnDownloadListener listener) {
        this.listener = listener;
        return this;
    }

    public DownloadObserver setButtonStatus(TextView download_btn) {
        this.download_btn = download_btn;
        return this;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
    }

    @Override
    public void onNext(DownloadInfo info) {
        this.downloadInfo = info;
        downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD);
        int progress = (int) (info.getProgress() * 100 / info.getTotal());
        if (listener != null) {
            listener.onDownloading(progress);
            listener.onDownloadInfo(info);
        }
        if (download_btn != null) {
//            download_progress.setVisibility(View.VISIBLE);
//            if (info.getTotal() == 0) {
//                download_progress.setProgress(0);
//            } else {
//                download_progress.setProgress((int) progress);
//                download_btn.setBackgroundResource(0);
//            LogUtil.e("下载：" + new Gson().toJson(downloadInfo));
            download_btn.setText("下载中" + (progress <= 0 || progress > 100 ? "" : progress + "%"));
            download_btn.setEnabled(false);
//            }
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.d("My_Log", "onError");
        if (DownloadManager.getInstance().getDownloadUrl(downloadInfo.getUrl())) {
            DownloadManager.getInstance().pauseDownload(downloadInfo.getUrl());
            downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_ERROR);
        } else {
            downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_PAUSE);
        }
        if (listener != null) {
            listener.onDownloadFailed(e);
        }
        if (download_btn != null) {
//            download_progress.setVisibility(View.GONE);
//            download_btn.setVisibility(View.VISIBLE);
            download_btn.setEnabled(true);
        }
    }

    @Override
    public void onComplete() {
        Log.d("My_Log", "onComplete");
        if (downloadInfo != null) {
            downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_OVER);
            if (listener != null) {
                listener.onDownloadSuccess(downloadInfo.getFileName());
            }
            if (download_btn != null) {
//                download_progress.setVisibility(View.GONE);
//                download_btn.setVisibility(View.VISIBLE);
                download_btn.setEnabled(true);
//                download_btn.setBackgroundResource(R.drawable.shape_download_yellow);
                File file = new File(DownloadManager.getFilePath(), downloadInfo.getFileName());
                ToolUtils.installApk(file);
                download_btn.setText("安装");
            }
        }
    }
}
