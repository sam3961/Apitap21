package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.apitap.App;
import com.apitap.BuildConfig;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/*
 * Created by rishav on 20/1/17.
 */

public class PlaceParser {

    private static final String TAG = PlaceParser.class.getSimpleName();
    private String input;

    public void getAddress(Context context, String params, String input) {
        this.input = input;
        new ExecuteAddress(context).execute(params);
    }

    private class ExecuteAddress extends AsyncTask<String, String, String> {

        Context mContext;


        ExecuteAddress(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... params) {

            HttpHandler httpHandler = new HttpHandler();
            String API_KEY = BuildConfig.GOOGLE_MAP_KEY;
            String place_url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + params[0] + "&key=" + API_KEY;

            String json = httpHandler.makeServiceCall(place_url);

            Log.w(TAG, "response-- " + json);
            Log.e(TAG, "place_url-- " + place_url);
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("placeParserResult",s+"");
            try {

                JSONObject jsonObj = new JSONObject(s);
                JSONObject jsonObject = jsonObj.getJSONObject("result");
                JSONObject jsonObject1 = jsonObject.getJSONObject("geometry");
                JSONObject jsonObject2 = jsonObject1.getJSONObject("location");
                String latitude = jsonObject2.getString("lat");
                String longitude = jsonObject2.getString("lng");


                App.latitude= Double.valueOf(latitude);
                App.longitude= Double.valueOf(longitude);
                // CSPreferences.putString(mContext, "source_latitude", latitude);
                //CSPreferences.putString(mContext, "source_longitude", longitude);
                //  EventBus.getDefault().post(new Event(Constant.SOURCE_SUCCESS, ""));

            } catch (JSONException e) {

                Log.e("", "Cannot process JSON results", e);
            }
        }
    }
}