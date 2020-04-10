package com.example.a21516.ceshi_jiguang.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.a21516.ceshi_jiguang.R;
import com.example.a21516.ceshi_jiguang.famework.base.BaseActivity;
import com.example.a21516.ceshi_jiguang.view.MyFloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends BaseActivity{
    @BindView(R.id.web_progress)
    ProgressBar mWebProgress;
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.web_bt)
    MyFloatingActionButton mWebBt;
    private String urlString;
    @Override
    protected int setContentView() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initView() {
        initIsWebPeoress();
        mWebView.loadUrl(urlString);

    }
//设置 webview属性 能够执行javascript脚本
    private void initIsWebPeoress() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        urlString = getIntent().getStringExtra("URL");
        mWebView.setWebViewClient(new WebViewClient() {
        //重写shouldOverrideUrlLoading 实现内部显示页面
              @Override
              public boolean shouldOverrideUrlLoading(WebView view, String url) {
                 view.loadUrl(url);
                 return true;
              }
           });
        mWebView.setWebChromeClient(new WebChromeClient(){
                                        @Override
                                        public void onProgressChanged(WebView view, int newProgress) {
                                            if (newProgress == 100){
                                                mWebProgress.setVisibility(View.GONE);//加载完网页进度条消失
                                            }else {
                                                mWebProgress.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                                                mWebProgress.setProgress(newProgress);//设置进度值
                                            }
                                        }
                                    }

        );
    }

    @OnClick(R.id.web_bt)
    public void onViewClicked(){
        showToast(WebViewActivity.this,"使用浏览器打开");
        Intent intent =new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri contnet_url = Uri.parse(urlString);
        intent.setData(contnet_url);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (mWebView.canGoBack()){
                mWebView.canGoBack();
                return  true;
            }else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
