package com.apitap.views;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.ProgressDialogLoading;
import com.apitap.model.Utils;
import com.apitap.model.customclasses.Event;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by apple on 10/08/16.
 */
public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {

    EditText editEmail;
    RelativeLayout forgotpassword, rootLayout;
    LinearLayout back_ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorGreenLogo));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.forgotpassword_activity);
        initView();
    }

    private void initView() {

        editEmail = findViewById(R.id.editEmail);
        forgotpassword = findViewById(R.id.forgotpassword);
        rootLayout = findViewById(R.id.rootLayout);
        back_ll = findViewById(R.id.back_ll);
        forgotpassword.setOnClickListener(this);
        back_ll.setOnClickListener(this);
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case -1:
                hideProgress();
                Utils.baseshowFeedbackMessage(this, rootLayout, event.getResponse());
                break;
            case Constants.FORGOT_PASSWORD_SUCCESS:
                hideProgress();
                Utils.baseshowFeedbackMessage(ForgotPasswordActivity.this, rootLayout, "Please check your email address for temporary password.");
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 800);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgotpassword:
                String email = editEmail.getText().toString();
                Utils.hideKeyboardFrom(this, rootLayout);
                if (Utils.isEmpty(email)) {
                    //Toast.makeText(ForgotPasswordActivity.this, "please fill email id", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(ForgotPasswordActivity.this, rootLayout, "Please enter email address");

                } else if (!Utils.validEmail(email)) {
                    //Toast.makeText(ForgotPasswordActivity.this, "please fill email id", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(ForgotPasswordActivity.this, rootLayout, "Please enter valid email address");

                } else {
                    showProgress();
                    ModelManager.getInstance().getforgotManager().getForgotPassword(ForgotPasswordActivity.this,
                            Operations.makeJsonUserForgotPassword(ForgotPasswordActivity.this, email));
                }
                break;
            case R.id.back_ll:
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
