package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.customclasses.Event;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Shami on 22/9/2017.
 */

public class AddMerchantFavorite {
    private static final String TAG = AddToFavoriteManager.class.getSimpleName();


    public void addMerchantToFavorite(Context context, String params) {
        new ExecuteApi(context).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_add_Merchantto_fav---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            EventBus.getDefault().post(new Event(Constants.ADD_MERCHANT_FAVORITE_SUCCESS,""));
        }
    }

}
