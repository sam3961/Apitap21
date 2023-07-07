package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Logger;
import com.apitap.model.bean.MessageListBean;
import com.apitap.model.customclasses.Event;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Sahil on 10/08/16.
 */
public class MessageManager {

    private static final String TAG = MessageManager.class.getSimpleName();
    public MessageListBean messageListBean;
    public MessageListBean messageDetailBean;


    public void getAllMessages(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }

    public void getMessageDetail(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }
    public void readMessage(Context context, String params) {
        new ExecuteReadApi(context).execute(params);
    }
    public void sendMessage(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {
        private int key;
        Context mContext;

        ExecuteApi(Context context, int key) {
            mContext = context;
            this.key = key;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_message_Item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, s);
                if (key == Constants.ALL_MESSAGES_SUCCESS) {
                    Logger.addRecordToLog("responsemessage14   "+s);
                    messageListBean = new Gson().fromJson(s, MessageListBean.class);
                    if (messageListBean.getRESULT().get(0).getStatus().equals("Transaction Approved")) {
                        EventBus.getDefault().post(new Event(key, ""));
                    } else {
                        EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, ""));

                    }
                } else if (key == Constants.MESSAGE_DETAIL_SUCCESS) {

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String trascation = jsonObject1.getString("_44");
                    JSONObject jobj = jsonArray.getJSONObject(0);
                    JSONArray imgeArray = jobj.getJSONArray("RESULT");
                    Log.d("imgeArraySize",imgeArray.length()+"  l");
                    if (trascation.equals("Transaction Approved")&&imgeArray.length()>0) {
                        messageDetailBean = new Gson().fromJson(s, MessageListBean.class);
                        EventBus.getDefault().post(new Event(key, ""));
                    } else {
                        EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, ""));

                    }
                }
                else if (key == Constants.MESSAGE_SEND_SUCCESS) {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String trascation = jsonObject1.getString("_44");
                    JSONObject jobj = jsonArray.getJSONObject(0);
                    JSONArray imgeArray = jobj.getJSONArray("RESULT");
                    Log.d("imgeArraySize",imgeArray.length()+"  l");
                    if (trascation.equals("Transaction Approved")){
                        EventBus.getDefault().post(new Event(key, ""));
                    }else{
                        EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, ""));
                    }
                }
                else if (key == Constants.MESSAGE_SEND_ABUSE_SUCCESS) {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String trascation = jsonObject1.getString("_44");
                    JSONObject jobj = jsonArray.getJSONObject(0);
                    JSONArray imgeArray = jobj.getJSONArray("RESULT");
                    if (trascation.equals("Transaction Approved")){
                        EventBus.getDefault().post(new Event(key, ""));
                    }else{
                        EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, ""));
                    }
                }
            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }
        }
    }
    private class ExecuteReadApi extends AsyncTask<String, String, String> {
        private int key;
        Context mContext;

        ExecuteReadApi(Context context) {
            mContext = context;
            this.key = key;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_read_Message---" + response);
            Logger.addRecordToLog("response_read_Message"+response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, s);
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}
