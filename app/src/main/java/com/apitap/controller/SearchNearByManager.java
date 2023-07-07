package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.bean.SearchFavoritesBean;
import com.apitap.model.customclasses.Event;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by rishav on 9/11/16.
 */

public class SearchNearByManager {

    private static final String TAG = SearchNearByManager.class.getSimpleName();
    public SearchFavoritesBean searchFavoritesBean;

    public void searchNearBy(Context context, String params, int key) {
        new ExecuteSearchApi(context, key).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "search_nearby_result " + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class ExecuteSearchApi extends AsyncTask<String, String, String> {

        Context context;
        int key;

        public ExecuteSearchApi(Context context, int key) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            return Client.Caller(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("TAG","Search nearby Results: " + s);

            try {
                Log.d(TAG, s);
                searchFavoritesBean = new Gson().fromJson(s, SearchFavoritesBean.class);
                if (searchFavoritesBean.getResult().get(0).getTransaction().equals("Transaction Approved")) {
                    EventBus.getDefault().post(new Event(Constants.SEARCH_FAVORITES_SUCCESS, ""));
                } else {
                    EventBus.getDefault().post(new Event(-1, ""));
                }
            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }
        }
    }
}
