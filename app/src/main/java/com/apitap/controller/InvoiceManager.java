package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.bean.DetailsBean;
import com.apitap.model.bean.HistoryInvoiceBean;
import com.apitap.model.bean.InvoiceItemsBean;
import com.apitap.model.bean.RelatedDetailsBean;
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
public class InvoiceManager {

    private static final String TAG = InvoiceManager.class.getSimpleName();
    public ArrayList<DetailsBean> arrayDetails = new ArrayList<>();
    public InvoiceItemsBean invoiceItemsBean;

    public ArrayList<DetailsBean> getListOfInvoice(Context context, String params) {
        new ExecuteRelatedItemsApi(context).execute(params);
        return arrayDetails;
    }

    private class ExecuteRelatedItemsApi extends AsyncTask<String, String, String> {
        Context mContext;
        HashMap<String, String> url_maps = new HashMap<String, String>();

        ExecuteRelatedItemsApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_invoice_item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                JSONArray array=jsonObject.getJSONArray("RESULT");
                if(array.getJSONObject(0).getString("_44").equals("Transaction Approved")){
                    invoiceItemsBean = new Gson().fromJson(s, InvoiceItemsBean.class);
                    EventBus.getDefault().post(new Event(Constants.INVOICE_ITEMS, ""));
                }else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // relatedDetailsBean = new Gson().fromJson(s, RelatedDetailsBean.class);
//            if (relatedDetailsBean.getResult().get(0).getStatus().equals("Transaction Approved")) {
//                EventBus.getDefault().post(new Event(Constants.ITEM_DELETED, ""));
//            } else {
//                EventBus.getDefault().post(new Event(-1, ""));
//            }
        }
    }

}
