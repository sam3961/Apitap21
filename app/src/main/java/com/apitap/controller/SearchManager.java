package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.SearchBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rishav on 9/11/16.
 */

public class SearchManager {

    private static final String TAG = SearchManager.class.getSimpleName();
    public ArrayList<String> listAddresses;
    public ArrayList<String> searchAddresses = new ArrayList<>();
    public HashMap<Integer, ArrayList<SearchBean>> itemsData = new HashMap<Integer, ArrayList<SearchBean>>();


    public void searchAddresses(Context context, String params, int key) {
        new ExecuteSearchImageApi(context, key).execute(params);
    }

    public void imageSearch(Context context, String params, int key) {
        new ExecuteSearchImageApi(context, key).execute(params);
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
            Log.d(TAG, "search_result-- " + response);
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
                                for (int k = 0; k < addressArray.length(); k++) {
                                    JSONObject addressObj = addressArray.getJSONObject(k);
                                    String addresses = addressObj.getString("_114_53");
                                    //Log.e("Addresses: ", ""+addresses);
                                    if (!addresses.isEmpty() && !listAddresses.contains(addresses))
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


    private class ExecuteSearchImageApi extends AsyncTask<String, String, String> {

        Context context;
        int key;

        public ExecuteSearchImageApi(Context context, int key) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            return Client.Caller(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("Search Results: ", "" + s);

            JSONObject jsonObjectMain = null;
            boolean isResult = false;
            try {
                jsonObjectMain = new JSONObject(s);
                JSONArray mainArray = jsonObjectMain.getJSONArray("RESULT");
                JSONObject jobj = mainArray.getJSONObject(0);
                JSONArray imgeArray = jobj.getJSONArray("RESULT");
                itemsData = new HashMap<>();
                JSONObject imgObj1 = imgeArray.getJSONObject(0);
                if (imgObj1.has("_114_53")) {
                    for (int j = 0; j < imgeArray.length(); j++) {
                        JSONObject imgObj = imgeArray.getJSONObject(j);
                        String categoryName = imgObj.getString("_114_53");
                        JSONArray pcArr = imgObj.getJSONArray("PC");
                        if (pcArr.length() > 0) {
                            ArrayList<SearchBean> urlArr = new ArrayList<>();
                            isResult = true;
                            for (int k = 0; k < pcArr.length(); k++) {
                                JSONObject object = pcArr.getJSONObject(k);
                                SearchBean bean = new SearchBean();
                                bean.setImageUrls(ATPreferences.readString(context, Constants.KEY_IMAGE_URL) + "_t_" + object.getString("_121_170"));
                                bean.setProductId(object.getString("_114_144"));
                                bean.setProdcutType(object.getString("_114_112"));
                                bean.setProductName(object.getString("_120_83"));
                                bean.setCategoryName(categoryName);
                                bean.setIsFavorite(object.getString("_121_80"));
                                bean.setSellerName(Utils.hexToASCII(object.getString("_120_83")));
                                bean.setIsSeen(object.getString("_114_9"));
                                Log.d("setIsSeens", object.getString("_114_9"));
                                bean.setActualPrice(object.getString("_114_98"));
                                bean.setPriceAfterDiscount(object.getString("_122_158"));
                                bean.setDescription(object.getString("_120_157"));
                                urlArr.add(bean);
                            }
                            itemsData.put(j, urlArr);
                        }
                    }
                    if (isResult)
                        EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS, ""));
                } else
                    EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS_Empty, ""));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
