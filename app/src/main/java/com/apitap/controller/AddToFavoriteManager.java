package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ashok-kumar on 26/7/16.
 */

public class AddToFavoriteManager {
    private static final String TAG = AddToFavoriteManager.class.getSimpleName();


    public void addToFavorite(Context context, String params) {
        new ExecuteApi(context).execute(params);
    }

    public void adToFavorite(Context context, String params) {
        new ExecuteAdApi(context).execute(params);
    }


    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_add_to_fav---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            EventBus.getDefault().post(new Event(Constants.ADD_TO_FAVORITE_SUCCESS,""));
        }
    }
    private class ExecuteAdApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteAdApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_add_to_adfav---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG,s);
            EventBus.getDefault().post(new Event(Constants.ADD_Ad_TO_FAVORITE_SUCCESS,""));
        }
    }

}
