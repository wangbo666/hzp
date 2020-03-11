package com.qgyyzs.globalcosmetics.activity;

import android.content.Intent;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.qgyyzs.globalcosmetics.R;
import com.qgyyzs.globalcosmetics.base.BaseActivity;
import com.qgyyzs.globalcosmetics.utils.MJavascriptInterface;
import com.qgyyzs.globalcosmetics.utils.ShareUtils;
import com.qgyyzs.globalcosmetics.utils.StatusBarUtil;

import butterknife.BindView;
import ezy.ui.layout.LoadingLayout;

public class WebBaseActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_right)
    TextView mTvright;
    @BindView(R.id.base_webview)
    WebView mWebView;
    @BindView(R.id.btn_more)
    Button mBtnMore;
    @BindView(R.id.loading)
    LoadingLayout loadingLayout;

    private String url, title;
    private String type;

    @Override
    protected int getLayout() {
        return R.layout.activity_web_base;
    }

    @Override
    public void initData() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new MJavascriptInterface(this),"showImage");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//                if(url.contains("hzpzs.net")){
                    return super.shouldInterceptRequest(view, url);
//                }else{//去掉广告
//                    return new WebResourceResponse(null,null,null);
//                }
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(loadingLayout!=null)
                    loadingLayout.showContent();
            }
        });
        mWebView.loadUrl(url);
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvright.setOnClickListener(this);
        mBtnMore.setOnClickListener(this);
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                        mWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void initView() {
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,toolbar);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        type=intent.getStringExtra("type");
        toolbar.setTitle(TextUtils.isEmpty(title)?"":title);
        if(!TextUtils.isEmpty(type)) {
            mTvright.setText("分享");
        }
        if(!TextUtils.isEmpty(intent.getStringExtra("toutiao"))){
            mBtnMore.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                UMImage image;
                image = new UMImage(this, R.mipmap.ic_logo);
                UMWeb web = new UMWeb(url+"&OpenFromApp=1");
                if(type.equals("bid")) {
                    web.setTitle("[中标]"+title);//标题
                }else if(type.equals("zhanhui")){
                    web.setTitle("[美容展会]"+title);
                }else{
                    web.setTitle("[日化资讯]"+title);
                }
                web.setThumb(image);  //缩略图
                web.setDescription(" ");//描述
                new ShareUtils(this, web, v);
                break;
            case R.id.btn_more:
                startActivity(new Intent(this,MedicalNewsActivity.class));
                finish();
                break;
        }
    }
}
