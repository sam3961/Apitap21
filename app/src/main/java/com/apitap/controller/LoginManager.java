package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Operations;
import com.apitap.model.bean.LoginModel;
import com.apitap.model.bean.guestActivity.GuestActivityBean;
import com.apitap.model.bean.guestLogin.GuestLoginBean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ashok-kumar on 26/7/16.
 */

public class LoginManager {
    private static final String TAG = LoginManager.class.getSimpleName();
    boolean isLogin;
    private GuestLoginBean guestLoginBean;
    private GuestActivityBean guestActivityBean;

    public void getLogin(Context context, String params) {
        isLogin = false;
        new ExecuteApi(context).execute(params);
    }

    public void guestLogin(Context context, String params) {
        new ExecuteGuestLoginApi(context).execute(params);
    }

    public void guestLastActivity(Context context, String params) {
        new ExecuteGuestActivityApi(context).execute(params);
    }

    public void doLogin(Context context, String params) {
        isLogin = true;
        new ExecuteApi(context).execute(params);
    }

    public void registerFCM(Context context, String params) {
        isLogin = false;
        new ExecuteFCMApi(context).execute(params);
    }

    public void getTermsAndConditions(Context context, String params) {
        new ExecuteTCApi(context).execute(params);
    }


    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_default_api---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("ResponseLogin", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject imageURLobj = new JSONObject();
                if (!isLogin)
                    ATPreferences.putString(mContext, Constants.KEY_USER_DEFAULT, jsonObject.has("_192") ? jsonObject.getString("_192") : "");
                JSONArray jArray = jsonObject.getJSONArray("RESULT");
                JSONObject jobj = jArray.getJSONObject(0);
                if (!jobj.has("_44"))
                    EventBus.getDefault().post(new Event(Constants.LOGIN_Failure, "Login Failed"));
                else if (!jobj.getString("_44").equals("Transaction Approved"))
                    EventBus.getDefault().post(new Event(Constants.LOGIN_Failure, jobj.getString("_44")));
                if (!isLogin)
                    ATPreferences.putString(mContext, Constants.KEY_USER_PIN, jsonObject.has("_39") ? jsonObject.getString("_39") : "");

                JSONArray imageURLArray = jobj.getJSONArray("RESULT");
                if (imageURLArray.length()>0)
                    imageURLobj = imageURLArray.getJSONObject(0);

                if (!isLogin) {
                    for (int i = 0; i < imageURLArray.length(); i++) {
                        JSONObject object = imageURLArray.getJSONObject(i);
                        if (object.getString("_127_11").equals("IMAGE_URL"))
                            ATPreferences.putString(mContext, Constants.KEY_IMAGE_URL, object.has("_127_12") ? object.getString("_127_12") : "");
                        // ATPreferences.putString(mContext, Constants.KEY_IMAGE_URL, "https://aioimages20.z5.web.core.windows.net/images/");
                        if (object.getString("_127_11").equals("MESSAGES_IMAGES_URL"))
                            ATPreferences.putString(mContext, Constants.KEY_MESSAGE_IMAGE_URL, object.has("_127_12") ? object.getString("_127_12") : "");
                        if (object.getString("_127_11").equals("VIDEO_URL"))
                            ATPreferences.putString(mContext, Constants.KEY_VIDEO_URL, object.has("_127_12") ? object.getString("_127_12") : "");
                    }

                }
                if (jobj.getString("_44").equals("Transaction Approved")) {
                    if (isLogin) {
                        ATPreferences.putString(mContext, Constants.KEY_USERID, imageURLobj.getString("_53"));
                        String loginType = imageURLobj.getString("_114_143");
                        if (imageURLobj.has("_114_8"))
                            ATPreferences.putString(mContext, Constants.USER_DOB, imageURLobj.getString("_114_8"));

                        // if (imageURLobj.has("_114_3")){
                        if (imageURLobj.getString("_114_53").contains(" ")) {

                            String[] fullName = imageURLobj.getString("_114_53").split(" ");
                            ATPreferences.putString(mContext, Constants.KEY_USERNAME, fullName[0]);
                            Log.d("KEY_USERNAME", fullName[0]);
                        } else {
                            ATPreferences.putString(mContext, Constants.KEY_USERNAME, imageURLobj.getString("_114_53"));
                            Log.d("KEY_USERNAME", imageURLobj.getString("_114_53"));
                        }
                        //   }
                        LoginModel loginBean = new Gson().fromJson(s, LoginModel.class);
                        ATPreferences.putString(mContext,
                                Constants.TOKEN,
                                loginBean.getRESULT().get(0).getRESULT().get(0).get_127_18());

                        ATPreferences.putString(mContext,
                                Constants.LOCATION_ID,
                                loginBean.getRESULT().get(0).getRESULT().get(0).get_114_47());

                        Log.d("tokensss", loginBean.getRESULT().get(0).getRESULT().get(0).get_127_18() + "  ");
                        EventBus.getDefault().post(new Event(Constants.LOGIN_SUCCESS, loginType));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ExecuteGuestLoginApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteGuestLoginApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_guest_login_api---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            guestLoginBean = new Gson().fromJson(s, GuestLoginBean.class);
            ATPreferences.putString(mContext,
                    Constants.TOKEN,
                    guestLoginBean.getRESULT().get(0).getRESULT().get(0).get12718());

            ATPreferences.putString(mContext,
                    Constants.KEY_USERID, guestLoginBean.getRESULT().get(0).getRESULT().get(0).get53());
            EventBus.getDefault().post(new Event(Constants.GUEST_LOGIN_SUCCESS, ""));

        }
    }

    private class ExecuteGuestActivityApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteGuestActivityApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_guest_activity_api---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                guestActivityBean = new Gson().fromJson(s, GuestActivityBean.class);
                if (guestActivityBean.getRESULT().get(0).get39().equals("0000")) {
                    EventBus.getDefault().post(new Event(Constants.GUEST_ACTIVITY_SUCCESS, ""));
                } else if (guestActivityBean.getRESULT().get(0).get39().equals("1025")) {
                    EventBus.getDefault().post(new Event(Constants.GUEST_ACTIVITY_TIMEOUT, ""));
                    new ExecuteGuestActivityApi(mContext).execute(Operations.makeJsonGuestLogin(mContext));
                } else {
                    EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, ""));

                }
            }
        }
    }

    private class ExecuteFCMApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteFCMApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_fcm_api---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("ResponseFCM", s + "");
            EventBus.getDefault().post(new Event(Constants.FCM_REGISTERED, ""));
        }
    }

    private class ExecuteTCApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteTCApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_terms_api---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("response_terms_api", s + "");
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                String terms = jsonObject2.getString("_119_15");
                String policies = jsonObject2.getString("_119_16");
                ATPreferences.putString(mContext, Constants.TERMS, terms);
                ATPreferences.putString(mContext, Constants.POLICIES, policies);
                EventBus.getDefault().post(new Event(Constants.TERMS_CONDITIONS, ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
