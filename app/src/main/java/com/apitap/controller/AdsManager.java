package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.adDetail.AdDetailResponse;
import com.apitap.model.bean.AdsBean;
import com.apitap.model.bean.AdsDetailBean;
import com.apitap.model.bean.AdsDetailWithMerchant;
import com.apitap.model.bean.AdsIRbean;
import com.apitap.model.bean.AdsListBean;
import com.apitap.model.bean.AdsListBean2;
import com.apitap.model.bean.RelatedAdBean;
import com.apitap.model.bean.ads.AdsModel;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.model.storeFrontAds.StoreFrontAdsResponse;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sahil on 10/08/16.
 */
public class AdsManager {

    private static final String TAG = AdsManager.class.getSimpleName();
    public AdsListBean adsListBean;
    public AdsListBean2 adsListBean2;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> nameArayList = new ArrayList<>();
    public ArrayList<AdsDetailBean> arrayAds = new ArrayList<AdsDetailBean>();
    public ArrayList<AdsIRbean> arrayIR = new ArrayList<AdsIRbean>();
    public static ArrayList<AdsIRbean> AdsarrayIR = new ArrayList<AdsIRbean>();
    public static ArrayList<AdsDetailWithMerchant> url_maps = new ArrayList<AdsDetailWithMerchant>();
    public static HashMap<Integer, AdsBean> url_maps_ads = new HashMap<Integer, AdsBean>();
    public HashMap<Integer, AdsBean> ads = new HashMap<Integer, AdsBean>();
    public static RelatedAdBean relatedAdBean;
    public StoreFrontAdsResponse storeFrontAdsModel;
    public AdsModel adsModel;
    public ArrayList<String> arrayListLocation = new ArrayList<>();
    public ArrayList<String> arrayListLocationId = new ArrayList<>();

    public void getAllAds(Context context, String params, int key) {
        new ExecuteApi(context, key).execute(params);
    }

    public void adsRelatedItem(Context context, String params) {
        new ExecuteApiAdsOnly(context).execute(params);
    }

    public void adDetail(Context context, String params) {
        new ExecuteApiAdDetail(context).execute(params);
    }

    public void getBusinessAds(Context context, String params, int key) {
        new ExecuteAdApi(context, key).execute(params);
    }

    public void getAdsList(Context activity, String params) {
        new ExecuteApiAdsList(activity).execute(params);

    }

