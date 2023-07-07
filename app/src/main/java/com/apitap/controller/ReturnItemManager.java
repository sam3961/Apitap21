package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.bean.DetailsBean;
import com.apitap.model.bean.InvoiceItemsBean;
import com.apitap.model.customclasses.Event;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by apple on 10/08/16.
 */
public class ReturnItemManager {

    private static final String TAG = ReturnItemManager.class.getSimpleName();
    public ArrayList<DetailsBean> arrayDetails = new ArrayList<>();
    public InvoiceItemsBean invoiceItemsBean;

    public ArrayList<DetailsBean> returnItems(Context context, String params) {
        new ExecuteReturnItemsApi(context).execute(params);
        return arrayDetails;
    }

    private class ExecuteReturnItemsApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteReturnItemsApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.e(TAG, "response_return_item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                JSONArray array=jsonObject.getJSONArray("RESULT");
                if(array.getJSONObject(0).getString("_44").equals("Transaction Approved")){
                  //  invoiceItemsBean = new Gson().fromJson(s, InvoiceItemsBean.class);
                    EventBus.getDefault().post(new Event(Constants.RETURN_INVOICE_ITEMS, ""));
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
