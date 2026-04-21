package com.apitap.app.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.app.model.Client;
import com.apitap.app.model.Constants;
import com.apitap.app.model.customclasses.Event;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ashok-kumar on 26/7/16.
 */

public class SignUpManager {
    private static final String TAG = SignUpManager.class.getSimpleName();

    public void postUserDetails(Context context, String params, boolean isValidateApi) {
        if (isValidateApi)
            new ExecuteApi(context).execute(params);
        else
            new ExecuteSignupApi(context).execute(params);
    }

    public void saveAddress(Context context, String params) {
        new ExecuteSaveAddressApi(context).execute(params);
    }


    private class ExecuteSaveAddressApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteSaveAddressApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "save_address_api---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d("responsezip", s);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "sign_up_validate---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray resultArray = jsonObject.getJSONArray("RESULT");
                JSONObject resultObj = resultArray.getJSONObject(0);
                JSONArray innerResultArr = resultObj.getJSONArray("RESULT");
                JSONObject innerResultObj = innerResultArr.getJSONObject(0);
                String status = innerResultObj.getString("_122_73");
                if (status.equals("1")) {
                    EventBus.getDefault().post(new Event(Constants.ACCOUNT_ALREADY_REGISTERED, "EmailId Already Registered"));
                } else {
                    EventBus.getDefault().post(new Event(Constants.ACCOUNT_NOT_REGISTERED, ""));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class ExecuteSignupApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteSignupApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "sign_up---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray resultArray = jsonObject.getJSONArray("RESULT");

                JSONObject resultObj = resultArray.getJSONObject(0);

                JSONArray resultArray1 = resultObj.getJSONArray("RESULT");
                JSONObject resultObj1 = resultArray1.getJSONObject(0);

                String transaction = resultObj.getString("_44");
                String id = resultObj1.getString("_53");

                if (transaction.equals("Transaction Approved")) {
                    EventBus.getDefault().post(new Event(Constants.ACCOUNT_CREATED, id));
                } else
                    EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, transaction));
                JSONArray innerResultArr = resultObj.getJSONArray("RESULT");
                JSONObject innerResultObj = innerResultArr.getJSONObject(0);
//                String status = innerResultObj.getString("122.73");
//                if (status.equals("1")) {
//                    EventBus.getDefault().post(new Event(Constants.ACCOUNT_ALREADY_REGISTERED,"EmailId Already Registered"));
//                } else {
//                    EventBus.getDefault().post(new Event(Constants.ACCOUNT_NOT_REGISTERED,""));
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
