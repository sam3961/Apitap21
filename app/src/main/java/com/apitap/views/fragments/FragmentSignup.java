package com.apitap.views.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.customclasses.Event;
import com.apitap.views.ForgotPasswordActivity;
import com.apitap.views.LoginActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by ashok-kumar on 9/6/16.
 */

public class FragmentSignup extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {
    Activity mActivity;
    Spinner mSpinner;
    EditText mFirstName, mLastName, mEmail, mPassword, mConfirm_Password;
    ImageView imageViewHideShowPass, imageViewHideShowConfirmPass;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    String gender = "51";
    LinearLayout rootLayout;
    EditText editTextDOB;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        mActivity = getActivity();
        initViews(v);
        setListeners(v);
        setGenderSpinner();
        return v;
    }

    private void initViews(View v) {
        mFirstName = v.findViewById(R.id.first_name);
        imageViewHideShowPass = v.findViewById(R.id.imageViewHideShowPass);
        imageViewHideShowConfirmPass = v.findViewById(R.id.imageViewHideShowConfirmPass);
        mLastName = v.findViewById(R.id.second_name);
        mEmail = v.findViewById(R.id.email);
        mPassword = v.findViewById(R.id.password);
        mConfirm_Password = v.findViewById(R.id.confirm_password);
        mSpinner = v.findViewById(R.id.spinner);
        rootLayout = v.findViewById(R.id.rootLayout);
        editTextDOB = v.findViewById(R.id.editTextDOB);
    }

    private void setListeners(View v) {
        v.findViewById(R.id.create_account).setOnClickListener(this);
        v.findViewById(R.id.imageViewHideShowPass).setOnClickListener(this);
        v.findViewById(R.id.imageViewHideShowConfirmPass).setOnClickListener(this);
        v.findViewById(R.id.editTextDOB).setOnClickListener(this);
        mSpinner.setOnItemSelectedListener(this);
    }

    private void setGenderSpinner() {
        List<String> categories = new ArrayList<String>();
        categories.add("Male");
        categories.add("Female");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);
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
            case Constants.ACCOUNT_ALREADY_REGISTERED:
                hideProgress();
                // Toast.makeText(mActivity, event.getResponse(), Toast.LENGTH_SHORT).show();
                Utils.baseshowFeedbackMessage(getActivity(), rootLayout, event.getResponse() + "");
                break;

            case Constants.ACCOUNT_CREATED:
                hideProgress();
                // Toast.makeText(mActivity, "Please Check your Mail for Confirm", Toast.LENGTH_SHORT).show();
                //Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Please Check your Mail for Confirm");
                //   startActivity(new Intent(getActivity(), LoginActivity.class));
                //   showAlertDialog("Please check your email to confirm your email address.");
                showEmailConfirmDialog();
                break;

            case Constants.GET_SERVER_ERROR:
                hideProgress();
                Utils.baseshowFeedbackMessage(getActivity(), rootLayout, event.getResponse() + "");
                break;

            case Constants.ACCOUNT_NOT_REGISTERED:
                String email = mEmail.getText().toString();
                String firstName = mFirstName.getText().toString();
                String lastName = mLastName.getText().toString();
                String password = mPassword.getText().toString();
                String confirmPassword = mConfirm_Password.getText().toString();
                String dob = editTextDOB.getText().toString();
                if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())
                    //  Toast.makeText(mActivity, "Please Fill all fields", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Please fill all fields");

                else if (!Utils.validEmail(email)) {
                    //Toast.makeText(ForgotPasswordActivity.this, "please fill email id", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Please enter valid email address");
                } else if (!Utils.isValidPassword(password, mPassword))
                    //  Toast.makeText(mActivity, "Password must be 8 digits and contain at least: 1 CAP letter,a number,a special character", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Password must be 8 digits and contain at least: 1 CAP letter,a number,a special character");
                else if (!Utils.isValidPassword(confirmPassword, mConfirm_Password))
                    //Toast.makeText(mActivity, "Password must be 8 digits and contain at least: 1 CAP letter,a number,a special character", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Password must be 8 digits and contain at least: 1 CAP letter,a number,a special character");

                else if (!password.equals(confirmPassword))
                    // Toast.makeText(mActivity, "Password and Confirm Password not Same", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Password and Confirm Password not Same");
                else
                    ModelManager.getInstance().getSignUpManager().postUserDetails(mActivity, Operations.makeJsonUserSignup
                            (mActivity, email,
                                    Utils.convertStringToHex(firstName), Utils.convertStringToHex(lastName),
                                    Utils.convertStringToHex(password),
                                    dob), false);
                break;
        }
    }

    private void showEmailConfirmDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setMessage("Please check your email to confirm your email address.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewHideShowConfirmPass:
                toggleConfirmPasswordVisibility();
                break;
            case R.id.imageViewHideShowPass:
                togglePasswordVisibility();
                break;
            case R.id.editTextDOB:
                showDatePicker();
                break;
            case R.id.create_account:
                Utils.dismissKeyboard(getActivity(), mConfirm_Password);
                String email = mEmail.getText().toString();
                String firstName = mFirstName.getText().toString();
                String lastName = mLastName.getText().toString();
                if (Utils.isEmpty(email) || Utils.isEmpty(firstName) || Utils.isEmpty(lastName))
                    // Toast.makeText(mActivity, "Please fill all details", Toast.LENGTH_SHORT).show();
                    Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Please fill all details");
                else {
                    ModelManager.getInstance().getSignUpManager().postUserDetails(mActivity,
                            Operations.makeJsonValidateUser(mActivity, email), true);
                    showProgress();
                }
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        gender = i == 0 ? "51" : "52";
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void showDatePicker() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                this,
                year, month, day
        );
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month = month + 1;
        String monthValue = String.valueOf(month);
        String dayValue = String.valueOf(day);

        if (String.valueOf(month).length() == 1)
            monthValue = "0" + month;

        if (String.valueOf(day).length() == 1)
            dayValue = "0" + day;

        Log.d("onDateSet", month + "/" + day + "/" + year);
        editTextDOB.setText(year + "/" + monthValue + "/" + dayValue);

    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide the password
            mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imageViewHideShowPass.setImageResource(R.drawable.ic_hide_pass);
        } else {
            // Show the password
            mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imageViewHideShowPass.setImageResource(R.drawable.ic_show_pass);
        }
        // Move the cursor to the end of the text
        mPassword.setSelection(mPassword.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

    private void toggleConfirmPasswordVisibility() {
        if (isConfirmPasswordVisible) {
            // Hide the password
            mConfirm_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imageViewHideShowConfirmPass.setImageResource(R.drawable.ic_hide_pass);
        } else {
            // Show the password
            mConfirm_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imageViewHideShowConfirmPass.setImageResource(R.drawable.ic_show_pass);
        }
        // Move the cursor to the end of the text
        mConfirm_Password.setSelection(mConfirm_Password.getText().length());
        isConfirmPasswordVisible = !isConfirmPasswordVisible;
    }
}
