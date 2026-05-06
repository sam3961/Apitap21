package com.apitap.app.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.app.model.Client;
import com.apitap.app.model.Constants;
import com.apitap.app.model.Logger;
import com.apitap.app.model.bean.MessageListBean;
import com.apitap.app.model.customclasses.Event;
import com.apitap.app.model.unreadMessageMerchant.UnreadMerchantMessages;
import com.apitap.app.model.unreadMessages.UnreadMessagesResponse;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

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

    public void getUnreadMessages(Context context, String params, int key) {
        new ExecuteUnreadMessagesApi(context, key).execute(params);
    }

    public void getUnreadMessagesMerchant(Context context, String params, int key) {
        new ExecuteUnreadMessagesMerchantApi(context, key).execute(params);
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
                    try {
                        messageListBean = new Gson().fromJson(s, MessageListBean.class);

                        if (messageListBean == null
                                || messageListBean.getRESULT() == null
                                || messageListBean.getRESULT().isEmpty()
                                || messageListBean.getRESULT().get(0) == null) {
                            EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, ""));
                            return;
                        }

                        MessageListBean.ResultWrapper wrapper = messageListBean.getRESULT().get(0);
                        String status = wrapper.getStatus();

                        if ("Transaction Approved".equalsIgnoreCase(status)) {
                            EventBus.getDefault().post(new Event(key, ""));
                        } else {
                            EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, ""));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, ""));
                    }
                } else if (key == Constants.MESSAGE_DETAIL_SUCCESS) {

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String trascation = jsonObject1.getString("_44");
                    JSONObject jobj = jsonArray.getJSONObject(0);
                    JSONArray imgeArray = jobj.getJSONArray("RESULT");
                    Log.d("imgeArraySize", imgeArray.length() + "  l");
                    if (trascation.equals("Transaction Approved") && imgeArray.length() > 0) {
                        messageDetailBean = new Gson().fromJson(s, MessageListBean.class);
                        EventBus.getDefault().post(new Event(key, ""));
                    } else {
                        EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, ""));

                    }
                } else if (key == Constants.MESSAGE_SEND_SUCCESS) {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String trascation = jsonObject1.getString("_44");
                    JSONObject jobj = jsonArray.getJSONObject(0);
                    JSONArray imgeArray = jobj.getJSONArray("RESULT");
                    Log.d("imgeArraySize", imgeArray.length() + "  l");
                    if (trascation.equals("Transaction Approved")) {
                        EventBus.getDefault().post(new Event(key, ""));
                    } else {
                        EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, ""));
                    }
                } else if (key == Constants.MESSAGE_SEND_ABUSE_SUCCESS) {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String trascation = jsonObject1.getString("_44");
                    JSONObject jobj = jsonArray.getJSONObject(0);
                    JSONArray imgeArray = jobj.getJSONArray("RESULT");
                    if (trascation.equals("Transaction Approved")) {
                        EventBus.getDefault().post(new Event(key, ""));
                    } else {
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
            Logger.addRecordToLog("response_read_Message" + response);
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

    private class ExecuteUnreadMessagesApi extends AsyncTask<String, String, String> {
        private int key;
        Context mContext;

        ExecuteUnreadMessagesApi(Context context, int key) {
            mContext = context;
            this.key = key;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_unread_Message---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, s);

                UnreadMessagesResponse unreadMessagesResponse = new Gson().fromJson(s, UnreadMessagesResponse.class);
                EventBus.getDefault().post(new Event(Constants.UNREAD_MESSAGES_SUCCESS, Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(unreadMessagesResponse.getRESULT()).get(0)).getRESULT()).get(0)).getJsonMember114121()));

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private class ExecuteUnreadMessagesMerchantApi extends AsyncTask<String, String, String> {
        private int key;
        Context mContext;

        ExecuteUnreadMessagesMerchantApi(Context context, int key) {
            mContext = context;
            this.key = key;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_unread_Message_merchant---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, s);

                UnreadMerchantMessages unreadMessagesResponse = new Gson().fromJson(s, UnreadMerchantMessages.class);
                EventBus.getDefault().post(new Event(Constants.UNREAD_MESSAGES_MERCHANT_SUCCESS, unreadMessagesResponse));

            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }
        }
    }
}
