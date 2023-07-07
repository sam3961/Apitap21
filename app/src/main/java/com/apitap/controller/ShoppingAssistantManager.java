package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.ShoppingAsstListBean;
import com.apitap.model.bean.getaddress.GetAddressProp;
import com.apitap.model.customclasses.Event;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Shami on 16/7/2018.
 */

public class ShoppingAssistantManager {
    private static final String TAG = ShoppingAssistantManager.class.getSimpleName();
    private int event_key;
    public ArrayList<ShoppingAsstListBean> shoppingAsstListBeen = new ArrayList<>();
    public static ShoppingAsstListBean shoppingAsstListBean;

    public void hitApi(Context context, String params, int key) {
        event_key = key;
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
            Log.d(TAG, "assistant_result-- " + response);
            return response;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                switch (event_key) {
                    case Constants.GET_ASSISTANT_LIST:
                        shoppingAsstListBean = new Gson().fromJson(s, ShoppingAsstListBean.class);
                        EventBus.getDefault().post(new Event(Constants.GET_ASSISTANT_LIST, ""));
                        break;
                    case Constants.ADD_ASSISTANT_LIST:
                        EventBus.getDefault().post(new Event(Constants.ADD_ASSISTANT_LIST, ""));
                        break;
                    case Constants.REMOVE_ASSISTANT_LIST:
                        EventBus.getDefault().post(new Event(Constants.REMOVE_ASSISTANT_LIST, ""));
                        break;
                    case Constants.EDIT_ASSISTANT_LIST:
                        EventBus.getDefault().post(new Event(Constants.EDIT_ASSISTANT_LIST, ""));
                        break;
                    case Constants.ADD_ITEM_ASSISTANT_LIST:
                        EventBus.getDefault().post(new Event(Constants.ADD_ITEM_ASSISTANT_LIST, ""));
                        break;
                    case Constants.REMOVE_ITEM_IASSISTANT_LIST:
                        EventBus.getDefault().post(new Event(Constants.REMOVE_ITEM_IASSISTANT_LIST, ""));
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
