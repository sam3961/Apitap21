package com.apitap.app.views.fragments.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apitap.app.R;
import com.apitap.app.controller.ModelManager;
import com.apitap.app.model.Constants;
import com.apitap.app.model.Operations;
import com.apitap.app.model.Utils;
import com.apitap.app.model.customclasses.Event;
import com.apitap.app.model.preferences.ATPreferences;
import com.apitap.app.views.ForgotPasswordActivity;
import com.apitap.app.views.HomeActivity;
import com.apitap.app.views.TermsAndConditionsActivity;
import com.apitap.app.views.fragments.BaseFragment;
/*import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.CredentialsOptions;*/
import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ashok-kumar on 9/6/16.
 */

public class FragmentLogin extends BaseFragment implements View.OnClickListener {
    EditText editEmail, editPassword;
    ImageView imageViewHideShowPass;

    private boolean isPasswordVisible = false;
    Activity mActivity;
    TextView forgot_password, policy;
    private CheckBox checkBox;
    private LinearLayout rootLayout;
    private boolean isTermsClicked;
    private String firebaseToken = "";
    private String TAG = FragmentLogin.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mActivity = getActivity();
        initViews(v);
        setListeners(v);
        fetchToken();
        return v;
    }

    private void fetchToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    firebaseToken = task.getResult();
                    Log.d(TAG, "firebaseToken: "+firebaseToken);

                });
    }

    private void setListeners(View v) {
        v.findViewById(R.id.login).setOnClickListener(this);
        v.findViewById(R.id.txtForgotPassword).setOnClickListener(this);
        v.findViewById(R.id.create_password).setOnClickListener(this);
        v.findViewById(R.id.imageViewHideShowPass).setOnClickListener(this);
    }

    private void initViews(View v) {
        editEmail = (EditText) v.findViewById(R.id.editEmail);
        imageViewHideShowPass =  v.findViewById(R.id.imageViewHideShowPass);
        editPassword = (EditText) v.findViewById(R.id.editPassword);
        forgot_password = (TextView) v.findViewById(R.id.create_password);
        policy = (TextView) v.findViewById(R.id.policy);
        checkBox = (CheckBox) v.findViewById(R.id.checkbox);
        rootLayout = v.findViewById(R.id.rootLayout);
        setSpan();
        checkBoxlistener();
        getCredentials();
        getTermsConditions();
    }

    private void getTermsConditions() {
        ModelManager.getInstance().getLoginManager().getTermsAndConditions(getActivity(),
                Operations.makeJsonGetTermsConditions(getActivity()));
    }

    private void checkBoxlistener() {
        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
                ATPreferences.putString(getActivity(), Constants.StaySignedIn, "true");
            else
                ATPreferences.putString(getActivity(), Constants.StaySignedIn, "false");

        });
    }

    private void setSpan() {
        SpannableString ss = new SpannableString(getContext().getResources().getString(R.string.privacy));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (!ATPreferences.readString(getActivity(), Constants.TERMS).isEmpty())
                    startActivity(new Intent(getContext(), TermsAndConditionsActivity.class));
                else {
                    showProgress();
                    isTermsClicked = true;
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getContext().getResources().getColor(R.color.colorBlue));
            }
        };
        ss.setSpan(clickableSpan, 31, 53, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        policy.setText(ss);
        policy.setMovementMethod(LinkMovementMethod.getInstance());
        policy.setHighlightColor(Color.TRANSPARENT);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.LOGIN_SUCCESS:
                if (event.getResponse().equals("39")) { //login from temp password
                    ATPreferences.putBoolean(requireContext(), Constants.LOGIN_FROM_TEMP_PASS, true);
                } else {
                    ATPreferences.putBoolean(requireContext(), Constants.LOGIN_FROM_TEMP_PASS, false);
                }
                saveCredentials();
                ModelManager.getInstance().getLoginManager().registerFCM(getActivity(),
                        Operations.makeJsonGetFCM(getActivity(), firebaseToken));
                hideProgress();
                startActivity(new Intent(getActivity(), HomeActivity.class));
                break;
            case Constants.LOGIN_Failure:
                hideProgress();
                Utils.baseshowFeedbackMessage(getActivity(), rootLayout, event.getResponse() + "");
                break;

            case Constants.ACCOUNT_NOT_REGISTERED:
                break;
            case Constants.FCM_REGISTERED:
                break;
            case Constants.TERMS_CONDITIONS:
                hideProgress();
                if (isTermsClicked)
                    startActivity(new Intent(getActivity(), TermsAndConditionsActivity.class));
                break;
        }
    }

    private void saveCredentials() {
        ATPreferences.putString(getActivity(), Constants.USER_MAIL, editEmail.getText().toString());
        ATPreferences.putString(getActivity(), Constants.USER_PASSWORD, editPassword.getText().toString());

     /*   Credential credential = new Credential.Builder(editEmail.getText().toString())
                .setPassword(editPassword.getText().toString())  // Important: only store passwords in this field.
                .build();

        CredentialsOptions options = new CredentialsOptions.Builder()
                .forceEnableSaveDialog()
                .build();

        CredentialsClient credentialsClient = Credentials.getClient(getActivity(), options);

        credentialsClient.save(credential);*/


    }

    private void getCredentials() {
        if (!ATPreferences.readString(getActivity(), Constants.USER_MAIL).isEmpty()) {
            editEmail.setText(ATPreferences.readString(getActivity(), Constants.USER_MAIL));
            editPassword.setText(ATPreferences.readString(getActivity(), Constants.USER_PASSWORD));
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewHideShowPass:
                togglePasswordVisibility();
                break;
            case R.id.login:
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                if (Utils.isEmpty(email) || Utils.isEmpty(password)) {
                    Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Please fill all details");
                } else {
                    ATPreferences.putBoolean(getActivity(), Constants.GUEST, false);
                    showProgress();
                    ModelManager.getInstance().getLoginManager()
                            .doLogin(mActivity, Operations.makeJsonUserLogin(mActivity, email, password));
                }
                break;
            case R.id.txtForgotPassword:
                break;
            case R.id.create_password:
                startActivity(new Intent(getContext(), ForgotPasswordActivity.class).putExtra("title", "Create Password"));
                break;
        }
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide the password
            editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imageViewHideShowPass.setImageResource(R.drawable.ic_hide_pass);
        } else {
            // Show the password
            editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imageViewHideShowPass.setImageResource(R.drawable.ic_show_pass);
        }
        // Move the cursor to the end of the text
        editPassword.setSelection(editPassword.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }
}
