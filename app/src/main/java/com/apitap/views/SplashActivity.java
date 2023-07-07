package com.apitap.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.LinearLayout;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.AppLocationManager;
import com.apitap.model.Constants;
import com.apitap.model.MyFirebaseMessagingService;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.preferences.ATPreferences;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


public class SplashActivity extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;
    private Handler handler;
    private Runnable runnable;
    private Activity mActivity;
    private LinearLayout linearLayoutRoot;

    private LocationSettingsRequest mLocationSettingsRequest;
    private SettingsClient mSettingsClient;
    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;
    private String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private AppLocationManager appLocationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorWhite));
        }
        setContentView(R.layout.activity_splash);
        linearLayoutRoot = findViewById(R.id.rootLayout);
        MyFirebaseMessagingService.badgeCount = 0;
        ModelManager.getInstance().getLoginManager().getLogin(this, Operations.makeJsonDefaultParams(
                SplashActivity.this));
        //  ModelManager.getInstance().getAddressManager().getlatlng(this);
        mActivity = this;
        Log.d("Utilsss", Utils.pxToDp(100) + "  " + Utils.pxToDp(400));
        // checkForPermissions();

        if (!hasPermissions(this, permission)) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.LOCATION_PERMISSION_CODE);
        } else {
            if (isGpsOn())
                handleSleep();
            else
                displayLocationSettingsRequest();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isGpsOn() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void displayLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(SplashActivity.this);

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Success Perform Task Here
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(SplashActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.e("GPS", "Unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Log.e("GPS", "Location settings are inadequate, and cannot be fixed here. Fix in FragmentSettings.");
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.e("GPS", "checkLocationSettings -> onCanceled");
                    }
                });
    }

    private void handleSleep() {
        appLocationManager = new AppLocationManager(this);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "run"+ATPreferences.readString(mActivity, Constants.StaySignedIn)+"     "+
                        ATPreferences.readString(mActivity, Constants.KEY_USERID));
                if (ATPreferences.readString(mActivity, Constants.StaySignedIn).equals("true")
                    && !ATPreferences.readString(mActivity, Constants.KEY_USERID).isEmpty()){
                    if (!Utils.isNetworkAvailable(mActivity)) {
                        Utils.baseshowFeedbackMessage(mActivity, linearLayoutRoot, "No Internet Connection");
                        return;
                    }
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.LOCATION_PERMISSION_CODE) {
            if (isGpsOn())
                handleSleep();
            else
                displayLocationSettingsRequest();
        }
    }

    private void openGpsEnableSetting() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, REQUEST_ENABLE_GPS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case RESULT_OK:
                    handleSleep();
                    break;
                case RESULT_CANCELED:
                    Log.e("GPS", "User denied to access location");
                    openGpsEnableSetting();
                    break;
            }
        } else if (requestCode == REQUEST_ENABLE_GPS) {
            if (!isGpsOn()) {
                openGpsEnableSetting();
            } else {
                handleSleep();
            }
        }
    }
}

