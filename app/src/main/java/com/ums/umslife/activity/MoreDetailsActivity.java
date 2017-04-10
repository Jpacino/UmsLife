package com.ums.umslife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ums.umslife.base.BaseActivity;
import com.ums.umslife.R;

/**
 * Created by Javen on 2017/3/21.
 */

public class MoreDetailsActivity extends BaseActivity {
    private WebView moreWv;
    private WebSettings webSettings;
    private String activityNo = "";
    private static  final  String URL = "http://172.16.87.212:8080/control/act/activityNote.do?activityNo=";
    private static  final  String URL2 ="http://www.baidu.com";
    private  ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        init();
        initView();
        initData();
    }
    private void init() {
        setBackBtn();
        setTitle("注意事项");
        Intent moreDetailsIt = getIntent();
        activityNo = moreDetailsIt.getStringExtra("activityNo");
    }

    private void initView() {
        moreWv = (WebView) findViewById(R.id.webV_main);
        webSettings = moreWv.getSettings();
        bar = (ProgressBar)findViewById(R.id.proBar);

    }
    private void initData() {
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        moreWv.loadUrl(URL+activityNo);
        Log.d(TAG, "============="+URL+activityNo);
        moreWv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        moreWv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                bar.setVisibility(View.VISIBLE);
                Log.d(TAG, "onProgressChanged: "+newProgress);
                if (newProgress == 100) {
                    bar.setVisibility(View.GONE);

                } else {

                    bar.setProgress(newProgress);
                }
            }

        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && moreWv.canGoBack()) {
            moreWv.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
