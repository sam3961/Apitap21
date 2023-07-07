package com.apitap.views;

/**
 * Created by Shami on 5/4/2018.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.apitap.R;
import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.preferences.ATPreferences;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

public class TermsAndConditionsActivity extends AppCompatActivity {
    Context context;
    WebView webViewTerms;
    WebView webViewPolicies;
    LinearLayout back_ll;
    LinearLayout linearLayoutTerms;
    LinearLayout linearLayoutPolicies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        context = this;

        init();
        if (getIntent().hasExtra(Constants.KEY)&&getIntent().getStringExtra(Constants.KEY).equals(Constants.PRIVACY_KEY)) {
            linearLayoutTerms.setVisibility(View.GONE);
        } else if (getIntent().hasExtra(Constants.KEY)&&getIntent().getStringExtra(Constants.KEY).equals(Constants.TERMS_KEY)) {
            linearLayoutPolicies.setVisibility(View.GONE);
        }
    }

    private void init() {
        back_ll = findViewById(R.id.back_ll);
        linearLayoutTerms = findViewById(R.id.linearLayoutTerms);
        linearLayoutPolicies = findViewById(R.id.linearLayoutPolicies);
        webViewTerms = findViewById(R.id.webViewTerms);
        webViewPolicies = findViewById(R.id.webViewPolicies);

        webViewTerms.loadData(Utils.hexToASCII(ATPreferences.readString(context, Constants.TERMS).trim()), "text/html", "UTF-8");
        webViewPolicies.loadData(Utils.hexToASCII(ATPreferences.readString(context, Constants.POLICIES).trim()), "text/html", "UTF-8");

        setListener();
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
