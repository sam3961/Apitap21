package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.ProductOptions2Bean;
import com.apitap.model.bean.ProductOptionsBean;
import com.apitap.model.customclasses.Event;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sahil on 10/08/16.
 */
public class GetProductOptions {

    private static final String TAG = GetProductOptions.class.getSimpleName();
    public ArrayList<ProductOptionsBean> productOptionsBeans1 = new ArrayList<ProductOptionsBean>();
    public ArrayList<ProductOptionsBean> productOptionsBeans2 = new ArrayList<ProductOptionsBean>();
    public ArrayList<String> arrayListOption1 = new ArrayList<String>();
    public ArrayList<String> arrayListOption2 = new ArrayList<String>();
    private int key;


    public void getOption1(Context context, String params, int i) {
        this.key = i;
        new ExecuteApiGetOption1(context).execute(params);
    }

    public void getOption2(Context context, String params, int i) {
        this.key = i;
        new ExecuteApiGetOption2(context).execute(params);
    }


    private class ExecuteApiGetOption1 extends AsyncTask<String, String, String> {

        Context mContext;

        ExecuteApiGetOption1(Context context) {
            mContext = context;
        }


        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "get_option1_Item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<ProductOptionsBean> arrayList = new ArrayList<ProductOptionsBean>();

            try {

                Log.d(TAG, s);

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                    ProductOptionsBean productOptionsBean = new ProductOptionsBean();
                    String choice_id = jsonObject2.getString("_122_111");
                    String choice_name = jsonObject2.getString("_122_135");
                    String choice_price = jsonObject2.getString("_114_98");
                    arrayListOption1.add(choice_id);
                    productOptionsBean.setChoice_id(choice_id);
                    productOptionsBean.setChoice_name(choice_name);
                    productOptionsBean.setChoice_price(choice_price);
                    arrayList.add(productOptionsBean);
                }
                productOptionsBeans1 = arrayList;
                EventBus.getDefault().post(new Event(Constants.GET_OPTIONS_CHOICES_1_SUCCESS, ""));
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiGetOption2 extends AsyncTask<String, String, String> {

        Context mContext;

        ExecuteApiGetOption2(Context context) {
            mContext = context;
        }


        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "get_option2_Item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<ProductOptionsBean> arrayList = new ArrayList<ProductOptionsBean>();

            try {

                Log.d(TAG, s);

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                    ProductOptionsBean productOptionsBean = new ProductOptionsBean();
                    String choice_id = jsonObject2.getString("_122_111");
                    String choice_name = jsonObject2.getString("_122_135");
                    String choice_price = jsonObject2.getString("_114_98");
                    arrayListOption2.add(choice_id);
                    productOptionsBean.setChoice_id(choice_id);
                    productOptionsBean.setChoice_name(choice_name);
                    productOptionsBean.setChoice_price(choice_price);
                    arrayList.add(productOptionsBean);
                }
                productOptionsBeans2 = arrayList;
                EventBus.getDefault().post(new Event(Constants.GET_OPTIONS_CHOICES_2_SUCCESS, ""));
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

}