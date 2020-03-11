package com.qgyyzs.globalcosmetics.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.application.MyApplication;
import com.qgyyzs.globalcosmetics.base.BaseFragment;
import com.qgyyzs.globalcosmetics.http.retrofit.RetrofitUtils;
import com.qgyyzs.globalcosmetics.nim.session.SessionHelper;
import com.qgyyzs.globalcosmetics.utils.LogUtils;
import com.qgyyzs.globalcosmetics.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by Administrator on 2017/11/23 0023.
 */

public class CompanyContactFragment extends BaseFragment {
    @BindView(R.id.mWebView)
    WebView mWebView;
    @BindView(R.id.loading)
    LoadingLayout loadingLayout;

    private String pcUserName;

    private SharedPreferences mSharedPreferences;

    @Override
    protected int getLayout() {
        return R.layout.fragment_company_contact;
    }

    @Override
    protected void initView(View view) {
        mSharedPreferences= getActivity().getSharedPreferences(MyApplication.USERSPINFO,Context.MODE_PRIVATE);
        pcUserName=getArguments().getString("username");
    }

    @Override
    protected void initData() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.loadUrl(RetrofitUtils.BASE_API + "caigou/contact?pcusername=" + pcUserName);
        mWebView.addJavascriptInterface(new JavaScriptInterface(), "openChat");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if(url.contains("hzpzs.net")){
                    return super.shouldInterceptRequest(view, url);
                }else{//去掉广告
                    return new WebResourceResponse(null,null,null);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(loadingLayout!=null)
                    loadingLayout.showContent();
            }
        });
    }

    @Override
    protected void initListener() {

    }

    public class JavaScriptInterface {
        @JavascriptInterface
        public void getCall(String paramFromJS){
            LogUtils.e(paramFromJS);
            try {
                JSONObject jsonObject=new JSONObject(paramFromJS);
                String nimId=jsonObject.getString("NimId");
                if(nimId.equals(mSharedPreferences.getString("nimid",""))){
                    ToastUtil.showToast(getActivity(),"不能给自己发消息",true);
                    return;
                }
                if (!TextUtils.isEmpty(nimId)) {
                    List<String> list = new ArrayList<>();
                    list.add(nimId);
                    NIMClient.getService(UserService.class).fetchUserInfo(list);
                }
                SessionHelper.startP2PSession(mContext,nimId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
