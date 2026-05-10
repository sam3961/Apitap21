package com.apitap.app.views;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.apitap.app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TestMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "TestMapActivity";

    private MapView mapView;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Log.d(TAG, "MapView created");
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d(TAG, "onMapReady called");

        googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng testLocation = new LatLng(30.7333, 76.7794); // Chandigarh

        googleMap.addMarker(new MarkerOptions()
                .position(testLocation)
                .title("Test Location"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(testLocation, 14));

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                Log.d(TAG, "Map fully loaded");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) mapView.onResume();
    }

    @Override
    protected void onPause() {
        if (mapView != null) mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mapView != null) mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) mapView.onLowMemory();
    }
}