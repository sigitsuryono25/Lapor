package com.lauwba.surelabs.lapor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;
    Intent intent;
    String data;
    FrameLayout pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = findViewById(R.id.webView);
        webView.getSettings().getJavaScriptCanOpenWindowsAutomatically();
        webView.getSettings().getJavaScriptCanOpenWindowsAutomatically();

        pb = findViewById(R.id.progressBar);

        try {
            intent = getIntent();
            data = intent.getStringExtra(Config.URL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(data);

    }

}
