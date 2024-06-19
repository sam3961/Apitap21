package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.ShoppingCompBean;
import com.apitap.model.customclasses.Event;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ashok-kumar on 26/7/16.
 */

public class ShoppingCartManager {
    private static final String TAG = ShoppingCartManager.class.getSimpleName();
    public ArrayList<ShoppingCompBean> shoopingArray = new ArrayList<>();

    public void getShoppingCarts(Context context, String params) {
        new ExecuteApi(context).execute(params);
    }

    public void addItemTOCart(Context context, String params) {
        new ExecuteAddItemApi(context).execute(params);
    }

    public void reOrderCart(Context context, String params) {
        new ExecuteReorderApi(context).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_shoppingcartss---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                String deliveryId = "";
                JSONObject jsonObject = new JSONObject(s);
                JSONArray resultArray = jsonObject.getJSONArray("RESULT");
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject resultObj = resultArray.getJSONObject(i);
                    String trans = resultObj.getString("_44");
                    JSONArray resultInner = resultObj.getJSONArray("RESULT");
                    if (resultInner.length() > 0) {
                        shoopingArray = new ArrayList<>();
                        for (int j = 0; j < resultInner.length(); j++) {
                            JSONObject objInner = resultInner.getJSONObject(j);
                            ShoppingCompBean shoppingCompBean = new ShoppingCompBean();
                            JSONObject objectDelivery = objInner.getJSONObject("DE");
                            if (objectDelivery.has("_122_109")) {
                                shoppingCompBean.setDeliveryId(objectDelivery.getString("_122_109"));
                            } else
                                shoppingCompBean.setDeliveryId("");


                            shoppingCompBean.setShoppingCartId(objInner.getString("_122_31"));   // ShoppingCart id
                            shoppingCompBean.setMerchantId(objInner.getString("_114_179"));   // Merchant id
                            shoppingCompBean.setCompanyName(Utils.hexToASCII(objInner.getString("_114_70")));   // Company name
                            shoppingCompBean.setCompanyImage(objInner.getString("_121_170"));  // image
                            shoppingCompBean.setItemCounter(objInner.getString("_114_121"));  //  Item counter
                            shoppingCompBean.setTotalAmount(objInner.getString("_55_3"));     //  Total amount
                            shoppingCompBean.setShoppingCartStatus(objInner.getString("_114_143"));  //  Shopping Cart Status
                            shoppingCompBean.setLastDate(objInner.getString("_114_138"));  //  Shopping Cart Time
                            shoppingCompBean.setExpiring(objInner.getString("_114_132"));  //  Shopping Cart Status
                            shoopingArray.add(shoppingCompBean);
                        }
                        EventBus.getDefault().post(new Event(Constants.SHOPPING_SUCCESS, ""));

                    } else {
                        EventBus.getDefault().post(new Event(-1, ""));
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
                EventBus.getDefault().post(new Event(-1, ""));
            }
        }
    }

    private class ExecuteAddItemApi extends AsyncTask<String, String, String> {

        Context mContext;

        ExecuteAddItemApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_cartadd---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("helloCart", s + "");
            String shoppingId = "";
            try {
                if (s != null) {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray resultArray = jsonObject.getJSONArray("RESULT");
                    // for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject resultObj = resultArray.getJSONObject(0);
                    String trans = resultObj.getString("_44");
                    JSONArray jsonArray = resultObj.getJSONArray("RESULT");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    if (jsonObject1.has("_122_31")) shoppingId = jsonObject1.getString("_122_31");
                    if (trans.equals("Transaction Approved")) {

                        EventBus.getDefault().post(new Event(Constants.SHOPPING_SUCCESS, shoppingId));
                    } else {
                        EventBus.getDefault().post(new Event(-1, ""));
                    }

                    //}
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ExecuteReorderApi extends AsyncTask<String, String, String> {

        Context mContext;

        ExecuteReorderApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_cartadd---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("helloCart", s + "");
            try {
                if (s != null) {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray resultArray = jsonObject.getJSONArray("RESULT");
                    // for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject resultObj = resultArray.getJSONObject(0);
                    String trans = resultObj.getString("_44");
                    JSONArray jsonArray = resultObj.getJSONArray("RESULT");
                    if (trans.equals("Transaction Approved")) {

                        EventBus.getDefault().post(new Event(Constants.SHOPPING_SUCCESS, ""));
                    } else {
                        EventBus.getDefault().post(new Event(-1, ""));
                    }

                    //}
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
