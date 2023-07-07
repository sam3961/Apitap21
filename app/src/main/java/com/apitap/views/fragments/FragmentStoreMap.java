package com.apitap.views.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.controller.MerchantFavouriteManager;
import com.apitap.controller.ModelManager;
import com.apitap.controller.SearchAddressManager;
import com.apitap.model.Constants;
import com.apitap.model.CustomMapView;
import com.apitap.model.GpsLocation;
import com.apitap.model.Operations;
import com.apitap.model.Utils;
import com.apitap.model.bean.LocationListBean;
import com.apitap.model.bean.RatingBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.storeFrontItems.details.RESULTItem;
import com.apitap.views.HomeActivity;
import com.apitap.views.MerchantDetailActivity;
import com.apitap.views.NavigationMenu.FragmentDrawer;
import com.apitap.views.adapters.LocationListAdpater;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

import static com.apitap.controller.MerchantManager.merchantCategoryList;
import static com.apitap.views.fragments.home.FragmentHome.setFavouriteMerchantView;

/**
 * Created by Shami on 7/10/2017.
 */

public class FragmentStoreMap extends BaseFragment implements View.OnClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, LocationListAdpater.AdapterClick {

    public static boolean mMapIsTouched = false;
    private TextView storeName, ratingNo;
    private ImageView share, img_main;
    private RecyclerView recyclerView;
    SmoothProgressBar smoothProgressBar;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    private RatingBar ratingBar;
    private String merchantId, image_str, merchantName, searchkey;
    private LinearLayout giveRating, ratingNumbers;
    CircularProgressView mPocketBar;
    List<LocationListBean.RESULT_> arrayList;
    private Activity mActivity;
    private String current_lat, current_long;
    private static int toolint = 0;
    FrameLayout frameLayout;
    CustomMapView mapView;
    LinearLayout scan_tool;
    LinearLayout msg_tool;
    LinearLayout search_tool;
    LinearLayout back_tool;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    LocationListAdpater locationListAdpater;
    Uri uri;
    ArrayList<String> latlist = new ArrayList<String>();
    ArrayList<String> longlist = new ArrayList<String>();
    private GoogleApiClient mGoogleApiClient;
    LinearLayout selectMap, selectSatelite;
    private GoogleMap mMap;
    int state = 0;
    String addressTwo = "";
    private Location mLastLocation;
    private FusedLocationProviderApi fusedLocationProviderApi;
    private LocationRequest locationRequest;
    private ImageView backll;
    private LocationListAdpater.AdapterClick adapterClick;
    Button storeBrowse;
    CardView nolocationTxt;
    ImageView naviagte_to,imageViewCurrentLocation;
    int position = 99;
    int clicked_pos = 99;
    public static TabLayout tabLayout;
    private TextView tabOne, tabTwo, tabThree, tabFour, tabFive, tabSix;
    private ImageView homeTab2;
    LinearLayout tabConatiner;
    boolean isFavorite = false;
    Spinner sotre_categorySpinner;
    private LinearLayout ll_storeMessages, linearLayoutRating, nolocationLinear, view_store_detail_header,view_store_tabs;
    private RESULTItem data;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_store_map, container, false);

        mActivity = getActivity();
        adapterClick = this;
        if (getArguments() != null && getArguments().getString("merchantId") != null) {
            merchantId = getArguments().getString("merchantId");
            position = getArguments().getInt("position", 99);
            Log.d("MerchantID", merchantId + "   s");
        }

        merchantId = ATPreferences.readString(mActivity, Constants.MERCHANT_ID);
        getloc();

        checkLocationPermission();
        initGoogleAPIClient();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews(savedInstanceState);
    }

    private void initGoogleAPIClient() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void initViews(Bundle savedInstance) {
        mapView = rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstance);
        mapView.onResume();
        mapView.getMapAsync(this);

        frameLayout = rootView.findViewById(R.id.container_body);
        frameLayout.setVisibility(View.GONE);
        storeName = rootView.findViewById(R.id.storeName);
        sotre_categorySpinner = rootView.findViewById(R.id.category_dropdown);
        selectMap = rootView.findViewById(R.id.maptxt);
        selectSatelite = rootView.findViewById(R.id.sattxt);
        tabConatiner = rootView.findViewById(R.id.tab_container);
        tabLayout = rootView.findViewById(R.id.tabs);
        ll_storeMessages = rootView.findViewById(R.id.message_store);
        linearLayoutRating = rootView.findViewById(R.id.linearLayoutRating);

        ratingNo = rootView.findViewById(R.id.ratingNo);
        homeTab2 = rootView.findViewById(R.id.tab_one_image);


        img_main = rootView.findViewById(R.id.adstoreImg);
        share = rootView.findViewById(R.id.share);
        recyclerView = rootView.findViewById(R.id.recycler);
        ratingBar = rootView.findViewById(R.id.ratingBar);
        giveRating = rootView.findViewById(R.id.giveRating);
        ratingNumbers = rootView.findViewById(R.id.ratingnumbers);
        smoothProgressBar = rootView.findViewById(R.id.pocketa);
        mPocketBar = rootView.findViewById(R.id.pocket);
        scan_tool = rootView.findViewById(R.id.ll_scan);
        msg_tool = rootView.findViewById(R.id.ll_message);
        search_tool = rootView.findViewById(R.id.ll_search);
        back_tool = rootView.findViewById(R.id.iv_back);
        nolocationTxt = rootView.findViewById(R.id.nolocation);
        storeBrowse = rootView.findViewById(R.id.details_store);
        backll = rootView.findViewById(R.id.tv_back);
        nolocationLinear = rootView.findViewById(R.id.locationlinear);
        view_store_tabs = getActivity().findViewById(R.id.view_store_tabs);
        view_store_detail_header = getActivity().findViewById(R.id.view_store_detail_header);
        view_store_tabs.setVisibility(View.VISIBLE);
        view_store_detail_header.setVisibility(View.VISIBLE);
        naviagte_to = rootView.findViewById(R.id.naviagte_to);
        imageViewCurrentLocation = rootView.findViewById(R.id.imageViewCurrentLocation);

        linearLayoutRating.setOnClickListener(this);
        imageViewCurrentLocation.setOnClickListener(this);

        backll.setOnClickListener(this);
        naviagte_to.setOnClickListener(this);
        storeName.setOnClickListener(this);
        share.setOnClickListener(this);
        scan_tool.setOnClickListener(this);
        msg_tool.setOnClickListener(this);
        search_tool.setOnClickListener(this);
        storeBrowse.setOnClickListener(this);
        ll_storeMessages.setOnClickListener(this);

        selectMap.setOnClickListener(this);
        selectSatelite.setOnClickListener(this);


