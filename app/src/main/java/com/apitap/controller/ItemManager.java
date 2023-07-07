package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.bean.getaddress.GetAddressProp;
import com.apitap.model.bean.itemStoreFront.ItemStoreFrontResponse;
import com.apitap.model.bean.levelOneCategories.LevelOneCategory;
import com.apitap.model.bean.merchantStores.MerchantStoreBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.customclasses.Event_Address;
import com.apitap.model.itemModel.ItemListModel;
import com.apitap.model.preferences.ATPreferences;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by apple on 10/08/16.
 */
public class ItemManager {

    private static final String TAG = ItemManager.class.getSimpleName();
    public ItemListModel itemListModel;
    public ItemStoreFrontResponse itemStoreFrontListModel;


    public void getItemList(Context context, String params) {
        new ExecuteApiItemList(context).execute(params);
    }

    public void getItemCategoryItemsList(Context context, String params) {
        new ExecuteApiItemCategoryList(context).execute(params);
    }

    private class ExecuteApiItemList extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiItemList(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_ItemList---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null && !s.isEmpty()) {
                    ItemListModel itemListModelTemp = new Gson().fromJson(s, ItemListModel.class);
                    if (itemListModelTemp.getRESULT().get(0).getRESULT().get(0).get_1149() != null) {
                        itemListModel = new Gson().fromJson(s, ItemListModel.class);
                        EventBus.getDefault().post(new Event(Constants.ITEM_LIST_DATA, true));
                        String dob = itemListModel.getRESULT().get(0).getRESULT().get(0).get_1148();
                        ATPreferences.putString(mContext, Constants.USER_DOB, dob);

                    } else
                        EventBus.getDefault().post(new Event(Constants.ITEM_LIST_DATA, false));
                } else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiItemCategoryList extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiItemCategoryList(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_ItemList---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null && !s.isEmpty()) {
                    ItemStoreFrontResponse itemListModelTemp = new Gson().fromJson(s, ItemStoreFrontResponse.class);
                    if (itemListModelTemp.getRESULT().get(0).getRESULT().get(0).getJsonMember12044() != null) {
                        itemStoreFrontListModel = new Gson().fromJson(s, ItemStoreFrontResponse.class);
                        EventBus.getDefault().post(new Event(Constants.ITEM_LIST_DATA, true));
                        String dob = itemStoreFrontListModel.getRESULT().get(0).getRESULT().get(0).getJsonMember1148();
                        ATPreferences.putString(mContext, Constants.USER_DOB, dob);
                    } else
                        EventBus.getDefault().post(new Event(Constants.ITEM_LIST_DATA, false));
                } else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
