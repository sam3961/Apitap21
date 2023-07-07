package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.bean.DetailsBean;
import com.apitap.model.bean.InvoiceItemsBean;
import com.apitap.model.bean.NotificationBean;
import com.apitap.model.customclasses.Event;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by apple on 10/08/16.
 */
public class NotificationsManager {

    private static final String TAG = NotificationsManager.class.getSimpleName();
    public NotificationBean notificationBean;

    public void getListOfNotifications(Context context, String params) {
        new ExecuteListOfNotificationsApi(context).execute(params);
    }

    private class ExecuteListOfNotificationsApi extends AsyncTask<String, String, String> {
        Context mContext;
        HashMap<String, String> url_maps = new HashMap<String, String>();

        ExecuteListOfNotificationsApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_notifications---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                JSONArray array=jsonObject.getJSONArray("RESULT");
                if(array.getJSONObject(0).getString("_44").equals("Transaction Approved")){
                    notificationBean = new Gson().fromJson(s, NotificationBean.class);
                    if (notificationBean.getRESULT().get(0).getRESULT().get(0).get_114_70()!=null)
                    EventBus.getDefault().post(new Event(Constants.NOTIFICATION_LIST, ""));
                    else
                        EventBus.getDefault().post(new Event(Constants.NOTIFICATION_LIST_EMPTY, ""));
                }else
                    EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
