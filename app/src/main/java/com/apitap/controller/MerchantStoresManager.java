package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;

import com.apitap.model.Logger;
import com.apitap.model.Utils;
import com.apitap.model.activeTvInfo.ActiveTvInfoResponse;
import com.apitap.model.bean.BusinessBean;
import com.apitap.model.bean.MerchantStore;
import com.apitap.model.bean.NearbyStoreBean;
import com.apitap.model.bean.Storebean;
import com.apitap.model.bean.levelOneCategories.LevelOneCategory;
import com.apitap.model.bean.merchantStores.MerchantStoreBean;
import com.apitap.model.brandNames.BrandNamesResponse;
import com.apitap.model.customclasses.Event;
import com.apitap.model.deliveryServices.DeliveryServiceModel;
import com.apitap.model.headerCategory.HeaderCategoryResponse;
import com.apitap.model.merchantCategoryList.MerchantCategoryListModel;
import com.apitap.model.ratings.RatingResponse;
import com.apitap.model.showCase.ShowCaseResponse;
import com.apitap.model.showCaseTitle.ShowCaseTitleResponse;
import com.apitap.model.storeAbout.StoreAboutResponse;
import com.apitap.model.storeCategories.StoreCategoryResponse;
import com.apitap.model.storeFrontItems.ads.StoreAdsResponse;
import com.apitap.model.storeFrontItems.browseCategory.BrowseCategoryResponse;
import com.apitap.model.storeFrontItems.details.StoreDetailsResponse;
import com.apitap.model.storeFrontItems.favourites.StoreFavouriteResponse;
import com.apitap.model.storeFrontItems.items.StoreItemsResponse;
import com.apitap.model.storeFrontItems.specials.StoreSpecialsResponse;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sahil on 10/08/16.
 */
public class MerchantStoresManager {

    private static final String TAG = MerchantStoresManager.class.getSimpleName();
    public static MerchantStore merchantStoresBean;
    public static BusinessBean businessBean;
    public static NearbyStoreBean nearbyStoreBean;
    public StoreCategoryResponse storeCategoryResponse;
    public HashMap<Integer, ArrayList<Storebean>> itemsData = new HashMap<Integer, ArrayList<Storebean>>();
    public int filter;
    public LevelOneCategory levelOneCategoryModel;
    public DeliveryServiceModel deliveryServiceModel;
    public MerchantCategoryListModel merchantCategoryListModel;
    public MerchantStoreBean merchantStoreBeanModel;
    public StoreSpecialsResponse storeSpecialsModel;
    public StoreItemsResponse storeItemsModel;
    public ShowCaseResponse showCaseModel;
    public StoreAdsResponse storeAdsModel;
    public StoreDetailsResponse storeDetailsModel;
    public StoreFavouriteResponse storeFavouriteModel;
    public static BrowseCategoryResponse browseCategoryModel;
    public BrandNamesResponse brandNamesModel;
    public HeaderCategoryResponse headerCategoryModel;
    public StoreAboutResponse storeAboutResponse;
    public RatingResponse ratingModel;
    public ActiveTvInfoResponse activeTvInfoResponse;

    public void getMerchantStoreDetail(Context context, String params, int withoutFiilter) {
        filter = withoutFiilter;
        new ExecuteApi(context).execute(params);
    }

    public void getStoreByCategory(Context context, String params) {
        new ExecuteStoreByCategoryApi(context).execute(params);
    }

    public void getCategoryLvlOne(Context context, String params) {
        new ExecuteApiCategoryLvlOne(context).execute(params);
    }

    public void getDeliveryServices(Context context, String params) {
        new ExecuteApiDeliveryServices(context).execute(params);
    }

    public void getBrandNames(Context context, String params) {
        new ExecuteApiBrandNames(context).execute(params);
    }

    public void getHeaderCategory(Context context, String params) {
        new ExecuteApiHeaderCategory(context).execute(params);
    }

    public void getStoreImages(Context context, String params) {
        new ExecuteApiStoreImages(context).execute(params);
    }

    public void getRatings(Context context, String params) {
        new ExecuteApiRatings(context).execute(params);
    }

