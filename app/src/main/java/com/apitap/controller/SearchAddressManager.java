
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

    public void getAddress(Context context, String params) {
        new ExecuteTask(context).execute(params);
    }

    public void getPlaceId(Context context, String params) {
        new ExecuteTaskPlaceID(context).execute(params);
    }

    private class ExecuteTask extends AsyncTask<String, String, String> {
        Context mContext;
        StringBuilder sb;
        private String address, area;

        ExecuteTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler httpHandler = new HttpHandler();
            String response = "";
            placeIdList = new ArrayList<>();
            addressList = new ArrayList<>();

            try {
                sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
                sb.append("?key=").append(BuildConfig.GOOGLE_MAP_KEY);
                sb.append("&input=").append(URLEncoder.encode(strings[0], "utf8"));

                Log.e(TAG, "URL-- " + sb.toString());

                response = httpHandler.makeServiceCall(sb.toString());
                try {

                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArray = jsonObj.getJSONArray("predictions");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        JSONArray array = jsonObject.getJSONArray("terms");

                        int length = array.length();
                        if (length >= 3) {
                            length = length - 1;
                        }

                        StringBuilder s3 = new StringBuilder();

                        for (int j = 0; j < length; j++) {
                            JSONObject terms = array.getJSONObject(j);

                            if (j == 0) {
                                address = terms.getString("value");
                            } else {

                                s3.append(terms.getString("value")).append(", ");
                                area = s3.toString().substring(0, s3.toString().length() - 2);
                            }
                        }

                        SearchAddressBeans searchAddressBeans = new SearchAddressBeans(address, area);

                        addressList.add(searchAddressBeans);

                        String place_id = jsonObject.getString("place_id");

                        placeIdList.add(place_id);
                    }

                } catch (JSONException e) {
                    Log.e("", "Cannot process JSON results", e);

                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "response--" + s);
            EventBus.getDefault().post(new Event(Constants.ADDRESS_NEARBY_SUCCESS, ""));

        }
    }

    private class ExecuteTaskPlaceID extends AsyncTask<String, String, String> {
        Context mContext;
        StringBuilder sb;
        private String address, area;

        ExecuteTaskPlaceID(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler httpHandler = new HttpHandler();
            String response = "";
            String place_id = "";

            try {
                sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
                sb.append("?key=").append(BuildConfig.GOOGLE_MAP_KEY);
                sb.append("&input=").append(URLEncoder.encode(strings[0], "utf8"));

                Log.e(TAG, "URL-- " + sb.toString());

                response = httpHandler.makeServiceCall(sb.toString());
                try {

                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArray = jsonObj.getJSONArray("predictions");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        JSONArray array = jsonObject.getJSONArray("terms");

                        int length = array.length();
                        if (length >= 3) {
                            length = length - 1;
                        }

                        StringBuilder s3 = new StringBuilder();

                        for (int j = 0; j < length; j++) {
                            JSONObject terms = array.getJSONObject(j);

                            if (j == 0) {
                                address = terms.getString("value");
                            } else {

                                s3.append(terms.getString("value")).append(", ");
                                area = s3.toString().substring(0, s3.toString().length() - 2);
                            }
                        }

                        place_id = jsonObject.getString("place_id");

                    }

                } catch (JSONException e) {
                    Log.e("", "Cannot process JSON results", e);

                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return place_id;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "response--" + s);
            if (s != null) {
                new ExecuteTaskLatLong(mContext).execute(s);
            } else
                EventBus.getDefault().post(new Event(Constants.LATLNG_FAILURE, ""));

        }
    }

    private class ExecuteTaskLatLong extends AsyncTask<String, String, String> {
        Context mContext;
        StringBuilder sb;
        private String address, area;

        ExecuteTaskLatLong(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(String... params) {

            HttpHandler httpHandler = new HttpHandler();
            //String API_KEY = "AIzaSyCpxjdSXb9v61fadm7mUsmxrggE3KMCQD0";
            String place_url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + params[0] + "&key=" + BuildConfig.GOOGLE_MAP_KEY;

            String json = httpHandler.makeServiceCall(place_url);
            Log.w(TAG, "response--" + json);
            Log.e(TAG, "place_url--" + place_url);
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "response--" + s);
            if (s != null && !s.isEmpty()) {
                try {

                    JSONObject jsonObj = new JSONObject(s);
                    JSONObject jsonObject = jsonObj.getJSONObject("result");
                    JSONObject jsonObject1 = jsonObject.getJSONObject("geometry");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("location");
                    String latitude = jsonObject2.getString("lat");
                    String longitude = jsonObject2.getString("lng");
                    latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    EventBus.getDefault().post(new Event(Constants.LATLNG_SUCCESS, ""));
                } catch (Exception e) {
                    EventBus.getDefault().post(new Event(Constants.LATLNG_FAILURE, ""));
                }
            } else
                EventBus.getDefault().post(new Event(Constants.LATLNG_FAILURE, ""));

        }
    }
}
