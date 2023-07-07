package com.apitap.views;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.MerchantDetailBean;
import com.apitap.model.bean.MerchantLocationBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.fragments.FragmentWriteReview;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public class AllStoresActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private TextView btn_write,tv_view_store;
    private SmoothProgressBar mPocketBar;
    private TextView txt_name, txt_address, txt_call, txt_msg, txt_reviews, txt_terms, txt_timings/*, txt_directions*/;
    private RoundedImageView img_main;
    private RelativeLayout lay_main;
    private RatingBar ratingBar;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private FusedLocationProviderApi fusedLocationProviderApi;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_detail);
        ModelManager.getInstance().getMerchantManager().getMerchantDetail(this,
                Operations.makeJsonGetStoresWithItems(this), Constants.GET_MERCHANT_SUCCESS);

        initGoogleAPIClient();

        initViews();
        setListener();
    }

    private void initGoogleAPIClient() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Merchant Detail");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lay_main = (RelativeLayout) findViewById(R.id.lay_main);
        btn_write = (TextView) findViewById(R.id.btn_write);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_call = (TextView) findViewById(R.id.txt_call);
        txt_msg = (TextView) findViewById(R.id.txt_msg);
        txt_terms = (TextView) findViewById(R.id.txt_terms);
        txt_reviews = (TextView) findViewById(R.id.txt_reviews);
        txt_timings = (TextView) findViewById(R.id.txt_timings);
        tv_view_store=(TextView)findViewById(R.id.tv_view_store);
        //  txt_directions = (TextView) findViewById(R.id.txt_directions);
        img_main = (RoundedImageView) findViewById(R.id.img_main);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        mPocketBar = (SmoothProgressBar) findViewById(R.id.pocket);

        mPocketBar.setSmoothProgressDrawableBackgroundDrawable(
                SmoothProgressBarUtils.generateDrawableWithColors(
                        getResources().getIntArray(R.array.pocket_background_colors),
                        ((SmoothProgressDrawable) mPocketBar.getIndeterminateDrawable()).getStrokeWidth()));
        mPocketBar.progressiveStart();

        btn_write.setOnClickListener(this);
        txt_call.setOnClickListener(this);
        txt_msg.setOnClickListener(this);
        txt_terms.setOnClickListener(this);
        //     txt_directions.setOnClickListener(this);

        ModelManager.getInstance().getMerchantManager().getMerchantDetail(this,
                Operations.makeJsonGetMerchantDetail(this, ATPreferences.readString(this, Constants.KEY_USERID)), Constants.GET_MERCHANT_SUCCESS);

    }

    private void setListener(){
        tv_view_store.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_write:
                FragmentManager fm = getSupportFragmentManager();
                FragmentWriteReview dialogFragment = new FragmentWriteReview();
                dialogFragment.show(fm, "");
                break;
            case R.id.txt_call:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + v.getTag()));
                startActivity(intent);
                break;
            case R.id.txt_msg:
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + v.getTag()));
                startActivity(intent1);
                break;
            case R.id.txt_terms:
                AlertDialog.Builder dia = new AlertDialog.Builder(this)
                        .setMessage(String.valueOf(v.getTag()))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = dia.create();
                dialog.show();
                break;
           case R.id.tv_view_store:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
//        mGoogleApiClient.connect();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
     //   mGoogleApiClient.disconnect();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.GET_MERCHANT_SUCCESS:

                if (ModelManager.getInstance().getMerchantManager().merchantDetailBean.getRESULT().get(0).getRESULT().size() > 0) {
                    MerchantDetailBean.RESULT.DetailData data = ModelManager.getInstance().getMerchantManager().merchantDetailBean.getRESULT().get(0).getRESULT().get(0);
                    txt_name.setText(data.getName());
                    txt_address.setText(data.getAddress());
                    txt_call.setText("Call " + data.getPhone());
                    txt_call.setTag(data.getPhone());
                    txt_msg.setTag(data.getPhone());
                    txt_reviews.setText(data.getReview_count() + " Reviews");
                    ratingBar.setProgress((int) Double.parseDouble(data.getRating()));
                    txt_terms.setTag(Utils.getStringHexaDecimal(data.getPolicy()));
                    Picasso.get().load(ATPreferences.readString(this, Constants.KEY_IMAGE_URL) + data.getImage()).into(img_main);

                }
                ModelManager.getInstance().getMerchantManager().getMerchantLocation(this,
                        Operations.makeJsonGetMerchantLocation(this, ATPreferences.readString(this, Constants.KEY_USERID)), Constants.GET_MERCHANT_LOCATION_SUCCESS);

                break;
            case Constants.GET_MERCHANT_LOCATION_SUCCESS:
                mPocketBar.progressiveStop();
                mPocketBar.setVisibility(View.INVISIBLE);
                List<MerchantLocationBean.RESULT.MerchantLocationData> data = ModelManager.getInstance().getMerchantManager().merchantLocationBean.getRESULT().get(0).getRESULT();

                updateLocation();

                for (int i = 0; i < data.size(); i++) {
                    double lat = Double.parseDouble(data.get(i).getaD().getzP().getLat());
                    double lng = Double.parseDouble(data.get(i).getaD().getzP().getLng());

                    data.get(i).setDistance(Utils.getDistance(mLastLocation.getLatitude(), mLastLocation.getLongitude(), lat, lng));
                }

                Collections.sort(data, new Comparator<MerchantLocationBean.RESULT.MerchantLocationData>() {
                    @Override
                    public int compare(MerchantLocationBean.RESULT.MerchantLocationData c1, MerchantLocationBean.RESULT.MerchantLocationData c2) {
                        return Double.compare(c1.getDistance(), c2.getDistance());
                    }
                });

                for (int i = 0; i < data.size(); i++) {
                    double lat = Double.parseDouble(data.get(i).getaD().getzP().getLat());
                    double lng = Double.parseDouble(data.get(i).getaD().getzP().getLng());

                    LatLng location = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(location).title(data.get(i).getStoreName()));
                    if (i == 0) {
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(location, 17)));
                        //    txt_directions.setText("Distance " + Utils.setDistance(data.get(i).getDistance()) + " KM");

                        for (int j = 0; j < data.get(i).getsL().size(); j++) {
                            if (data.get(i).getsL().get(j).getDayOfWeek().equals("" + (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1))) {
                                try {
                                    txt_timings.setText("Today: " + Utils.getTime(data.get(i).getsL().get(j).getHourOpen()) + " - "
                                            + Utils.getTime(data.get(i).getsL().get(j).getHourClose()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }


                    }

                }

                lay_main.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation();
    }
}