    public void getAdsByCategoryList(Context activity, String params) {
        new ExecuteApiAdsByCategoryList(activity).execute(params);

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
            Log.d(TAG, "response_ads_Item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int maxLogSize = 2000;
            for (int i = 0; i <= s.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > s.length() ? s.length() : end;
                Log.v("Adsss", s.substring(start, end));
            }
            ATPreferences.putString(mContext, Constants.BUSINESS_ADS, "no");
            try {
                ArrayList<AdsDetailWithMerchant> imageArray = new ArrayList<>();
                ArrayList<String> matchList = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                for (int i = 0; i < jsonArray1.length(); i++) {
                    AdsDetailWithMerchant detailsBean = new AdsDetailWithMerchant();
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);

                    String adName = jsonObject2.getString("_120_83");
                    String imageUrl = jsonObject2.getString("_121_170");
                    String merchantName = jsonObject2.getString("_114_70");
                    String videoName = jsonObject2.getString("_121_15");
                    Log.d("adNameNew", adName + "  " + videoName);
                    String isSeen = jsonObject2.getString("_114_9");
                    String id = jsonObject2.getString("_121_18");
                    String ad_id = jsonObject2.getString("_123_21");
                    String merchantId = jsonObject2.getString("_53");
                    String business_type = jsonObject2.getString("_120_45");
                    String desc = jsonObject2.getString("_120_157");
                    JSONArray jsonArray2 = jsonObject2.getJSONArray("IR");
                    detailsBean.setName(Utils.hexToASCII(adName));
                    detailsBean.setId(id);
                    detailsBean.setAdId(ad_id);
                    detailsBean.setMerchantId(merchantId);
                    detailsBean.setVideo(videoName);
                    detailsBean.setBusinssType(business_type);
                    detailsBean.setMerchantname(merchantName);
                    detailsBean.setSeen(isSeen);
                    detailsBean.setDesc(Utils.hexToASCII(desc));
                    detailsBean.setImageUrl(imageUrl);

                    imageArray.add(detailsBean);

                }

                url_maps = imageArray;

                if (key == Constants.ADS_LISTING_SUCCESS) {
                    Log.d(TAG, s);
                    adsListBean = new Gson().fromJson(s, AdsListBean.class);
                    if (adsListBean.getRESULT().get(0).getStatus().equals("Transaction Approved")) {
                        EventBus.getDefault().post(new Event(key, ""));
                    } else {
                        EventBus.getDefault().post(new Event(-1, ""));
                    }

                }
                if (key == Constants.NOTIFICATION_ARRIVED) {
                    EventBus.getDefault().post(new Event(key, ""));
                }
            } catch (Exception e) {
                EventBus.getDefault().post(new Event(-1, ""));
                e.printStackTrace();
            }
        }
    }

    private class ExecuteApiAdsByCategoryList extends AsyncTask<String, String, String> {
        private int key;
        Context mContext;

        ExecuteApiAdsByCategoryList(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_ads_by_cat---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null && !s.isEmpty()) {
                storeFrontAdsModel = new Gson().fromJson(s, StoreFrontAdsResponse.class);
                if (storeFrontAdsModel.getRESULT().get(0).getRESULT().size() > 0 &&
                        storeFrontAdsModel.getRESULT().get(0).getRESULT().get(0).getAD() != null
                ) {
                    EventBus.getDefault().post(new Event(Constants.ADS_LIST_DATA, true));
                } else {
                    EventBus.getDefault().post(new Event(Constants.ADS_LIST_DATA, false));
                }
            } else {
                EventBus.getDefault().post(new Event(-1, ""));
            }

        }
    }

    private class ExecuteApiAdsOnly extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiAdsOnly(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_getads_Item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                relatedAdBean = new Gson().fromJson(s, RelatedAdBean.class);
                if (relatedAdBean.getRESULT().get(0).get44().equals("Transaction Approved")) {
                    EventBus.getDefault().post(new Event(Constants.RELATED_ADITEM_SUCCESS, ""));
                } else {
                    EventBus.getDefault().post(new Event(-1, ""));
                }
            }
        }
    }

    private class ExecuteApiAdDetail extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiAdDetail(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_getad_detail---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                AdDetailResponse adDetail = new Gson().fromJson(s, AdDetailResponse.class);
                if (adDetail.getRESULT().get(0).getJsonMember44().equals("Transaction Approved")) {
                    if (adDetail.getRESULT().get(0).getRESULT().get(0).getLO().size() > 0) {
                        arrayListLocation = new ArrayList<>();
                        arrayListLocationId = new ArrayList<>();
                        try {
                            for (int lo = 0; lo < adDetail.getRESULT().get(0).getRESULT().get(0).getLO().size(); lo++) {
                                arrayListLocationId.add(adDetail.getRESULT().get(0).getRESULT().get(0).getLO().get(lo).getJsonMember11447());
                                String locationFullName = Utils.hexToASCII(adDetail.getRESULT().get(0).getRESULT().get(0).getLO().get(lo).getAD().getJsonMember11412());
                                String locationName = adDetail.getRESULT().get(0).getRESULT().get(0).getLO().get(lo).getAD().getCI().getJsonMember4715();
                                String locationSate =adDetail.getRESULT().get(0).getRESULT().get(0).getLO().get(lo).getAD().getST().getJsonMember4716();
                                String locationCountry = adDetail.getRESULT().get(0).getRESULT().get(0).getLO().get(lo).getAD().getCO().getJsonMember4718();
                                arrayListLocation.add(locationFullName + " " + locationName + " " + locationSate + " " + locationCountry);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    EventBus.getDefault().post(new Event(Constants.AD_DETAIL_SUCCESS, ""));

                } else {

                }
            }
        }
    }


    private class ExecuteAdApi extends AsyncTask<String, String, String> {
        private int key;
        Context mContext;

        ExecuteAdApi(Context context, int key) {
            mContext = context;
            this.key = key;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_adsbusiness_Item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int maxLogSize = 2000;
            for (int i = 0; i <= s.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > s.length() ? s.length() : end;
                Log.v("Adsss", s.substring(start, end));
            }
            arrayList = new ArrayList<>();
            nameArayList = new ArrayList<>();
            try {
                ArrayList<AdsDetailWithMerchant> imageArray = new ArrayList<>();
                ArrayList<String> matchList = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("RESULT");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONArray jsonArray1 = jsonObject1.getJSONArray("RESULT");
                if (jsonArray1.length() > 0 && jsonArray1.getJSONObject(0).has("_120_83")) {
                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject jsonObject21 = jsonArray1.getJSONObject(i);
                        String business_type = jsonObject21.getString("_120_83");
                        JSONArray jsonArray2 = jsonObject21.getJSONArray("AD");
                        //Log.d("businessType",business_type+jsonArray2.length());
                        if (jsonArray2.length() > 0) {
                            for (int j = 0; j < jsonArray2.length(); j++) {
                                AdsDetailWithMerchant detailsBean = new AdsDetailWithMerchant();
                                JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
                                String adName = jsonObject2.getString("_120_83");
                                String imageUrl = jsonObject2.getString("_121_170");
                                String merchantName = jsonObject2.getString("_114_70");
                                String videoName = jsonObject2.getString("_121_15");

                                String isSeen = jsonObject2.getString("_114_9");
                                //String isSeen = "true";
                                String id = jsonObject2.getString("_121_18");
                                String ad_id = jsonObject2.getString("_123_21");
                                String merchantId = jsonObject2.getString("_53");

                                String desc = jsonObject2.getString("_120_157");
                                JSONArray jsonArray3 = jsonObject2.getJSONArray("IR");
                                Log.d("adNameNew", adName + "  " + videoName + "   " + "    " + ad_id + "    " + business_type);
                                String adIdWithBusiness = ad_id + "," + business_type;
                                if (!arrayList.contains(adIdWithBusiness)) {
                                    arrayList.add(adIdWithBusiness);
                                    detailsBean.setName(adName);
                                    detailsBean.setId(id);
                                    detailsBean.setAdId(ad_id);
                                    detailsBean.setMerchantId(merchantId);
                                    detailsBean.setVideo(videoName);
                                    detailsBean.setBusinssType(business_type);
                                    detailsBean.setMerchantname(merchantName);
                                    detailsBean.setSeen(isSeen);
                                    detailsBean.setDesc(desc);
                                    detailsBean.setImageUrl(imageUrl);


                                    imageArray.add(detailsBean);
                                }
                            }
                            url_maps = imageArray;
                        }
                    }

                    url_maps = imageArray;
                }

                if (key == Constants.ADS_LISTING_SUCCESS) {
                    Log.d(TAG, s);
                    url_maps_ads = new HashMap<Integer, AdsBean>();
                    arrayIR = new ArrayList<AdsIRbean>();
                    AdsarrayIR = new ArrayList<AdsIRbean>();
                    ATPreferences.putString(mContext, Constants.BUSINESS_ADS, "yes");
                    adsListBean2 = new Gson().fromJson(s, AdsListBean2.class);

                    if (jsonArray1.length() > 0 && jsonArray1.getJSONObject(0).has("_120_83")) {
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            JSONObject imgObj1 = jsonArray1.getJSONObject(j);
                            JSONArray jsonArray2 = imgObj1.getJSONArray("AD");
                            if (jsonArray2.length() > 0) {
                                for (int k = 0; k < jsonArray2.length(); k++) {
                                    JSONObject imgObj = jsonArray2.getJSONObject(k);
                                    if (!nameArayList.contains(imgObj.getString("_121_170"))) {
                                        nameArayList.add(imgObj.getString("_121_170"));
                                        String url = ATPreferences.readString(mContext, Constants.KEY_IMAGE_URL) + imgObj.getString("_121_170");
                                        String videoUrl = imgObj.getString("_121_15");
                                        String isSeen = imgObj.getString("_114_9");
                                        String merchantName = imgObj.getString("_114_70");
                                        String merchantLogo = imgObj.getString("_121_77");
                                        String id = imgObj.getString("_121_18");
                                        String adid = imgObj.getString("_123_21");
                                        String imageUrl = imgObj.getString("_121_170");
                                        String adName = imgObj.getString("_120_83");
                                        String description = imgObj.getString("_120_157");
                                        String merchantId = imgObj.getString("_53");

                                        //  String actualPrice = object.getString("_114_98");
                                        //String priceAfterDiscount = object.getString("_122_158");

                                        Log.d("IsSEENAD", isSeen + "");
                                        Log.d("imageUrlq", imageUrl + "");

                                        AdsDetailBean adsDetailBean = new AdsDetailBean();
                                        adsDetailBean.setId(id);
                                        adsDetailBean.setImageUrl(imageUrl);
                                        adsDetailBean.setName(adName);
                                        adsDetailBean.setSeen(isSeen);
                                        adsDetailBean.setMerchantName(merchantName);
                                        //adsDetailBean.setActualPrice(actualPrice);
                                        //adsDetailBean.setPriceAfterDiscount(priceAfterDiscount);
                                        arrayAds.add(adsDetailBean);

                                        JSONArray array = imgObj.getJSONArray("IR");
                                        if (array.length() > 0) {
                                            for (int l = 0; l < array.length(); l++) {
                                                JSONObject object = array.getJSONObject(l);

                                                String imageUrlIR = object.getString("_121_170");
                                                String actualPriceIR = object.getString("_114_98");
                                                String priceAfterDiscountIR = object.getString("_122_158");
                                                String isSeenIR = object.getString("_114_9");
                                                String descriptionIR = object.getString("_120_83");
                                                String productType = object.getString("_114_112");
                                                String productId = object.getString("_114_144");


                                                AdsIRbean adsIRbean = new AdsIRbean();
                                                adsIRbean.setImageUrl(imageUrlIR);
                                                adsIRbean.setSeen(isSeenIR);
                                                adsIRbean.setDescription(descriptionIR);
                                                adsIRbean.setActualPrice(actualPriceIR);
                                                adsIRbean.setPriceAfterDiscount(priceAfterDiscountIR);
                                                adsIRbean.setProductType(productType);
                                                adsIRbean.setId(productId);
                                                arrayIR.add(adsIRbean);
                                            }
                                            AdsarrayIR = arrayIR;
                                        }


                                        AdsBean adsBean = new AdsBean();
                                        adsBean.setImageUrl(url);
                                        Log.d("imageUrlqa", url + "");
                                        adsBean.setVideoUrl(videoUrl);
                                        adsBean.setSeen(isSeen);
                                        adsBean.setMerchantName(merchantName);
                                        adsBean.setMerchantLogo(merchantLogo);
                                        adsBean.setMerchantId(merchantId);
                                        adsBean.setDescription(description);
                                        adsBean.setAdName(adName);
                                        adsBean.setId(id);
                                        adsBean.setAdId(adid);
                                        adsBean.setArrayList(arrayAds);

                                        url_maps_ads.put(nameArayList.size() - 1, adsBean);
                                        Log.d("url_maps_adssize", url_maps_ads.size() + "  " + ((nameArayList.size() - 1)) + "  " + url);

                                    }
                                }

                            }

                        }
                        ads = url_maps_ads;

                    } else {
                        ads.clear();
                        AdsBean bean = new AdsBean();
                        AdsDetailBean beans = new AdsDetailBean();
                    }
                    if (adsListBean2.getRESULT().get(0).get44().equals("Transaction Approved")) {
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

    private class ExecuteApiAdsList extends AsyncTask<String, String, String> {
        Context mContext;

        ExecuteApiAdsList(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_AdsList---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null && !s.isEmpty()) {
                    AdsModel adsModelTemp = new Gson().fromJson(s, AdsModel.class);
                    if (adsModelTemp.getRESULT().get(0).getRESULT().size() > 0 && adsModelTemp.getRESULT().get(0).getRESULT().get(0).get_1149() != null) {
                        adsModel = new Gson().fromJson(s, AdsModel.class);
                        EventBus.getDefault().post(new Event(Constants.ADS_LIST_DATA, true));
                    } else
                        EventBus.getDefault().post(new Event(Constants.ADS_LIST_DATA, false));
                } else
                    EventBus.getDefault().post(new Event(-1, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
