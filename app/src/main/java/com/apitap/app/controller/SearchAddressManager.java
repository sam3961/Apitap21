
package com.apitap.app.controller;

import com.apitap.app.model.bean.SearchAddressBeans;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class SearchAddressManager {

    private static final String TAG = SearchAddressManager.class.getSimpleName();
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    public static LatLng latLng;

    public static ArrayList<String> placeIdList;
    public static ArrayList<SearchAddressBeans> addressList;





}
