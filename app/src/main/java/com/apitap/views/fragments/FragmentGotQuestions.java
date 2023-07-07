package com.apitap.views.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.apitap.R;
import com.apitap.views.HomeActivity;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

public class FragmentGotQuestions extends BaseFragment implements View.OnClickListener {

    LinearLayout ll_back;
    private WebView webView;
    private String url;

    public FragmentGotQuestions() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.got_questions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // holder = new ViewHolder(view);
        tabContainer2Visible();

        ll_back = view.findViewById(R.id.back_ll);
        webView = view.findViewById(R.id.webView);

        ll_back.setOnClickListener(this);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        url = "http://support.apitap.com/";

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                try {
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
            webView.loadUrl(url);
            Log.d("LoadUrls", url);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));

    }


    public void tabContainer2Visible() {
        //homeTab2.setBackgroundColor(getActivity().getResources().getColor(R.color.colorWhite));
        //imageViewHomeTab2.setImageResource(R.drawable.ic_icon_home);

        HomeActivity.tabContainer2.setVisibility(View.GONE);
        HomeActivity.tabContainer2.setVisibility(View.GONE);
    }


}
