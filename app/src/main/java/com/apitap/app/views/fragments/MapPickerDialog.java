package com.apitap.app.views.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.apitap.app.App;
import com.apitap.app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class MapPickerDialog extends DialogFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView tvAddress;
    private ImageView imageViewClose;
    private double selectedLat, selectedLng;
    private Address selectedAddress;

    public interface OnLocationSelectedListener {
        void onLocationSelected(Address address, double lat, double lng);
    }

    private OnLocationSelectedListener listener;

    public MapPickerDialog(OnLocationSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_map_picker, container, false);

        tvAddress = view.findViewById(R.id.tvAddress);
        imageViewClose = view.findViewById(R.id.imageViewClose);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        imageViewClose.setOnClickListener(view1 -> dismiss());

        btnConfirm.setOnClickListener(v -> {
            if (listener != null && selectedAddress != null) {
                listener.onLocationSelected(selectedAddress, selectedLat, selectedLng);
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng defaultLatLng = new LatLng(App.latitude, App.longitude); // Delhi default
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 15));

        // Detect map move (Uber-style)
        mMap.setOnCameraIdleListener(() -> {
            LatLng center = mMap.getCameraPosition().target;
            selectedLat = center.latitude;
            selectedLng = center.longitude;

            fetchAddressFromLatLng(center);
        });
    }

    private void fetchAddressFromLatLng(LatLng latLng) {
        new Thread(() -> {
            try {
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                if (list != null && !list.isEmpty()) {
                    selectedAddress = list.get(0);

                    requireActivity().runOnUiThread(() -> {
                        tvAddress.setText(selectedAddress.getAddressLine(0));
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT // or WRAP_CONTENT if you want shorter
            );
        }
    }

}
