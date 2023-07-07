package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.AdsBean;
import com.apitap.model.bean.AdsDetailBean;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.bean.FavBeanOld;
import com.apitap.model.bean.FavBeanSpecial;
import com.apitap.model.bean.FavMerchantBean;
import com.apitap.model.bean.Favdetailsbean;
import com.apitap.model.bean.Favspecialbean;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sahil on 10/08/16.
 */
public class FavouriteManager {

    private static final String TAG = FavouriteManager.class.getSimpleName();
    public FavBeanOld favBean;
    public FavBeanSpecial favBeanSpecial;
    public FavMerchantBean favMerchantBean;
    public ArrayList<Favdetailsbean> favdetailsbeenlist = new ArrayList<Favdetailsbean>();
    public ArrayList<String> favIds;
    public HashMap<Integer, AdsBean> url_maps = new HashMap<Integer, AdsBean>();
    public static ArrayList<AdsDetailWithMerchant> url_maps1 = new ArrayList<AdsDetailWithMerchant>();
    public ArrayList<String> hexIds = new ArrayList<String>();
    public ArrayList<AdsDetailBean> arrayAds = new ArrayList<AdsDetailBean>();
    public HashMap<Integer, AdsBean> ads = new HashMap<Integer, AdsBean>();
    public ArrayList<String> business_typeList = new ArrayList<>();
    public HashMap<Integer, ArrayList<Favdetailsbean>> itemsData = new HashMap<Integer, ArrayList<Favdetailsbean>>();
    public HashMap<Integer, ArrayList<Favspecialbean>> itemsDataSpecial = new HashMap<Integer, ArrayList<Favspecialbean>>();


    public void removeFavourites(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }

    public void getFavourites(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }

