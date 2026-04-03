package com.apitap.views.fragments;

import static android.view.View.NOT_FOCUSABLE;
import static com.apitap.views.fragments.specials.utils.CommonFunctions.hideKeyboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
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

import com.apitap.App;
import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.customclasses.Event;
import com.apitap.views.ForgotPasswordActivity;
import com.apitap.views.LoginActivity;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceTypes;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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

    EditText etAddress1, etZip, etCity, etState, etAddressNickname;
    String gender = "51";
    //    String latitude = "27.9916640", longitude= "-81.6884960";
    String latitude = "", longitude = "";
    LinearLayout rootLayout;
    EditText editTextDOB;
    TextView textViewErrorAddress, textViewErrorZip;
    boolean isAddressValid = false;
    boolean isZipValid = false;
    boolean isLatLngCalled = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        mActivity = getActivity();
        initViews(v);
        setListeners(v);
        textWatcher();
        setGenderSpinner();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
//                fillAddressFromLatLng(App.latitude, App.longitude);
            }
        }, 2000);

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
        textViewErrorAddress = v.findViewById(R.id.textViewErrorAddress);
        textViewErrorZip = v.findViewById(R.id.textViewErrorZip);
        etAddress1 = v.findViewById(R.id.address1);
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
                ModelManager.getInstance().getSignUpManager().saveAddress(mActivity,
                        Operations.makeJsonSaveAddress(mActivity,
                                event.getResponse(), etAddress1.getText().toString(),
                                etCity.getText().toString(),
                                etState.getText().toString(),
                                etZip.getText().toString(), latitude, longitude, etAddressNickname.getText().toString()));
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

                else if (etAddress1.getText().toString().isEmpty() ||
                        etCity.getText().toString().isEmpty() ||
                        etState.getText().toString().isEmpty() ||
                        etZip.getText().toString().isEmpty())
                    Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Please enter address properly");
                else if (etZip.getText().toString().length() < 5) {
                    Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "ZIP must be exactly 5 digits.");
                } else
                    ModelManager.getInstance().getSignUpManager().postUserDetails(mActivity, Operations.makeJsonUserSignup(
                            mActivity,
                            email,
                            Utils.convertStringToHex(firstName),
                            Utils.convertStringToHex(lastName),
                            Utils.convertStringToHex(password),
                            dob.replace("/", "-"),
                            etAddress1.getText().toString(),
                            etCity.getText().toString(),
                            etState.getText().toString(),
                            etZip.getText().toString(),
                            etAddressNickname.getText().toString(),
                            latitude,
                            longitude
                    ), false);
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
            case R.id.imageViewMap:
//                startAddressPickup();
                MapPickerDialog dialog = new MapPickerDialog((address, lat, lng) -> {

                    // 🔥 Fill UI
//                    etAddress1.setText("5860 Cypress Gardens Blvd, Winter Haven, FL");
                    etAddress1.setText(address.getAddressLine(0));
                    etCity.setText(address.getLocality());
                    etState.setText(address.getAdminArea());
                    etZip.setText(address.getPostalCode());

                    // 🔥 Save lat/lng
//                    selectedLat = lat;
//                    selectedLng = lng;
                    latitude = String.valueOf(lat);
                    longitude = String.valueOf(lng);

                    callAddressApi(etAddress1.getText().toString(), etCity.getText().toString(), etState.getText().toString());

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
                String email = mEmail.getText().toString();
                String firstName = mFirstName.getText().toString();
                String lastName = mLastName.getText().toString();
                String cityName = etCity.getText().toString();
                String address = etAddress1.getText().toString();
                String stateName = etState.getText().toString();
                String zipCode = etZip.getText().toString();
                String addressNickname = etAddressNickname.getText().toString();
                if (Utils.isEmpty(email) || Utils.isEmpty(firstName) || Utils.isEmpty(lastName)) {
                    Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Please fill all details");
                } else if (latitude.isEmpty() || longitude.isEmpty() || cityName.isEmpty() || address.isEmpty() || stateName.isEmpty() || zipCode.isEmpty()) {
                    Utils.baseshowFeedbackMessage(getActivity(), rootLayout,
                            "We couldn’t verify this address. Please enter a valid one to continue.");
                } else if (addressNickname.isEmpty()) {
                    Utils.baseshowFeedbackMessage(getActivity(), rootLayout, "Please enter an address nickname.");
                } else {
                    ModelManager.getInstance().getSignUpManager().postUserDetails(mActivity,
                            Operations.makeJsonValidateUser(mActivity, email), true);
                    showProgress();
                }
                break;
        }
    }

