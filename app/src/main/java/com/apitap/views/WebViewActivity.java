package com.apitap.views;

/**
 * Created by Shami on 5/4/2018.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.model.Client;
import com.apitap.model.Utils;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

public class WebViewActivity extends AppCompatActivity  {
    WebView webView;
    Context context;
    TextView textViewTitle;
    LinearLayout back_ll;
    CircularProgressView mPocketBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        context = this;

        init();
        textViewTitle.setText(getIntent().getStringExtra("title"));
    }

    private void init() {
        mPocketBar = (CircularProgressView) findViewById(R.id.pocket);
        webView = (WebView) findViewById(R.id.webview);
        textViewTitle =  findViewById(R.id.titleName);
        back_ll =  findViewById(R.id.back_ll);
        setListener();
        mPocketBar.setVisibility(View.VISIBLE);

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        webView.loadUrl(Client.URL_CREATE_PASSWORD);
       // webView.loadUrl("http://aiodctesting.org:8090/Apitap_ShoppingCart/?nmcId=00011010000000000254&scId=265&merId=0001202A000000000197&t=582b895061023421f56df0cbeadc358c5f010daf10192a18");


        webView.setWebViewClient(new WebViewClient()
        {
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url)
            {
                return shouldOverrideUrlLoading(url);
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request)
            {
                Uri uri = request.getUrl();
                return shouldOverrideUrlLoading(uri.toString());
            }

            private boolean shouldOverrideUrlLoading(final String url)
            {

                // Here put your code

                return true; // Returning True means that application wants to leave the current WebView and handle the url itself, otherwise return false.
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mPocketBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mPocketBar.setVisibility(View.GONE);
                Utils.baseshowFeedbackMessage(WebViewActivity.this,webView,error.getDescription().toString());
            }
        });

    }

    private void setListener() {
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}
