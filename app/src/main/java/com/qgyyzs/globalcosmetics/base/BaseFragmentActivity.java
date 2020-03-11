package com.qgyyzs.globalcosmetics.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.http.listener.LifeCycleListener;
import com.qgyyzs.globalcosmetics.http.manager.ActivityStackManager;
import com.qgyyzs.globalcosmetics.utils.SystemBarUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基类Activity
 * 备注:所有的Activity都继承自此Activity
 * 1.规范团队开发
 * 2.统一处理Activity所需配置,初始化
 *
 * @author ZhongDaFeng
 */
public abstract class BaseFragmentActivity extends RxFragmentActivity {

    private AlertDialog mAlertDialog;
    protected Context mContext;
    protected Unbinder unBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        SystemBarUtils.setColor(this,getResources().getColor(R.color.main_color));
        if (mListener != null) {
            mListener.onCreate(savedInstanceState);
        }
        ActivityStackManager.getManager().push(this);
        mContext = this;
        unBinder = ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mListener != null) {
            mListener.onStart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mListener != null) {
            mListener.onRestart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mListener != null) {
            mListener.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mListener != null) {
            mListener.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.onDestroy();
        }
        //移除view绑定
        if (unBinder != null) {
            unBinder.unbind();
        }
        ActivityStackManager.getManager().remove(this);
    }

    // 设置布局
    protected abstract int getLayout();
    // 初始化组件
    protected abstract void initView();
    // 初始化数据
    protected abstract void initData();
    // 点击事件
    protected abstract void initListener();


    /**
     * 回调函数
     */
    public LifeCycleListener mListener;

    public void setOnLifeCycleListener(LifeCycleListener listener) {
        mListener = listener;
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