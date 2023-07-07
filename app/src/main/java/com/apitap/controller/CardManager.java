package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.bean.GetCardBean;
import com.apitap.model.bean.SearchBean;
import com.apitap.model.bean.SearchItemBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sahil on 10/08/16.
 */
public class CardManager {

    private static final String TAG = CardManager.class.getSimpleName();
    public SearchItemBean messageListBean;
    public HashMap<Integer, ArrayList<SearchBean>> itemsData = new HashMap<Integer, ArrayList<SearchBean>>();
    public  ArrayList<GetCardBean> getCardBeen = new ArrayList<GetCardBean>();
    public  ArrayList<GetCardBean> getCardAdd = new ArrayList<GetCardBean>();
    private  GetCardBean detailsBean;
    private  GetCardBean detailsBeanAdd;
    public void getCardDetails(Context context, String params) {
        new ExecuteApi(context).execute(params);
    }

    public void addCardDetails(Context context, String params) {
        new ExecuteApiProducts(context).execute(params);
    }

    public void deleteCard(Context context, String params) {
        new ExecuteApiRemove(context).execute(params);
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
            Log.d(TAG, "get_card_Item---" + response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int maxLogSize = 2000;
            for (int i = 0; i <= s.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > s.length() ? s.length() : end;
                Log.v("searchOne", s.substring(start, end));
            }
            try {

                Log.d(TAG, s);

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
           /*     Log.d("JsonSizes", jsonArray1.length() + "");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    jsonObject2 = jsonArray1.getJSONObject(i);
                    Log.d("Helos", "Json" + jsonObject2.length());
                }*/
                Log.d("JsonSizews", jsonArray1.length() + "");
                assert jsonArray1 != null;
                if (jsonArray1.length() != 0) {


                        ArrayList<GetCardBean> arrayAds = new ArrayList<GetCardBean>();
                        for (int i = 0;i<jsonArray1.length();i++){
                             detailsBean = new GetCardBean();
                             detailsBeanAdd = new GetCardBean();
                            JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                            String name=jsonObject2.getString("_48_6");
                            detailsBean.setName(name);
                            String type=jsonObject2.getString("_120_7");
                            detailsBean.setCard_type(type);
                            String card_id=jsonObject2.getString("_122_34");
                            detailsBean.setCard_token(card_id);
                            String number=jsonObject2.getString("_48_15");
                            detailsBean.setCard_number(number);
                            arrayAds.add(detailsBean);
                        }
                    detailsBeanAdd.setCard_type("------");
                    detailsBeanAdd.setCard_type("Add Card");
                    getCardAdd.add(detailsBeanAdd);

                        getCardBeen = arrayAds;
                    EventBus.getDefault().post(new Event(Constants.GET_CARD_SUCCESS, ""));


             /*       searchItemBean = new Gson().fromJson(s, SearchItemBean.class);


                    if (searchItemBean.getResult().get(0).getStatus().equals("Transaction Approved")) {
                        {
                            EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS, ""));
                        }
                    } else {
                        EventBus.getDefault().post(new Event(-1, ""));
                    }*/
                } else {
                    EventBus.getDefault().post(new Event(Constants.GET_CARD_SUCCESS_Empty, ""));
                }
            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }
        }
    }
    private class ExecuteApiProducts extends AsyncTask<String, String, String> {
        private int key;
        Context mContext;

        ExecuteApiProducts(Context context) {
            mContext = context;
        }


        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_search_Item2---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int maxLogSize = 2000;
            for(int i = 0; i <= s.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i+1) * maxLogSize;
                end = end > s.length() ? s.length() : end;
                Log.v("addcards", s.substring(start, end));
            }
        }
    }


    private class ExecuteApiRemove extends AsyncTask<String, String, String> {
        private int key;
        Context mContext;

        ExecuteApiRemove(Context context) {
            mContext = context;
        }


        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_remove_card2---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG,"ss"+s);
            EventBus.getDefault().post(new Event(Constants.REMOVE_CARD_SUCCESS, ""));
        }
    }
}