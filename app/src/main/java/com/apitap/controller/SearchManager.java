package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.customclasses.Event;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rishav on 9/11/16.
 */

public class SearchManager {

    private static final String TAG = SearchManager.class.getSimpleName();
    public ArrayList<String> listAddresses;
    public ArrayList<String> searchAddresses = new ArrayList<>();


    public void searchAddresses(Context context, String params, int key) {
            new ExecuteSearchApi(context, key).execute(params);
    }
    public void getAddresses(Context context, String params) {
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
            Log.d(TAG, "search_result-- "+response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
              // Log.d(TAG, s);

                listAddresses.add("Use Current Location - GPS");
                JSONObject jsonObject = new JSONObject(s);
                JSONArray mainArray = jsonObject.getJSONArray("RESULT");

                for (int i = 0; i < mainArray.length(); i++) {
                    JSONObject jobj = mainArray.getJSONObject(i);
                    if (jobj.has("_101")) {
                        if (jobj.getString("_101").equals("010100055")) {
                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            for (int j = 0; j < imgeArray.length(); j++) {
                                JSONObject resObj = imgeArray.getJSONObject(j);

                                JSONArray addressArray = resObj.getJSONArray("AD");
                                for(int k=0; k < addressArray.length(); k++) {
                                    JSONObject addressObj = addressArray.getJSONObject(k);
                                    String addresses = addressObj.getString("_114_53");
                                    //Log.e("Addresses: ", ""+addresses);
                                    if (!addresses.isEmpty()&&!listAddresses.contains(addresses))
                                    listAddresses.add(addresses);
                                }

                            }
                            EventBus.getDefault().post(new Event(Constants.ADDRESS_SUCCESS, ""));

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class ExecuteSearchApi extends  AsyncTask<String, String, String> {

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

            Log.e("Search Results: ", ""+s);
        }
    }
}
