package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.apitap.model.Client;


/**
 * Created by Shami on 22/9/2017.
 */

public class AddProductSeen {
    private static final String TAG = AddProductSeen.class.getSimpleName();

    public void setCounter(Context context, String params) {
        new ExecuteCounterApi(context).execute(params);
    }


    public void setProductSeen(Context context, String params) {
        new ExecuteApi(context).execute(params);
    }


    public void setAdSeen(Context context, String params) {
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
            Log.d(TAG, "response_productSeen---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

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
            Log.d(TAG, "response_AdSeen---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private class ExecuteCounterApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteCounterApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_Token---CounterApi" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
}