/*
    private void startAddressPickup() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
//        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.FORMATTED_ADDRESS);
        List<Place.Field> fields = Arrays.asList(
                Place.Field.ID,
                Place.Field.FORMATTED_ADDRESS,
                Place.Field.ADDRESS_COMPONENTS
        );
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .setTypesFilter(List.of(PlaceTypes.ESTABLISHMENT))
                .build(requireActivity());
        startAutocomplete.launch(intent);


    }

    private final ActivityResultLauncher<Intent> startAutocomplete =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {

                            Place place = Autocomplete.getPlaceFromIntent(result.getData());

                            String address = place.getFormattedAddress();
                            etAddress1.setText(address);

                            // 🔥 Extract components
                            String street = "";
                            String city = "";
                            String state = "";

                            for (AddressComponent component : place.getAddressComponents().asList()) {

                                if (component.getTypes().contains("route")) {
                                    street += component.getName() + " ";
                                }

                                if (component.getTypes().contains("street_number")) {
                                    street = component.getName() + " " + street;
                                }

                                if (component.getTypes().contains("locality")) {
                                    city = component.getName();
                                }

                                if (component.getTypes().contains("administrative_area_level_1")) {
                                    state = component.getShortName(); // FL, CA etc
                                }
                            }

                            // 🔥 Call YOUR API (IMPORTANT)
                            callAddressApi(street.trim(), city, state);

                        }
                    }
            );

*/

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
                String address = etAddress1.getText().toString();
                if (!address.isEmpty()) {
                    callAddressApi(address, "", "");
                }
            }
        });

/*        etZip.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String zip = etZip.getText().toString();
                if (!zip.isEmpty()) {
                    callZipApi(zip);
//                    ModelManager.getInstance().getSignUpManager().getZipCode(requireContext(), Operations.makeJsonZipLookup(requireActivity(), zip));

                }
            }
        });*/

        etZip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 5 && etZip.hasFocus()) {
                    callZipApi(editable.toString());
                }
            }
        });

