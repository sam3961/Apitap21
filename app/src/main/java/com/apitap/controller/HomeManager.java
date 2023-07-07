package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Logger;
import com.apitap.model.Utils;
import com.apitap.model.bean.AdsBean;
import com.apitap.model.bean.AdsDetailBean;
import com.apitap.model.bean.FilterCategoryBean;
import com.apitap.model.bean.FilterCategoryNewBean;
import com.apitap.model.bean.HomeItemBean;
import com.apitap.model.bean.ImagesBean;
import com.apitap.model.bean.SpecialsBean;
import com.apitap.model.bean.categories.CategoryBean;
import com.apitap.model.bean.items.ItemsResponse;
import com.apitap.model.customclasses.Event;
import com.apitap.model.home.address.HomeAddressResponse;
import com.apitap.model.home.ads.HomeAdsResponse;
import com.apitap.model.home.specials.HomeSpecialsResponse;
import com.apitap.model.home.stores.HomeStoresResponse;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.storeCategories.StoreCategoryResponse;
import com.apitap.model.storeFrontItems.ads.StoreAdsResponse;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by apple on 10/08/16.
 */
public class HomeManager {

    private static final String TAG = HomeManager.class.getSimpleName();
    public HashMap<Integer, AdsBean> ads = new HashMap<Integer, AdsBean>();
    public ArrayList<String> listAddresses;
    public ArrayList<String> addressNickName;
    public static ArrayList<String> businessTypesList = new ArrayList<>();
    public HashMap<Integer, ArrayList<ImagesBean>> itemsData = new HashMap<Integer, ArrayList<ImagesBean>>();
    public HomeItemBean homeItemBean;
    public HashMap<Integer, ArrayList<ImagesBean>> specialData = new HashMap<Integer, ArrayList<ImagesBean>>();
    public ArrayList<AdsDetailBean> arrayAds = new ArrayList<AdsDetailBean>();
    public static FilterCategoryBean filterCategoryBean1;
    public static FilterCategoryBean filterCategoryBean2;
    public static FilterCategoryBean filterCategoryBean3;
    public static FilterCategoryNewBean filterCategoryNewBean;
    public static ItemsResponse itemsBean;
    public static SpecialsBean specialsBean;
    private boolean isShowingAll = false;
    public static CategoryBean categoryBean;
    public HomeAdsResponse homeAdsResponse;
    public HomeStoresResponse homeStoresResponse;
    public StoreCategoryResponse storeCategoryResponse;
    public HomeSpecialsResponse homeSpecialsResponse;
    public HomeAddressResponse homeAddressResponse;


    public HashMap<Integer, AdsBean> getHome(Context context, String params) {
        new ExecuteApi(context).execute(params);
        return ads;
    }

    public void getItems(Context context, String params, boolean b) {
        this.isShowingAll = b;
        new ExecuteItemsApi(context).execute(params);

    }

    public void getSpecials(Context context, String params) {
        new ExecuteSpecialsApi(context).execute(params);

    }

    public void getCategoriesItems(Context context, String params) {
        new ExecuteCategoriesItemsApi(context).execute(params);

    }

    public HashMap<Integer, AdsBean> getHomeWithOutCategories(Context context, String params) {
        new ExecuteApiNoCat(context).execute(params);
        return ads;
    }

    public void getHomeScreesData(Context context, String params) {
        new ExecuteApiHomeData(context).execute(params);

    }

    private class ExecuteApiHomeData extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiHomeData(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_home_data---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "" + s);

