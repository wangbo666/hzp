package com.qgyyzs.globalcosmetics.base;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/7/11.
 */

public abstract class BaseFragment extends Fragment {
    protected Unbinder unBinder;
    protected Context mContext;
    private AlertDialog mAlertDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        unBinder = ButterKnife.bind(this,view);
        mContext = getActivity();
        initView(view);
        initData();
        initListener();
        return view;
    }
    // 设置布局
    protected abstract int getLayout();
    // 初始化组件
    protected abstract void initView(View view);
    // 初始化数据
    protected abstract void initData();
    // 点击事件
    protected abstract void initListener();
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //移除view绑定
        if (unBinder != null) {
            unBinder.unbind();
        }
    }

    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(permission)) {
                showAlertDialog("权限需求", rationale,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{permission}, requestCode);
                                }
                            }
                        }, "确定", null, "取消");
            } else {
                requestPermissions(new String[]{permission}, requestCode);
            }
        }
    }
    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
    }
}