/*        etAddress1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 5) {
                    callZipApi(editable.toString());
                }
            }
        });*/
    }

    private void callAddressApi(String street, String city, String state) {
        try {

            if (street.isEmpty()) street = "";
            if (city.isEmpty()) city = "";
            if (state.isEmpty()) state = "";

            // 🔥 Only split if REALLY needed
            if (!street.isEmpty() && street.contains(",")) {
                String[] split = street.split(",");

                if (split.length >= 1) street = split[0].trim();
                if (split.length >= 2 /*&& city.isEmpty()*/) city = split[1].trim();
                if (split.length >= 3 /*&& state.isEmpty()*/) state = split[2].trim().split(" ")[0];
            }

            // 🔥 Proper encoding
            String url = Client.BASE_URL_ONLY + "api/external/zipcode?streetAddress="
                    + URLEncoder.encode(street, "UTF-8")
                    + "&city=" + URLEncoder.encode(city, "UTF-8")
                    + "&state=" + URLEncoder.encode(state, "UTF-8");

            new GetAddressApiTask("ADDRESS").execute(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callZipApi(String zip) {
        String url = Client.BASE_URL_ONLY + "api/external/city-state?ZIPCode=" + zip;
        new GetAddressApiTask("ZIP").execute(url);
    }

    private void callLatLongApi(String address) {
        String url = "";
        try {
            url = Client.BASE_URL_ONLY + "api/external/sugestLocation?q="
                    + URLEncoder.encode(address, "UTF-8")
                    + "&format=json";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        new GetAddressApiTask("LATLNG").execute(url);
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


    private class GetAddressApiTask extends AsyncTask<String, Void, String> {

        String type;

        GetAddressApiTask(String type) {
            showProgress();
            this.type = type;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                Log.d("API_REQUEST", "URL: " + urls[0]);
                Log.d("API_REQUEST", "Method: GET");

                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("TimeZone", TimeZone.getDefault().getID());

                Log.d("API_REQUEST", "Headers: Accept=application/json, TimeZone=" + TimeZone.getDefault().getID());

                int responseCode = conn.getResponseCode();
                Log.d("API_RESPONSE", "Response Code: " + responseCode);

                BufferedReader in;
                if (responseCode >= 200 && responseCode < 300) {
                    in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Log.d("API_RESPONSE", "Response Body: " + response.toString());

                return response.toString();

            } catch (Exception e) {
                Log.e("API_ERROR", "Exception: ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgress();
            Log.d("GetAddressApiTask", "onPostExecute: " + result);
            try {
                if (type.equals("LATLNG")) {

                    JSONArray jsonArray = new JSONArray(result);

                    if (jsonArray.length() > 0) {

                        JSONObject obj = jsonArray.getJSONObject(0);

                        latitude = obj.getString("lat");
                        longitude = obj.getString("lon");

                        textViewErrorAddress.setVisibility(View.GONE);

                        Log.d("LATLNG", "Lat: " + latitude + ", Lon: " + longitude);

                    }
                } else if (type.equals("ZIP")) {
                    JSONObject json = new JSONObject(result);
                    etZip.clearFocus();
                    hideKeyboard(etZip);

                    etCity.setText(json.getString("city"));
                    etState.setText(json.getString("state"));
                    textViewErrorZip.setVisibility(View.GONE);

                    isZipValid = true;
                    if (!etAddress1.getText().toString().isEmpty() && !etZip.getText().toString().isEmpty()) {
                        callLatLongApi(etAddress1.getText().toString());
                    }

                } else {
                    JSONObject json = new JSONObject(result);
                    JSONObject address = json.getJSONObject("address");
                    etAddress1.setText(address.getString("streetAddress"));
                    etCity.setText(address.getString("city"));
                    etState.setText(address.getString("state"));
                    etZip.setText(address.getString("ZIPCode"));

                    isAddressValid = true;

                    if (!etAddress1.getText().toString().isEmpty()&& !etZip.getText().toString().isEmpty()) {
                        callLatLongApi(etAddress1.getText().toString());
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
//                baseshowFeedbackMessage(requireActivity(), rootLayout, "No data found.");

                if (type.equals("ZIP")) {
                    etZip.clearFocus();
                    hideKeyboard(etZip);

                    etCity.setText("");
                    etState.setText("");
                    textViewErrorZip.setVisibility(View.VISIBLE);

                } else if (type.equals("ADDRESS")) {
                    if (!etZip.getText().toString().isEmpty())
                        textViewErrorAddress.setVisibility(View.VISIBLE);

                }else if (type.equals("LATLNG")) {
                    if (!etAddress1.getText().toString().isEmpty())
                        textViewErrorAddress.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void fillAddressFromLatLng(double lat, double lng) {

        new Thread(() -> {
            try {

                if (!Geocoder.isPresent()) {
                    Log.e("TAG", "Geocoder not available on this device");
                    return;
                }

                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                Log.d("TAG", "Lat: " + lat + ", Lng: " + lng);

                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

                if (addresses == null || addresses.isEmpty()) {
                    Log.e("TAG", "No address found!");
                    return;
                }

                Address address = addresses.get(0);

               /* requireActivity().runOnUiThread(() -> {
                    etAddress1.setText(address.getAddressLine(0));
                    etCity.setText(address.getLocality());
                    etState.setText(address.getAdminArea());
                    etZip.setText(address.getPostalCode());
                });
*/
                callAddressApi(address.getAddressLine(0), address.getLocality(), address.getAdminArea());
//                callAddressApi("5860+Cypress+Gardens+Blvd", "Winter+Haven", "FL");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
