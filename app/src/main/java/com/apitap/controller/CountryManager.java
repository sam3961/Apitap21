package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.bean.getaddress.GetAddressProp;
import com.apitap.model.customclasses.Event_Address;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by apple on 10/08/16.
 */
public class CountryManager {

    private static final String TAG = CountryManager.class.getSimpleName();


    public void getCode(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;
        int key;

        ExecuteApi(Context context, int key) {
            mContext = context;
            this.key = key;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_Code " + key + " Item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, s);

                GetAddressProp prop = new Gson().fromJson(s, GetAddressProp.class);

                Event_Address event = new Event_Address();
                event.setKey(key);
                if (prop.getRESULT().get(0).getStatus().equals("Transaction Approved")) {
                    event.setCode(true);
                    event.setResponse_code(prop.getRESULT().get(0).getRESULT().get(0));
                } else {
                    event.setResponse_code(null);
                }
                EventBus.getDefault().post(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