//        List<RatingBean.RESULT_> ratingBeen = ModelManager.getInstance().getAddMerchantRating().ratingBean.getRESULT().get(0).getRESULT();
        ratingNo.setText("(" + ATPreferences.readString(getActivity(), Constants.STORE_RATE) + ")");
        if (ATPreferences.readString(getActivity(), Constants.STORE_RATE).equalsIgnoreCase("0")) {
            ratingNo.setText("No Ratings");
        }


//        ModelManager.getInstance().getMerchantManager().getMerchantDetail(this,
//                Operations.makeJsonGetMerchantDetail(this, merchantId), Constants.GET_MERCHANT_SUCCESS);
//
//        ModelManager.getInstance().getMerchantFavouriteManager().getFavourites(mActivity,
//                Operations.makeJsonGetMerchantFavourite(mActivity));

        showProgress();
        ModelManager.getInstance().getMerchantManager().getMerchantDistance(getActivity(),
                Operations.makeJsonGetMerchantDistance(getActivity(),
                        ATPreferences.readString(getActivity(), Constants.MERCHANT_ID),
                        current_lat, current_long), Constants.GET_MERCHANT_DISTANCE_SUCCESS);

    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        if (ModelManager.getInstance().getMerchantStoresManager().storeDetailsModel.getRESULT().size() > 0) {
            data = ModelManager.getInstance().getMerchantStoresManager().storeDetailsModel.getRESULT().get(0);

//            if (data.getName().length() > 15)
//                storeName.setText(data.getName().substring(0,14) + "...");
//            else
            storeName.setText(Utils.hexToASCII(data.getJsonMember11470()));
            merchantName = Utils.hexToASCII(data.getJsonMember11470());
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();


            if (data.getJsonMember12219().equals("0")) {
                stars.getDrawable(0).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);

                ratingBar.setRating(Float.parseFloat(data.getJsonMember12219()));
            } else {
                stars.getDrawable(2).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(0).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(Color.parseColor("#fcb74d"), PorterDuff.Mode.SRC_ATOP);
                ratingBar.setRating(Float.parseFloat(data.getJsonMember12219()));
                //ratingNo.setText("(" +data.getReview_count()+ ")");
            }
            try {
                //if (!data.getAbout().equals(""))
                //  storeDetails.setText(Utils.getStringHexaDecimal(data.getAbout()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Picasso.get().load(ATPreferences.readString(mActivity, Constants.KEY_IMAGE_URL) + data.getJsonMember121170()).into(img_main);
            image_str = data.getJsonMember121170();
            uri = getLocalBitmapUri(img_main);
            setCategorySpinnerAdapter();
            if (MerchantFavouriteManager.isCurrentMerchantFav)
                isFavorite = true;
            setFavouriteMerchantView(storeName);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.LATLNG_SUCCESS:
                hideProgress();
                LatLng markerLoc1 = null;
                if (SearchAddressManager.latLng != null) {
                    markerLoc1 = ModelManager.getInstance().getSearchAddressManager().latLng;
                    mMap.clear();
//            mMap.addMarker(new MarkerOptions().position(markerLoc).title(
//                    arrayList.get(i).get11470()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    mMap.addMarker(new MarkerOptions().position(markerLoc1).title(
                            arrayList.get(clicked_pos).get11470()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    //   mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                 //   CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerLoc1, 14);
                  //  mMap.animateCamera(cameraUpdate);
                }// else
                //   getLatLongGeoCode();
                break;

            case Constants.LATLNG_FAILURE:
                //getLatLongGeoCode();
                break;
            case Constants.ADDRESS_NEARBY_SUCCESS:
                Utils.setRecyclerNearByAdapter(getActivity());
                break;

            case Constants.GET_MERCHANT_CATEGORIES:

                setCategorySpinnerAdapter();

                break;
            case Constants.REMOVE_MERCHANT_FAVORITES:
                hideProgress();
                // smoothProgressBar.setVisibility(View.GONE);
                //smoothProgressBar.progressiveStop();
                //  favourite.setBackgroundResource(R.drawable.ic_icon_fav_gray);
                MerchantFavouriteManager.isCurrentMerchantFav = false;
                isFavorite = false;
                setFavouriteMerchantView(storeName);
                break;

            case Constants.ADD_MERCHANT_FAVORITE_SUCCESS:
                hideProgress();
                //    if (isFavorite.equals("1"))
                // smoothProgressBar.setVisibility(View.GONE);
                // smoothProgressBar.progressiveStop();
                //  favourite.setBackgroundResource(R.drawable.ic_icon_fav);
                MerchantFavouriteManager.isCurrentMerchantFav = true;
                isFavorite = true;
                setFavouriteMerchantView(storeName);
                break;
            case Constants.GET_MERCHANT_DISTANCE_SUCCESS:
                hideProgress();
                arrayList = ModelManager.getInstance().
                        getMerchantManager().locationListBean.getRESULT().get(0).getRESULT();
                locationListAdpater = new LocationListAdpater(adapterClick, mActivity, arrayList, naviagte_to);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(locationListAdpater);
                updateLocation();
                currentLocation();
                double latus = 36.2423876;
                double lngus = -113.7481853;

                LatLng markerLoc = new LatLng(latus, lngus);

//                final CameraPosition cameraPosition = new CameraPosition.Builder()
//                        .target(markerLoc)      // Sets the center of the map to Mountain View
//                        .zoom(3)                   // Sets the zoom
//                        .build();
//                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                //   CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerLoc, 16);
                //     mMap.animateCamera(cameraUpdate);
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getAD()!=null&&!arrayList.get(i).getAD().get12038().isEmpty()) {
                        double lat = Double.parseDouble(arrayList.get(i).getAD().get12038());
                        double lng = Double.parseDouble(arrayList.get(i).getAD().get12039());

                        LatLng location = new LatLng(lat, lng);
                        // LatLng location = getLocationFromAddress(arrayList.get(i).getAD().get11412());
                        mMap.addMarker(new MarkerOptions().position(location).title(/*Utils.hexToASCII(*/arrayList.get(i).get11470()/*)*/));
                    }
                }


                if (arrayList.size() == 0) {
                    nolocationTxt.setVisibility(View.VISIBLE);
                    nolocationLinear.setVisibility(View.GONE);
                }
                if (position != 99) {
                    //navigateToMap(position);
                    locationListAdpater.updateNotify(arrayList, position);
                }

                naviagte_to.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int selctPos;
                        if (position != 99 && clicked_pos == 99) {
                            selctPos = position;
                        } else {
                            if (clicked_pos == 99)
                                selctPos = 0;
                            else
                                selctPos = clicked_pos;
                        }
                        boolean isReturn = getloc();
                        if (!isReturn) {
                            current_lat = "41.881832";
                            current_long = "-87.623177";
                        }
                        Log.d("positionLocation", selctPos + "  lop");
