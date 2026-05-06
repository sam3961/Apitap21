package com.apitap.app.views.fragments;

import static com.apitap.app.views.fragments.specials.utils.CommonFunctions.hideKeyboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.apitap.app.R;
import com.apitap.app.controller.ModelManager;
import com.apitap.app.model.Client;
import com.apitap.app.model.Constants;
import com.apitap.app.model.Operations;
import com.apitap.app.model.Utils;
import com.apitap.app.model.customclasses.Event;
import com.apitap.app.views.LoginActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class FragmentSignup extends BaseFragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "FragmentSignup";

    Activity mActivity;
    Spinner mSpinner;
    EditText mFirstName, mLastName, mEmail, mPassword, mConfirm_Password,mConfirmEmail;
    ImageView imageViewHideShowPass, imageViewHideShowConfirmPass;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    EditText etAddress1,etAddress2, etZip, etCity, etState, etAddressNickname;
    String gender = "51";

    String latitude = "";
    String longitude = "";
    String originalStreetForLatLng = "";
    String countryCode = "";

    LinearLayout rootLayout;
    EditText editTextDOB;
    TextView textViewErrorAddress, textViewErrorZip;

    boolean isAddressValid = false;
    boolean isZipValid = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        mActivity = getActivity();
        initViews(v);
        setListeners(v);
        textWatcher();
        setGenderSpinner();
        return v;
    }

    private void initViews(View v) {
        mFirstName = v.findViewById(R.id.first_name);
        imageViewHideShowPass = v.findViewById(R.id.imageViewHideShowPass);
        imageViewHideShowConfirmPass = v.findViewById(R.id.imageViewHideShowConfirmPass);
        mLastName = v.findViewById(R.id.second_name);
        mEmail = v.findViewById(R.id.email);
        mConfirmEmail = v.findViewById(R.id.confirm_email);
        mPassword = v.findViewById(R.id.password);
        mConfirm_Password = v.findViewById(R.id.confirm_password);
        mSpinner = v.findViewById(R.id.spinner);
        rootLayout = v.findViewById(R.id.rootLayout);
        editTextDOB = v.findViewById(R.id.editTextDOB);
        textViewErrorAddress = v.findViewById(R.id.textViewErrorAddress);
        textViewErrorZip = v.findViewById(R.id.textViewErrorZip);
        etAddress1 = v.findViewById(R.id.address1);
        etAddress2 = v.findViewById(R.id.address2);
        etZip = v.findViewById(R.id.zipcode);
        etCity = v.findViewById(R.id.city);
        etState = v.findViewById(R.id.state);
        etAddressNickname = v.findViewById(R.id.addressNickname);
    }

    private void setListeners(View v) {
        v.findViewById(R.id.create_account).setOnClickListener(this);
        v.findViewById(R.id.imageViewHideShowPass).setOnClickListener(this);
        v.findViewById(R.id.imageViewHideShowConfirmPass).setOnClickListener(this);
        v.findViewById(R.id.editTextDOB).setOnClickListener(this);
        v.findViewById(R.id.imageViewMap).setOnClickListener(this);
        mSpinner.setOnItemSelectedListener(this);
    }

    private void setGenderSpinner() {
        List<String> categories = new ArrayList<>();
        categories.add("Male");
        categories.add("Female");

        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item, categories);
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
                Utils.baseshowFeedbackMessage(getActivity(), rootLayout, event.getResponse() + "");
                break;

            case Constants.ACCOUNT_CREATED:
                hideProgress();
                showEmailConfirmDialog();
                break;

            case Constants.GET_SERVER_ERROR:
                hideProgress();
                Utils.baseshowFeedbackMessage(getActivity(), rootLayout, event.getResponse() + "");
                break;

            case Constants.ACCOUNT_NOT_REGISTERED:
                validateAndCreateAccount();
                break;

            case -1:
                hideProgress();
                Utils.baseshowFeedbackMessage(getActivity(), rootLayout,  "Something went wrong.");
                break;
        }
    }

    private boolean validateFormOnly() {
        String email = mEmail.getText().toString().trim();
        String confirmEmail = mConfirmEmail.getText().toString().trim();
        String firstName = mFirstName.getText().toString().trim();
        String lastName = mLastName.getText().toString().trim();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirm_Password.getText().toString();
        String dob = editTextDOB.getText().toString().trim();

        String address = etAddress1.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String state = etState.getText().toString().trim();
        String zip = etZip.getText().toString().trim();
        String addressNickname = etAddressNickname.getText().toString().trim();

        if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty()
                || dob.isEmpty() || confirmEmail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Please fill all fields");
            return false;
        }

        if (!Utils.validEmail(email)) {
            Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Please enter valid email address");
            return false;
        }

        if (!email.equalsIgnoreCase(confirmEmail)) {
            Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Email and Confirm Email do not match");
            return false;
        }

        if (address.isEmpty() || city.isEmpty() || state.isEmpty() || zip.isEmpty()) {
            Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Please enter address properly");
            return false;
        }

        if (zip.length() < 5) {
            Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "ZIP must be exactly 5 digits.");
            return false;
        }

        if (addressNickname.isEmpty()) {
            Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Please enter an address nickname.");
            return false;
        }

        if (!isAddressValid || !isZipValid || latitude.isEmpty() || longitude.isEmpty()) {
            Utils.baseshowFeedbackMessage(getActivity(), rootLayout,
                    "We couldn’t verify this address. Please enter a valid one to continue.");
            return false;
        }

        if (!Utils.isValidPassword(password, mPassword)) {
            Utils.baseshowFeedbackMessage(getActivity(), rootLayout,
                    "Password must be 8 digits and contain at least: 1 CAP letter,a number,a special character");
            return false;
        }

        if (!Utils.isValidPassword(confirmPassword, mConfirm_Password)) {
            Utils.baseshowFeedbackMessage(getActivity(), rootLayout,
                    "Password must be 8 digits and contain at least: 1 CAP letter,a number,a special character");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Password and Confirm Password not Same");
            return false;
        }

        return true;
    }

    private void validateAndCreateAccount() {
        String email = mEmail.getText().toString().trim();
        String firstName = mFirstName.getText().toString().trim();
        String lastName = mLastName.getText().toString().trim();
        String password = mPassword.getText().toString();
        String dob = editTextDOB.getText().toString().trim();

        String address = etAddress1.getText().toString().trim();
        String addressSecondLine = etAddress2.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String state = etState.getText().toString().trim();
        String zip = etZip.getText().toString().trim();
        String addressNickname = etAddressNickname.getText().toString().trim();

        ModelManager.getInstance().getSignUpManager().postUserDetails(
                mActivity,
                Operations.makeJsonUserSignup(
                        mActivity,
                        email,
                        Utils.convertStringToHex(firstName),
                        Utils.convertStringToHex(lastName),
                        Utils.convertStringToHex(password),
                        dob.replace("/", "-"),
                        address,
                        addressSecondLine,
                        city,
                        state,
                        zip,
                        addressNickname,
                        latitude,
                        longitude
                ),
                false
        );
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
            case R.id.imageViewMap:
                MapPickerDialog dialog = new MapPickerDialog((address, lat, lng) -> {
                    resetValidationState();

                    etAddress1.setText(address.getAddressLine(0) != null ? address.getAddressLine(0) : "");
                    etCity.setText(address.getLocality() != null ? address.getLocality() : "");
                    etState.setText(address.getAdminArea() != null ? address.getAdminArea() : "");
                    etZip.setText(address.getPostalCode() != null ? address.getPostalCode() : "");
                    countryCode = normalizeCountryCode(address.getCountryCode());
                    originalStreetForLatLng = getStreetOnly(etAddress1.getText().toString().trim());

                    callAddressApi(
                            etAddress1.getText().toString().trim(),
                            etCity.getText().toString().trim(),
                            etState.getText().toString().trim()
                    );
                });
                dialog.show(getChildFragmentManager(), "MapPicker");
                break;

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

                if (!validateFormOnly()) {
                    return;
                }

                String email = mEmail.getText().toString().trim();

                showProgress();
                ModelManager.getInstance().getSignUpManager().postUserDetails(
                        mActivity,
                        Operations.makeJsonValidateUser(mActivity, email),
                        true
                );
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

    private void textWatcher() {
        etAddress1.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String address = etAddress1.getText().toString().trim();
                if (!address.isEmpty()) {
                    originalStreetForLatLng = getStreetOnly(address);
                    callAddressApi(address, etCity.getText().toString().trim(), etState.getText().toString().trim());
                }
            }
        });

        etAddress1.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetAddressValidationOnly();
                textViewErrorAddress.setVisibility(View.GONE);
            }
        });

        etZip.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetValidationState();
                textViewErrorZip.setVisibility(View.GONE);
                textViewErrorAddress.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String zip = editable.toString().trim();
                if (zip.length() == 5 && etZip.hasFocus()) {
                    callZipApi(zip);
                }
            }
        });
    }

    private void resetValidationState() {
        isZipValid = false;
        isAddressValid = false;
        countryCode = "";
        resetLatLng();
    }

    private void resetAddressValidationOnly() {
        isAddressValid = false;
        resetLatLng();
    }

    private void resetLatLng() {
        latitude = "";
        longitude = "";
    }

    private void callAddressApi(String street, String city, String state) {
        try {
            street = street == null ? "" : street.trim();
            city = city == null ? "" : city.trim();
            state = state == null ? "" : state.trim();

            if (!street.isEmpty() && street.contains(",")) {
                String[] split = street.split(",");

                if (split.length >= 1) street = split[0].trim();
                if (split.length >= 2 && city.isEmpty()) city = split[1].trim();
                if (split.length >= 3 && state.isEmpty()) {
                    String statePart = split[2].trim();
                    String[] stateTokens = statePart.split(" ");
                    if (stateTokens.length > 0) {
                        state = stateTokens[0].trim();
                    }
                }
            }

            String url = Client.BASE_URL_ONLY + "api/external/zipcode?streetAddress="
                    + URLEncoder.encode(street, "UTF-8")
                    + "&city=" + URLEncoder.encode(city, "UTF-8")
                    + "&state=" + URLEncoder.encode(state, "UTF-8");

            new GetAddressApiTask("ADDRESS").execute(url);

        } catch (Exception e) {
            Log.e(TAG, "callAddressApi error", e);
        }
    }

    private void callZipApi(String zip) {
        try {
            String url = Client.BASE_URL_ONLY + "api/external/city-state?ZIPCode="
                    + URLEncoder.encode(zip, "UTF-8");
            new GetAddressApiTask("ZIP").execute(url);
        } catch (Exception e) {
            Log.e(TAG, "callZipApi error", e);
        }
    }


    private void callLatLongApi(String userStreet, String city, String state, String zip) {
        try {
            String locationQuery = getStreetOnly(userStreet) + ", " + zip.trim() + ", " + getLocationCountryCode();

            String url = Client.BASE_URL_ONLY + "api/external/sugestLocation?format=json&q="
                    + URLEncoder.encode(locationQuery, "UTF-8");

            Log.d("LATLNG_QUERY", "Query = " + locationQuery);
            Log.d("LATLNG_QUERY", "URL = " + url);

            new GetAddressApiTask("LATLNG").execute(url);

        } catch (Exception e) {
            Log.e("LATLNG_QUERY", "Error", e);
        }
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
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month = month + 1;
        String monthValue = String.valueOf(month);
        String dayValue = String.valueOf(day);

        if (monthValue.length() == 1) monthValue = "0" + monthValue;
        if (dayValue.length() == 1) dayValue = "0" + dayValue;

        editTextDOB.setText(year + "/" + monthValue + "/" + dayValue);
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imageViewHideShowPass.setImageResource(R.drawable.ic_hide_pass);
        } else {
            mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imageViewHideShowPass.setImageResource(R.drawable.ic_show_pass);
        }
        mPassword.setSelection(mPassword.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

    private void toggleConfirmPasswordVisibility() {
        if (isConfirmPasswordVisible) {
            mConfirm_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imageViewHideShowConfirmPass.setImageResource(R.drawable.ic_hide_pass);
        } else {
            mConfirm_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imageViewHideShowConfirmPass.setImageResource(R.drawable.ic_show_pass);
        }
        mConfirm_Password.setSelection(mConfirm_Password.getText().length());
        isConfirmPasswordVisible = !isConfirmPasswordVisible;
    }

    private class GetAddressApiTask extends AsyncTask<String, Void, String> {

        String type;

        GetAddressApiTask(String type) {
            showProgress();
            this.type = type;
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection conn = null;
            BufferedReader in = null;

            try {
                Log.d("API_REQUEST", "URL: " + urls[0]);
                Log.d("API_REQUEST", "Method: GET");

                URL url = new URL(urls[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("TimeZone", TimeZone.getDefault().getID());

                int responseCode = conn.getResponseCode();
                Log.d("API_RESPONSE", "Response Code: " + responseCode);

                if (responseCode >= 200 && responseCode < 300) {
                    in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    if (conn.getErrorStream() != null) {
                        in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    }
                }

                if (in == null) return null;

                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                Log.d("API_RESPONSE", "Response Body: " + response);
                return response.toString();

            } catch (Exception e) {
                Log.e("API_ERROR", "Exception: ", e);
                return null;
            } finally {
                try {
                    if (in != null) in.close();
                } catch (Exception ignored) {
                }
                if (conn != null) conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgress();
            Log.d("GetAddressApiTask", "onPostExecute type=" + type + " result=" + result);

            if (!isAdded()) return;

            try {
                if (result == null || result.trim().isEmpty()) {
                    throw new Exception("Empty response");
                }

                if ("ZIP".equals(type)) {
                    handleZipResponse(result);
                } else if ("ADDRESS".equals(type)) {
                    handleAddressResponse(result);
                } else if ("LATLNG".equals(type)) {
                    handleLatLngResponse(result);
                }

            } catch (Exception e) {
                Log.e(TAG, "onPostExecute parse error type=" + type, e);

                if ("ZIP".equals(type)) {
                    isZipValid = false;
                    etZip.clearFocus();
                    hideKeyboard(etZip);
                    etCity.setText("");
                    etState.setText("");
                    textViewErrorZip.setVisibility(View.VISIBLE);
                    resetLatLng();

                } else if ("ADDRESS".equals(type)) {
                    isAddressValid = false;
                    resetLatLng();
                    if (!etZip.getText().toString().trim().isEmpty()) {
                        textViewErrorAddress.setVisibility(View.VISIBLE);
                    }

                } else if ("LATLNG".equals(type)) {
                    resetLatLng();
                    if (!etAddress1.getText().toString().trim().isEmpty()) {
                        textViewErrorAddress.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        private void handleZipResponse(String result) throws Exception {
            JSONObject json = new JSONObject(result);

            updateCountryCode(json);

            String city = json.optString("city", "").trim();
            String state = json.optString("state", "").trim();

            if (city.isEmpty() || state.isEmpty()) {
                throw new Exception("Invalid ZIP response");
            }

            etZip.clearFocus();
            hideKeyboard(etZip);

            etCity.setText(city);
            etState.setText(state);

            isZipValid = true;
            textViewErrorZip.setVisibility(View.GONE);

            // Correct flow:
            // ZIP -> city/state
            // if address exists, validate address next
            String street = etAddress1.getText().toString().trim();
            if (!street.isEmpty()) {
                callAddressApi(street, city, state);
            }
        }

        private void handleAddressResponse(String result) throws Exception {
            JSONObject json = new JSONObject(result);
            JSONObject address = json.getJSONObject("address");

            updateCountryCode(json);
            updateCountryCode(address);

            String validatedStreet = address.optString("streetAddress", "").trim();
            String validatedCity = address.optString("city", "").trim();
            String validatedState = address.optString("state", "").trim();
            String validatedZip = address.optString("ZIPCode", "").trim();

            if (validatedStreet.isEmpty() || validatedCity.isEmpty()
                    || validatedState.isEmpty() || validatedZip.isEmpty()) {
                throw new Exception("Invalid address response");
            }

            originalStreetForLatLng = getStreetOnly(etAddress1.getText().toString().trim());

            etAddress1.setText(validatedStreet);
            etCity.setText(validatedCity);
            etState.setText(validatedState);
            etZip.setText(validatedZip);

            isAddressValid = true;
            isZipValid = true;

            textViewErrorAddress.setVisibility(View.GONE);
            textViewErrorZip.setVisibility(View.GONE);

            resetLatLng();

            // IMPORTANT: use user-entered street, not validated street
            callLatLongApi(originalStreetForLatLng, validatedCity, validatedState, validatedZip);
        }

        private void handleLatLngResponse(String result) throws Exception {
            JSONArray jsonArray = new JSONArray(result);

            if (jsonArray.length() <= 0) {
                throw new Exception("Empty lat/lng response");
            }

            // Business rule: always use first item only
            JSONObject obj = jsonArray.getJSONObject(0);

            String lat = obj.optString("lat", "").trim();
            String lon = obj.optString("lon", "").trim();

            if (lat.isEmpty() || lon.isEmpty()) {
                throw new Exception("Missing lat/lon");
            }

            latitude = lat;
            longitude = lon;

            textViewErrorAddress.setVisibility(View.GONE);

            Log.d("LATLNG", "Lat: " + latitude + ", Lon: " + longitude);
        }
    }

    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private String getStreetOnly(String fullAddress) {
        if (fullAddress == null) return "";

        fullAddress = fullAddress.trim();
        if (fullAddress.isEmpty()) return "";

        String[] parts = fullAddress.split(",");

        if (parts.length > 0) {
            return parts[0].trim();
        }

        return fullAddress;
    }

    private void updateCountryCode(JSONObject json) {
        if (json == null) return;

        String code = json.optString("countryCode", "").trim();
        if (code.isEmpty()) code = json.optString("country_code", "").trim();
        if (code.isEmpty()) code = json.optString("country", "").trim();

        code = normalizeCountryCode(code);
        if (!code.isEmpty()) {
            countryCode = code;
        }
    }

    private String getLocationCountryCode() {
        String code = normalizeCountryCode(countryCode);
        return code.isEmpty() ? "US" : code;
    }

    private String normalizeCountryCode(String code) {
        if (code == null) return "";

        code = code.trim();
        if (code.isEmpty()) return "";

        if ("USA".equalsIgnoreCase(code)
                || "United States".equalsIgnoreCase(code)
                || "EE. UU.".equalsIgnoreCase(code)) {
            return "US";
        }

        if (code.length() == 2) {
            return code.toUpperCase(Locale.US);
        }

        return "";
    }
}