    public void getMerchantCategoryList(Context context, String params) {
        new ExecuteApiMerchantCategory(context).execute(params);
    }

    public void getMerchantCategoryListHome(Context context, String params) {
        new ExecuteApiMerchantCategoryHome(context).execute(params);
    }

    public void getMerchantCategoryListHomeOnly(Context context, String params) {
        new ExecuteApiMerchantCategoryHomeOnly(context).execute(params);
    }

    public void getLocationByMerchant(Context context, String params) {
        new ExecuteApiLocationByMerchant(context).execute(params);
    }

    public void getMerchantStoreItems(Context context, String params) {
        new ExecuteApiMerchantStoreItems(context).execute(params);
    }

    public void getMerchantStoreDetails(Context context, String params) {
        new ExecuteApiStoreDetails(context).execute(params);
    }
    public void getActivePlayersInfo(Context context, String params) {
        new ExecuteApiActivePlayers(context).execute(params);
    }

    public void getMerchantStoresList(Context context, String params) {
        new ExecuteApiMerchantStoreList(context).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_merchantStoreItem---" + response);
            Logger.addRecordToLog(response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "" + s);
            try {
                Log.d(TAG, "" + s);
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");

                // storebeanArrayList =arrayAds;
                if (filter == Constants.WITHOUT_FIILTER) {
                    businessBean = new Gson().fromJson(s, BusinessBean.class);
                    if (businessBean.getRESULT().get(0).get_44().equalsIgnoreCase("Transaction Approved")) {
                        if (businessBean.getRESULT().get(0).getRESULT().size() > 0 &&
                                businessBean.getRESULT().get(0).getRESULT().get(0).get_120_45() != null)
                            EventBus.getDefault().post(new Event(Constants.STORES_SUCCESS, true));
                        else
                            EventBus.getDefault().post(new Event(Constants.STORES_SUCCESS, false));

                    } else {
                        EventBus.getDefault().post(new Event(Constants.STORES_FAILURE, ""));
                    }

                } else {
                    merchantStoresBean = new Gson().fromJson(s, MerchantStore.class);
                    if (merchantStoresBean.getRESULT().get(0).get_44().equals("Transaction Approved"))
                        EventBus.getDefault().post(new Event(Constants.FILTER_STORES_SUCCESS, ""));
                    else
                        EventBus.getDefault().post(new Event(Constants.STORES_FAILURE, ""));
                }
            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiCategoryLvlOne extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiCategoryLvlOne(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "ApiCategoryLvlOne---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, "" + s);
                if (s != null && !s.isEmpty()) {
                    LevelOneCategory levelOneCategoryTemp = new Gson().fromJson(s, LevelOneCategory.class);
                    if (levelOneCategoryTemp.getRESULT().get(0).getRESULT().get(0).get_12045() != null) {
                        levelOneCategoryModel = new Gson().fromJson(s, LevelOneCategory.class);
                        EventBus.getDefault().post(new Event(Constants.LEVEL_ONE_CATEGORY, true));
                    } else
                        EventBus.getDefault().post(new Event(Constants.LEVEL_ONE_CATEGORY, false));
                } else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiDeliveryServices extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiDeliveryServices(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "ApiDeliveryServices---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, "" + s);
                if (s != null && !s.isEmpty()) {
                    DeliveryServiceModel deliveryServiceModelTemp = new Gson().fromJson(s, DeliveryServiceModel.class);
                    if (deliveryServiceModelTemp.getRESULT().get(0).getRESULT().get(0).get_12239() != null) {
                        deliveryServiceModel = new Gson().fromJson(s, DeliveryServiceModel.class);
                        EventBus.getDefault().post(new Event(Constants.DELIVERY_SERVICES, true));
                    } else
                        EventBus.getDefault().post(new Event(Constants.DELIVERY_SERVICES, false));
                } else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiBrandNames extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiBrandNames(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "ApiBrandNames---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, "" + s);
                if (s != null && !s.isEmpty()) {
                    brandNamesModel = new Gson().fromJson(s, BrandNamesResponse.class);
                    EventBus.getDefault().post(new Event(Constants.BRAND_NAMES, true));
                } else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiHeaderCategory extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiHeaderCategory(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "ApiHeaderCategory---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, "" + s);
                if (s != null && !s.isEmpty()) {
                    headerCategoryModel = new Gson().fromJson(s, HeaderCategoryResponse.class);
                    EventBus.getDefault().post(new Event(Constants.MERCHANT_HEADER_CATEGORY, true));
                } else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiStoreImages extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiStoreImages(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "ApiStoreImages---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, "" + s);
                if (s != null && !s.isEmpty()) {
                    storeAboutResponse = new Gson().fromJson(s, StoreAboutResponse.class);
                    EventBus.getDefault().post(new Event(Constants.STORE_ABOUT_IMAGES, true));
                } else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiRatings extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiRatings(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "ApiRatings---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, "" + s);
                if (s != null && !s.isEmpty()) {
                    ratingModel = new Gson().fromJson(s, RatingResponse.class);
                    EventBus.getDefault().post(new Event(Constants.RATINGS, true));
                } else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiMerchantCategory extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiMerchantCategory(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "ApiMerchantCategory---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, "" + s);
                if (s != null && !s.isEmpty()) {
                    MerchantCategoryListModel merchantCategoryListModelTemp = new Gson().fromJson(s, MerchantCategoryListModel.class);
                    if (merchantCategoryListModelTemp.getRESULT().get(0).getRESULT().get(0).get_12045() != null) {
                        merchantCategoryListModel = new Gson().fromJson(s, MerchantCategoryListModel.class);
                        EventBus.getDefault().post(new Event(Constants.MERCHANT_CATEGORY_LIST, true));
                    } else
                        EventBus.getDefault().post(new Event(Constants.MERCHANT_CATEGORY_LIST, false));
                } else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiMerchantCategoryHome extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiMerchantCategoryHome(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "ApiMerchantCategory---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, "" + s);
                if (s != null && !s.isEmpty()) {
                    MerchantCategoryListModel merchantCategoryListModelTemp = new Gson().fromJson(s, MerchantCategoryListModel.class);
                    if (merchantCategoryListModelTemp.getRESULT().get(0).getRESULT().get(0).get_12045() != null) {
                        merchantCategoryListModel = new Gson().fromJson(s, MerchantCategoryListModel.class);
                        EventBus.getDefault().post(new Event(Constants.MERCHANT_CATEGORY_LIST_HOME, true));
                    } else
                        EventBus.getDefault().post(new Event(Constants.MERCHANT_CATEGORY_LIST_HOME, false));
                } else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiMerchantCategoryHomeOnly extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiMerchantCategoryHomeOnly(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "ApiMerchantCategory---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, "" + s);
                if (s != null && !s.isEmpty()) {
                    MerchantCategoryListModel merchantCategoryListModelTemp = new Gson().fromJson(s, MerchantCategoryListModel.class);
                    if (merchantCategoryListModelTemp.getRESULT().get(0).getRESULT().get(0).get_12045() != null) {
                        merchantCategoryListModel = new Gson().fromJson(s, MerchantCategoryListModel.class);
                        EventBus.getDefault().post(new Event(Constants.MERCHANT_CATEGORY_LIST_HOME_ONLY, true));
                    } else
                        EventBus.getDefault().post(new Event(Constants.MERCHANT_CATEGORY_LIST_HOME_ONLY, false));
                } else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiLocationByMerchant extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiLocationByMerchant(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "ApiLocationByMerchant---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, "" + s);
                if (s != null && !s.isEmpty()) {
                } else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiMerchantStoreList extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiMerchantStoreList(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_merchant_list---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.d(TAG, "" + s);
                if (s != null && !s.isEmpty()) {
                    MerchantStoreBean merchantStoreBean = new Gson().fromJson(s, MerchantStoreBean.class);
                    if (merchantStoreBean.getRESULT().get(0).getRESULT().get(0).get_11470() != null) {
                        merchantStoreBeanModel = new Gson().fromJson(s, MerchantStoreBean.class);
                        EventBus.getDefault().post(new Event(Constants.MERCHANT_STORES_LIST, true));
                    } else {
                        EventBus.getDefault().post(new Event(Constants.MERCHANT_STORES_LIST, false));
                    }
                } else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiMerchantStoreItems extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiMerchantStoreItems(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_merchant_items---" + response);
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
                            case "010100710": //specials
                                StoreSpecialsResponse storeSpecialsResponseTemp = new Gson().fromJson(jsonObject1.toString(),
                                        StoreSpecialsResponse.class);
                                if (storeSpecialsResponseTemp.getRESULT().size() > 0 &&
                                        storeSpecialsResponseTemp.getRESULT().get(0).getJsonMember1149() != null) {
                                    storeSpecialsModel = new Gson().fromJson(jsonObject1.toString(), StoreSpecialsResponse.class);
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_SPECIALS, true));
                                } else
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_SPECIALS, false));
                                break;

                            case "010400677": //ads

                                StoreAdsResponse storeAdsResponseTemp = new Gson().fromJson(jsonObject1.toString(),
                                        StoreAdsResponse.class);
                                if (storeAdsResponseTemp.getRESULT().size() > 0 &&
                                        storeAdsResponseTemp.getRESULT().get(0).get11493() != null) {
                                    storeAdsModel = new Gson().fromJson(jsonObject1.toString(), StoreAdsResponse.class);
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_ADS, true));
                                } else
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_ADS, false));


                                break;
                            case "010100709": //items
                                StoreItemsResponse storeItemsResponseTemp = new Gson().fromJson(jsonObject1.toString(),
                                        StoreItemsResponse.class);
                                if (storeItemsResponseTemp.getRESULT().size() > 0 &&
                                        storeItemsResponseTemp.getRESULT().get(0).getJsonMember1149() != null) {
                                    storeItemsModel = new Gson().fromJson(jsonObject1.toString(), StoreItemsResponse.class);
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_ITEMS, true));
                                } else
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_ITEMS, false));

                                break;
                            case "010100900": //showcase
                                ShowCaseResponse showCaseResponse = new Gson().fromJson(jsonObject1.toString(),
                                        ShowCaseResponse.class);
                                if (showCaseResponse.getRESULT().size() > 0 &&
                                        showCaseResponse.getRESULT().get(0).getJsonMember114144() != null) {
                                    showCaseModel = new Gson().fromJson(jsonObject1.toString(), ShowCaseResponse.class);
                                    EventBus.getDefault().post(new Event(Constants.SHOWCASE_RESPONSE, true));
                                } else
                                    EventBus.getDefault().post(new Event(Constants.SHOWCASE_RESPONSE, false));

                                break;
                            case "010100020": //business details
                                StoreDetailsResponse storeDetailResponseTemp = new Gson().fromJson(jsonObject1.toString(),
                                        StoreDetailsResponse.class);
                                if (storeDetailResponseTemp.getRESULT().size() > 0 &&
                                        storeDetailResponseTemp.getRESULT().get(0).getJsonMember4828() != null) {
                                    storeDetailsModel = new Gson().fromJson(jsonObject1.toString(), StoreDetailsResponse.class);
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_DETAILS, true));
                                } else
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_DETAILS, false));

                                break;

                            case "010400094": //merchantFavourite

                                StoreFavouriteResponse favouriteResponseTemp = new Gson().fromJson(jsonObject1.toString(),
                                        StoreFavouriteResponse.class);
                                if (favouriteResponseTemp.getRESULT().size() > 0 &&
                                        favouriteResponseTemp.getRESULT().get(0).getJsonMember11470() != null) {
                                    storeFavouriteModel = new Gson().fromJson(jsonObject1.toString(), StoreFavouriteResponse.class);
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_FAVOURITE, true));
                                } else {
                                    storeFavouriteModel = null;
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_FAVOURITE, false));
                                }

                                break;

                            case "010200714": //browse merchant category
                                Log.d(TAG, "merchantCategory---" + jsonObject1.toString());
                                BrowseCategoryResponse browseCategoryResponseTemp = new Gson().fromJson(jsonObject1.toString(),
                                        BrowseCategoryResponse.class);
                                if (browseCategoryResponseTemp.getRESULT().size() > 0 &&
                                        browseCategoryResponseTemp.getRESULT().get(0).getJsonMember11493() != null) {
                                    browseCategoryModel = new Gson().fromJson(jsonObject1.toString(), BrowseCategoryResponse.class);
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_BROWSE_CAT, true));
                                } else
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_BROWSE_CAT, false));

                                break;
                            case "010100916": //browse merchant category
                                Log.d(TAG, "showCaseTitle---" + jsonObject1.toString());
                                ShowCaseTitleResponse showCaseTitleResponse = new Gson().fromJson(jsonObject1.toString(),
                                        ShowCaseTitleResponse.class);
                                if (showCaseTitleResponse.getRESULT().size() > 0)
                                    EventBus.getDefault().post(new Event(Constants.SHOWCASE_TITLE_RESPONSE, Utils.hexToASCII(showCaseTitleResponse.getRESULT().get(0).getJsonMember1237())));
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

    private class ExecuteApiStoreDetails extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiStoreDetails(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_store_details---" + response);
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
                            case "010100020": //business details
                                StoreDetailsResponse storeDetailResponseTemp = new Gson().fromJson(jsonObject1.toString(),
                                        StoreDetailsResponse.class);
                                if (storeDetailResponseTemp.getRESULT().size() > 0 &&
                                        storeDetailResponseTemp.getRESULT().get(0).getJsonMember4828() != null) {
                                    storeDetailsModel = new Gson().fromJson(jsonObject1.toString(), StoreDetailsResponse.class);
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_DETAILS, true));
                                } else
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_DETAILS, false));

                                break;

                            case "010400094": //merchantFavourite

                                StoreFavouriteResponse favouriteResponseTemp = new Gson().fromJson(jsonObject1.toString(),
                                        StoreFavouriteResponse.class);
                                if (favouriteResponseTemp.getRESULT().size() > 0 &&
                                        favouriteResponseTemp.getRESULT().get(0).getJsonMember11470() != null) {
                                    storeFavouriteModel = new Gson().fromJson(jsonObject1.toString(), StoreFavouriteResponse.class);
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_FAVOURITE, true));
                                } else
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_FAVOURITE, false));

                                break;

                            case "010200714": //browse merchant category
                                Log.d(TAG, "merchantCategory---" + jsonObject1.toString());
                                BrowseCategoryResponse browseCategoryResponseTemp = new Gson().fromJson(jsonObject1.toString(),
                                        BrowseCategoryResponse.class);
                                if (browseCategoryResponseTemp.getRESULT().size() > 0 &&
                                        browseCategoryResponseTemp.getRESULT().get(0).getJsonMember11493() != null) {
                                    browseCategoryModel = new Gson().fromJson(jsonObject1.toString(), BrowseCategoryResponse.class);
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_BROWSE_CAT, true));
                                } else
                                    EventBus.getDefault().post(new Event(Constants.STORE_FRONT_BROWSE_CAT, false));

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
    private class ExecuteApiActivePlayers extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiActivePlayers(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_active_players---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "" + s);
            activeTvInfoResponse = new Gson().fromJson(s, ActiveTvInfoResponse.class);
            EventBus.getDefault().post(new Event(Constants.ACTIVE_TV_INFO, true));
        }
    }

    private class ExecuteStoreByCategoryApi extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteStoreByCategoryApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "ExecuteStoreByCategoryApi---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null && !s.isEmpty()) {
                    storeCategoryResponse = new Gson().fromJson(s, StoreCategoryResponse.class);
                    if (storeCategoryResponse.getRESULT().get(0).getRESULT().size() > 0 &&
                            storeCategoryResponse.getRESULT().get(0).getRESULT().get(0).getJsonMember12044() != null) {
                        EventBus.getDefault().post(new Event(Constants.CATEGORY_STORES, true));
                    } else {
                        EventBus.getDefault().post(new Event(Constants.CATEGORY_STORES, false));
                    }
                } else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
