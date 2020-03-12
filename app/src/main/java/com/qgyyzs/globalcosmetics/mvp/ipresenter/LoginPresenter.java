package com.qgyyzs.globalcosmetics.mvp.ipresenter;

import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.qgyyzs.globalcosmetics.uikit.api.NimUIKit;
import com.trello.rxlifecycle2.components.RxActivity;
import com.qgyyzs.globalcosmetics.base.IBasePresenter;
import com.qgyyzs.globalcosmetics.bean.UserDetialBean;
import com.qgyyzs.globalcosmetics.http.Api.ApiUtils;
import com.qgyyzs.globalcosmetics.http.exception.ApiException;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObservable;
import com.qgyyzs.globalcosmetics.http.observer.HttpRxObserver;
import com.qgyyzs.globalcosmetics.mvp.iface.UserDetialView;
import com.qgyyzs.globalcosmetics.nim.DemoCache;
import com.qgyyzs.globalcosmetics.nim.config.preference.Preferences;
import com.qgyyzs.globalcosmetics.nim.config.preference.UserPreferences;
import com.qgyyzs.globalcosmetics.utils.AesUtils;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class LoginPresenter extends IBasePresenter<UserDetialView, RxActivity> {
    private String nimPW;
    private String nimID;

    private UserDetialBean bean;

    private AbortableFuture<LoginInfo> loginRequest;

    private final String TAG = LoginPresenter.class.getSimpleName();

    public LoginPresenter(UserDetialView view, RxActivity activity) {
        super(view, activity);
    }

    /**
     * 登录
     */
    public void Login() {

        final HttpRxObserver httpRxObserver = new HttpRxObserver(TAG + "getInfo") {

            @Override
            protected void onStart(Disposable d) {
                if (getView() != null)
                    getView().showLoading();
            }

            @Override
            protected void onError(ApiException e) {
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showToast(e.getMsg());
                    getView().showUserResult(null);
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.e(response.toString());
                try {
                    bean = JSONObject.parseObject(response.toString(), UserDetialBean.class);
                    nimPW = bean.getJsonData().getNimPW();
                    nimID = bean.getJsonData().getNimID();
                } catch (Exception e) {
                }
                if (loginRequest != null) {
                    loginRequest.abort();
                    onLoginDone();
                }
                LogUtils.e("云信账号：" + nimID + "  云信密码：" + nimPW);
                loginRequest = NimUIKit.login(new LoginInfo(nimID, nimPW), new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        LogUtils.e("login success");

                        saveLoginInfo(nimID, nimPW);
                        onLoginDone();
                        // Demo缓存当前假登录的账号
                        DemoCache.setAccount(nimID);

                        // 初始化消息提醒配置
                        initNotificationConfig();

                        if (getView() != null) {
                            getView().closeLoading();
                            getView().showUserResult(bean);
                        }
                    }

                    @Override
                    public void onFailed(int code) {
                        LogUtils.e("login faild" + code);
                        onLoginDone();
                        if (code == 302 || code == 404) {
                            ToastUtil.showLongMsg("云信账号或密码错误");
                        } else {
                            ToastUtil.showLongMsg("登录失败");
                        }
                        if (getView() != null) {
                            getView().closeLoading();
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        LogUtils.e("login exception");
                        onLoginDone();
                        if (getView() != null) {
                            getView().closeLoading();
                        }
                    }
                });
//                Login(nimID, nimPW);
            }
        };
        if (null == getView()) return;

        HttpRxObservable.getObservable(ApiUtils.getLoginApi().Login(AesUtils.AesString(getView().getJsonString())), getActivity()).subscribe(httpRxObserver);

    }

    private void Login(String nimID, String nimPW) {
        if (loginRequest != null) {
            loginRequest.abort();
            onLoginDone();
        }
        loginRequest = NimUIKit.login(new LoginInfo(nimID, nimPW), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtils.e("login success");

                saveLoginInfo(nimID, nimPW);
                onLoginDone();
                // Demo缓存当前假登录的账号
                DemoCache.setAccount(nimID);

                // 初始化消息提醒配置
                initNotificationConfig();
            }

            @Override
            public void onFailed(int code) {
                LogUtils.e("login faild" + code);
                onLoginDone();
                if (code == 302 || code == 404) {
                    ToastUtil.showLongMsg("账号或密码错误");
                } else {
                    ToastUtil.showLongMsg("登录失败");
                }
            }

            @Override
            public void onException(Throwable exception) {
                LogUtils.e("login exception");
                onLoginDone();
            }
        });
    }

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

    private void onLoginDone() {
        loginRequest = null;
    }
}
