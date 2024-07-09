package com.apitap.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apitap.model.Client;
import com.apitap.model.Constants;
import com.apitap.model.Utils;
import com.apitap.model.bean.CategoryDetailsBean;
import com.apitap.model.bean.DetailsBean;
import com.apitap.model.bean.FavBean;
import com.apitap.model.bean.Favdetailsbean;
import com.apitap.model.bean.ProductDetailsBean;
import com.apitap.model.bean.ProductOptionsBean;
import com.apitap.model.bean.ProductVideoBean;
import com.apitap.model.bean.RelatedDetailsBean;
import com.apitap.model.bean.SizeBean;
import com.apitap.model.bean.Sizedata;
import com.apitap.model.bean.SpecialItemBean;
import com.apitap.model.customclasses.Event;
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
public class DetailsManager {

    private static final String TAG = DetailsManager.class.getSimpleName();
    public ArrayList<DetailsBean> arrayDetails = new ArrayList<>();
    public ArrayList<String> arrayListLocation = new ArrayList<>();
    public ArrayList<String> arrayListLocationId = new ArrayList<>();
    public RelatedDetailsBean relatedDetailsBean;
    public SpecialItemBean specialItemBean;
    public CategoryDetailsBean categoryDetailsBean;
    public static ArrayList<ProductOptionsBean> arrayOptions1 = new ArrayList<ProductOptionsBean>();
    public FavBean favBean;
    public ProductVideoBean productVideoBean;
    public ArrayList<String> favIds = new ArrayList<String>();
    public ArrayList<String> hexIds = new ArrayList<String>();
    public HashMap<Integer, ArrayList<Favdetailsbean>> itemsData = new HashMap<Integer, ArrayList<Favdetailsbean>>();

    public ArrayList<DetailsBean> getDetails(Context context, String params, boolean isRelatedItems) {
        if (isRelatedItems)
            new ExecuteRelatedItemsApi(context).execute(params);
        else
            new ExecuteApi(context).execute(params);
        return arrayDetails;
    }

    public ArrayList<DetailsBean> getSpecialDetails(Context context, String params) {
        new ExecuteCategoryApi(context).execute(params);
        return arrayDetails;
    }


    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;
        HashMap<String, String> url_maps = new HashMap<String, String>();

        ExecuteApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_item_details---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("ResultDetails", s + "");
            String priceAfterDiscount = "0.00";
            ArrayList<ProductOptionsBean> arrayList = new ArrayList<ProductOptionsBean>();
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray mainArray = jsonObject.getJSONArray("RESULT");

