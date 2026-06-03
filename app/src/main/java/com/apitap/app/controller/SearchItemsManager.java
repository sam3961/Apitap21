package com.apitap.app.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.app.model.Client;
import com.apitap.app.model.Constants;
import com.apitap.app.model.Utils;
import com.apitap.app.model.bean.AdsBean;
import com.apitap.app.model.bean.AdsDetailBean;
import com.apitap.app.model.bean.AdsDetailWithMerchant;
import com.apitap.app.model.bean.AdsListBean;
import com.apitap.app.model.bean.SearchBean;
import com.apitap.app.model.bean.SearchBusinessBean;
import com.apitap.app.model.bean.SearchItemBean;
import com.apitap.app.model.bean.SearchSpecialBean;
import com.apitap.app.model.bean.ShopAsstSearchBean;
import com.apitap.app.model.customclasses.Event;
import com.apitap.app.model.preferences.ATPreferences;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sahil on 10/08/16.
 */
public class SearchItemsManager {

    private static final String TAG = SearchItemsManager.class.getSimpleName();
    public SearchItemBean searchItemBean;
    public SearchSpecialBean searchSpecialBean;
    public ShopAsstSearchBean asstSearchBean;
    public ArrayList<String> searchDuplicacyRemoveList = new ArrayList<>();
    public HashMap<Integer, AdsBean> ads = new HashMap<Integer, AdsBean>();
    public ArrayList<AdsDetailBean> arrayAds = new ArrayList<AdsDetailBean>();
    public AdsListBean adsListBean;
    public HashMap<Integer, ArrayList<SearchBean>> itemsData = new HashMap<Integer, ArrayList<SearchBean>>();
    public ArrayList<SearchBusinessBean> businessData = new ArrayList<>();
    public static ArrayList<AdsDetailWithMerchant> url_maps_new = new ArrayList<AdsDetailWithMerchant>();
    public HashMap<Integer, AdsBean> url_maps = new HashMap<Integer, AdsBean>();
    public static ArrayList<AdsDetailWithMerchant> url_maps1 = new ArrayList<AdsDetailWithMerchant>();


    public void getAllSearchProduct(Context context, String params) {
        itemsData = new HashMap<>();
        businessData = new ArrayList<>();
        ads = new HashMap<>();
        url_maps = new HashMap<>();
        url_maps1 = new ArrayList<>();
        arrayAds = new ArrayList<>();
        searchSpecialBean = null;
        new ExecuteApiProducts(context).execute(params);
    }


    public void imageSearch(Context context, String params, int key) {
        new ExecuteSearchImageApi(context, key).execute(params);
    }

    private class ExecuteApiProducts extends AsyncTask<String, String, String> {
        private int key;
        Context mContext;

