package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.addReservation.AddReservationResponse;
import com.apitap.model.assignedToUser.AssignUserLocationResponse;
import com.apitap.model.customclasses.Event;
import com.apitap.model.getReservation.GetReservationResponse;
import com.apitap.model.promoByLocation.PromoByLocationResponse;
import com.apitap.model.seatingAreaByLocation.SeatingAreaLocationResponse;
import com.apitap.model.tablesBySeatingArea.TablesBySeatingAreaResponse;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Sahil on 10/08/16.
 */
public class ReservationManager {

    private static final String TAG = ReservationManager.class.getSimpleName();
    private String apiConstant = "";
    public SeatingAreaLocationResponse seatingAreaLocationResponse;
    public GetReservationResponse getReservationResponse;
    public TablesBySeatingAreaResponse tablesBySeatingAreaResponse;
    public PromoByLocationResponse promoByLocationResponse;
    public AssignUserLocationResponse assignUserLocationResponse;

    public void addReservationDetails(Context context, String params, String tag) {
        apiConstant = tag;
        new ExecuteApi(context).execute(params);
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
            Log.d(TAG, apiConstant + "=> " + response);

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            switch (apiConstant) {
                case Constants.TAG_ADD_RESERVATION:
                    AddReservationResponse addReservationResponse = new Gson().fromJson(s, AddReservationResponse.class);
                    EventBus.getDefault().post(new Event(Constants.ADD_RESERVATION_SUCCESS, true));
                    break;
                case Constants.TAG_GET_RESERVATION:
                    getReservationResponse = new Gson().fromJson(s, GetReservationResponse.class);
                    EventBus.getDefault().post(new Event(Constants.GET_RESERVATION_SUCCESS, true));
                    break;
                case Constants.TAG_DELETE_RESERVATION:
                    EventBus.getDefault().post(Constants.DELETE_RESERVATION_SUCCESS);
                    EventBus.getDefault().post(Constants.DELETE_RESERVATION_FAILURE);
                    break;
                case Constants.TAG_EDIT_RESERVATION:
                    EventBus.getDefault().post(Constants.EDIT_RESERVATION_SUCCESS);
                    EventBus.getDefault().post(Constants.EDIT_RESERVATION_FAILURE);
                    break;
                case Constants.TAG_SEATING_AREA_BY_LOCATION:
                    seatingAreaLocationResponse = new Gson().fromJson(s, SeatingAreaLocationResponse.class);
                    EventBus.getDefault().post(new Event(Constants.GET_SEATING_AREA_LOCATION_SUCCESS, true));
                    break;
                case Constants.TAG_TABLES_BY_SEATING_AREA:
                    tablesBySeatingAreaResponse = new Gson().fromJson(s, TablesBySeatingAreaResponse.class);
                    EventBus.getDefault().post(new Event(Constants.GET_TABLES_BY_SEATING_AREA_SUCCESS, true));
                    break;
                case Constants.TAG_ASSIGN_USER_BY_LOCATION:
                    assignUserLocationResponse = new Gson().fromJson(s, AssignUserLocationResponse.class);
                    EventBus.getDefault().post(new Event(Constants.GET_ASSIGNED_TO_USER_SUCCESS, true));
                    break;
                case Constants.TAG_PROMO_BY_LOCATION:
                    promoByLocationResponse = new Gson().fromJson(s, PromoByLocationResponse.class);
                    EventBus.getDefault().post(new Event(Constants.GET_PROMO_BY_LOCATION_SUCCESS, true));
                    break;
            }

        }
    }


}