                for (int z = 0; z < mainArray.length(); z++) {
                    JSONObject jobj = mainArray.getJSONObject(z);
                    if (jobj.has("_101")) {
                        if (jobj.getString("_101").equals("010200008")) {
                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            arrayDetails = new ArrayList<>();

                            for (int i = 0; i < imgeArray.length(); i++) {
                                DetailsBean detailsBean = new DetailsBean();
                                JSONObject object = imgeArray.getJSONObject(i);
                                String merchantId = object.getString("_53");
                                String specialPrice = object.getString("_122_162");
                                String image = object.getString("_121_170");
                                String name = Utils.hexToASCII(object.getString("_120_83"));
                                String desc = object.getString("_120_157");
                                String limit = object.getString("_114_121");
                                String sellerName = object.getString("_121_150");
                                String title = object.getString("_120_83");
                                JSONArray isArray = object.getJSONArray("IS");
                                JSONArray loArray = object.getJSONArray("LO");
                                if (loArray.length() > 0) {
                                    arrayListLocation = new ArrayList<>();
                                    arrayListLocationId = new ArrayList<>();
                                    try {
                                        for (int lo = 0; lo < loArray.length(); lo++) {
                                            JSONObject jsonObjectLo = loArray.getJSONObject(lo);
                                            arrayListLocationId.add(jsonObjectLo.getString("_114_47"));
                                            JSONObject jsonObjectAd = jsonObjectLo.getJSONObject("AD");
                                            String locationFullName = Utils.hexToASCII(jsonObjectAd.getString("_114_12"));
                                            JSONObject jsonObjectCI = jsonObjectAd.getJSONObject("CI");
                                            String locationName = jsonObjectCI.getString("_47_15");
                                            JSONObject jsonObjectST = jsonObjectAd.getJSONObject("ST");
                                            String locationSate = jsonObjectST.getString("_47_16");
                                            JSONObject jsonObjectCO = jsonObjectAd.getJSONObject("CO");
                                            String locationCountry = jsonObjectCO.getString("_47_18");
                                            arrayListLocation.add(locationFullName + " " + locationName + " " + locationSate + " " + locationCountry);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (isArray.length() > 0) {
                                    JSONObject jsonObject1 = isArray.getJSONObject(0);
                                    if (jsonObject1.has("_122_158")) {
                                        priceAfterDiscount = jsonObject1.getString("_122_158");
                                    }
                                }
                                String actualPrice = object.getString("_122_158");
                                String availability = object.getString("_121_39");
                                String barcode = object.getString("_121_108");
                                String specs = object.getString("_121_100");
                                String quantity = object.getString("_114_121");
                                String sku = object.getString("_121_102");
                                String model = object.getString("_122_131");
                                String age21flag = object.getString("_123_22");
                                String age18flag = object.getString("_123_23");
                                if (object.has("_114_149"))
                                    detailsBean.setBrand(object.getString("_114_149"));
                                detailsBean.setImage(image);
                                detailsBean.setMerchantID(merchantId);
                                Log.d("mERCHANTS", merchantId);
                                detailsBean.setName(name);
                                detailsBean.setSellerName(Utils.hexToASCII(sellerName));
                                detailsBean.setPrice(actualPrice);
                                detailsBean.setPrice_AfterDiscount(priceAfterDiscount);
                                detailsBean.setProductDesc(desc);
                                detailsBean.setLimit(limit);
                                detailsBean.setAvailability(availability);
                                detailsBean.setSpecial_price(specialPrice);
                                detailsBean.setTitle(title);
                                detailsBean.setBarcode(barcode);
                                detailsBean.setSpecs(specs);
                                detailsBean.setSku(sku);
                                detailsBean.setModel(model);
                                detailsBean.setAge18(age18flag);
                                detailsBean.setAge21(age21flag);
                                detailsBean.setQuantity(quantity);
                                JSONArray imArray = object.getJSONArray("IM");


                                ArrayList<ProductDetailsBean> arrayProductDetails = new ArrayList<>();
                                for (int j = 0; j < imArray.length(); j++) {
                                    JSONObject imObject = imArray.getJSONObject(j);
                                    String productImage = imObject.getString("_47_42");
                                    ProductDetailsBean productDetailsBean = new ProductDetailsBean();
                                    productDetailsBean.setProductImage(productImage);
                                    arrayProductDetails.add(productDetailsBean);
                                }

                                detailsBean.setArrayProductDetails(arrayProductDetails);

                                JSONArray opArray = object.getJSONArray("OP");
                                ArrayList<Sizedata> sizedataArrayList = new ArrayList<>();
                                for (int j = 0; j < opArray.length(); j++) {
                                    ArrayList<SizeBean> arraySize = new ArrayList<>();
                                    JSONObject chObject = opArray.getJSONObject(j);
                                    String nameSize = Utils.hexToASCII(chObject.getString("_122_134"));
                                    JSONArray chArray = chObject.getJSONArray("CH");
                                    for (int k = 0; k < chArray.length(); k++) {
                                        JSONObject o = chArray.getJSONObject(k);
                                        String size = Utils.hexToASCII(o.getString("_127_16"));
                                        SizeBean sizeBean = new SizeBean(size, "", "");
                                        arraySize.add(sizeBean);
                                    }
                                    Sizedata sizedata = new Sizedata();
                                    sizedata.setName(nameSize);
                                    sizedata.setSizeArray(arraySize);
                                    sizedataArrayList.add(sizedata);
                                }
                                detailsBean.setSizedata(sizedataArrayList);
                                arrayDetails.add(detailsBean);

                            }
                            EventBus.getDefault().post(new Event(Constants.DETAILS_SUCCESS, ""));
                        } else if (jobj.getString("_101").equals("010100012")) {
                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            for (int i = 0; i < imgeArray.length(); i++) {
                                JSONObject jsonObject2 = imgeArray.getJSONObject(i);
                                ProductOptionsBean productOptionsBean = new ProductOptionsBean();
                                String option_id = jsonObject2.getString("_122_111");
                                String option_name = Utils.hexToASCII(jsonObject2.getString("_122_134"));
                                productOptionsBean.setOption_id(option_id);
                                productOptionsBean.setName_option(option_name);
//                                if (!option_name.equals("Default"))
                                    arrayList.add(productOptionsBean);
                                Log.d("optionsIdbEAN", option_id);
                            }
                            arrayOptions1 = arrayList;
                            EventBus.getDefault().post(new Event(Constants.GET_ITEM_OPTIONS_SUCCESS, ""));

                        } else if (jobj.getString("_101").equals("010400599")) {

                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            Log.d("imgeArrays", imgeArray + "");
                            for (int i = 0; i < imgeArray.length(); i++) {
                                JSONObject jsonObject2 = imgeArray.getJSONObject(i);
                                //  Log.d("hello599", jsonObject2 + "");
                            }
                            categoryDetailsBean = new Gson().fromJson(jobj.toString(), CategoryDetailsBean.class);
                            if (categoryDetailsBean != null && categoryDetailsBean.get44().equals("Transaction Approved")) {
                                EventBus.getDefault().post(new Event(Constants.CATEGORY_DETAILS, ""));
                            } else {
                                EventBus.getDefault().post(new Event(-1, ""));
                            }
                        } else if (jobj.getString("_101").equals("010400221")) {
                            JSONArray imgeArray = jobj.getJSONArray("RESULT");
                            String imageUrl = "";
                            hexIds.clear();
                            Log.d(TAG, s);
                            itemsData = new HashMap<>();

                            for (int i = 0; i < imgeArray.length(); i++) {

                                ArrayList<Favdetailsbean> arrayAds = new ArrayList<>();
                                JSONObject jsonObject2 = imgeArray.getJSONObject(i);
                                if (jsonObject2.has("_120_45")) {
                                    String categoryName = jsonObject2.getString("_120_45");
                                    JSONArray jsonArray2 = jsonObject2.getJSONArray("PC");

                                    for (int j = 0; j < jsonArray2.length(); j++) {
                                        Favdetailsbean favdetailsbean = new Favdetailsbean();
                                        JSONObject jsonObject4 = jsonArray2.getJSONObject(j);
                                        String productId = jsonObject4.getString("_114_144");
                                        Log.d("productIdfav", productId);
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
                                        arrayAds.add(favdetailsbean);
                                    }
                                    itemsData.put(i, arrayAds);

                                }
                            }

                            favBean = new Gson().fromJson(jobj.toString(), FavBean.class);

                            if (favBean.get44().equals("Transaction Approved")) {
                                EventBus.getDefault().post(new Event(Constants.GET_FAVOURITE_SUCCESS, ""));
                            } else {
                                EventBus.getDefault().post(new Event(-1, ""));
                            }

                        } else if (jobj.getString("_101").equals("010200008")) {
                            Log.d("sss", s);
                        } else if (jobj.getString("_101").equals("010100631")) {
                            Log.d("sssadss", jobj.toString());
                            JSONObject object = new JSONObject(jobj.toString());
                            JSONArray jsonArray = object.getJSONArray("RESULT");
                            if (jsonArray.length() > 0) {
                                JSONObject object1 = jsonArray.getJSONObject(0);
                                EventBus.getDefault().post(new Event(Constants.PRODUCT_VIDEO_EXISTS,
                                        object1.getString("_121_2")));
                            }

                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                EventBus.getDefault().post(new Event(-1, ""));
            }
        }
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
            Log.d(TAG, "response_related_item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            relatedDetailsBean = new Gson().fromJson(s, RelatedDetailsBean.class);
            if (relatedDetailsBean.getRESULT().get(0).get44().equals("Transaction Approved")) {
                EventBus.getDefault().post(new Event(Constants.RELATED_DETAILS, ""));
            } else {
                EventBus.getDefault().post(new Event(-1, ""));
            }
        }
    }

    private class ExecuteCategoryApi extends AsyncTask<String, String, String> {
        Context mContext;
        HashMap<String, String> url_maps = new HashMap<String, String>();

        ExecuteCategoryApi(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... param) {
            String response = Client.Caller(param[0]);
            Log.d(TAG, "response_special_item---" + response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            specialItemBean = new Gson().fromJson(s, SpecialItemBean.class);
            if (specialItemBean.getRESULT().get(0).get44().equals("Transaction Approved")) {
                EventBus.getDefault().post(new Event(Constants.RELATED_SPECIAL_DETAILS, ""));
            } else {
                EventBus.getDefault().post(new Event(-1, ""));
            }

        }
    }

}
