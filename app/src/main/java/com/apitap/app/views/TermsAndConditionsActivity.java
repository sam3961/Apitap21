package com.apitap.app.views;

/**
 * Created by Shami on 5/4/2018.
 */

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.apitap.app.R;
import com.apitap.app.model.Constants;
import com.apitap.app.model.preferences.ATPreferences;

public class TermsAndConditionsActivity extends AppCompatActivity {
    Context context;
    WebView webViewTerms;
    WebView webViewPolicies;
    WebView webViewReturns;
    TextView textViewContent;
    TextView titleName;
    LinearLayout back_ll;
    LinearLayout linearLayoutTerms;
    LinearLayout linearLayoutPolicies;
    LinearLayout linearLayoutReturns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        context = this;

        init();

        if (getIntent().hasExtra(Constants.KEY) && getIntent().getStringExtra(Constants.KEY).equals(Constants.PRIVACY_KEY)) {
            linearLayoutTerms.setVisibility(View.GONE);
            linearLayoutReturns.setVisibility(View.GONE);
            textViewContent.setText(Html.fromHtml(ATPreferences.readString(context, Constants.POLICIES).trim()));
            titleName.setText("Privacy Policies");
        } else if (getIntent().hasExtra(Constants.KEY) && getIntent().getStringExtra(Constants.KEY).equals(Constants.TERMS_KEY)) {
            linearLayoutPolicies.setVisibility(View.GONE);
            linearLayoutReturns.setVisibility(View.GONE);
            textViewContent.setText(Html.fromHtml(ATPreferences.readString(context, Constants.TERMS).trim()));
            titleName.setText("Terms and Conditions");
        } else if (getIntent().hasExtra(Constants.KEY) && getIntent().getStringExtra(Constants.KEY).equals(Constants.RETURNS_KEY)) {
            linearLayoutPolicies.setVisibility(View.GONE);
            linearLayoutTerms.setVisibility(View.GONE);
            textViewContent.setText(Html.fromHtml(ATPreferences.readString(context, Constants.RETURNS).trim()));
            titleName.setText("Return Policies");
        }
    }

    private void init() {
        back_ll = findViewById(R.id.back_ll);
        linearLayoutTerms = findViewById(R.id.linearLayoutTerms);
        linearLayoutPolicies = findViewById(R.id.linearLayoutPolicies);
        linearLayoutReturns = findViewById(R.id.linearLayoutReturns);
        webViewTerms = findViewById(R.id.webViewTerms);
        webViewPolicies = findViewById(R.id.webViewPolicies);
        webViewReturns = findViewById(R.id.webViewReturns);
        textViewContent = findViewById(R.id.textViewContent);
        titleName = findViewById(R.id.titleName);

      /*  webViewTerms.loadData(Utils.hexToASCII(ATPreferences.readString(context, Constants.TERMS).trim()), "text/html", "UTF-8");
        webViewPolicies.loadData(Utils.hexToASCII(ATPreferences.readString(context, Constants.POLICIES).trim()), "text/html", "UTF-8");
        webViewReturns.loadData(Utils.hexToASCII(ATPreferences.readString(context, Constants.RETURNS).trim()), "text/html", "UTF-8");
*/
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
