
package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.BuildConfig;
import com.apitap.model.Constants;
import com.apitap.model.bean.SearchAddressBeans;
import com.apitap.model.customclasses.Event;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
