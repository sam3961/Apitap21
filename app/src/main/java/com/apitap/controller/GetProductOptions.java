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
    public ArrayList<ProductOptionsBean> arrayOptions1 = new ArrayList<ProductOptionsBean>();
    public ArrayList<ProductOptions2Bean> arrayOptions2 = new ArrayList<ProductOptions2Bean>();
    public ArrayList<String> arrayOptionsStr = new ArrayList<String>();
    public ArrayList<String> arrayOptionsStr2 = new ArrayList<String>();
    private int key;

    public void getOption1(Context context, String params) {
        new ExecuteApi(context).execute(params);
    }

    public void getOption2(Context context, String params, int i) {
        this.key = i;
        new ExecuteApiProducts(context).execute(params);
    }


    private class ExecuteApi extends AsyncTask<String, String, String> {
        private int key;
        Context mContext;

        ExecuteApi(Context context) {
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
                    String option_id = jsonObject2.getString("_122_111");
                    String option_name = Utils.hexToASCII(jsonObject2.getString("_122_134"));

                    productOptionsBean.setOption_id(option_id);
                    productOptionsBean.setName_option(option_name);
                    arrayList.add(productOptionsBean);
                    Log.d("optionsIdbEAN", option_id);
                }
                arrayOptions1 = arrayList;
                EventBus.getDefault().post(new Event(Constants.GET_OPTIONS1_SUCCESS, ""));
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiProducts extends AsyncTask<String, String, String> {

        Context mContext;

        ExecuteApiProducts(Context context) {
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
            ArrayList<ProductOptions2Bean> arrayList = new ArrayList<ProductOptions2Bean>();

            try {

                Log.d(TAG, s);

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                    ProductOptions2Bean productOptionsBean = new ProductOptions2Bean();
                    String choice_id = jsonObject2.getString("_122_111");
                    String choice_name = jsonObject2.getString("_122_135");
                    String choice_price = jsonObject2.getString("_114_98");
                    if (key == 0)
                        arrayOptionsStr.add(choice_id);
                    else
                        arrayOptionsStr2.add(choice_id);
                    productOptionsBean.setChoice_id(choice_id);
                    productOptionsBean.setChoice_name(choice_name);
                    productOptionsBean.setChoice_price(choice_price);
                    arrayList.add(productOptionsBean);
                }
                arrayOptions2 = arrayList;
                EventBus.getDefault().post(new Event(Constants.GET_OPTIONS2_SUCCESS, ""));
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

}