        ExecuteApiProducts(Context context) {
            mContext = context;
        }


        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_search_Item2---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            boolean isResult=false;
            try {

                JSONObject jsonObjectMain = new JSONObject(s);
                JSONArray mainArray = jsonObjectMain.getJSONArray("RESULT");
                for (int z = 0; z < mainArray.length(); z++) {
                    JSONObject jobj = mainArray.getJSONObject(z);
                    if (jobj.has("_101")) {
                        if (jobj.getString("_101").equals("010400478") || jobj.getString("_101").equals("010400807")) {
                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            itemsData = new HashMap<>();
                            JSONObject imgObj1 = imgeArray.getJSONObject(0);
                            if (imgObj1.has("_114_53")) {
                                int itemIndex = 0;
                                for (int j = 0; j < imgeArray.length(); j++) {
                                    JSONObject imgObj = imgeArray.getJSONObject(j);
                                    String categoryName = imgObj.getString("_114_53");
                                    JSONArray pcArr = imgObj.getJSONArray("PC");
                                    if (pcArr.length() > 0) {
                                        isResult = true;
                                        for (int k = 0; k < pcArr.length(); k++) {
                                            JSONObject object = pcArr.getJSONObject(k);
                                            SearchBean bean = new SearchBean();
                                            bean.setImageUrls(ATPreferences.readString(mContext, Constants.KEY_IMAGE_URL) + "_t_" + object.getString("_121_170"));
                                            bean.setProductId(object.optString("_114_144"));
                                            bean.setProdcutType(object.optString("_114_112"));
                                            bean.setProductName(object.optString("_120_83"));
                                            bean.setCategoryName(categoryName);
                                            bean.setIsFavorite(object.optString("_121_80"));
                                            bean.setSellerName(Utils.hexToASCII(object.optString("_120_83")));
                                            bean.setIsSeen(object.optString("_114_9"));
                                            Log.d("setIsSeens", object.optString("_114_9"));
                                            bean.setActualPrice(object.optString("_114_98"));
                                            bean.setPriceAfterDiscount(object.optString("_122_158"));
                                            bean.setDescription(object.optString("_120_157"));
                                            ArrayList<SearchBean> urlArr = new ArrayList<>();
                                            urlArr.add(bean);
                                            itemsData.put(itemIndex++, urlArr);
                                        }
                                    }
                                }
                                if (isResult)
                                    EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS, ""));
                            }
                            else
                                EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS_Empty, ""));

                        } else if (jobj.getString("_101").equals("010400868")) {
                            businessData = new ArrayList<>();
                            JSONArray businessArray = jobj.getJSONArray("RESULT");
                            for (int i = 0; i < businessArray.length(); i++) {
                                JSONObject businessObject = businessArray.getJSONObject(i);
                                if (businessObject.has("_114_179")) {
                                    SearchBusinessBean businessBean = new SearchBusinessBean();
                                    businessBean.setRank(businessObject.optString("_114_1"));
                                    businessBean.setBusinessId(businessObject.optString("_114_179"));
                                    businessBean.setBusinessName(Utils.hexToASCII(businessObject.optString("_114_70")));
                                    businessBean.setImageUrl(ATPreferences.readString(mContext, Constants.KEY_IMAGE_URL)
                                            + businessObject.optString("_121_170"));
                                    businessBean.setIsSeen(businessObject.optString("_114_9"));
                                    businessData.add(businessBean);
                                }
                            }
                            if (itemsData != null && itemsData.size() > 0) {
                                EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS, ""));
                            } else {
                                EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS_Empty, ""));
                            }
                        } else if (jobj.getString("_101").equals("010400479") || jobj.getString("_101").equals("010400787")) {
                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            // for (int i=0;i<jsonArray1.length();i++){
                            JSONObject jsonObject2 = imgeArray.getJSONObject(0);
                            if (jsonObject2.has("_114_93")) {
                                searchSpecialBean = new Gson().fromJson(jobj.toString(), SearchSpecialBean.class);
                                EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS_List2, ""));
                            } else {
                                EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS_List2_Empty, ""));
                            }

                        } else if (jobj.getString("_101").equals("010400676") || jobj.getString("_101").equals("010400677")) {
                            ArrayList<AdsDetailWithMerchant> imageArray = new ArrayList<>();
                            HashMap<Integer, AdsBean> localAds = new HashMap<>();
                            Log.d("beanfavmerchant", jobj.toString());
                            JSONArray imgeArrayMain = jobj.getJSONArray("RESULT");
                            int adIndex = 0;

                            for (int i = 0; i < imgeArrayMain.length(); i++) {
                                JSONObject categoryObject = imgeArrayMain.getJSONObject(i);
                                JSONArray adArray = categoryObject.optJSONArray("AD");
                                if (adArray == null || adArray.length() == 0) {
                                    continue;
                                }

                                for (int j = 0; j < adArray.length(); j++) {
                                    JSONObject adObject = adArray.getJSONObject(j);

                                    AdsDetailWithMerchant detailsBean = new AdsDetailWithMerchant();
                                    String adName = adObject.optString("_120_83");
                                    String videoName = adObject.optString("_121_15");
                                    String id = adObject.optString("_121_18");
                                    String adId = adObject.optString("_123_21");
                                    String desc = adObject.optString("_120_157");
                                    String seen = adObject.optString("_114_9");
                                    String merchantName = adObject.optString("_114_70");
                                    String merchantId = adObject.optString("_53");
                                    String merchantLogo = adObject.optString("_121_77");
                                    String imageUrl = adObject.optString("_121_170");

                                    detailsBean.setName(adName);
                                    detailsBean.setId(id);
                                    detailsBean.setAdId(adId);
                                    detailsBean.setMerchantname(merchantName);
                                    detailsBean.setMerchantId(merchantId);
                                    detailsBean.setSeen(seen);
                                    detailsBean.setVideo(videoName);
                                    detailsBean.setDesc(desc);
                                    detailsBean.setImageUrl(imageUrl);
                                    detailsBean.setBusinssType(categoryObject.optString("_120_45"));
                                    imageArray.add(detailsBean);

                                    ArrayList<AdsDetailBean> innerItems = new ArrayList<>();
                                    JSONArray irArray = adObject.optJSONArray("IR");
                                    if (irArray != null) {
                                        for (int k = 0; k < irArray.length(); k++) {
                                            JSONObject itemObject = irArray.getJSONObject(k);
                                            AdsDetailBean adsDetailBean = new AdsDetailBean();
                                            adsDetailBean.setId(itemObject.optString("_114_144"));
                                            adsDetailBean.setImageUrl(itemObject.optString("_121_170"));
                                            adsDetailBean.setName(itemObject.optString("_120_83"));
                                            adsDetailBean.setSeen(itemObject.optString("_114_9"));
                                            adsDetailBean.setMerchantName(itemObject.optString("_114_179"));
                                            adsDetailBean.setActualPrice(itemObject.optString("_114_98"));
                                            adsDetailBean.setPriceAfterDiscount(itemObject.optString("_122_158"));
                                            innerItems.add(adsDetailBean);
                                        }
                                    }

                                    AdsBean adsBean = new AdsBean();
                                    adsBean.setImageUrl(ATPreferences.readString(mContext, Constants.KEY_IMAGE_URL) + imageUrl);
                                    adsBean.setSeen(seen);
                                    adsBean.setMerchantName(merchantName);
                                    adsBean.setVideoUrl(videoName);
                                    adsBean.setArrayList(innerItems);
                                    adsBean.setMerchantLogo(merchantLogo);
                                    adsBean.setMerchantId(merchantId);
                                    adsBean.setDescription(desc);
                                    adsBean.setAdName(adName);
                                    adsBean.setId(id);
                                    adsBean.setAdId(adId);
                                    adsBean.setBusiness_type(categoryObject.optString("_120_45"));
                                    localAds.put(adIndex++, adsBean);
                                }
                            }

                            url_maps1 = imageArray;
                            url_maps = localAds;
                            ads = localAds;
                            arrayAds = new ArrayList<>();

                            if (localAds.size() > 0) {
                                EventBus.getDefault().post(new Event(Constants.ADS_LISTING_SUCCESS, ""));
                            } else {
                                EventBus.getDefault().post(new Event(Constants.ADS_LISTING_EMPTY, ""));
                            }
                        }
                    }
                }

            } catch (Exception e) {
                Log.d("exception Here", e.getMessage());
                EventBus.getDefault().post(new Event(-1, ""));
            }
        }
    }



    private class ExecuteSearchImageApi extends AsyncTask<String, String, String> {

        Context context;
        int key;

        public ExecuteSearchImageApi(Context context, int key) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            return Client.Caller(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("Search Results: ", "" + s);

            JSONObject jsonObjectMain = null;
            boolean isResult = false;
            try {
                jsonObjectMain = new JSONObject(s);
                JSONArray mainArray = jsonObjectMain.getJSONArray("RESULT");
                JSONObject jobj = mainArray.getJSONObject(0);
                JSONArray imgeArray = jobj.getJSONArray("RESULT");
                itemsData = new HashMap<>();
                JSONObject imgObj1 = imgeArray.getJSONObject(0);
                if (imgObj1.has("_114_53")) {
                    for (int j = 0; j < imgeArray.length(); j++) {
                        JSONObject imgObj = imgeArray.getJSONObject(j);
                        String categoryName = imgObj.getString("_114_53");
                        JSONArray pcArr = imgObj.getJSONArray("PC");
                        if (pcArr.length() > 0) {
                            ArrayList<SearchBean> urlArr = new ArrayList<>();
                            isResult = true;
                            for (int k = 0; k < pcArr.length(); k++) {
                                JSONObject object = pcArr.getJSONObject(k);
                                SearchBean bean = new SearchBean();
                                bean.setImageUrls(ATPreferences.readString(context, Constants.KEY_IMAGE_URL) + "_t_" + object.getString("_121_170"));
                                bean.setProductId(object.getString("_114_144"));
                                bean.setProdcutType(object.getString("_114_112"));
                                bean.setProductName(object.getString("_120_83"));
                                bean.setCategoryName(categoryName);
                                bean.setIsFavorite(object.getString("_121_80"));
                                bean.setSellerName(Utils.hexToASCII(object.getString("_120_83")));
                                bean.setIsSeen(object.getString("_114_9"));
                                Log.d("setIsSeens", object.getString("_114_9"));
                                bean.setActualPrice(object.getString("_114_98"));
                                bean.setPriceAfterDiscount(object.getString("_122_158"));
                                bean.setDescription("");
                                urlArr.add(bean);
                            }
                            itemsData.put(j, urlArr);
                        }
                    }
                    if (isResult)
                        EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS, ""));
                } else
                    EventBus.getDefault().post(new Event(Constants.SEARCH_ITEM_SUCCESS_Empty, ""));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
