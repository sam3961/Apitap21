package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.bean.AboutUsBean;
import com.apitap.model.bean.GetCardBean;
import com.apitap.model.bean.RelatedAdBean;
import com.apitap.model.customclasses.Event;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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
