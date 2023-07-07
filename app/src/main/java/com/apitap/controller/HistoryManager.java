package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.DetailsInoviceBean;
import com.apitap.model.Logger;
import com.apitap.model.bean.DetailsBean;
import com.apitap.model.bean.HistoryInvoiceBean;
import com.apitap.model.bean.InvoiceDetailBean;
import com.apitap.model.bean.ProductDetailsBean;
import com.apitap.model.bean.ProductDetailsInvoiceBean;
import com.apitap.model.bean.ReturnReasonsBean;
import com.apitap.model.bean.SizeBean;
import com.apitap.model.bean.Sizedata;
import com.apitap.model.customclasses.Event;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by apple on 10/08/16.
 */
public class HistoryManager {

    private static final String TAG = HistoryManager.class.getSimpleName();
    public HistoryInvoiceBean historyInvoiceBean;
    public InvoiceDetailBean invoiceDetailBean;
    public static  ReturnReasonsBean returnReasonsBean;
    public ArrayList<DetailsInoviceBean> arrayDetails = new ArrayList<>();

    public void getInvoiceHistory(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }

    public void getInvoiceDetail(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }
    public void getReturnReasons(Context context, String params) {
        new ExecuteReturnReasonsApi(context).execute(params);
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
            Log.d(TAG, "response_history_Item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, s);
                if (key == Constants.HISTORY_INVOICE_SUCCESS) {
                    historyInvoiceBean = new Gson().fromJson(s, HistoryInvoiceBean.class);
                    if (historyInvoiceBean.getRESULT().get(0).getStatus().equals("Transaction Approved")) {
                        EventBus.getDefault().post(new Event(key, ""));
                    } else {
                        EventBus.getDefault().post(new Event(-1, ""));
                    }
                } else if (key == Constants.INVOICE_DETAIL_SUCCESS) {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray mainArray = jsonObject.getJSONArray("RESULT");
                    JSONObject resultObject = mainArray.getJSONObject(0);
                    JSONArray resultArray = resultObject.getJSONArray("RESULT");
                    arrayDetails = new ArrayList<>();

                    for (int i = 0; i < resultArray.length(); i++) {
                        DetailsInoviceBean detailsBean = new DetailsInoviceBean();
                        JSONObject object = resultArray.getJSONObject(i);
                        String merchantId = object.getString("_53");
                        String cartId = object.getString("_121_75");

                        String name = object.getString("_120_83");
                        String desc = object.getString("_120_157");



                        detailsBean.setMerchantID(merchantId);
                        Log.d("mERCHANTS",merchantId);
                        detailsBean.setName(name);
                        detailsBean.setCartId(cartId);

                        detailsBean.setProductDesc(desc);

                        JSONArray imArray = object.getJSONArray("IM");

                        ArrayList<ProductDetailsInvoiceBean> arrayProductDetails = new ArrayList<>();
                        for (int j = 0; j < imArray.length(); j++) {
                            JSONObject imObject = imArray.getJSONObject(j);
                            String productImage = imObject.getString("_47_42");
                            ProductDetailsInvoiceBean productDetailsBean = new ProductDetailsInvoiceBean();
                            productDetailsBean.setProductImage(productImage);
                            arrayProductDetails.add(productDetailsBean);
                        }

                        detailsBean.setArrayProductDetails(arrayProductDetails);

                        arrayDetails.add(detailsBean);
                    }
                    invoiceDetailBean = new Gson().fromJson(s, InvoiceDetailBean.class);
                    if (invoiceDetailBean.getRESULT().get(0).getStatus().equals("Transaction Approved")) {
                        EventBus.getDefault().post(new Event(key, ""));
                    } else {
                        EventBus.getDefault().post(new Event(-1, ""));
                    }
                }
            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }
        }
    }
    private class ExecuteReturnReasonsApi extends AsyncTask<String, String, String> {

        Context mContext;

        ExecuteReturnReasonsApi(Context context) {
            mContext = context;

        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_return_reasons---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            returnReasonsBean = new Gson().fromJson(s, ReturnReasonsBean.class);
            EventBus.getDefault().post(new Event(Constants.RETURN_REASONS,""));
        }
    }
}