            try {
                if (s != null && !s.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("RESULT");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String param101 = jsonObject1.getString("_101");
                        switch (param101) {
                            case "010400239": //for stores

                                HomeStoresResponse homeStoresResponseTemp = new Gson().fromJson(jsonObject1.toString(),
                                        HomeStoresResponse.class);
                                if (homeStoresResponseTemp.getRESULT().size() > 0 &&
                                        homeStoresResponseTemp.getRESULT().get(0).getJsonMember478() != null) {
                                    homeStoresResponse = new Gson().fromJson(jsonObject1.toString(), HomeStoresResponse.class);
                                    EventBus.getDefault().post(new Event(Constants.HOME_STORES, true));
                                } else
                                    EventBus.getDefault().post(new Event(Constants.HOME_STORES, false));

                                break;
                            case "010400645": //for stores by categories

                                StoreCategoryResponse storeCategoryResponseTemp = new Gson().fromJson(jsonObject1.toString(),
                                        StoreCategoryResponse.class);
                                if (storeCategoryResponseTemp.getRESULT().size() > 0 &&
                                        storeCategoryResponseTemp.getRESULT().get(0).getJsonMember11493() != null) {
                                    storeCategoryResponse = new Gson().fromJson(jsonObject1.toString(), StoreCategoryResponse.class);
                                    EventBus.getDefault().post(new Event(Constants.HOME_STORES_BY_CATEGORY, true));
                                } else
                                    EventBus.getDefault().post(new Event(Constants.HOME_STORES_BY_CATEGORY, false));

                                break;

                            case "010100710": //for specials

                                HomeSpecialsResponse homeSpecialsResponseTemp = new Gson().fromJson(jsonObject1.toString(),
                                        HomeSpecialsResponse.class);
                                if (homeSpecialsResponseTemp.getRESULT().size() > 0 &&
                                        homeSpecialsResponseTemp.getRESULT().get(0).getJsonMember1149() != null) {
                                    homeSpecialsResponse = new Gson().fromJson(jsonObject1.toString(), HomeSpecialsResponse.class);
                                    EventBus.getDefault().post(new Event(Constants.HOME_SPECIALS, true));
                                } else
                                    EventBus.getDefault().post(new Event(Constants.HOME_SPECIALS, false));

                                break;

                            case "010100055": //for address list
                                HomeAddressResponse homeAddressResponseTemp = new Gson().fromJson(jsonObject1.toString(),
                                        HomeAddressResponse.class);
                                if (homeAddressResponseTemp.getRESULT().size() > 0 &&
                                        homeAddressResponseTemp.getRESULT().get(0).getAD() != null &&
                                        homeAddressResponseTemp.getRESULT().size() > 0 &&
                                        homeAddressResponseTemp.getRESULT().get(0).getAD().size() > 0 &&
                                        homeAddressResponseTemp.getRESULT().get(0).getAD().get(0).getJsonMember53() != null) {
                                    homeAddressResponse = new Gson().fromJson(jsonObject1.toString(), HomeAddressResponse.class);
                                    EventBus.getDefault().post(new Event(Constants.HOME_ADDRESS, true));
                                } else
                                    EventBus.getDefault().post(new Event(Constants.HOME_ADDRESS, false));

                                break;

                            case "010400676": //ads
                                HomeAdsResponse homeAdsResponseTemp = new Gson().fromJson(jsonObject1.toString(),
                                        HomeAdsResponse.class);
                                if (homeAdsResponseTemp.getRESULT().size() > 0 &&
                                        homeAdsResponseTemp.getRESULT().get(0).getJsonMember1149() != null) {

                                    homeAdsResponse = new Gson().fromJson(jsonObject1.toString(), HomeAdsResponse.class);
                                    EventBus.getDefault().post(new Event(Constants.HOME_ADS, true));
                                } else
                                    EventBus.getDefault().post(new Event(Constants.HOME_ADS, false));

                                break;
                        }
                    }
                } else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;
        HashMap<Integer, AdsBean> url_maps = new HashMap<Integer, AdsBean>();

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_Search_Item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.v(TAG, s);
            Logger.addRecordToLog(s);
            try {
                //   listAddresses = new ArrayList<>();
                // listAddresses.add("- GPS");
                JSONObject jsonObject = new JSONObject(s);
                ATPreferences.putString(mContext, Constants.KEY_USER_DEFAULT, jsonObject.has("_192") ? jsonObject.getString("_192") : "");
                JSONArray mainArray = jsonObject.getJSONArray("RESULT");

                for (int i = 0; i < mainArray.length(); i++) {
                    JSONObject jobj = mainArray.getJSONObject(i);
                    if (jobj.has("_101")) {
                        if (jobj.getString("_101").equals("010200517")) {//for ads
                            ATPreferences.putString(mContext, Constants.KEY_USER_PIN, jsonObject.has("_39") ? jsonObject.getString("_39") : "");
                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            if (imgeArray.length() > 0) {
                                for (int j = 0; j < imgeArray.length(); j++) {
                                    JSONObject imgObj = imgeArray.getJSONObject(j);
                                    String url = ATPreferences.readString(mContext, Constants.KEY_IMAGE_URL) + imgObj.getString("_121_170");
                                    String videoUrl = imgObj.getString("_121_15");
                                    String isSeen = imgObj.getString("_114_9");
                                    String merchantName = imgObj.getString("_114_70");
                                    String merchantLogo = imgObj.getString("_121_77");
                                    String id = imgObj.getString("_121_18");
                                    if (videoUrl.equals("")) {
                                        JSONArray array = imgObj.getJSONArray("IR");
                                        if (array.length() > 0) {
                                            for (int k = 0; k < array.length(); k++) {
                                                JSONObject object = array.getJSONObject(k);
                                                String imageUrl = object.getString("_121_170");

                                                String imageName = object.getString("_120_83");

                                                String actualPrice = object.getString("_114_98");
                                                String priceAfterDiscount = object.getString("_122_158");

                                                Log.d("IsSEENAD", isSeen + "");
                                                Log.d("imageUrlq", imageUrl + "");

                                                AdsDetailBean adsDetailBean = new AdsDetailBean();
                                                adsDetailBean.setId(id);
                                                adsDetailBean.setImageUrl(imageUrl);
                                                adsDetailBean.setName(imageName);
                                                adsDetailBean.setSeen(isSeen);
                                                adsDetailBean.setMerchantName(Utils.hexToASCII(merchantName));
                                                adsDetailBean.setActualPrice(actualPrice);
                                                adsDetailBean.setPriceAfterDiscount(priceAfterDiscount);
                                                arrayAds.add(adsDetailBean);

                                            }
                                        }
                                    }

                                    AdsBean adsBean = new AdsBean();
                                    adsBean.setImageUrl(url);
                                    adsBean.setVideoUrl(videoUrl);
                                    adsBean.setSeen(isSeen);
                                    adsBean.setMerchantName(Utils.hexToASCII(merchantName));
                                    adsBean.setMerchantLogo(merchantLogo);
                                    adsBean.setArrayList(arrayAds);

                                    url_maps.put(j, adsBean);
                                    ads = url_maps;
                                }
                            } else {
                                ads.clear();
                                AdsBean bean = new AdsBean();
                                AdsDetailBean beans = new AdsDetailBean();
                            }
                            Log.e("size of arrayads", arrayAds.size() + "");
                            ATPreferences.putString(mContext, Constants.KEY_STOP, "true");
                        } else if (jobj.getString("_101").equals("010400478")) { //for items
                            businessTypesList = new ArrayList<>();
                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            //Log.d("ImageArrayLeng", imgeArray.length() + "");
                            if (imgeArray.length() > 0) {
                                itemsData = new HashMap<>();
                                for (int j = 0; j < imgeArray.length(); j++) {
                                    JSONObject imgObj = imgeArray.getJSONObject(j);
                                    if (imgObj.has("_120_45")) {

                                        String Businesstype = imgObj.getString("_120_83");
                                        Log.d("BusinessTypess", Businesstype + "");
                                        String categoryName = imgObj.getString("_120_45");
                                        String subCategoryName = imgObj.getString("_114_53");
                                        //  businessTypesList.add(Businesstype);
                                        JSONArray pcArr = imgObj.getJSONArray("PC");
                                        ArrayList<ImagesBean> urlArr = new ArrayList<>();
                                        if (pcArr.length() > 0) {
                                            for (int k = 0; k < pcArr.length(); k++) {
                                                JSONObject object = pcArr.getJSONObject(k);
                                                ImagesBean bean = new ImagesBean();
                                                bean.setImageUrls(ATPreferences.readString(mContext, Constants.KEY_IMAGE_URL) + "_t_" + object.getString("_121_170"));
                                                bean.setProductId(object.getString("_114_144"));
                                                bean.setProdcutType(object.getString("_114_112"));
                                                bean.setBusiness_type(Businesstype);
                                                bean.setCategoryName(categoryName);
                                                bean.setSubCategoryName(subCategoryName);
                                                bean.setIsFavorite(object.getString("_121_80"));
                                                bean.setSellerName(object.getString("_120_83"));
                                                bean.setIsSeen(object.getString("_114_9"));
                                                bean.setActualPrice(object.getString("_114_98"));
                                                bean.setPriceAfterDiscount(object.getString("_122_158"));
                                                bean.setDescription(object.getString("_120_157"));
                                                Log.d("seeprices", Utils.hexToASCII(object.getString("_120_83")) + "  " + object.getString("_114_98") + "  " + object.getString("_122_158"));

                                                urlArr.add(bean);
                                            }
                                            //      if (!businessTypesList.contains(Businesstype))
                                            //       businessTypesList.add(Businesstype);
                                            // else
                                            //   businessTypesList.add("");
                                            Log.d("whtsJ", j + "  d");
                                            itemsData.put(j, urlArr);
                                        }
                                    }
                                }
                            } else {
                                itemsData.clear();
                                ImagesBean bean = new ImagesBean();
                            }
                        } else if (jobj.getString("_101").equals("010400479")) { //for specials
                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            if (imgeArray.length() > 0) {
                                specialData = new HashMap<>();
                                for (int j = 0; j < imgeArray.length(); j++) {
                                    JSONObject imgObj = imgeArray.getJSONObject(j);
                                    if (imgObj.has("_120_45")) {
                                        String businessType = imgObj.getString("_120_83");
                                        String categoryName = imgObj.getString("_120_45");
                                        String subCategoryName = imgObj.getString("_114_53");
                                        JSONArray pcArr = imgObj.getJSONArray("PC");
                                        ArrayList<ImagesBean> urlArr = new ArrayList<>();
                                        for (int k = 0; k < pcArr.length(); k++) {
                                            JSONObject object = pcArr.getJSONObject(k);
                                            ImagesBean bean = new ImagesBean();
                                            bean.setImageUrls(ATPreferences.readString(mContext, Constants.KEY_IMAGE_URL) + "_t_" + object.getString("_121_170"));
                                            bean.setProductId(object.getString("_114_144"));
                                            bean.setProdcutType(object.getString("_114_112"));
                                            bean.setCategoryName(categoryName);
                                            bean.setIsFavorite(object.getString("_121_80"));
                                            bean.setSellerName(object.getString("_120_83"));

                                            Log.d("sellerNames", object.getString("_120_83"));
                                            bean.setIsSeen(object.getString("_114_9"));
                                            bean.setBusiness_type(businessType);
                                            bean.setSubCategoryName(subCategoryName);
                                            Log.d("setIsSeenspe", object.getString("_114_9"));
                                            bean.setActualPrice(object.getString("_114_98"));
                                            bean.setPriceAfterDiscount(object.getString("_122_162"));
                                            bean.setDescription(object.getString("_120_157"));
                                            urlArr.add(bean);
                                            Log.d("SpecialsMange", object + "");
                                        }
                                        specialData.put(j, urlArr);
                                    }


                                }
                            } else {
                                specialData.clear();
                                ImagesBean bean = new ImagesBean();
                            }
                        } else if (jobj.getString("_101").equals("010100055")) {

                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            for (int j = 0; j < imgeArray.length(); j++) {
                                JSONObject resObj = imgeArray.getJSONObject(j);

                                JSONArray addressArray = resObj.getJSONArray("AD");
                                for (int k = 0; k < addressArray.length(); k++) {
                                    JSONObject addressObj = addressArray.getJSONObject(k);
                                    String addresses = addressObj.getString("_114_53");
                                    //Log.e("Addresses: ", "" + addresses);
                                    //if (!addresses.isEmpty())
                                    // listAddresses.add(addresses);
                                }

                            }
                            EventBus.getDefault().post(new Event(Constants.ADDRESS_SUCCESS, ""));

                        }

                    }
                }

                EventBus.getDefault().post(new Event(Constants.ALL_IMAGES_SUCCESS, ""));
            } catch (JSONException e) {
                e.printStackTrace();
                EventBus.getDefault().post(new Event(-1, ""));
            }
        }
    }

    private class ExecuteApiNoCat extends AsyncTask<String, String, String> {
        Context mContext;
        HashMap<Integer, AdsBean> url_maps = new HashMap<Integer, AdsBean>();


        ExecuteApiNoCat(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_HomeNoCat---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.v(TAG, s);
            try {
                listAddresses = new ArrayList<>();
                addressNickName = new ArrayList<>();

                addressNickName.add("GPS -");
                listAddresses.add("Use Current Location");
                JSONObject jsonObject = new JSONObject(s);
                ATPreferences.putString(mContext, Constants.KEY_USER_DEFAULT, jsonObject.has("_192") ? jsonObject.getString("_192") : "");
                JSONArray mainArray = jsonObject.getJSONArray("RESULT");

                for (int i = 0; i < mainArray.length(); i++) {
                    JSONObject jobj = mainArray.getJSONObject(i);
                    if (jobj.has("_101")) {
                        switch (jobj.getString("_101")) {
                            case "010100709": { //for items
                                businessTypesList = new ArrayList<>();
                                homeItemBean = new Gson().fromJson(mainArray.getJSONObject(i).toString(), HomeItemBean.class);

                                JSONArray imgeArray = jobj.getJSONArray("RESULT");
                                //Log.d("ImageArrayLeng", imgeArray.length() + "");
                                if (imgeArray.length() > 0) {
                                    ArrayList<ImagesBean> urlArr = new ArrayList<>();
                                    itemsData = new HashMap<>();
                                    for (int j = 0; j < imgeArray.length(); j++) {
                                        JSONObject object = imgeArray.getJSONObject(j);

                                        ImagesBean bean = new ImagesBean();
                                        bean.setImageUrls(ATPreferences.readString(mContext, Constants.KEY_IMAGE_URL) + "_t_" + object.getString("_121_170"));
                                        bean.setProductId(object.getString("_114_144"));
                                        bean.setProdcutType(object.getString("_114_112"));
                                        //  bean.setIsFavorite(object.getString("_121_80"));
                                        bean.setSellerName(object.getString("_120_83"));
                                        bean.setDescription(object.getString("_120_83"));
                                        bean.setIsSeen(object.getString("_114_9"));
                                        bean.setActualPrice(object.getString("_114_98"));
                                        bean.setPriceAfterDiscount(object.getString("_122_158"));
                                        //    bean.setDescription(object.getString("_120_157"));
                                        Log.d("seeprices", Utils.hexToASCII(object.getString("_120_83")) + "  " + object.getString("_114_98") + "  " + object.getString("_122_158"));

                                        urlArr.add(bean);
                                        itemsData.put(j, urlArr);
                                    }
                                    //  Log.d("whtsJ", j + "  d");


                                } else {
                                    itemsData = new HashMap<>();
                                }
                                break;
                            }
                            case "010100710": { //for specials
                                JSONArray imgeArray = jobj.getJSONArray("RESULT");
                                if (imgeArray.length() > 0) {
                                    specialData = new HashMap<>();
                                    ArrayList<ImagesBean> urlArr = new ArrayList<>();
                                    for (int j = 0; j < imgeArray.length(); j++) {
                                        JSONObject object = imgeArray.getJSONObject(j);

                                        ImagesBean bean = new ImagesBean();
                                        bean.setImageUrls(ATPreferences.readString(mContext, Constants.KEY_IMAGE_URL) + "_t_" + object.getString("_121_170"));
                                        bean.setProductId(object.getString("_114_144"));
                                        bean.setProdcutType(object.getString("_114_112"));

                                        bean.setSellerName(object.getString("_120_83"));
                                        bean.setDescription(object.getString("_120_83"));
                                        Log.d("sellerNames", object.getString("_120_83"));
                                        bean.setIsSeen(object.getString("_114_9"));

                                        Log.d("setIsSeenspe", object.getString("_114_9"));
                                        bean.setActualPrice(object.getString("_114_98"));
                                        bean.setPriceAfterDiscount(object.getString("_122_162"));
                                        //  bean.setDescription(object.getString("_120_157"));
                                        urlArr.add(bean);
                                        Log.d("SpecialsMange", object + "");
                                        specialData.put(j, urlArr);
                                    }

                                } else
                                    specialData = new HashMap<>();

                                break;
                            }
                            case "010100055": {
                                System.out.println("addresses****" + jobj.toString());
                                JSONArray imgeArray = jobj.getJSONArray("RESULT");
                                for (int j = 0; j < imgeArray.length(); j++) {
                                    JSONObject resObj = imgeArray.getJSONObject(j);

                                    JSONArray addressArray = resObj.getJSONArray("AD");
                                    for (int k = 0; k < addressArray.length(); k++) {
                                        JSONObject addressObj = addressArray.getJSONObject(k);
                                        String addresses = addressObj.getString("_114_53");
                                        JSONObject jsonObject1 = addressObj.getJSONObject("CO");
                                        JSONObject ci = addressObj.getJSONObject("CI");
                                        String location1 = ci.getString("_47_15");
                                        String address1 = jsonObject1.getString("_114_17");
                                        JSONObject jsonObject2 = addressObj.getJSONObject("ST");
                                        JSONObject zp = addressObj.getJSONObject("ZP");
                                        String zpCode = zp.getString("_47_17");
                                        String address2 = jsonObject2.getString("_123_4");
                                        //Log.e("Addresses: ", "" + addresses);

                                        if (!addresses.isEmpty()) {
                                            addressNickName.add(Utils.hexToASCII(addresses) + " -");
                                            listAddresses.add(location1 + " " + address2 + " " + zpCode + "(" + address1 + ")");
                                        }
                                    }

                                }
                                EventBus.getDefault().post(new Event(Constants.ADDRESS_SUCCESS, ""));

                                break;
                            }
                        }

                    }
                }

                EventBus.getDefault().post(new Event(Constants.ALL_IMAGES_SUCCESS, ""));
            } catch (JSONException e) {
                e.printStackTrace();
                EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, ""));
            }
        }
    }

    private class ExecuteItemsApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteItemsApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_Itemss---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty()) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                    JSONObject object = jsonArray1.getJSONObject(0);
                    if (!object.has("_114_53")) {
                        EventBus.getDefault().post(new Event(Constants.ALL_IMAGES_SUCCESS, "false"));
                        return;
                    }

                    itemsBean = new Gson().fromJson(s, ItemsResponse.class);
                    if (itemsBean.getRESULT().get(0).getRESULT().get(0).getPC() != null)
                        EventBus.getDefault().post(new Event(Constants.ALL_IMAGES_SUCCESS, ""));
                    else
                        EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, ""));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private class ExecuteSpecialsApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteSpecialsApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_Specials---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.isEmpty()) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);

                    JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                    JSONObject object = jsonArray1.getJSONObject(0);
                    if (!object.has("_114_53")) {
                        EventBus.getDefault().post(new Event(Constants.ALL_IMAGES_SUCCESS, "false"));
                        return;
                    }

                    specialsBean = new Gson().fromJson(s, SpecialsBean.class);
                    if (specialsBean.getRESULT().get(0).getRESULT().get(0).getPC() != null)
                        EventBus.getDefault().post(new Event(Constants.ALL_IMAGES_SUCCESS, ""));
                    else
                        EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, ""));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
                EventBus.getDefault().post(new Event(Constants.GET_SERVER_ERROR, ""));


        }
    }

    private class ExecuteCategoriesItemsApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteCategoriesItemsApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_CategoriesItemsApi" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.v(TAG, s);
            if (s != null) {
           /*   CategoryBean  bean = new Gson().fromJson(s,CategoryBean.class);
                if (bean.getRESULT().get(0).getRESULT().size()>0) {
                    categoryBean =new Gson().fromJson(s,CategoryBean.class);
                    EventBus.getDefault().post(new Event(Constants.CATEGORY_FILTER, "true"));
                }
                else
                    EventBus.getDefault().post(new Event(Constants.CATEGORY_FILTER, "false"));
*/

                try {


                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                    if (jsonArray1.length() < 1) {
                        EventBus.getDefault().post(new Event(Constants.CATEGORY_FILTER, "false"));
                        return;
                    }
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                    switch (jsonObject2.getString("_120_44")) {
                        case "1":
                            filterCategoryBean1 = new Gson().fromJson(s, FilterCategoryBean.class);
                            break;
                        case "2":
                            filterCategoryBean2 = new Gson().fromJson(s, FilterCategoryBean.class);
                            break;
                        case "3":
                            filterCategoryBean3 = new Gson().fromJson(s, FilterCategoryBean.class);
                            break;
                    }

                    filterCategoryNewBean = new Gson().fromJson(s, FilterCategoryNewBean.class);
                    EventBus.getDefault().post(new Event(Constants.CATEGORY_FILTER, ""));


                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post(new Event(Constants.CATEGORY_FILTER, "false"));
                }
            }
        }
    }
}
