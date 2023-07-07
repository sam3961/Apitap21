package com.apitap.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.apitap.R;
import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

public class FragmentSettings extends BaseFragment implements View.OnClickListener {


    private WebView webView;
    private LinearLayout backll;
    String url = "";
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        backll = (LinearLayout) view.findViewById(R.id.back_ll);


        backll.setOnClickListener(this);
        webView = (WebView) view.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setBuiltInZoomControls(true);
        showProgress();
        try {
            String token = ATPreferences.readString(getActivity(), Constants.TOKEN);
            String id = ATPreferences.readString(getActivity(), Constants.KEY_USERID);
            byte[] data = new byte[0];
            data = id.getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            url = Client.URL_SETTINGS + token;
            // url = "https://aiodc.com:8050/MobileClient/?t="+token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  Toast.makeText(getActivity(),"No orders are saved",Toast.LENGTH_SHORT).show();

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                try {
                 /*   String id = ATPreferences.readString(getActivity(), Constants.KEY_USERID);
                    byte[] data = new byte[0];
                    data = id.getBytes("UTF-8");
                    String base64 = Base64.encodeToString(data, Base64.DEFAULT);*/
                    view.loadUrl(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return super.shouldOverrideUrlLoading(view, url);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showProgress();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hideProgress();

            }

        });

        try {

        /*    String id = ATPreferences.readString(getActivity(), Constants.KEY_USERID);
            byte[] data = id.getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
*/
            webView.loadUrl(url);

            Log.d("LoadUrls", url);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));
    }
}

