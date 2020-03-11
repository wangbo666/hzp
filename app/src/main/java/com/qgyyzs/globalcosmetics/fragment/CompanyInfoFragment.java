package com.qgyyzs.globalcosmetics.fragment;

import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseFragment;
import com.qgyyzs.globalcosmetics.http.retrofit.RetrofitUtils;

import butterknife.BindView;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by Administrator on 2017/11/23 0023.
 */

public class CompanyInfoFragment extends BaseFragment {
    @BindView(R.id.mWebView)
    WebView mWebView;
    @BindView(R.id.loading)
    LoadingLayout loadingLayout;

    private String pcUserName;

    @Override
    protected int getLayout() {
        return R.layout.fragment_company_contact;
    }

    @Override
    protected void initView(View view) {
        pcUserName=getArguments().getString("username");
    }

    @Override
    protected void initData() {
        String url= RetrofitUtils.BASE_API + "caigou/about?pcusername=" + pcUserName;
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.loadUrl(url);

        mWebView.setWebViewClient(new WebViewClient() {
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
                if(loadingLayout!=null)loadingLayout.showContent();
            }
        });
    }

    @Override
    protected void initListener() {

    }
}
