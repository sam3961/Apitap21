package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.bean.FavBean;
import com.apitap.model.bean.FavMerchantBean;
import com.apitap.model.bean.Favdetailsbean;
import com.apitap.model.customclasses.Event;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sahil on 10/08/16.
 */
public class MerchantFavouriteManager {

    private static final String TAG = MerchantFavouriteManager.class.getSimpleName();
    public static ArrayList<String> mernchantfavlist = new ArrayList<>();
    public static boolean isCurrentMerchantFav = false;

    public FavMerchantBean favMerchantBean;


    public void getFavourites(Context context, String params) {
        new ExecuteApi(context).execute(params);
    }
     public void removeFavourite(Context context, String params) {
        new ExecuteRemoveApi(context).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {

        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "responsemerhcant_favourites---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("merchantFavres", s + "");
            mernchantfavlist = new ArrayList<>();
            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                    mernchantfavlist.add(Utils.hexToASCII(jsonObject2.getString("_114_70")));
                }
                    EventBus.getDefault().post(new Event(Constants.GET_MERCHANT_FAVORITES,""));

            } catch (Exception e) {
            Log.d("exceptions",e.getMessage());
            }
        }
    }
    private class ExecuteRemoveApi extends AsyncTask<String, String, String> {

        Context mContext;

        ExecuteRemoveApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "responsemerhcant_remove_favourites---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("merchantremoveFavres", s + "");
            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);

                }
                EventBus.getDefault().post(new Event(Constants.REMOVE_MERCHANT_FAVORITES,""));

            } catch (Exception e) {

            }
        }
    }



}
