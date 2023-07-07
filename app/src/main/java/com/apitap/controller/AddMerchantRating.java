package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.bean.AdsListBean;
import com.apitap.model.bean.RatingBean;
import com.apitap.model.customclasses.Event;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Shami on 21/9/2017.
 */

public class AddMerchantRating {
    private static final String TAG = AddMerchantRating.class.getSimpleName();
    public RatingBean ratingBean;


    public void addMerchantRating(Context context, String params) {

        new ExecuteApi(context).execute(params);
    }

    public void getMerchantRating(Context context, String params) {

        new ExecuteRatingApi(context).execute(params);
    }
    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_add_to_rate---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.contains("_121_80")){
                Log.d("RatingSucces","Rating");
            }
           EventBus.getDefault().post(new Event(Constants.ADD_TO_RATING_SUCCESS,""));
        }
    }

    private class ExecuteRatingApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteRatingApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_get_rate---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ratingBean = new Gson().fromJson(s, RatingBean.class);
            if (ratingBean.getRESULT().get(0).get44().equals("Transaction Approved")) {
                EventBus.getDefault().post(new Event(Constants.GET_RATING_SUCCESS,""));
            } else {
                EventBus.getDefault().post(new Event(-1, ""));
            }

        }
    }

}
