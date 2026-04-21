package com.apitap.app.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.app.model.Client;
import com.apitap.app.model.Constants;
import com.apitap.app.model.bean.AboutUsBean;
import com.apitap.app.model.customclasses.Event;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

public class ContactUsManger {

    private static final String TAG = ContactUsManger.class.getSimpleName();
    public static AboutUsBean aboutUsBean;


    public void fetchReasonsListing(Context context, String params) {
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
            Log.d(TAG, "getListing---" + response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null){
                aboutUsBean = new Gson().fromJson(s, AboutUsBean.class);
                EventBus.getDefault().post(new Event(Constants.ABOUT_LIST_SUCCESS,""));
            }
        }
    }

}
