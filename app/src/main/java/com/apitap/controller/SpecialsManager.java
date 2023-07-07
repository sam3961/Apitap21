package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.customclasses.Event;
import com.apitap.model.itemModel.ItemListModel;
import com.apitap.model.specialModel.SpecialModel;
import com.apitap.model.storeFrontItems.specials.StoreSpecialsResponse;
import com.apitap.model.storeFrontSpecials.StoreFrontSpecialsResponse;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by apple on 10/08/16.
 */
public class SpecialsManager {

    private static final String TAG = SpecialsManager.class.getSimpleName();
    public SpecialModel specialModel;
    public StoreFrontSpecialsResponse storeSpecialsModel;


    public void getSpecialsList(Context context, String params) {
        new ExecuteApiSpecialsList(context).execute(params);
    }
    public void getCategorySpecialsList(Context context, String params) {
        new ExecuteApiSpecialsCategoryList(context).execute(params);
    }

    private class ExecuteApiSpecialsList extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiSpecialsList(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_SpecialsList---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null && !s.isEmpty()) {
                    SpecialModel specialModelTemp = new Gson().fromJson(s, SpecialModel.class);
                    if (specialModelTemp.getRESULT().get(0).getRESULT().get(0).get_1149() != null) {
                        specialModel = new Gson().fromJson(s, SpecialModel.class);
                        EventBus.getDefault().post(new Event(Constants.SPECIAL_LIST_DATA, true));
                    } else
                        EventBus.getDefault().post(new Event(Constants.SPECIAL_LIST_DATA, false));
                }
                else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private class ExecuteApiSpecialsCategoryList extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiSpecialsCategoryList(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_SpecialsCatgoryList---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null && !s.isEmpty()) {
                    StoreFrontSpecialsResponse specialModelTemp = new Gson().fromJson(s, StoreFrontSpecialsResponse.class);
                    if (specialModelTemp.getRESULT().get(0).getRESULT().size()>0&&
                    specialModelTemp.getRESULT().get(0).getRESULT().get(0).getPC()!=null) {
                        storeSpecialsModel = new Gson().fromJson(s, StoreFrontSpecialsResponse.class);
                            EventBus.getDefault().post(new Event(Constants.SPECIAL_LIST_DATA, true));
                    } else
                        EventBus.getDefault().post(new Event(Constants.SPECIAL_LIST_DATA, false));
                }
                else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