//                     Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr=" + current_long + "," + current_lat + "&daddr=" + list.getAD().getZP().get12038() + "," + list.getAD().getZP().get12039()));
//                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//                    context.startActivity(intent);
                        // Uri gmmIntentUri = Uri.parse("google.navigation:q=" + arrayList.get(selctPos).getAD().getZP().get12038() + "," + arrayList.get(selctPos).getAD().getZP().get12039());

                        String address_one = Utils.hexToASCII(arrayList.get(selctPos).getAD().get11412().trim());
                        String addressTwo = "";
                        if (!arrayList.get(selctPos).getAD().get11413().isEmpty())
                            addressTwo = Utils.hexToASCII(arrayList.get(selctPos).getAD().get11413().trim()) + " " + arrayList.get(selctPos).getAD().getZP().get4717();
                        else
                            addressTwo = arrayList.get(selctPos).getAD().getCI().get4715().trim() + " " +
                                    arrayList.get(selctPos).getAD().getCO().get11417() + " "
                                    + arrayList.get(selctPos).getAD().getZP().get4717() + " "
                                    + arrayList.get(selctPos).getAD().getST().get_1234();

                        Log.d("addressTwos", arrayList.get(selctPos).getAD().getCI().get4715() + "  "
                                + arrayList.get(selctPos).getAD().getCO().get11417() + "   " +
                                arrayList.get(selctPos).getAD().getZP().get4717() + "   " +
                                arrayList.get(selctPos).getAD().getST().get_1234());

                        //Uri gmmIntentUri = Uri.parse("google.navigation:q="/*+current_lat+","+current_long+"?q=" */+ address_one + " " + addressTwo);
                       /* Uri gmmIntentUri = Uri.parse("geo:" + current_lat + "," + current_long + "?q=" + address_one + " " + addressTwo);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);*/
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=" + current_lat + "," + current_long + "&daddr=" + address_one + " " + addressTwo));
                        startActivity(intent);
                    }

                });
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                rlp.setMargins(0, 90, 180, 0);
                break;
            case Constants.GET_MERCHANT_FAVORITES:
                ArrayList<String> mername = ModelManager.getInstance().getMerchantFavouriteManager().mernchantfavlist;
                Log.d("MernMae", mername + "  " + merchantName);
                try {
                    if (mername.contains(merchantName)) {
                        //  favourite.setBackgroundResource(R.drawable.ic_icon_fav);
                        setFavouriteMerchantView(storeName);
                        isFavorite = true;
                    }
                } catch (Exception e) {
                }
                break;

            case -1:
                hideProgress();
                baseshowFeedbackMessage(getActivity(), rootView, "Something went wrong");
                break;

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewCurrentLocation:
                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (location != null) {
                    //Getting longitude and latitude
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    //moving the map to location
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                }
                break;

            case R.id.linearLayoutRating:

                Bundle bundle = new Bundle();
                bundle.putString("merchantId", merchantId);
                bundle.putString("storeName", storeName.getText().toString());
                bundle.putString("storeImage", image_str);
                ((HomeActivity) getActivity()).displayView(new FragmentStoreRate(), Constants.TAG_STORE_RATE, bundle);

                break;

            case R.id.details_store:
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constants.MERCHANT_ID, merchantId);
                ATPreferences.putBoolean(mActivity, Constants.HEADER_STORE, true);
                ATPreferences.putString(mActivity, Constants.MERCHANT_ID, merchantId);
                startActivity(new Intent(mActivity, HomeActivity.class)
                        .putExtras(bundle1));
                getActivity().finish();
                //displayView(new FragmentStoreFront(), Constants.TAG_STORESFRONTPAGE, bundle1);

                break;
            case R.id.iv_back:
            case R.id.tv_back:
                onBackPress();
                break;
            case R.id.storeName:
                //  smoothProgressBar.setVisibility(View.VISIBLE);
                //smoothProgressBar.progressiveStart();
                showProgress();
                if (!isFavorite) {
                    ModelManager.getInstance().getAddMerchantFavorite().addMerchantToFavorite(mActivity,
                            Operations.makeJsonMerchantAddToFavorite(mActivity, ATPreferences.readString(mActivity, Constants.MERCHANT_ID)));
                } else
                    ModelManager.getInstance().getMerchantFavouriteManager().removeFavourite(mActivity,
                            Operations.makeJsonRemoveMerchantFavourite(mActivity, ATPreferences.readString(mActivity, Constants.MERCHANT_ID)));
                break;

            case R.id.share:
                shareImage();
                break;

            case R.id.showall:
                Intent intent = new Intent(mActivity, MerchantDetailActivity.class);
                intent.putExtra("merchantId", merchantId);
                startActivity(intent);
                break;


            case R.id.sattxt:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;

            case R.id.maptxt:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                break;
        }
    }


    private void shareImage() {
        Bitmap loadedImage = getBitmapFromURL(ATPreferences.readString(getActivity(), Constants.KEY_IMAGE_URL) + image_str);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), loadedImage, "", null);
        Uri screenshotUri = Uri.parse(path);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Nice Merchant on ApiTap\n" + merchantName
                + "\n" + "http://aiodc.com");
        shareIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        shareIntent.setType("image/*");
        //shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Via"));
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    private void getCurrentLocation() {
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            //moving the map to location
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(2));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getCurrentLocation();
        fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        updateLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    public void currentLocation() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //mMap.setMyLocationEnabled(true);
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(mActivity)
                        .setTitle("Location Permissions")
                        .setMessage("Please enable location to get you current location.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(mActivity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    private boolean getloc() {
        boolean isReturn = false;
        GpsLocation gps = new GpsLocation(mActivity);

        if (gps.canGetLocation()) {
            current_lat = String.valueOf(gps.getLatitude());
            current_long = String.valueOf(gps.getLongitude());
            isReturn = true;
        } else {
            current_lat = "41.881832";
            current_long = "-87.623177";
            gps.showSettingsAlertLocation();
        }
        return isReturn;
    }

    @Override
    public void onItemClick(View v, int i) {
        clicked_pos = i;
        navigateToMap(i);
    }

    public void navigateToMap(int i) {

        if (!arrayList.get(i).getAD().get12038().isEmpty()) {
            naviagte_to.setVisibility(View.VISIBLE);
            double lat = Double.parseDouble(arrayList.get(i).getAD().get12038());
            double lng = Double.parseDouble(arrayList.get(i).getAD().get12039());
            Log.e("LATLocation=" + lat, "LONG  " + lng + "");
            LatLng markerLoc = new LatLng(lat, lng);
            final CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(markerLoc)      // Sets the center of the map to Mountain View
                    .zoom(6)                   // Sets the zoom
                    //  .bearing(90)                // Sets the orientation of the camera to east
                    // .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();


            //  if (!arrayList.get(i).getAD().get11413().isEmpty())
            addressTwo = arrayList.get(i).getAD().get11412().trim() + " " + arrayList.get(i).getAD().get11413().trim()
                    + " " + arrayList.get(i).getAD().getZP().get4717();
            //else
            //  addressTwo = arrayList.get(i).getAD().getCI().get4715().trim() + " " +
            //        arrayList.get(i).getAD().getCO().get11417() + " "
            //      + arrayList.get(i).getAD().getZP().get4717() + " "
            //    + arrayList.get(i).getAD().getST().get_1234();

            Log.d("latitudelogitude", arrayList.get(i).getAD().getCI().get4715().trim() + " " +
                    arrayList.get(i).getAD().getCO().get11417() + " "
                    + arrayList.get(i).getAD().getZP().get4717() + " "
                    + arrayList.get(i).getAD().getST().get_1234() + "      " + arrayList.get(i).getAD().getCO().get4718().trim() + "   " +
                    arrayList.get(i).getAD().get11412().trim());
            getLatLongGeoCode(i);
            //LatLng location = getLocationFromAddress(addressTwo);
            //  ModelManager.getInstance().getSearchAddressManager().getPlaceId(mActivity, addressTwo);

            /*  if (location != null)
                markerLoc = location;

            mMap.clear();
//            mMap.addMarker(new MarkerOptions().position(markerLoc).title(
//                    arrayList.get(i).get11470()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            mMap.addMarker(new MarkerOptions().position(markerLoc).title(
                    arrayList.get(i).get11470()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            //   mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerLoc, 14);
            mMap.animateCamera(cameraUpdate);*/
        } else {
            Toast.makeText(mActivity, "No Location Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLatLongGeoCode(int clicked_pos) {
        LatLng location;
        Log.d("LocationName", addressTwo);
       /* if (addressTwo.contains("Cherokee")) {
            Log.d("LocationName", "Cherokee");
            location = new LatLng(34.1046493, -118.3366733);
        } else*/
        location = getLocationFromAddress(addressTwo);
        mMap.clear();
//            mMap.addMarker(new MarkerOptions().position(markerLoc).title(
//                    arrayList.get(i).get11470()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.addMarker(new MarkerOptions().position(location).title(
                arrayList.get(clicked_pos).get11470()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        //   mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 14);
        mMap.animateCamera(cameraUpdate);

    }


    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(mActivity);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null || address.size() < 1) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private void setCategorySpinnerAdapter() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mActivity,
                R.layout.simple_item_blue, merchantCategoryList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item_txt);
        sotre_categorySpinner.setAdapter(arrayAdapter);

        sotre_categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
      //  view_store_detail_header.setVisibility(View.GONE);
      //  view_store_tabs.setVisibility(View.GONE);
    }
}




