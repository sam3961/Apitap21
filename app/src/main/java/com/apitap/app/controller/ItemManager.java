package com.apitap.app.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.app.model.Client;
import com.apitap.app.model.Constants;
import com.apitap.app.model.bean.itemStoreFront.ItemStoreFrontResponse;
import com.apitap.app.model.customclasses.Event;
import com.apitap.app.model.itemModel.ItemListModel;
import com.apitap.app.model.itemModel.items.ItemsWithCategoryResponse;
import com.apitap.app.model.preferences.ATPreferences;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by apple on 10/08/16.
 */
public class ItemManager {

    private static final String TAG = ItemManager.class.getSimpleName();
    public ItemListModel itemListModel;
    public ItemStoreFrontResponse itemStoreFrontListModel;
    public ItemsWithCategoryResponse itemsWithCategoryResponse;


    public void getItemCategoryItemsList(Context context, String params) {
        new ExecuteApiItemCategoryList(context).execute(params);
    }

    public void getItemsWithCategoryList(Context context, String params) {
        new ExecuteApiItemWithCategoryList(context).execute(params);
    }

    private class ExecuteApiItemCategoryList extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiItemCategoryList(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_Item_category_List---" + response);
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

    private class ExecuteApiItemWithCategoryList extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiItemWithCategoryList(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_Item_with_category_List---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null && !s.isEmpty()) {
                    ItemsWithCategoryResponse itemListModelTemp = new Gson().fromJson(s, ItemsWithCategoryResponse.class);
                    if (itemListModelTemp.getRESULT().get(0).getRESULT().get(0).getJsonMember12044() != null) {
                        itemsWithCategoryResponse = new Gson().fromJson(s, ItemsWithCategoryResponse.class);
                        EventBus.getDefault().post(new Event(Constants.ITEM_LIST_DATA, true));


                        if (itemsWithCategoryResponse != null
                                && itemsWithCategoryResponse.getRESULT() != null
                                && !itemsWithCategoryResponse.getRESULT().isEmpty()
                                && itemsWithCategoryResponse.getRESULT().get(0).getRESULT() != null
                                && !itemsWithCategoryResponse.getRESULT().get(0).getRESULT().isEmpty()
                                && itemsWithCategoryResponse.getRESULT().get(0).getRESULT().get(0).getPC() != null
                                && !itemsWithCategoryResponse.getRESULT().get(0).getRESULT().get(0).getPC().isEmpty()) {

                            String dob = itemsWithCategoryResponse.getRESULT()
                                    .get(0)
                                    .getRESULT()
                                    .get(0)
                                    .getPC()
                                    .get(0)
                                    .getJsonMember1148();

                            if (dob != null && !dob.isEmpty()) {
                                ATPreferences.putString(mContext, Constants.USER_DOB, dob);
                            }
                        }

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