    public void getAdFavourites(Context context, String params) {
        new ExecuteAdApi(context).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {
        private final int key;
        Context mContext;

        ExecuteApi(Context context, int key) {
            mContext = context;
            this.key = key;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_favouritItem---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String imageUrl = "";
            hexIds.clear();
            Log.d(TAG, s);
            itemsData = new HashMap<>();
            try {

                JSONObject jsonObjectMain = new JSONObject(s);
                JSONArray mainArray = jsonObjectMain.getJSONArray("RESULT");
                for (int z = 0; z < mainArray.length(); z++) {
                    JSONObject jobj = mainArray.getJSONObject(z);
                    if (jobj.has("_101")) {
                        if (jobj.getString("_101").equals("010400221")) // specials and items
                        {
                            JSONArray imgeTrsult = jobj.getJSONArray("RESULT");
                            JSONObject jsonObjectTest = imgeTrsult.getJSONObject(0);
                            if (!jsonObjectTest.has("PC")) {
                                EventBus.getDefault().post(new Event(Constants.NO_SPECIAL_ITEMS_FAVOURITES, ""));
                            } else {

                                JSONArray jsonArrayTest = jsonObjectTest.getJSONArray("PC");
                                JSONObject jsonObject2Test = jsonArrayTest.getJSONObject(0);
                                String idItemorSpecial = jsonObject2Test.getString("_114_112");
                                if (idItemorSpecial.equals("23")) { //specials
                                    for (int i = 0; i < imgeTrsult.length(); i++) {
                                        ArrayList<Favspecialbean> arrayAds = new ArrayList<>();
                                        JSONObject jsonObject2 = imgeTrsult.getJSONObject(i);
                                        String categoryName = jsonObject2.getString("_120_45");

                                        JSONArray jsonArray2 = jsonObject2.getJSONArray("PC");

                                        for (int j = 0; j < jsonArray2.length(); j++) {
                                            Favspecialbean favdetailsbean = new Favspecialbean();
                                            JSONObject jsonObject4 = jsonArray2.getJSONObject(j);
                                            String productId = jsonObject4.getString("_114_144");
                                            String productName = jsonObject4.getString("_120_83");
                                            // String business_type = jsonObject2.getString("_120_83");
                                            Log.d("productName_special", Utils.hexToASCII(productName));
                                            String productIdHex = Utils.lengtT(11, productId);
                                            hexIds.add(productIdHex);
                                            imageUrl = jsonObject4.getString("_121_170");
                                            String productType = jsonObject4.getString("_114_112");
                                            String ActualPrice = jsonObject4.getString("_114_98");
                                            String PriceAfterDiscount = jsonObject4.getString("_122_162");
                                            //  bean.setPriceAfterDiscount(object.getString("_122_162"));

                                            favdetailsbean.setImageUrl(imageUrl);
                                            favdetailsbean.setId(productId);
                                            favdetailsbean.setIdHex(productIdHex);
                                            favdetailsbean.setProductType(productType);
                                            favdetailsbean.setActualPrice(ActualPrice);
                                            favdetailsbean.setPriceAfterDiscount(PriceAfterDiscount);
                                            favdetailsbean.setCategoryName(categoryName);
                                            favdetailsbean.setBusiness_type(categoryName);
                                            arrayAds.add(favdetailsbean);
                                        }
                                        itemsDataSpecial.put(i, arrayAds);
                                    }
                                    favBeanSpecial = new Gson().fromJson(jobj.toString(), FavBeanSpecial.class);
                                    if (favBeanSpecial.getRESULT().size() < 1 || favBeanSpecial.getRESULT().get(0).get11493() == null) {
                                        EventBus.getDefault().post(new Event(Constants.GET_FAVOURITE_SPECIAL_EMPTY, ""));
                                    } else if (favBeanSpecial.get44().equals("Transaction Approved")) {
                                        EventBus.getDefault().post(new Event(Constants.GET_FAVOURITE_SPECIAL, ""));
                                    } else {
                                        EventBus.getDefault().post(new Event(-1, ""));
                                    }
                                } else { //items

                                    JSONArray imgeArrayMain = jobj.getJSONArray("RESULT");
                                    for (int i = 0; i < imgeArrayMain.length(); i++) {
                                        ArrayList<Favdetailsbean> arrayAds = new ArrayList<>();
                                        JSONObject jsonObject2 = imgeArrayMain.getJSONObject(i);
                                        String categoryName = jsonObject2.getString("_120_45");
                                        // String business_type = jsonObject2.getString("_120_83");
                                        JSONArray jsonArray2 = jsonObject2.getJSONArray("PC");

                                        for (int j = 0; j < jsonArray2.length(); j++) {
                                            Favdetailsbean favdetailsbean = new Favdetailsbean();
                                            JSONObject jsonObject4 = jsonArray2.getJSONObject(j);
                                            String productId = jsonObject4.getString("_114_144");
                                            String productName = jsonObject4.getString("_120_83");
                                            Log.d("productName_items", Utils.hexToASCII(productName));
                                            String productIdHex = Utils.lengtT(11, productId);
                                            hexIds.add(productIdHex);
                                            imageUrl = jsonObject4.getString("_121_170");
                                            String productType = jsonObject4.getString("_114_112");
                                            String ActualPrice = jsonObject4.getString("_114_98");
                                            String PriceAfterDiscount = jsonObject4.getString("_122_158");
                                            favdetailsbean.setImageUrl(imageUrl);
                                            favdetailsbean.setId(productId);
                                            favdetailsbean.setIdHex(productIdHex);
                                            favdetailsbean.setProductType(productType);
                                            favdetailsbean.setActualPrice(ActualPrice);
                                            favdetailsbean.setPriceAfterDiscount(PriceAfterDiscount);
                                            favdetailsbean.setCategoryName(categoryName);
                                            favdetailsbean.setBusiness_type(categoryName);
                                            arrayAds.add(favdetailsbean);
                                        }
                                        itemsData.put(i, arrayAds);

                                    }

                                    favBean = new Gson().fromJson(jobj.toString(), FavBeanOld.class);

                                    if (favBean.getRESULT().size() < 1 || favBean.getRESULT().get(0).get11493() == null) {
                                        EventBus.getDefault().post(new Event(Constants.GET_FAVOURITE_ITEM_EMPTY, ""));
                                    } else if (favBean.get44().equals("Transaction Approved")) {
                                        EventBus.getDefault().post(new Event(key, ""));
                                    } else {
                                        EventBus.getDefault().post(new Event(-1, ""));
                                    }
                                }
                            }


                        } else if (jobj.getString("_101").equals("010100303"))  // merchants
                        {
                            favMerchantBean = new Gson().fromJson(jobj.toString(), FavMerchantBean.class);
                            if (favMerchantBean.getRESULT().size() < 1 || favMerchantBean.getRESULT().get(0).get11470() == null) {
                                EventBus.getDefault().post(new Event(Constants.GET_FAVOURITE_MERCHNAT_EMPTY, ""));
                            } else if (favMerchantBean.get44().equals("Transaction Approved")) {
                                EventBus.getDefault().post(new Event(Constants.GET_FAVOURITE_MERCHNAT_SUCCESS, ""));
                            } else {
                                EventBus.getDefault().post(new Event(-1, ""));
                            }
                        } else if (jobj.get("_101").equals("010400681"))  // ads
                        {
                            ArrayList<AdsDetailWithMerchant> imageArray = new ArrayList<>();
                            Log.d("beanfavmerchant", jobj.toString());
                            JSONArray imgeArrayMain = jobj.getJSONArray("RESULT");
                            for (int i = 0; i < imgeArrayMain.length(); i++) {
                                JSONObject jsonObject21 = imgeArrayMain.getJSONObject(i);
                                if (!jsonObject21.has("_120_83")) {
                                    EventBus.getDefault().post(new Event(Constants.ADS_FAVOURITES_EMPTY, ""));
                                } else {
                                    String business_type = jsonObject21.getString("_120_83");
                                    JSONArray jsonArray2 = jsonObject21.getJSONArray("AD");

                                    for (int j = 0; j < jsonArray2.length(); j++) {
                                        JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
                                        AdsDetailWithMerchant detailsBean = new AdsDetailWithMerchant();

                                        String adName = jsonObject2.getString("_120_83");
                                        String videoName = jsonObject2.getString("_121_15");

                                        String id = jsonObject2.getString("_121_18");
                                        String ad_id = jsonObject2.getString("_123_21");
                                        //String id = "11";

                                        String desc = jsonObject2.getString("_120_157");
                                        String seen = jsonObject2.getString("_114_9");
                                        //String seen = "false";
                                        String merchantName = jsonObject2.getString("_114_70");
                                        String merchantId = jsonObject2.getString("_53");
                                        detailsBean.setName(adName);
                                        detailsBean.setId(id);
                                        detailsBean.setAdId(ad_id);
                                        detailsBean.setMerchantname(merchantName);
                                        detailsBean.setMerchantId(merchantId);
                                        detailsBean.setBusinssType(business_type);
                                        detailsBean.setSeen(seen);
                                        detailsBean.setVideo(videoName);
                                        detailsBean.setDesc(desc);
                                        imageArray.add(detailsBean);
                                    }
                                }
                            }
                            url_maps1 = imageArray;
                            for (int j = 0; j < imgeArrayMain.length(); j++) {
                                JSONObject jsonObject21 = imgeArrayMain.getJSONObject(j);
                                if (!jsonObject21.has("_120_83")) {
                                    EventBus.getDefault().post(new Event(Constants.ADS_FAVOURITES_EMPTY, ""));
                                } else {
                                    String business_type = jsonObject21.getString("_120_83");
                                    Log.d("business_tpe", business_type + "");
                                    JSONArray jsonArray2 = jsonObject21.getJSONArray("AD");

                                    for (int k = 0; k < jsonArray2.length(); k++) {
                                        JSONObject imgObj = jsonArray2.getJSONObject(k);
                                        String url = ATPreferences.readString(mContext, Constants.KEY_IMAGE_URL) + imgObj.getString("_121_170");
                                        String videoUrl = imgObj.getString("_121_15");
                                        String id = imgObj.getString("_121_18");
                                        //String id = "11";
                                        String imageName = imgObj.getString("_120_83");
                                        //  String actualPrice = imgObj.getString("_114_98");
                                        //String priceAfterDiscount = imgObj.getString("_122_158");
                                        String seen = imgObj.getString("_114_9");
                                        //String seen = "false";
                                        String merchantName = imgObj.getString("_114_70");
                                        String merchantId = imgObj.getString("_53");

                                        Log.d("imageUrlq", url + "l");

                                        AdsDetailBean adsDetailBean = new AdsDetailBean();
                                        adsDetailBean.setId(id);
                                        adsDetailBean.setImageUrl(url);
                                        adsDetailBean.setName(imageName);

                                        if (!business_typeList.contains(business_type)) {
                                            business_typeList.add(business_type);
                                            adsDetailBean.setBusiness_type(business_type);
                                            Log.d("business_tpe2", business_type + "");
                                        }
                                        adsDetailBean.setSeen(seen);
                                        adsDetailBean.setMerchantName(merchantName);
                                        //adsDetailBean.setActualPrice(actualPrice);
                                        //adsDetailBean.setPriceAfterDiscount(priceAfterDiscount);
                                        arrayAds.add(adsDetailBean);

                                        AdsBean adsBean = new AdsBean();
                                        adsBean.setImageUrl(url);
                                        adsBean.setSeen(seen);
                                        adsBean.setMerchantName(merchantName);
                                        adsBean.setBusiness_type(business_type);
                                        adsBean.setVideoUrl(videoUrl);
                                        adsBean.setAdName(imageName);
                                        adsBean.setArrayList(arrayAds);

                                        url_maps.put(k, adsBean);
                                        ads = url_maps;
                                    }
                                }
                            }
                            EventBus.getDefault().post(new Event(Constants.ADS_LISTING_SUCCESS, ""));
                        } else if (jobj.get("_101").equals("040400219")) {
                            EventBus.getDefault().post(new Event(Constants.REMOVE_FAVOURITE_SUCCESS, ""));
                        }
                    }


                }

            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }
        }
    }

    private class ExecuteAdApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteAdApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "ExecuteAdApi---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                favIds = new ArrayList<String>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                    String id = jsonObject2.getString("_121_18");
                    favIds.add(id);

                }
                EventBus.getDefault().post(new Event(Constants.GET_Ad_FAVOURITE_SUCCESS, ""));
            } catch (Exception e) {

            }
        }
    }

    private class ExecuteRemoveFavApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteRemoveFavApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "ExecuteRemoveFavApi---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String transaction = jsonObject1.getString("_44");
                if (transaction.equals("Transaction Approved"))
                    EventBus.getDefault().post(new Event(Constants.REMOVE_FAVOURITE_SUCCESS, ""));
                else
                    EventBus.getDefault().post(new Event(-1, ""));


            } catch (Exception e) {

            }
        }
    }

}