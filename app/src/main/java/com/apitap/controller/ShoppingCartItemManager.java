package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.bean.ShoppingCartDetailBean;
import com.apitap.model.bean.ShoppingCompBean;
import com.apitap.model.customclasses.Event;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ashok-kumar on 26/7/16.
 */

public class ShoppingCartItemManager {
    private static final String TAG = ShoppingCartItemManager.class.getSimpleName();
    public ShoppingCartDetailBean detailBean;

    public void getShoppingCartItems(Context context, String params) {
        new ExecuteApi(context).execute(params);
    }

    public void saveShoppingCart(Context context, String params) {
        new ExecuteSaveApi(context).execute(params);
    }
    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_shoppingcart_item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                detailBean = new Gson().fromJson(s, ShoppingCartDetailBean.class);
                if (detailBean.getRESULT().get(0).getStatus().equals("Transaction Approved")) {
                    EventBus.getDefault().post(new Event(Constants.SHOPPING_DETAIL_SUCCESS, ""));
                } else {
                    EventBus.getDefault().post(new Event(-1, ""));
                }
            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }
        }
    }

    private class ExecuteSaveApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteSaveApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_save_item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                EventBus.getDefault().post(new Event(Constants.SAVE_CART_SUCCESS, ""));
            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }
        }
    }

}
