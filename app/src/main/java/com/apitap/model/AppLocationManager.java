package com.apitap.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.util.Log;

import com.apitap.App;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import static android.content.Context.LOCATION_SERVICE;



public class AppLocationManager implements LocationListener {

    private Context activity;
    private Criteria criteria = new Criteria();
    private LocationManager locationManager;
    private String provider;
    private Location canGetLocation = null;

    @SuppressLint("MissingPermission")
    public AppLocationManager(final Context context) {
        activity = context;
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isNetworkEnabled)
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        if (isGPSEnabled)
            criteria.setAccuracy(Criteria.ACCURACY_FINE);


        provider = this.locationManager.getBestProvider(criteria, true);
        if ( provider == null ) {
            Log.e( "TAG", "No location provider found!" );
            return;
        }
        if (ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            locationManager.requestLocationUpdates(provider, 5000, 0.0f, this);
            canGetLocation = getLastKnownLocation();
            if (canGetLocation != null) {
                App.latitude = canGetLocation.getLatitude();
                App.longitude = canGetLocation.getLongitude();
            } else {
                getLastLocation();
            }

            locationManager.requestSingleUpdate(criteria, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    canGetLocation = location;
                    App.latitude = canGetLocation.getLatitude();
                    App.longitude = canGetLocation.getLongitude();

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            }, null);
        }
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            canGetLocation = location;
                            App.latitude = canGetLocation.getLatitude();
                            App.longitude = canGetLocation.getLongitude();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private Location setMostRecentLocation(Location lastKnownLocation) {
        return lastKnownLocation;
    }


    public void onLocationChanged(Location location) {
        canGetLocation = location;
        App.latitude = location.getLatitude();
        App.longitude = location.getLongitude();
    }

    public Location setCanGetLocation() {
        return canGetLocation;
    }

    public void onProviderDisabled(String arg0) {
    }

    public void onProviderEnabled(String arg0) {
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(AppLocationManager.this);
        }
    }
}