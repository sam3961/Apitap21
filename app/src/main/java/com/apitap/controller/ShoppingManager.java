package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.bean.ShoppingCartDetailBean;
import com.apitap.model.customclasses.Event;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Shami on 19/9/2017.
 */

public class ShoppingManager {
    private static final String TAG = ShoppingManager.class.getSimpleName();


    public void getShoppingCart(Context context, String params) {
        new ExecuteApi(context).execute(params);
    }
    public void updateGPSCoordinatesByCartId(Context context, String params) {
        new ExecuteGPSApi(context).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "responsetx_pay_item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                JSONArray array=jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = array.getJSONObject(0);
                String transaction = jsonObject1.getString("_44");
                if (transaction.equals("Transaction Approved")) {
                    EventBus.getDefault().post(new Event(Constants.GET_SHOPPING_SUCCESS, ""));
                }
                /*if(array.getJSONObject(0).getString("_44").equals("Transaction Approved")){
                    EventBus.getDefault().post(new Event(Constants.GET_SHOPPING_SUCCESS, ""));*/
                else
                    EventBus.getDefault().post(new Event(Constants.GET_SHOPPING_FAILED, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
          /*  try {
                detailBean = new Gson().fromJson(s, ShoppingCartDetailBean.class);
                if (detailBean.getRESULT().get(0).getStatus().equals("Transaction Approved")) {
                    EventBus.getDefault().post(new Event(Constants.SHOPPING_DETAIL_SUCCESS, ""));
                } else {
                    EventBus.getDefault().post(new Event(-1, ""));
                }
            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }*/
        }
    }
    private class ExecuteGPSApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteGPSApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "responsetx_pay_item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                JSONArray array=jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = array.getJSONObject(0);
                String transaction = jsonObject1.getString("_44");
                if (transaction.equals("Transaction Approved")) {
                    EventBus.getDefault().post(new Event(Constants.GET_SHOPPING_SUCCESS, ""));
                }
                else{
                    EventBus.getDefault().post(new Event(Constants.GET_SHOPPING_FAILED, ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
