package com.apitap.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.apitap.App;
import com.apitap.BuildConfig;
import com.apitap.controller.ModelManager;
import com.apitap.model.bean.DtoDefaultValues;
import com.apitap.model.bean.ShoppingCompBean;
import com.apitap.model.preferences.ATPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

@SuppressLint("NewApi")
public class Operations {

    private static String TAG = Operations.class.getSimpleName();
    private static Map<String, Object> map;
    private static Map<String, Object> map2;

    public static String makeJsonDefaultParams(Activity context) {
        DtoDefaultValues.setParam192("8f09eaddb545ff7c94b3c7106eede715");
        String dataPlana = "{\"101\":\"010100027\",\"PARAM\":{\"127.14\":\"MOBILE\"}}";
        String parametersToCall = "{\"192\":\"" + Constants.KEY_DEFAULT
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

//        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
//                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
//                + "en" + "\",\"57\":\""
//                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
//                + "0.0" + "\",\"120.39\":\""
//                + "0.0" + "\",\"OPTLST\":["
//                + dataPlana + "]}";


        Log.d(TAG, "parameters_default_api---" + parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonValidateUser(Activity context, String email) {
        String dataPlana = "{\"101\":\"050400009\",\"PARAM\":{\"114.7\":\""
                + email + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.v(TAG, "parameters_signup---" + parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonCounter(Activity context, String prodctId) {
        String dataPlana = "{\"101\":\"020400598\",\"PARAM\":{\"114.144\":\""
                + prodctId + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.v(TAG, "parameters_signup---" + parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonUserSignup(Activity context, String email, String fName, String lName, String password, String dob) {
        String dataPlana = "{\"101\":\"030300120\",\"PARAM\":{" +
                "\"114.7\":\"" + email +
                "\",\"114.3\":\"" + fName +
                "\",\"114.5\":\"" + lName +
                "\",\"114.8\":\"" + dob +
                "\",\"52\":\"" + password +
                "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.v(TAG, "parameters_createacc---" + parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonUserLogin(Activity context, String email, String password) {
        String lat = String.valueOf(App.latitude);
        String lon = String.valueOf(App.longitude);
        if (lat == null) {
            lat = "0.0";
            lon = "0.0";
        }
        //050300010 // previouse login
        //050300700 for token
        String dataPlana = "{\"101\":\"050300010\",\"PARAM\":{\"114.7\":\""
                + email + "\",\"52\":\""
                + Utils.convertStringToHex(password) + "\",\"57\":\"" + Utils.getDeviceId(context) + "\"}}";


        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + lat + "\",\"120.39\":\""
                + lon + "\",\"OPTLST\":["
                + dataPlana + "]}";
        Log.d("ResponseLoginParams", parametersToCall);
        return parametersToCall;
    }


    public static String makeJsonUserForgotPassword(Activity context, String email) {
        String dataPlana = "{\"101\":\"020300279\",\"PARAM\":{\"114.7\":\""
                + email + "\"}}";


        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.d("param_ForgotPassword", parametersToCall);
        return parametersToCall;
    }

    public static String updatePassword(Activity context, String password) {
        String dataPlana = "{\"101\":\"020300150\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"52\":\"" + password + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        Log.d("param_updatePassword", parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonShowCaseTitle(Activity context, String merchantId) {
        String dataPlana = "{\"101\":\"010100916\",\"PARAM\":{\"53\":\""
                + merchantId + "\"}}";


        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }


    public static String makeJsonGetAdsRelatedItemsOnly(Activity context, String AdId) {
        String lat = String.valueOf(App.latitude);
        String lon = String.valueOf(App.longitude);
        JSONObject obj1 = null;
        try {

            obj1 = new JSONObject();
            obj1.put("101", "010200517");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("EXPECTED", "ALL");
            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("123.21", AdId);
            obj2.put("operator", "eq");
            arr1.put(obj2);
            obj1.put("PARAM", obj_param);
            obj1.put("FILTER", arr1);


        } catch (Exception e) {

        }

        String dataPlana = obj1.toString();

/*        String lat = String.valueOf(App.latitude);
        String lon = String.valueOf(App.longitude);
        String dataPlana = "{\"101\":\"010200517\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\",\"114.179\":\"" + "" + "\"}}";

*/
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.d("makeJsonGetAdsOnly", parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonGetActivePlayersInfo(Context context) {
        String lat = String.valueOf(App.latitude);
        String lon = String.valueOf(App.longitude);
        JSONObject obj1 = null;
        try {

            obj1 = new JSONObject();
            obj1.put("101", "010101034");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.MERCHANT_ID));
            obj_param.put("121.141", Utils.GetToday());
            obj1.put("PARAM", obj_param);


        } catch (Exception e) {
            e.printStackTrace();
        }

        String dataPlana = obj1.toString();

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.d("getActivePlayersInfo", parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonGetAdFavourites(Activity context) {
        String dataPlana = "{\"101\":\"010100496\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }


    public static String getStoreByCategory(Activity context, String selectedCategoryId,
                                            String searchKey, String sort) {
        String dataPlana = "";
        if (selectedCategoryId.isEmpty()) {
            dataPlana = "{\"101\":\"010400645\",\"PARAM\":{" +
                    "\"53\":\"" + ATPreferences.readString(context, Constants.KEY_USERID) + "\"," +
                    "\"127.60\":\"" + sort +
                    "\"114.127\":\"" + searchKey + "\"}}";
        } else {
            dataPlana = "{\"101\":\"010400645\",\"PARAM\":{" +
                    "\"53\":\"" + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ," +
                    "\"114.93\":\"" + Utils.getElevenDigitId(selectedCategoryId) + "\" ," +
                    "\"127.60\":\"" + sort + "\" ," +
                    "\"114.127\":\"" + searchKey + "\"}}";

            //                    + "\" ,\"114.127\":\"" + searchKey}}";

        }
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.d(TAG, "getStoreByCategory" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonGetItemDetails(Activity context, String productId, String consumerId, String itemOrSpecial) {
        String dataPlana = "{\"101\":\"010200008\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{"
                + "\"53\":\"" + consumerId + "\" ,"
                + "\"120.38\":\"" + App.latitude + "\" ,"
                + "\"120.39\":\"" + App.longitude + "\" ,"
                + "\"114.144\":\"" + productId + "\"}}";
        String dataPlana1 = "{\"101\":\"030400471\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + consumerId + "\" ,\"114.144\":\"" + productId + "\"}}";
        String dataPlana2 = "{\"101\":\"020400598\",\"PARAM\":{\"114.144\":\""
                + productId + "\"}}";
        String dataPlana3 = "{\"101\":\"010100012\",\"PARAM\":{\"53\":\""
                + consumerId + "\" ,\"114.144\":\"" + productId + "\"}}";
        String dataPlana4 = "{\"101\":\"010400599\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + consumerId + "\" ,\"114.144\":\"" + productId + "\"}}";
        String dataPlan5 = "{\"101\":\"010400221\",\"PARAM\":{\"53\":\"" +
                consumerId + "\" ,\"127.10\":\"001\",\"114.112\":\"" + itemOrSpecial + "\",\"114.127\":\"\"}}";
        String dataPlan6 = "{\"101\":\"010100631\",\"PARAM\":{\"114.144\":\"" +
                productId + "\"}}";


        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "," + dataPlana1 + "," + dataPlana2 + "," + dataPlana3 + "," + dataPlana4 + "," + dataPlan5 + "," + dataPlan6 + "]}";

        Log.d(TAG, "parameters_makeJsonGetItems" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetRelatedItems(Activity context, String productId) {
        String dataPlana = "{\"101\":\"010100019\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{"
                + "\"53\":\"" + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,"
                + "\"120.38\":\"" + App.latitude + "\" ,"
                + "\"120.39\":\"" + App.longitude + "\" ,"
                + "\"114.144\":\"" + productId + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        Log.d("makeJsonGetRelatedItems", parametersToCall + "");
        return parametersToCall;
    }

    public static String makeJsonSpecialItemrequired(Activity context, String productId) {
        String dataPlana = "{\"101\":\"010100489\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.144\":\"" + productId + "\"}}";
        String dataPlana1 = "{\"101\":\"010100488\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.144\":\"" + productId + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                //      + dataPlana +","+ dataPlana1+ "]}";
                + dataPlana1 + "]}";

        Log.d("SpecialRelated", parametersToCall + "");
        return parametersToCall;
    }


    public static String makeJsonSpecialItemapplied(Activity context, String productId) {
        String dataPlana = "{\"101\":\"010100488\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.144\":\"" + productId + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }

    public static String makeSaveCart(Activity context, String cartId) {
        String dataPlana = "{\"101\":\"020400199\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"121.30\":\"" + cartId + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }


    public static String makeJsonGetItemsByCategoryViews(Activity context, String productId) {
        String dataPlana = "{\"101\":\"010400599\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.144\":\"" + productId + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }

    public static String makeJsonAdwatched(Activity context, String adId) {
        String dataPlana = "{\"101\":\"030400639\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"121.18\":\"" + adId + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }


    public static String makeSearchAds(Activity context, String searchAd) {
        String dataPlana = "{\"101\":\"010400676\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.127\":\"" + searchAd + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }

    public static String makeJsonAdsByBusiness(Activity context, String categoryId, String searchKey,
                                               String orderBy, String parentId, String merchantId, String zipCode) {
        String dataPlana2 = "";
        JSONObject obj1 = null;
        try {

            obj1 = new JSONObject();
            obj1.put("101", "010400677");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            if (!categoryId.isEmpty())
                obj_param.put("114.93", Utils.lengtT(11, categoryId));
            obj_param.put("114.127", searchKey);
            obj_param.put("120.38", String.valueOf(App.latitude));
            obj_param.put("120.39", String.valueOf(App.longitude));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("127.89", Client.getWeekDay());
            obj_param.put("127.60", orderBy);
            if (!parentId.isEmpty())
                obj_param.put("122.21", Utils.lengtT(11, parentId));
            if (!zipCode.isEmpty())
                obj_param.put("120.156", Utils.hexToASCII(zipCode));

            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("114.179", merchantId);
            obj2.put("operator", "eq");
            arr1.put(obj2);
            obj1.put("PARAM", obj_param);
            obj1.put("EXPECTED", "ALL");
            obj1.put("FILTER", arr1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        dataPlana2 = obj1.toString();
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana2 + "]}";
        Log.d("makeJsonAdsByBusiness", parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonSearchProduct(Activity context, String key, String sort, String merchantId, String zipCode,
                                               String brandName, String rating) {
        String dataPlana = "";
        String dataPlana1 = "";
        String dataPlana2 = "";
        String lat = String.valueOf(App.latitude);
        String lon = String.valueOf(App.longitude);
        if (lat == null) {
            lat = "0.0";
            lon = "0.0";
        }

        if (merchantId.isEmpty())
            dataPlana = "{\"101\":\"010400478\",\"PARAM\":{\"53\":\""
                    + ATPreferences.readString(context, Constants.KEY_USERID)
                    + "\" ,\"127.60\":\"" + sort
                    + "\" ,\"120.38\":\"" + lat
                    + "\" ,\"120.39\":\"" + lon
                    + "\" ,\"114.127\":\"" + key
                    + "\" ,\"120.156\":\"" + zipCode
                    + "\" ,\"114.149\":\"" + brandName
                    + "\" ,\"121.80\":\"" + rating
                    + "\" ,\"121.141\":\"" + Client.getDateTimeStamp()
                    + "\",\"127.89\":\"" + Client.getWeekDay() + "\"}}";
        else
            dataPlana = "{\"101\":\"010400478\",\"PARAM\":{\"53\":\""
                    + ATPreferences.readString(context, Constants.KEY_USERID)
                    + "\" ,\"127.60\":\"" + sort
                    + "\" ,\"120.38\":\"" + lat
                    + "\" ,\"120.39\":\"" + lon
                    + "\" ,\"114.127\":\"" + key
                    + "\" ,\"120.156\":\"" + zipCode
                    + "\" ,\"114.179\":\"" + merchantId
                    + "\" ,\"114.149\":\"" + brandName
                    + "\" ,\"121.80\":\"" + rating
                    + "\" ,\"121.141\":\"" + Client.getDateTimeStamp()
                    + "\",\"127.89\":\"" + Client.getWeekDay() + "\"}}";


        if (merchantId.isEmpty())
            dataPlana1 = "{\"101\":\"010400479\",\"PARAM\":{\"53\":\""
                    + ATPreferences.readString(context, Constants.KEY_USERID)
                    + "\" ,\"127.60\":\"" + sort
                    + "\" ,\"120.38\":\"" + lat
                    + "\" ,\"120.39\":\"" + lon
                    + "\" ,\"120.156\":\"" + zipCode
                    + "\" ,\"114.127\":\"" + key
                    + "\" ,\"121.141\":\"" + Client.getDateTimeStamp()
                    + "\",\"127.89\":\"" + Client.getWeekDay() + "\"}}";
        else
            dataPlana1 = "{\"101\":\"010400479\",\"PARAM\":{\"53\":\""
                    + ATPreferences.readString(context, Constants.KEY_USERID)
                    + "\" ,\"127.60\":\"" + sort
                    + "\" ,\"120.38\":\"" + lat
                    + "\" ,\"120.39\":\"" + lon
                    + "\" ,\"120.156\":\"" + zipCode
                    + "\" ,\"114.127\":\"" + key
                    + "\" ,\"114.179\":\"" + merchantId
                    + "\" ,\"121.141\":\"" + Client.getDateTimeStamp()
                    + "\",\"127.89\":\"" + Client.getWeekDay() + "\"}}";

        if (merchantId.isEmpty())
            dataPlana2 = "{\"101\":\"010400676\","
                    + "\"EXPECTED\":\"ALL\","
                    + "\"PARAM\":{\"53\":\"" + ATPreferences.readString(context, Constants.KEY_USERID)
                    + "\" ,\"120.156\":\"" + zipCode
                    + "\" ,\"114.127\":\"" + key + "\"}}";
        else
            dataPlana2 = "{\"101\":\"010400676\"," + "\"EXPECTED\":\"ALL\","
                    + "\"PARAM\":{\"53\":\"" + ATPreferences.readString(context, Constants.KEY_USERID)
                    + "\" ,\"114.179\":\"" + merchantId
                    + "\" ,\"120.156\":\"" + zipCode
                    + "\" ,\"114.127\":\"" + key + "\"}}";


        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "," + dataPlana1 + "," + dataPlana2 + "]}";

        Log.d("makeJsonSearchProduct", parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetAds(Activity context, String MerchantId, String Sort) {
        String lat = String.valueOf(App.latitude);
        String lon = String.valueOf(App.longitude);
        //For Ads
        String special_sort = Sort;
        if (Sort.equals(Constants.PriceHightoLow) || Sort.equals(Constants.LowtoPriceHigh)) {
            special_sort = Constants.NewToOld;
        }
        JSONObject obj1 = null;
        try {

            obj1 = new JSONObject();
            obj1.put("101", "010200517");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("EXPECTED", "ALL");
            if (!MerchantId.isEmpty()) {
                JSONArray arr1 = new JSONArray();
                JSONObject obj2 = new JSONObject();
                obj2.put("114.179", MerchantId);
                obj2.put("operator", "eq");
                arr1.put(obj2);
                obj1.put("PARAM", obj_param);
                obj1.put("FILTER", arr1);
            } else {
                obj1.put("PARAM", obj_param);
            }


        } catch (Exception e) {

        }

        String dataPlana = obj1.toString();/*"{\"101\":\"010100517\"," + "\"EXPECTED\":\"ALL\","+"\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\",\"114.179\":\"" + MerchantId  +"\"}}";*/

        //For Items
        String dataPlanItem = "{\"101\":\"010400478\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"120.38\":\"" + lat + "\" ,\"120.39\":\"" + lon + "\" ,\"127.60\":\"" + Sort + "\" ,\"121.141\":\"" + Client.getDateTimeStamp() + "\",\"127.89\":\"" + Client.getWeekDay() + "\",\"114.179\":\"" + MerchantId + "\"}}";
        //For Specials
        String dataPlanSpecial = "{\"101\":\"010400479\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"120.38\":\"" + lat + "\" ,\"120.39\":\"" + lon + "\" ,\"127.60\":\"" + special_sort + "\" ,\"121.141\":\"" + Client.getDateTimeStamp() + "\",\"127.89\":\"" + Client.getWeekDay() + "\",\"114.179\":\"" + MerchantId + "\"}}";

        String dataPlana2 = "{\"101\":\"010100055\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                /*+ dataPlana + ","*/ + dataPlanItem + "," + dataPlana2 + "," + dataPlanSpecial + "]}";
        Log.d("makeJsonGetAds", parametersToCall + "");
        Logger.addRecordToLog(parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonGetItemsCategory(Activity context, String MerchantId, String Sort, String categoryId) {
        String lat = String.valueOf(App.latitude);
        String lon = String.valueOf(App.longitude);
        String dataPlanItem = "";
        if (lat == null) {
            lat = "0.0";
            lon = "0.0";
        }
        //For Ads
        String special_sort = Sort;
        if (Sort.equals(Constants.PriceHightoLow) || Sort.equals(Constants.LowtoPriceHigh)) {
            special_sort = Constants.NewToOld;
        }

        //For Items
        if (!categoryId.isEmpty()) {
            dataPlanItem = "{\"101\":\"010400478\",\"PARAM\":{\"53\":\""
                    + ATPreferences.readString(context, Constants.KEY_USERID) +
                    "\" ,\"120.38\":\"" + lat +
                    "\" ,\"120.39\":\"" + lon +
                    "\" ,\"127.60\":\"" + Sort +
                    "\" ,\"114.93\":\"" + Utils.getElevenDigitId(categoryId) +
                    "\" ,\"121.141\":\"" + Client.getDateTimeStamp() +
                    "\",\"127.89\":\"" + Client.getWeekDay() +
                    "\",\"114.179\":\"" + MerchantId + "\"}}";
        } else {
            dataPlanItem = "{\"101\":\"010400478\",\"PARAM\":{\"53\":\""
                    + ATPreferences.readString(context, Constants.KEY_USERID) +
                    "\" ,\"120.38\":\"" + lat +
                    "\" ,\"120.39\":\"" + lon +
                    "\" ,\"127.60\":\"" + Sort +
                    "\" ,\"121.141\":\"" + Client.getDateTimeStamp() +
                    "\",\"127.89\":\"" + Client.getWeekDay() +
                    "\",\"114.179\":\"" + MerchantId + "\"}}";

        }
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                /*+ dataPlana + ","*/ + dataPlanItem + "]}";
        Log.d("GetItemsCategory", parametersToCall + "");
        Logger.addRecordToLog(parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonSpecialsCategory(Activity context, String MerchantId, String Sort, String categoryId) {
        String lat = String.valueOf(App.latitude);
        String lon = String.valueOf(App.longitude);
        String dataPlanItem = "";
        if (lat == null) {
            lat = "0.0";
            lon = "0.0";
        }
        //For Ads
        String special_sort = Sort;
        if (Sort.equals(Constants.PriceHightoLow) || Sort.equals(Constants.LowtoPriceHigh)) {
            special_sort = Constants.NewToOld;
        }

        //For Items
        if (!categoryId.isEmpty()) {
            dataPlanItem = "{\"101\":\"010400479\",\"PARAM\":{\"53\":\""
                    + ATPreferences.readString(context, Constants.KEY_USERID) +
                    "\" ,\"120.38\":\"" + lat +
                    "\" ,\"120.39\":\"" + lon +
                    "\" ,\"127.60\":\"" + Sort +
                    "\" ,\"114.93\":\"" + Utils.getElevenDigitId(categoryId) +
                    "\" ,\"121.141\":\"" + Client.getDateTimeStamp() +
                    "\",\"127.89\":\"" + Client.getWeekDay() +
                    "\",\"114.179\":\"" + MerchantId + "\"}}";
        } else {
            dataPlanItem = "{\"101\":\"010400479\",\"PARAM\":{\"53\":\""
                    + ATPreferences.readString(context, Constants.KEY_USERID) +
                    "\" ,\"120.38\":\"" + lat +
                    "\" ,\"120.39\":\"" + lon +
                    "\" ,\"127.60\":\"" + Sort +
                    "\" ,\"121.141\":\"" + Client.getDateTimeStamp() +
                    "\",\"127.89\":\"" + Client.getWeekDay() +
                    "\",\"114.179\":\"" + MerchantId + "\"}}";

        }
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                /*+ dataPlana + ","*/ + dataPlanItem + "]}";
        Log.d("SpecialsCategory", parametersToCall + "");
        Logger.addRecordToLog(parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonGetBusinessDetails(Activity context, String merchantId) {
        String dataPlana = "{\"101\":\"010100020\",\"PARAM\":{\"53\":\""
                + merchantId + "\"}}";
        return dataPlana;
    }

    /*public static String makeJsonGetItems(Activity context, String productId, String productType) {
        String dataPlana = "{\"101\":\"010200008\"," + "\"EXPECTED\":\"ALL\"," + "\"PARAM\":{\"53\":\""
                + "00011010000000000004" + "\" ,\"114.144\":\"" + productId + "\" ,\"114.112\":\"" + productType + "\"}}";


        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + *//*Util.GetDeviceId()*//*Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }*/


    public static String makeJsonGet(Activity context) {
        String dataPlana = "{\"101\":\"010100003\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\"}}";


        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }

    public static String makeJsonAddToFavorite(Activity context, String productId) {
        String dataPlana = "{\"101\":\"030400218\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.144\":\"" + productId + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        Log.d("parameters_add_fav", parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonAdToFavorite(Activity context, String productId) {
        String dataPlana = "{\"101\":\"030400497\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"121.18\":\"" + productId + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        Log.d("AdToFavorite__params", parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonMerchantAddToFavorite(Activity context, String MerchantId) {
        String dataPlana = "{\"101\":\"030400095\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"127.56\":\"" + MerchantId + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }

    public static String makeJsonGetOptions(Activity context, String productId) {
        String dataPlana = "{\"101\":\"010100012\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.144\":\"" + Utils.getElevenDigitId(productId) + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        return parametersToCall;
    }

    public static String makeJsonGetOptions2(Activity context, String optionId) {
        String dataPlana = "{\"101\":\"010100013\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"122.111\":\"" + optionId + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.d("paramsGetOptions2", parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonGetFavouriteMerchant(Activity context) {
        String dataPlana = "{\"101\":\"010100303\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }

    public static String makeJsonGetShoppingCart(Activity context) {
        String dataPlana = "{\"101\":\"010100200\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.d("getCartOp", parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetShoppingCartItem(Activity context, ShoppingCompBean shopping, String ifDeleted) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100201");
            JSONObject obj_param = new JSONObject();
            if (!ifDeleted.isEmpty())
                obj_param.put("122.31", ifDeleted);
            else
                obj_param.put("122.31", shopping.getShoppingCartId());
            obj1.put("PARAM", obj_param);

            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();

            obj2.put("114.143", shopping.getShoppingCartStatus());
            obj2.put("operator", "eq");
            arr1.put(obj2);
            obj1.put("FILTER", arr1);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_shoppingcartdetail_api---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonPayShoppingCart(Activity context, String merchant_Id, String shopping_id, String amount,
                                                 String gratuity, String shipping_iD, String delivery_Id, String card_token,
                                                 String card_amout, String tax, ShoppingCompBean shoppingCompBean, String choiceId,
                                                 String choicePrice, JSONArray jsonArray, String specialInstructions) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", App.latitude);
            obj.put("120.39", App.longitude);
            JSONArray arr = new JSONArray();
            JSONArray arrkt = new JSONArray();
            JSONArray arram = new JSONArray();
            JSONArray arrtx = new JSONArray();
            JSONArray arrchoice = new JSONArray();
            JSONObject obj1 = new JSONObject();
            JSONObject objkt = new JSONObject();
            JSONObject objam = new JSONObject();
            JSONObject objtx = new JSONObject();
            JSONObject objchoice = new JSONObject();
            obj1.put("101", "030300023");
            JSONObject obj_param = new JSONObject();

            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.179", merchant_Id);
            obj_param.put("122.29", Utils.getElevenDigitId(shoppingCompBean.getShoppingCartId()));
            obj_param.put("13", Client.getTime());
            obj_param.put("55.3", amount);
            obj_param.put("120.109", gratuity);
            obj_param.put("114.115", shipping_iD);
            obj_param.put("122.109", delivery_Id);
            obj_param.put("121.55", specialInstructions);
            obj_param.put("120.38", "0.0");
            obj_param.put("120.39", "0.0");


            objkt.put("48.15", card_token);
            arrkt.put(objkt);
            obj_param.put("KT", arrkt);

            objam.put("48.30", card_amout);
            arram.put(objam);
            obj_param.put("AM", arram);

            objtx.put("121.97", tax);
            arrtx.put(objtx);
            obj_param.put("TX", arrtx);

            objchoice.put("121.30", Utils.getElevenDigitId(shoppingCompBean.getShoppingCartId()));
            objchoice.put("121.104", choiceId);
            objchoice.put("114.98", choicePrice);
            objchoice.put("114.144", "00000000003");
            arrchoice.put(objchoice);
            obj_param.put("CH", jsonArray);


            obj1.put("PARAM", obj_param);

         /*   JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("114.143", shopping.getShoppingCartStatus());
            obj2.put("operator", "eq");
            arr1.put(obj2);
            obj1.put("FILTER", arr1);*/

            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "payShopping_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetFavourite(Activity context, String word) {

        JSONObject objMain = null;
        JSONObject objMain2 = null;

        try {
            objMain2 = new JSONObject();
            objMain2.put("101", "010400681");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            objMain2.put("PARAM", obj_param);
        } catch (Exception w) {
            w.printStackTrace();
        }

        try {
            objMain = new JSONObject();
            objMain.put("101", "010100303");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            objMain.put("PARAM", obj_param);

        } catch (Exception w) {
            w.printStackTrace();

        }
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400221");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("127.10", "001");
            obj_param.put("114.112", "21");
            obj_param.put("114.127", word);
            obj1.put("PARAM", obj_param);
            JSONObject obj2 = new JSONObject();
            obj2.put("101", "010400221");
            JSONObject obj_param2 = new JSONObject();
            obj_param2.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param2.put("127.10", "001");
            obj_param2.put("114.112", "23");
            obj_param2.put("114.127", word);
            obj2.put("PARAM", obj_param2);

            arr.put(obj2);
            arr.put(obj1);
            arr.put(objMain);
            arr.put(objMain2);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_favourite_api---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonGetMerchantFavourite(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400094");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("127.10", "001");
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_favourite_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetStoreImages(Activity context, String id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100906");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", id);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "makeJsonGetStoreImages---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonRemoveMerchantFavourite(Activity context, String merchantID) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "040400096");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("127.56", merchantID);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_remove_favourite_api---" + parametersToCall);

        return parametersToCall;
    }


    public static String sendMessage(Activity context, String merchantId, String type, String subject,
                                     String message, String productId, String locationId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030300192");
            JSONObject obj_param = new JSONObject();
           /* obj_param.put("53", merchantId);
            obj_param.put("114.179", ATPreferences.readString(context, Constants.KEY_USERID));*/
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.179", merchantId);
            obj_param.put("120.16", type);
            obj_param.put("114.9", "true");
            obj_param.put("122.128", Utils.convertStringToHex(subject));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("120.157", Utils.convertStringToHex(message));
            if (!locationId.isEmpty())
                obj_param.put("114.47", Utils.getElevenDigitId(locationId));
            if (!productId.isEmpty())
                obj_param.put("114.144", productId);

            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_sendmsg_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String sendMessageForProduct(Activity context, String merchantId, String type, String subject,
                                               String message, String productId, JSONArray image, String locationId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030300192");
            JSONObject obj_param = new JSONObject();
           /* obj_param.put("53", merchantId);
            obj_param.put("114.179", ATPreferences.readString(context, Constants.KEY_USERID));*/
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.179", merchantId);
            obj_param.put("120.16", type);
            obj_param.put("114.9", "true");
            obj_param.put("122.128", Utils.convertStringToHex(subject));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("120.157", Utils.convertStringToHex(message));
            if (image.length() > 0)
                obj_param.put("MI", image);
            //  obj_param.put("121.75", invoiceId);
            obj_param.put("114.144", productId);

            if (!locationId.isEmpty())
                obj_param.put("114.47", Utils.getElevenDigitId(locationId));

            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_sendmsg_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String sendMessageForAd(Activity context, String merchantId, String type, String subject,
                                          String message, String productId, JSONArray image, String locationId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030300192");
            JSONObject obj_param = new JSONObject();
           /* obj_param.put("53", merchantId);
            obj_param.put("114.179", ATPreferences.readString(context, Constants.KEY_USERID));*/
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.179", merchantId);
            obj_param.put("120.16", type);
            obj_param.put("114.9", "true");

            obj_param.put("122.128", Utils.convertStringToHex(subject));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("120.157", Utils.convertStringToHex(message));
            if (image.length() > 0)
                obj_param.put("MI", image);
            //  obj_param.put("121.75", invoiceId);
            obj_param.put("123.21", productId);

            if (!locationId.isEmpty())
                obj_param.put("114.47", Utils.getElevenDigitId(locationId));


            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_sendmsg_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String sendMessageProduct(Activity context, String productId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030300192");
            JSONObject obj_param = new JSONObject();
//            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.144", productId);
            obj_param.put("121.141", Utils.GetToday());
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_sendproductmsg_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String getCategoriesLvlOne(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100481");
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getCategoriesLvlOne---" + parametersToCall);

        return parametersToCall;
    }

    public static String imageSearch(Context context, String image) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030301056");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("119.17", image);
//            obj_param.put("119.17", "/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAErASsDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD3aiiikAUUUUAUtYj8zRrxf+mLH8hmvO4TyfavTpYxLC8Z6MpX868viHzkEVyYpapndhHo0aKfdFObkDp0qKL7oFSnnFcjOhmbff6ts15fcsIvHOjynomoQOT7CRTXqN4Mq1eUeIWMGuQSggFJlYZ9jmtKHxk1f4bNr4raR/Zfjm7ZQBHexrdLjpk5Vvx3KT+NebzA7yF5r6I+Nuhrd+E01mPcLjT5ADgfejkYKQfo20+3NfPHJ616iPLIBGxHJApyxKBySam20EcUAMVFU8KAfWn0lLTEWrRiZY+xU8frTkNul9LGiP5/mkhh3BXoOeMHPY5z2xy20U5RgefMx+lTraubua73Fts+wqFyFyM5Y/w57cHO1um3lALKySrEtonlyCNRMeeWzyxyTnr2Ax6dy69MEs93JaReXbhjheQAePVmJPvmn3Nu1kY5M7xcRhgV6DJ6A9yOh9DxUV5bGzae2EokVh5m8DhuP4fUe/H0FAGZONszY6cEfiBURPFSz8yknuF/9BFRUARsAewppGOlSGmHigBu5h70of1BpDRQMuadZT6pqVrp9oA1xdSrDGCcAsxwM+3NfYmg6Na+H9Cs9JsxiC2jCAnqx6lj7kkk+5r5v+DOkDVfiJayucJYRPdkY6kYVR+bg/hX1DUgfO/xksfs/jySYf8ALzbxy/kCn/slczo0eZh9a9L+N2mn7RpmogEho2hY+mDkf+hN+Ved6KuJB65rmrbHdh9bHeafEBEOKsSpwabY8RCp5Oa4Gdq3Ma5XBNdd8Nj/AMTG6H/TH/2YVy94nBNdN8Nmxq9yvc25P/jy10Yd+8Y4r+Gz0uiiivQPKCiiigAooooAKKKKACvNbyLyNVuIiPuyNj6Zr0quA8Qp5fiG47Btrf8AjornxK91M6sI/eaIY+vFSnrUUXIqUnA4rgZ2sp3SblNeTeNI8XJb3NetTn5DXmPi+PfNn3rSlpNEyXus+iNY06LX9AvNOkbbHeW7RbgPu7hgEe4618hXNpNYXs9ncgCe3kaGUDoHUkEfmDX2Do832jRNPn/56W0b/moNeReMfhvFe+Pb2/muvIs7sLMIoU+YttCtyeBkgnofvV6TmoK7PMjFydkeL0+G3muZRFBDJNIeAkalifwFe32fgfw3ZgFNMjlYdWuGMufwJx+lbVtZ2tjH5dpbQ26f3IYwg/ICsJYtfZR0LCvqzxWy8B+JL0Kyaa0KH+Kd1jx+BO79K3bb4VX/AFvL62T2h3N+pAr1YHFITnrWMsVN7GscPBeZ5tH8MmhAEeo4Oc/Mmang+FsTXPnz6jLvx1jUD+f1r0DHNSLxUqvU7lOlDscN/wAKvsWbJ1K6yB/dX/Cmv8LLQ5K6ncAnruQHtj2rvxyMigtgUe2n3J9lHseYT/CQOCyaqQx7mLj+dY1z8K9bhBMNzZTAf7TKT+GMfrXsbN1xUDEmksTUXUfsIPoeEXngfxHaZ8zTJGA7xMr5+gBz+lYNxa3Fo224glhb+7IhU/rX0dJyc1RureG4QpNEki91YAg1axkuqJeEi9mfPFJXsGpeFdGmBP8AZ8KN6xgp/LFcffeEreNm8h5EHYE5xW8cTCRnLCTWqO//AGe7EeZruoMvIEUCN/30zD/0Gvcq81+CemDTvB93k5kkvnLHGOAiAf5969KrdO+pzNNOzOF+LNgbvwYZh/y6zpIfocp/NhXi2kLtlr6M8U2B1PwtqVooyzwMUHqw+YfqBXzxpq7bjHvXPX2OvCs7mxGYhVmRcVXsP9WMVclHy1553dTLvB8lb3w4P/E+uF/6dWP/AI8lYl0DtNafw/m8vxTs5/eQun8m/wDZa3w798zxH8Jnq1FFFeieSFFFFABRRRQAUUUUAFcT4qXGto3rCp/U/wCFdtXHeK1B1aA9zDj/AMeNY4j4Dow38QzIgdvSnv06U6JcKKimbbnmvOZ3FebkGvPPFcRDMcdTxXoJbINcX4uj/dbj1zVQ0kh9Ge1eEn8zwZoT5zu0+3Of+2a1T8XQgxWlx3VzH+Yz/wCy/rSfD6YT+A9HIOQkAi/75JX+laPiKLzdCueOUAcfgcn9M16NVc1No82m+WomcfGeBUuarI4wOakEg7EfnXlHpE3aiow9LvHrVIQ+nAZ6VFvXPWke7t4ELyyLEg4LO20D8TVCZZAxTWPeoHu4ouWdVHqTioJdTslXLXUIHvIBRZiLLnA4qFm4qi+u6UgJOpWY+s6/41nXXivRYlJOqWh9kmVj+QJqeST6Duu5sOeOtV3ORXMT+PtCQcXbyH0SJv6gVnS/EnTVbEdpduOxKqAf/HqpUKj6A6tNdTqrgDmse5iUg8CuUvviLcTEi2sY417GRyx/IYrCuvFmr3H/AC3EYP8AzzQD9Tk1tHC1OpLxdNbH0R8LpF/se/gB5S53EemUX/Cu7rwH4A6rt8Q6zp8rkyXcCTqWPUxsQfx/eD8q9+rujHlVjzpy5pOQHkEda+bHtDYa1c2jfeglaM/VTj+lfSdfP3iiPyPHOqIepuGbn/aO7+tZV17pvhX7xu6d/qx6VfcfJ0rP03/VLWkfu4rzmehfUz7lMqaXwe3leMbIE4BZx+aNU8qEg1mQSrZa5Z3cjBY4p0kdj2UMCf0FXSdpIVRc0Gj26igHIor1DxwooooAKKKKACiiigArj/FPOsW//XH/ANmNdhXI+JcHW4uf+WAB/wC+mrHEfAb4b+IURkLmqF3JgnFXmbavXFZN6+MnrXnnehsUgc1zfitN1swrZs3Jm2+ucVleJGVrZj+VNbjPQPhRL5ngG0H9yWVT/wB9k/1rp9ZiefQ9QijYrI9tIqkHBBKnBrkPhCGHgk5zg3UhXntha7m4BNtKB12HH5V6i2PKl8TPkceJtZKgjUrwcf8APZqD4l1oZC6nd/XzTWUn+rGR260mRjjvT5I9hc8u5rf8JPrh/wCYndj/ALbMKY3iXXP+gtef9/2/xrLGM0hJHc0+SPYOeXcvvreqTHdJqV45xj5rhz/WnWaC7t5xLdlDLcwQ/Ou8kOWJIyeo2dBgnOMgZzm9e1bnh7XLrQlnksoFmmkdAct9xVyc4xk555HTHvTshNswZZRM5YKB2Geen1pAPYflUt6ipqNysaqqbyVVWDhc87dw4OOmRwcZqP2xQAmcUhycc9KXrSHjpQAm00ueBxQDnk0vUGmA3jrTG6daeMHsaa3fikI0/C2uSeG/FGnavGXxbTBpFQ8vGeHX8VJFfY8ciSxpJGwdHAZWByCD3FfERr6U+CniVdX8Hf2XNNuvNMbysMTkwnmM/QfMv/ARSYz0uvBvHkYh+IN/z95o2/ONK95rwr4mxmHx5K5wBLHG49/lC/8AstZVV7pvh375e0xswKa0i3GKw9JkzCOa11JNeY9z0SQglaydRgyDlcjuK2UGRUF1EGU0IpM9C8K3n27wzYSksWWPymLHksnykn64z+NbFcN8PLl1+32Dt8qss0YPXnhh9OF/Ou5r1KcuaKZ5NWPLNoKKKKszCiiigAooooAK4/xFk64PaFR+prsK43XX3a/KP7qKP0zWGJ+A6MN8ZnTE7ax7xsZBxWzIcLz6Vi3vIPPSvPPQiZxn8naynoT0+hH9ax9clDWrHPbFWLxyseM1g6hOWiCZ5J5rWKu0KWiZ7Z8MrY23gSyyADI0knHfLH/Cuk1S4FppF7cnpFA7n8FJqt4atPsHhfSrUjDRWkSt/vbRn9c1R8e3i2PgPW5m72rRj6v8g/8AQq9JHkvVnyepIQD2oJJPPNOfvUZY+35VZItJ1pMn/IpNx/yKQC45pRgSJ6bh/n9aYWOe35UEnufyoAmnQKwyOoqAGrV6OFIqnz60AOzigEGm++aKAFP0pN1JgUGgA5pNuetONFAxu0V0HgrxNN4R8UWmqJuaEHZcRr/y0iONwx69x7gdqwKQikI+09Pv7bVNPt76zlEttcRrLG443KRkcdq8e+MEezxVZS4wGtVGfU73/wDrVgfCT4jp4fuBoWrzbdLncmGdzxbOeuc9EY/kee5I7H41WhNvpV+o4UyRs3/fJH/s1RNXRrSdpo5vRZcxqPWt5GxiuK0S6IUAc812NqTIgzxXmyVmemndF+LOOKkmTclJGAq44qU8rUiuReG5xY+LrVicLOGgYkeoyP8Ax5VFeoV43f742EkRKyIwZG9GByD+eK9ds7lb2ygukBCTRrIoPXBGa7cNK8bHJi4+8pdyeiiiuk5AooooAKKKKACuF1mTPiK79io/8dFd1XnuoNv167JPWQj8uK5sS/dR1YVe8/QHwY81k3adTWq/3OazbkZVq4md0Tk9VO1a5lke7uVgjBLudqj1J4FdLrY2oTWZ4St2vPGOlQqNxN0jYP8AdVgzf+Og1vQV2RXdkfSiqEUKBgAYFcB8ZL37L8P5YeP9LuYofyO//wBkr0CvE/jtqzNd6Xo6n5Y42uXHqWO1fyw3513o8o8abvUZp7Uw1TEJSUtNzSGFLSE0ZpgXLj5rZX9QD/KqFX3+a0UD+6P8/pVGgQlFFLQMSiiigBe1JQKM0AFJSk0lIBOnNdpp/jeS68Hy+GdZkeSKErJp9wTkxMOPLb/Y2k4P8OMdMbeLoIoaGnZ3Ox0W72SKpPevQNPn3IpB7V4zp18bSdd5/d+vpXp+h3nmxqQ2QRXn14OLuejQmpqx2MTZxVgfdNUbZ8irobjFc5qZmoLnNdt4DvGufD5gdstbStGMnnacMP5kfhXHXgBrT+H92sGs3dm3BuIgyn3Qnj/x4/lW+HladjPExvSv2PRqKKK9A8wKKKKACiiigArzedt+q3Lesrfzr0ivM8k3kxPd2P61y4rZHZhN2WXA21n3Q4NaORs7VQuh8prjZ1x3OP17iM1e+ENp9p8Xz3LJlba2Zg391mIUfoWqhr+PLYV1fwTt2EGtXOPlZ4ogfdQxP/oQrpw25li3aJ6vXy58S9UXVPHuqSoxKRy+QuT0CAKce2QT+NfRvijVDovhbU9RRlEkFs7Rbum/GFH4sQK+SLiRpZmd3LOxyWPUn1ruR5pCxphpxNNpgFNNOpKQCUdqDRTAtwZa1xnpn/P61TNXbTHkvz3/AKCqRwDigQlJS0lAwoopKAFpKDSZoAU0lLSAUALR2oopAIwzW94Y1z+z7tYLiTFu/Csx4jP+FYNNYfrUzipKzKhNwd0e9WU+5RzWujZWvMfAmvtcL/Z9w482FR5ZPVk6fmOPwI969HgfK15c4uEuVnqRkprmQ266VV0S5+xeJ7GbHy+aEP0b5SfwDZq3cDI4rFuxiQHJHv6UQdpJltc0Gj3Aciiq2nXYvtNtrpeksav+JHNWa9U8YKKKKACiiigArzGP/Wsc9zXp1eZQAhsd81yYrZHZhOpcUfLVC7Pyk1oA/Jyazr1uDXIzrjucX4hb5Gr0n4RWf2bwQJ/+fq6kl/LCf+yV5f4ifGa9n+H1obLwDo0ZOd8Hn/8AfwmT/wBmrrwqMMY9EjjvjlrpsvD9lo0Z+a/lMkntHHg/+hFfyNeBMa634l+Ij4i8d30qn9xZk2UODwQjHJ/Fixz6Yrka7UeeIaSg0lIAoopKAA0dqDR2pgWrM8SL7f0P+FVpRiVvrU9p98j1x/h/WoZ/9affB/SgRHSUtJQMKQ0tJQAUfhR0ooADRS0UAFJ3paQ0AJSHkUtFIB9rdTWV1FdQNtlibcp/ofavctA1KLVNMhu4SfLlGcHqDnBB+hyPwrwgjBIrufhxrJgvZdKk+5LmWI+jDGR+I5/A+tc+IhePMuh04apaXK+p6nMAVFY94oxWsWBT1rOvF4Nef1PSieheBrgTeGY0HWGR0P4nd/7NXSVwfw5u+b2zJ7CVR+h/9lrvK9Sm7wR5FaPLNoKKKKszCiiigA7V5pt2XUqf3XI/WvS687vk8vVroekrfzrlxWyOvCPVoePu/hWXfH5D9K0gfl4rLvz8pHtXG9jtjucB4jkILbRluwHc9q921m/i8G+BLi6yu3TrMJEG4DOFCov4tgfjXisdr/aXi3S7PGRJdxAj1G4Fv0Brp/j/AK0YNG0zRYyQbqY3EpB/hQAAEe7Nn/gFd2GXunHjH7yR4WhJUFmLN3Y9T70tIv3RS10nGJRRSZoADRRmimAlKKSjPFAE9ocT89AM/qKZcLskwe3FEHMoX1BH6Gn3uDMWHQk/4/1oArUUGigQhopaQ/SgAopaKBhRRRQAUmM0tFAhtFKaSgYhqWwvX07ULe8jzugkD4HcDqPxGR+NQnpTTSauCdnc+gbK5ju7aOaJg8UiB0I7gjINJcruU1zfgO/N14ctkZsvDmI+2DwP++StdTIMpXkTXLJo9mErpMseCrn7N4niQnAmVozn6Z/mor1SvFrKcWWs2tw2dscqucegIJr2mu7DO8bHDjI2ncKKKK6DkCiiigArgdbUx69dY6lgfzUGu+rh/EabdclbH3lU/pj+lc2K+A6cK/fKacrWXqXyofxrUjGQBWbquNjD2ri6HfHcw/BEH2n4lWL9oFllx/2zZf8A2YVwfxU1s638RNSdXYw2r/ZIgT0CcN/4/vP416H4Gmi03Wde164IEOmaa5Oe5Zg3/tPH414XLNJcXLzTNulkYu7erE5J/OvRoK0EefineoydTwKXNNXpS1sc4tJSZozTAKKKKACjvSd6UdaAHxHEqf7w/wAKmvOQp47fyFQKcOp9CDUk0gdAvcYoEV6KB2ooGJRS0UAJmlpKKBC0UUUDCiiigBDTc0tFACHkGmGnnv8ASmGkB3Pw5uykl3bH7pKuOe5BB/kK9OHzJXjHg66+z6yQWwHjI/HIr2K2fzIQRnGK83EK1Rnp4Z3poz7tdsgOPrXsWkTG40WxmJyzwISffaM15Jfx/JvArvPDGtW9v4ctIpZUDqG4PUDccfpitMNJK9yMXFtJo6yiiiu088KKKKACuO8UDGrL7xA/qa7GuP8AE5H9rJ7Qr/NqwxP8M6ML/EMqM4FZ+qfcY/WrqtzVDU/9WTXn9D0VucNrF79g8A69tbDaheW9n15woeVvwwAP+BV5Yv3810/i+5mFwtmWPkqzTBf9pgqn9EH61y8fLV6lL4EeXX/iMtL0pc0iniitTIDRSUUCFopKWgYtAIHakoJoAX86Rhz1NJSt96gBDRRRQAUUgNKDmgQUUUUDCiiigAooooAaaQ048ikxSAQ9D9KZTz0NMNAF3SZPK1GJs4r2rRZxNaqc9q8d0DSbrVrm5W0QvJa2zXJQdWVWUNj6Bs/hXpXhS6zCqE81xYqOqZ34R+60dLdIPLIIzWSQykjznXHYGt2dMoD7VkyKd54rljJpnW4qS1PbKKKK9Y8UKKKKACuM8TNnVyPSNR/X+tdnXDeIJT/bNwCMfdAz/uisMT8B04X4zLL4OKrX3zR/WpT8zZ9KiuSDGPY1wNaHorc8b+IEYi1uPHeEZ+uTXKR/ert/iLCDcJcdwwT8CP8A61cPH96vRoO9NHmYlWqstL0pc0g6UprcwCikooAWjNJRmgQtFFJmgYpoY/N+FNNK/UfSgBR0opB0p1ACYoxzS4oxQAUUUUAFFFFAhKDRRSAbRRQaBiH7pplOPQ/Sm0Aer/AK2L+M7+cgFE050P1aSP8A+JNal5ozeG/FV1YquIQ/mQc5zG33fy6fUGl/Z5j3XXiCTH3Y7dc/Uyf4V3XxH0sPDZ6tGo3wN5UhA5Kk5Xn0Bz/31WFePNBnRhp8s7dzGQ+ZAPpVGRB5jcd6s2T7ofwpjrlzwK809SJ65RRRXsHhhRRRQAVxHi2PZqe8dXRWP8v6V29cJ4pmEmqSLnITCj8v8SawxPwHThV+8MVGyaiu8mEn05qRKbMu5CO1cJ6VtTz/AMZWD3mly+WuZFwyj6f5NeYRctXuWoW4ZTnkV5Fr+mnTNWYIuIZSWT29RXVhp/ZOPG09poqCl/CkXmnCu04BDRS4pMetACUUUGgApCaKQmgAJpc9KjLYpA/H0NICYVIAfSoEkZmCpge9TqmOSSTTAXtSU7pTTQAUlLSUAJRS0lABSHpS0h6ZoAbQaU0lIBD0NNpx6UgGWAFAH0Z8B9MFp4Mur8j5r27Yg/7KAKB/31vrv/E1st14a1CNh0hZx9V+YfqKyfhrYDTvhzocIH+sthcH6yEyf+zVv6uypo18z/dFvIT/AN8mpezHHRo8w07m3/Clf75p9mnl2oz1Ipr5Lkg15J7SPWaKKK9c8QKKKKAEJABJOAK8yv5mubiSU5+dixrv9auPs+k3D9yu0fjx/WvOnOW6ZrkxL1SO7Bx3kKgolHyHilT2qO5kAB5HArlOwyrsFuMdK878b24NtHNjmN+o9Dx/hXftcLNctEp+YDJHoK5nxRa+dp11EeoVmXjuOR/KtKXuzTFUXNTaOBjt1ZEKyo27oDwc+n1pjR4J9AcZ6jNSWHmPbFBD5iFt3JwMipTdXEbupCp22bANtekeMVCp+v0ptT71ZWMq7m7Ffl/lUWc53Ee2RTAjop5Q7d2DtPQjkVH9KAEpppxNMY4FICNjzTe9K1NoAmtc+eR/s1dqraQzOXeOJ2RBl2A4UE4GT25q1g+o/OgBKSnbT6j86cImY4HJPT3pgRUlTi2kKlgjEDOSFOBTvsU5OPJm9f8AVt/hQBWpO9WDZz/88Jv+/Z/wpptZh/yyk/74NAENJUphdV3GNwPUqaZsPUBj+BoER0lOIx6/lSHp1oGNPSrWl2D6rqtnp0Zw93OkCn0LsFB/WqzDpivQPg1ox1T4hWszoDFYRvdPuHGR8q/juYH/AIDSA+mreCO1toreFAkUSBEUdFUDAFc144vmhsraxQHN05LH/ZXBx+ZX9a6quP8AG0W+501/7ok/9lrKq2oOxrQSdRXOdVNkOKgOMng/nVqQ4jzUHyntXmNHqo9Wooor1zxQooooA5vxhOUtbeEdHcsfwH/164wk7gB6V0vi+QtqEMXZId34kn/CuYYHcCK4K7vNnqYZWpol3hV/nWZdz5baWwO9WpGbJA71zmvytHp17JnDCF2BBwQ2DisktTpStqLpt/ZyeITaCVDLLbjncOqE5H15/T2qbxDZHyy4UcjmvOPDGnvNrNvNEMPE4kDDgkj39+n417DrqK2mCTHAXJzxxitZrlehnTm5bo8FiF3bbo1eVVRip2kgAj6VM80kqr5jFiBwzdfz71qt4dv77U7qSwCsSxJi37WzjnHb9ao32k6hp5Vr6zntgxwGlQhSfY9D+Fd0ZxfU8qdOUW9CBSmzDgnnPDYx+lMkTZznKnofWmqCWxg8c0oLl8hsNVmYIWx/rNq+9NkQptJwwPGRTyI2kJZssfTtUJJCEfw5pAMbGcK351E/B54qVsYI/L2qFm5oAYc1qeHNAvfE2u2ulWCgzzvgFvuoByzH2ABP6Dk1ljJNfSfwQ8Ivo+gSa5eW6pd6iB5JI+dYOCPpuPP0Cn6AGP8AEfw3pvgf4YWmk6ZEA13fRLdXBUb5yqu+WP8AvAYHQcivGkbA9B0PtXvXx6Df8IzpTY+UXxBPv5b4/rXgo2jtyD+dCAlW5eIERnBP8ff8KdNdSywxIzHCr69eaikcPx5YXHcdfxp8UiJGAVjJJ/iTP5n0+lMCSLUbqCRXWZzjorMSOmOlXzcXd5bxzSXkduhbBwdpb8qzT9lFwN4fy85JRgePyq19jtn82aaYxR7sQgtglex6d6GATvcW8n2hLnz4iTjLZ68dOlVxPLKdz3UokPIO44+mKhETl5IVOcfrik8xhiOQFWAx8x4pAK8hJPnEhwcN7iommLKVHC+goEio7Hbn0qN5N/IQL9KYCOxbGeopFZhx1J9aAwIxgflzSbc/dJz34pAB3A4YDNe6fALQ0Wz1PXpM+az/AGOIZ4CgK7H8SV/75rwnI6scAdT6V9c+ANC/4RzwPpenuuJhF5s3HO9zuYH6Zx9AKAOlrG8S2H2zS2kUZkgy649Mc/4/hWzRUyV1YqMnFpo8rmceSpJxVBrj5iM9DitXVbUWtxPAOkchUfTt/Suem3CVsdK8ySs7HtU9VdHt9FFFeoeGFFFFAHnetXn2vVbskj5JDEB6Acf0NZuMwk+lP1CRRql0R/HK75/H/P51Bv8A3P1ArzZ7tns01aKRFE4kD56qxU/z/rXK+MGMekXQHUhR+bAf1rW0q7Bk1KNm/wBXcZ69ii/4GsbxQwuLGeMddu78jn+lKK1Rq9mQeBLEMTKVrvr+3E2nMgbDLkcDkA1zXgaMLYq2PWu0G1ZAW5RhtYe1OT1MU7M850eIW/iQxDowya9BRB5WCM56inazpVonhuzubWJFns8FmUY3Ho5PrkZP5VFbzCSFWweRRUVmTzc2pzureB9C1Hc5tTbSscl7Y7Of93lf0rh9Y+Ht/a5ksJ1vE/uNhHH64P5ivWJX5qk7c0QrTjsyJUYS3R4LdQXNtOYLqOSKUdUkBB/Wow6lNrcV7XqFnaX0ey6toZl7CRA2PpnpXF6v4Ls3y9jIbdv7hyyn8zkV1RxMX8Whzywkl8OpwRyRweneomJJ7VoX+k32mnM0Xyf89FOVP+H41njk8V0Jp6o5ZRcXZnR+BvCsvi/xXaaWqN5BPmXTj+CEEbjnsTkAe7CvsFEWONURQqKAFVRgAegrzH4LeDX0Dw6dZvFH2zU0V4xkHZBjK/QtnJHsvcGvUKBHk/x5SRvDWlFRmMXp3YH/AEzbB/nXgpcMVPyjb79a+p/H6btGtj6XA/8AQWrhorK0lwJbS3kH+3ErfzFYVMR7OVrHTTw/tI81zxR5FY4KKh9eeaZ5bn+En2xXvUWkaUeul2B+tqn+FObw7oTEltG07J9LVB/SpWLXYbwr7ng7KAxcASf498jqe/8AninTXBnC/uW3AYG58gfQY4r3dfCvh5hg6Jp//gMn+FObwj4df72jWn08vAqvrMexLw8u54DHLLCxKkHd94ZzmiWd3TaYwAe5r3aTwl4cQHbo1l/36GRVCbwpoHbSrf8A75pfWo9ilhZdzxJX2qVKAio2wTlVx7ZzXs7+FNCA402EeuM/41A3hfQx/wAw2D8jR9aj2D6pLueOmm54xXrsnhjRO2nQ/hmoG8L6M3/LhGPozD+RprExfQPqku5yXw/8Pt4m8a6dY7d0CuJrnjP7pCCw/E4X/gVfW1ed/DDwvYaUt5qlvbLHJLiBW3MSFHzN1J6nb/3zXolbJ3VzmlHldgooopiPOfFMnl6zeZ6blOf+ALXJyTKJG5712Hiu0STW7guwUsquM+m0D+lcZJCjSMd7N77ic/rXn1ElJntUHeC9D3WiiivQPFCg9KKa4JRgOuKAPIb2CQo7uGV9vOeCp75qm1yyIVC8EALjvWtr92oa/YEbhI+fwJH9KybdA2jwSk5LQqf/AB0V5r7HuRV4pnL6Tds+t6rAH+Ztjj6AkE/qtSa2j/ZZG9UbPOeMGqGh5XxpdSY+URlWPsSMf0rodcRDYXJA6Qtj8qpu1hrW5c8FpjTUJ711ZORXOeE49mlRV0LE1nLcxe4eYE068s9uUnGev3Wxgn8Rj8hVK3UxRbT2qyxqFj1pN3BDJG4qnI3vU8relUpXqSkQStk1nXJzVyRuDWdO3WmaIzpwCcHn60/w98P7XxV4igh2mCFT5twUGA0YIyMepyB+JPOKXbuevZPh/oQ0rQxdSKRc3gDtnsgzsH5En8fauqgnc5cU0oanWqqoiooCqowABgAelLRRXYeYcx44XdpMH/Xb/wBlNcTBxXc+NBnSof8Artj/AMdNcREMVwYr4j0sL/DLsZqwDn1qunHNSqa5kbMnU5FSFsA81FH0pzHjrVEsilaqUjVZk4BqrKe3WgpFaRqrN1qeQ81XagZE3XApVjLuFAyT2petb/hGwF3rSO67o4B5hz6j7v64P4VrTjdmdSfKmz0DTLNdP0y3tFx+7QBiOhbufxOat0UV6J5W4VR1XU4tLtDNINznhEBwWNXq4/xXPDdzRQxyLI0WeI/4SeoY9PwHPHbOaicrK5pSgpTSZHod0ms6jeXV2sctwqhQNnCqQeBnp0P51zWvW9vFrdyiC1jGQdpYLjIB6VvaE4scMUyHTBVOn64HrWNrGnafqOqz3cxCO5GVJ54AA/QVztpx13PQgrVHbY9UooorrPLCmyOsUTyOcKoJJ9hTqoatIFto48/66ZI/qCef0BpN2Q0rux5H4r+02NnM13buk8iNIVOMkEnP5mq6eba6bFaSctHEELL0JCgE/Tiun8YH+0NaisdpMUIjlkf+7gk7ffOF49M+2c7xHpN3pGmeZNJE0bKzyFRgpjseefwrhlHV26HswnpFPdnK+GLFZ7jXLxl4wFU+6rn+YFWNUA/s2625IETD5h6it7wdZeT4eilZP3lzmZlP+0S2P1rV+IJjngtbdF3XF8NkRA7KVJyfTDGkleLfYXPadu5meHoTFpMIIxgZrTJ5psEIt7dIx/CMU6sWyeownioHPNSscGq0jYNAyGVuaozNzVmVsGqErcmgtEMrEVQlJbpVqZ+KgRC7ACmimzZ8H6Ada1uJZEDWsREk5PQqOi/j0+mfSvbAAoAAAA4AHasHwjoQ0PRlWRNt1P8APN0yPRc+w/Un1rfr0aUOWJ5Fepzz8gooorQxOc8Zf8gyH/rt/wCymuITrXb+Mf8AkGQ/9df6GuITGfxrz8V8R6OF+Atr0qZRUKVMPWudG7JVPrTm6VGDjFDNxVkkUhqpJViQ1WkI6UikVZDzUDdKmc81CaaGCjNeh+C7IQaU9yfvXDcf7q5H881wUELzSxwxjLuwVR6k8CvW7O2W0s4bdOViQICe+B1rsox1ucWJlpYnooorpOMz9amkh0mdoX2SkYU5wevOPfGa42GFYtqk/M33cj9celdxfwtc2xhEe7fwTkfL7/WqA0RFVVABJ6yd+v8An29hWNSLk9Dpo1YwjZnPvHKzrYWag3Uvr0Re5J7D/PtWzbeD9LS3RbuJrm4x+8mMjruP0DYA/wA81sWlnHaIQgyzfeY9TViqhTS3JqVnLbYKKKK0MArl/FV1tmt4VJBQbyffPH8v1rqK4bxGSdZnyem0D/vkVjiJWgb4ZXqGWi4keVtzO7bmZiSScdyfYUvjGU6tqtto9vJG9vNEzXbqc7Y9y/KMdC2CPp9c0q8AU9VUOW2rubAJxya4oTaTXc9BrVPsTxRpFGsaABVAAFR3cX2qa2klYsbYMIl4wu7AP8hUp4UUx+lDYvMhbqajzTz1qJjUDI3NVJDVh+hqnKeaRSK8rdaoSNyatTE1TkpotED/ADGuu8CaCNQ1L7bMp8i1IYf7Ug5Ufh1/L1rk0ALDNe0eFraG28OWYhjCeZGJHx3Y9Sa6KEE5XZzYqo4wsupsUUUV3HmBRRRQBz3jEf8AEoiPpMP/AEFq4ZOCa7rxh/yB4/8AruP/AEFq4Uda4MV8R6OF+Atp0p4ODUcfKipe9c6OhjgeKU5Ipnr7Up6UySF+tVZDycdqtS1SfqaCkQOajXk09+9JH1q4rUTN/wAJWZuddjkxlIAZGz69APzOfwr0euN8BqP9POOf3f8A7NXZV30laJ5lZ3mFFFFaGQUUUUAFFFFAH//Z");
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "imageSearch---" + parametersToCall);

        return parametersToCall;
    }

    public static String getDeliveryServices(Activity context, String categoryId, String searchKey) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400873");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.93", Utils.lengtT(11, categoryId));
            obj_param.put("120.38", String.valueOf(App.latitude));
            obj_param.put("120.39", String.valueOf(App.longitude));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("127.89", Client.getWeekDay());
            if (!searchKey.isEmpty())
                obj_param.put("114.127", Utils.convertStringToHex(searchKey));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getDeliveryServices---" + parametersToCall);

        return parametersToCall;
    }

    public static String getBrandNames(Activity context, String categoryId, String searchKey, String merchantId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400808");
            JSONObject obj_param = new JSONObject();
            if (!categoryId.isEmpty())
                obj_param.put("114.93", Utils.lengtT(11, categoryId));
            if (!searchKey.isEmpty())
                obj_param.put("114.127", Utils.convertStringToHex(searchKey));
            if (!merchantId.isEmpty())
                obj_param.put("114.179", merchantId);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getBrandNames---" + parametersToCall);

        return parametersToCall;
    }

    public static String getRatingList(Activity context, String categoryId, String searchKey, String merchantId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400890");
            JSONObject obj_param = new JSONObject();
            if (!categoryId.isEmpty())
                obj_param.put("114.93", Utils.lengtT(11, categoryId));
            if (!searchKey.isEmpty())
                obj_param.put("114.127", Utils.convertStringToHex(searchKey));
            if (!merchantId.isEmpty())
                obj_param.put("114.179", merchantId);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getRatingList---" + parametersToCall);

        return parametersToCall;
    }

    public static String getSearchMerchantList(Activity context, String categoryId, String searchKey, String orderBy,
                                               String parentId
            , String deliveryServiceName, String zipCode) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400869");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.93", Utils.lengtT(11, categoryId));
            obj_param.put("120.38", String.valueOf(App.latitude));
            obj_param.put("120.39", String.valueOf(App.longitude));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("127.89", Client.getWeekDay());
            obj_param.put("127.60", orderBy);
            if (!deliveryServiceName.isEmpty())
                obj_param.put("122.39", deliveryServiceName);
            if (!zipCode.isEmpty())
                obj_param.put("47.17", zipCode);
            if (!searchKey.isEmpty())
                obj_param.put("114.127", searchKey);
            if (!parentId.isEmpty())
                obj_param.put("122.21", Utils.lengtT(11, parentId));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getSearchMerchantList---" + parametersToCall);

        return parametersToCall;
    }

    public static String getItemsList(Activity context, String categoryId, String searchKey, String orderBy, String parentId
            , String deliveryServiceName, String merchantId, String selectedConditionId, String zipCode) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400872");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.93", Utils.lengtT(11, categoryId));
            obj_param.put("120.38", String.valueOf(App.latitude));
            obj_param.put("120.39", String.valueOf(App.longitude));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("127.89", Client.getWeekDay());
            obj_param.put("127.60", orderBy);
            obj_param.put("120.40", selectedConditionId);
            // if (!deliveryServiceName.isEmpty())
            obj_param.put("122.39", deliveryServiceName);
            if (!zipCode.isEmpty())
                obj_param.put("120.156", Utils.convertStringToHex(zipCode));
            if (!merchantId.isEmpty())
                obj_param.put("114.179", merchantId);
            if (!searchKey.isEmpty())
                obj_param.put("114.127", Utils.convertStringToHex(searchKey));
            if (!parentId.isEmpty())
                obj_param.put("122.21", Utils.lengtT(11, parentId));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getItemsList---" + parametersToCall);

        return parametersToCall;
    }

    public static String getCategoriesItemsList(Activity context, String categoryId, String searchKey, String orderBy,
                                                String parentId
            , String deliveryServiceName, String merchantId, String zipCode, String brandName, String ratingId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            JSONObject obj_param = new JSONObject();

            obj1.put("101", "010400478");
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("120.38", String.valueOf(App.latitude));
            obj_param.put("120.39", String.valueOf(App.longitude));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("127.89", Client.getWeekDay());
            obj_param.put("127.60", orderBy);
            if (!categoryId.isEmpty())
                obj_param.put("114.93", Utils.lengtT(11, categoryId));

            if (!brandName.isEmpty())
                obj_param.put("122.39", brandName);
            if (!ratingId.isEmpty())
                obj_param.put("122.39", ratingId);
            if (!deliveryServiceName.isEmpty())
                obj_param.put("122.39", deliveryServiceName);
            if (!zipCode.isEmpty())
                obj_param.put("47.17", zipCode);
            if (!merchantId.isEmpty())
                obj_param.put("114.179", merchantId);
            if (!searchKey.isEmpty())
                obj_param.put("114.127", Utils.convertStringToHex(searchKey));
            if (!parentId.isEmpty())
                obj_param.put("122.21", Utils.lengtT(11, parentId));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getCategoriesItemsList---" + parametersToCall);

        return parametersToCall;
    }

    public static String getCategoriesSpecialsList(Activity context, String categoryId, String searchKey, String orderBy, String parentId
            , String deliveryServiceName, String merchantId, String zipCode) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400479");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("120.38", String.valueOf(App.latitude));
            obj_param.put("120.39", String.valueOf(App.longitude));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("127.89", Client.getWeekDay());
            obj_param.put("127.60", orderBy);

            if (!categoryId.isEmpty())
                obj_param.put("114.93", Utils.lengtT(11, categoryId));
            if (!deliveryServiceName.isEmpty())
                obj_param.put("122.39", deliveryServiceName);
            if (!zipCode.isEmpty())
                obj_param.put("47.17", zipCode);
            if (!merchantId.isEmpty())
                obj_param.put("114.179", merchantId);
            if (!searchKey.isEmpty())
                obj_param.put("114.127", Utils.convertStringToHex(searchKey));
            if (!parentId.isEmpty())
                obj_param.put("122.21", Utils.lengtT(11, parentId));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getCategoriesSpecialsList---" + parametersToCall);

        return parametersToCall;
    }

    public static String getAdsList(Activity context, String categoryId, String searchKey, String orderBy, String parentId
            , String deliveryServiceName, String merchantId, String zipCode) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400870");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.93", Utils.lengtT(11, categoryId));
            obj_param.put("120.38", String.valueOf(App.latitude));
            obj_param.put("120.39", String.valueOf(App.longitude));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("127.89", Client.getWeekDay());
            obj_param.put("127.60", orderBy);
            obj_param.put("122.39", deliveryServiceName);
            if (!zipCode.isEmpty())
                obj_param.put("120.156", Utils.convertStringToHex(zipCode));
            if (!merchantId.isEmpty())
                obj_param.put("114.179", merchantId);
            if (!searchKey.isEmpty())
                obj_param.put("114.127", Utils.convertStringToHex(searchKey));
            if (!parentId.isEmpty())
                obj_param.put("122.21", Utils.lengtT(11, parentId));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getAdsList---" + parametersToCall);

        return parametersToCall;
    }

    public static String getSpecialsList(Activity context, String categoryId, String searchKey, String orderBy, String parentId
            , String deliveryServiceName, String merchantId, String zipCode) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400871");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.93", Utils.lengtT(11, categoryId));
            obj_param.put("120.38", String.valueOf(App.latitude));
            obj_param.put("120.39", String.valueOf(App.longitude));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("127.89", Client.getWeekDay());
            obj_param.put("127.60", orderBy);
            obj_param.put("122.39", deliveryServiceName);
            if (!zipCode.isEmpty())
                obj_param.put("120.156", Utils.convertStringToHex(zipCode));
            if (!merchantId.isEmpty())
                obj_param.put("114.179", merchantId);
            if (!searchKey.isEmpty())
                obj_param.put("114.127", Utils.convertStringToHex(searchKey));
            if (!parentId.isEmpty())
                obj_param.put("122.21", Utils.lengtT(11, parentId));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getSpecialsList---" + parametersToCall);

        return parametersToCall;
    }

    public static String getMerchantCategoryList(Activity context, String categoryId, String searchKey
            , String deliveryServiceName, String categoryParentId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400874");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("120.38", String.valueOf(App.latitude));
            obj_param.put("120.39", String.valueOf(App.longitude));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("127.89", Client.getWeekDay());
            if (!searchKey.isEmpty())
                obj_param.put("114.127", Utils.convertStringToHex(searchKey));
            if (!deliveryServiceName.isEmpty())
                obj_param.put("122.39", deliveryServiceName);
            if (!categoryParentId.isEmpty())
                obj_param.put("122.21", Utils.lengtT(11, categoryParentId));
            if (!categoryId.isEmpty())
                obj_param.put("114.93", Utils.lengtT(11, categoryId));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getMerchantCategoryList---" + parametersToCall);

        return parametersToCall;
    }

    public static String getLocationByMerchant(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400645");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("120.38", String.valueOf(App.latitude));
            obj_param.put("120.39", String.valueOf(App.longitude));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("127.89", Client.getWeekDay());
//            obj_param.put("127.60", "120.11-ASC");
//            if (!searchKey.isEmpty())
//                obj_param.put("114.127", Utils.convertStringToHex(searchKey));
//            if (!deliveryServiceName.isEmpty())
//                obj_param.put("122.39", deliveryServiceName);
//            if (!categoryParentId.isEmpty())
//                obj_param.put("122.21", Utils.lengtT(11, categoryParentId));
//            if (!categoryId.isEmpty())
//                obj_param.put("114.93", Utils.lengtT(11, categoryId));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getLocationByMerchant---" + parametersToCall);

        return parametersToCall;
    }

    public static String getItemCategoryList(Activity context, String categoryId, String searchKey
            , String deliveryServiceName, String categoryParentId, String merchantId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400877");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("120.38", String.valueOf(App.latitude));
            obj_param.put("120.39", String.valueOf(App.longitude));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("127.89", Client.getWeekDay());
            if (!searchKey.isEmpty())
                obj_param.put("114.127", Utils.convertStringToHex(searchKey));
            if (!merchantId.isEmpty())
                obj_param.put("114.179", merchantId);
            if (!deliveryServiceName.isEmpty())
                obj_param.put("122.39", deliveryServiceName);
            if (!categoryParentId.isEmpty())
                obj_param.put("122.21", Utils.lengtT(11, categoryParentId));
            if (!categoryId.isEmpty())
                obj_param.put("114.93", Utils.lengtT(11, categoryId));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getItemCategoryList---" + parametersToCall);

        return parametersToCall;
    }

    public static String getAdsByCategoryList(Activity context, String categoryId, String searchKey,
                                              String orderBy, String parentId
            , String merchantId, String zipCode) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400677");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.93", Utils.lengtT(11, categoryId));
            obj_param.put("120.38", String.valueOf(App.latitude));
            obj_param.put("120.39", String.valueOf(App.longitude));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("127.89", Client.getWeekDay());
            obj_param.put("127.60", orderBy);
            obj_param.put("EXPECTED", "ALL");

            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("114.179", merchantId);
            obj2.put("operator", "eq");
            arr1.put(obj2);

            if (!zipCode.isEmpty())
                obj_param.put("120.156", Utils.convertStringToHex(zipCode));
/*
            if (!merchantId.isEmpty())
                obj_param.put("114.179", merchantId);
*/
            if (!searchKey.isEmpty())
                obj_param.put("114.127", Utils.convertStringToHex(searchKey));
            if (!parentId.isEmpty())
                obj_param.put("122.21", Utils.lengtT(11, parentId));
            obj1.put("PARAM", obj_param);
            obj1.put("FILTER", arr1);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getAdsByCategoryList---" + parametersToCall);

        return parametersToCall;
    }


    public static String getAdsCategoryList(Activity context, String categoryId, String searchKey
            , String deliveryServiceName, String categoryParentId, String merchantId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400875");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("120.38", String.valueOf(App.latitude));
            obj_param.put("120.39", String.valueOf(App.longitude));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("127.89", Client.getWeekDay());
            if (!merchantId.isEmpty())
                obj_param.put("114.179", merchantId);
            if (!searchKey.isEmpty())
                obj_param.put("114.127", Utils.convertStringToHex(searchKey));
            if (!deliveryServiceName.isEmpty())
                obj_param.put("122.39", deliveryServiceName);
            if (!categoryParentId.isEmpty())
                obj_param.put("122.21", Utils.lengtT(11, categoryParentId));
            if (!categoryId.isEmpty())
                obj_param.put("114.93", Utils.lengtT(11, categoryId));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getAdsCategoryList---" + parametersToCall);

        return parametersToCall;
    }

    public static String getSpecialsCategoryList(Activity context, String categoryId, String searchKey
            , String deliveryServiceName, String categoryParentId, String merchantId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400876");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("120.38", String.valueOf(App.latitude));
            obj_param.put("120.39", String.valueOf(App.longitude));
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("127.89", Client.getWeekDay());
            if (!merchantId.isEmpty())
                obj_param.put("114.179", merchantId);
            if (!searchKey.isEmpty())
                obj_param.put("114.127", Utils.convertStringToHex(searchKey));
            obj_param.put("122.39", deliveryServiceName);
            if (!categoryParentId.isEmpty())
                obj_param.put("122.21", Utils.lengtT(11, categoryParentId));
            if (!categoryId.isEmpty())
                obj_param.put("114.93", Utils.lengtT(11, categoryId));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getSpecialsCategoryList---" + parametersToCall);

        return parametersToCall;
    }

    public static String getMessageProduct(Activity context, String productId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400265");
            JSONObject obj_param = new JSONObject();
            obj_param.put("114.144", productId);
            obj_param.put("114.179", ATPreferences.readString(context, Constants.KEY_USERID));
            //  JSONArray arr1 = new JSONArray();
            //JSONObject obj2 = new JSONObject();
            //obj2.put("114.179", ATPreferences.readString(context, Constants.KEY_USERID));
            //obj2.put("operator", "eq");
            //arr1.put(obj2);
            // obj1.put("FILTER", arr1);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getproductmsg_api---" + parametersToCall);
        Logger.addRecordToLog("itemsMessageRequest" + parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonGetMessagesDetail(Activity context, String mmessageId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400265");
            JSONObject obj_param = new JSONObject();
            obj_param.put("114.9", "false");
            obj1.put("PARAM", obj_param);
            obj1.put("ORDER", "122.114-ASC");
            obj_param.put("114.179", ATPreferences.readString(context, Constants.KEY_USERID));
            //obj_param.put("53", merchantId);
            obj_param.put("114.150", mmessageId);
            // JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            //obj2.put("53", merchantId);
            obj2.put("operator", "eq");
            JSONObject obj3 = new JSONObject();
            obj3.put("120.16", "92");
            obj3.put("operator", "eq");

            //JSONObject obj4 = new JSONObject();
            // obj4.put("114.150", mmessageId);
            //obj4.put("operator", "eq");
            //arr1.put(obj2);
            // arr1.put(obj3);
            //   arr1.put(obj4);
            //  obj1.put("FILTER", arr1);
            obj1.put("EXPECTED", "ALL");

            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "makeJsonGetMessagesDetail---" + parametersToCall);

        return parametersToCall;
    }

    public static String searchStoresNearByGPS(Activity context) {
        String parametersToCall = "";
        String latitude = String.valueOf(App.latitude);
        String longitude = String.valueOf(App.longitude);
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400239");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("120.38", latitude);
            obj_param.put("120.39", longitude);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "RequestsearchStoresNearByGPS---" + parametersToCall);

        return parametersToCall;
    }

    public static String getHeaderCategory(Activity context, String merchantId) {
        String parametersToCall = "";
        String latitude = String.valueOf(App.latitude);
        String longitude = String.valueOf(App.longitude);
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400908");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.179", merchantId);
            obj_param.put("121.141", Client.getDateTimeStamp());
            obj_param.put("127.89", Client.getWeekDay());
            obj_param.put("120.38", latitude);
            obj_param.put("120.39", longitude);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "RequestgetHeaderCategory---" + parametersToCall);

        return parametersToCall;
    }

    public static String getMessageAd(Activity context, String productId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400265");
            JSONObject obj_param = new JSONObject();
            obj_param.put("123.21", productId);
            obj_param.put("114.179", ATPreferences.readString(context, Constants.KEY_USERID));
            //  JSONArray arr1 = new JSONArray();
            //JSONObject obj2 = new JSONObject();
            //obj2.put("114.179", ATPreferences.readString(context, Constants.KEY_USERID));
            //obj2.put("operator", "eq");
            //arr1.put(obj2);
            // obj1.put("FILTER", arr1);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getproductmsg_api---" + parametersToCall);
        Logger.addRecordToLog("itemsMessageRequest" + parametersToCall);
        return parametersToCall;
    }

    public static String addshoppingList(Activity context, String itemName) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030400427");
            JSONObject obj_param = new JSONObject();
            obj_param.put("120.157", itemName);
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "addshoppingList---" + parametersToCall);

        return parametersToCall;
    }

    public static String editshoppingList(Activity context, String itemName, String List_Id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "020400428");
            JSONObject obj_param = new JSONObject();
            obj_param.put("120.157", itemName);
            obj_param.put("122.31", List_Id);
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "editshoppingList---" + parametersToCall);

        return parametersToCall;
    }

    public static String deleteShoppingList(Activity context, String List_Id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "020400429");
            JSONObject obj_param = new JSONObject();
            obj1.put("PARAM", obj_param);
            obj_param.put("122.31", List_Id);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "deleteShoppingList---" + parametersToCall);

        return parametersToCall;
    }

    public static String getShoppingAssistantList(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100430");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "getShoppingList---" + parametersToCall);

        return parametersToCall;
    }

    public static String addItemToShoppingList(Activity context, String Shopping_Id, String product_name, String item_id, boolean b) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030400413");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("122.31", Shopping_Id);
            obj_param.put("120.83", product_name);
            if (b)
                obj_param.put("122.17", Utils.getElevenDigitId(item_id));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "addItemToShoppingList---" + parametersToCall);

        return parametersToCall;
    }

    public static String removeItemToShoppingList(Activity context, String Shopping_Id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "020400414");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("122.17", Utils.getElevenDigitId(Shopping_Id));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "removeItemToShoppingList---" + parametersToCall);

        return parametersToCall;
    }


    public static String sendMessageInvoice(Activity context, String invoiceId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400265");
            JSONObject obj_param = new JSONObject();
//            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("121.75", invoiceId);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getproductmsg_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String sendMessageReply(Activity context, String parentId, String msgId, String merchantId,
                                          String type, String subject, String message, String invoiceId, String Id,
                                          String productId, String lastMessageId, JSONArray image
            , String locationId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030300192");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.179", merchantId);
            obj_param.put("121.141", Utils.GetToday());

            obj_param.put("122.128", Utils.convertStringToHex(subject));
            obj_param.put("120.157", message);
            if (image.length() > 0)
                obj_param.put("MI", image);

            if (msgId.equals("2"))
                obj_param.put("114.144", productId);
            else if (msgId.equals("1"))
                obj_param.put("121.75", invoiceId);
            else if (msgId.equals("4"))
                obj_param.put("123.21", productId);

            if (msgId.equals("1") || msgId.equals("2") || msgId.equals("4")) {
                obj_param.put("114.9", "false");
                obj_param.put("120.16", "92");

            } else {
                obj_param.put("114.150", Utils.getElevenDigitId(parentId));
                // obj_param.put("120.16", type);
                obj_param.put("120.16", "92");
            }
            if (lastMessageId != null && !lastMessageId.isEmpty())
                obj_param.put("114.150", Utils.getElevenDigitId(lastMessageId));

          /*  if (parentId.equals("0"))
                obj_param.put("114.150", Utils.getElevenDigitId(Id));
            else
                obj_param.put("114.150", Utils.getElevenDigitId(parentId));*/

            if (!locationId.isEmpty())
                obj_param.put("114.47", Utils.getElevenDigitId(locationId));

            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_replymsg_api---" + parametersToCall);
        Logger.addRecordToLog("parameters_replymsg_api     " + parametersToCall);
        return parametersToCall;
    }

    public static String sendMessageReportAbuse(Activity context,
                                                String subject, String message, String id, String merchantId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030300192");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("120.16", "97");
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("122.128", Utils.convertStringToHex(subject));
            obj_param.put("120.157", Utils.convertStringToHex(message));
            obj_param.put("122.25", "85009");
            obj_param.put("120.21", Utils.getElevenDigitId(id));
            obj_param.put("114.179", merchantId);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_replymsg_api---" + parametersToCall);
        Logger.addRecordToLog("parameters_replymsg_api     " + parametersToCall);
        return parametersToCall;
    }

    public static String sendMessageAboutUs(Activity context,
                                            String subject, String message, String id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030300192");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("120.16", "97");
            obj_param.put("121.141", Utils.GetToday());
            obj_param.put("122.128", Utils.convertStringToHex(subject));
            obj_param.put("120.157", Utils.convertStringToHex(message));
            obj_param.put("122.25", Utils.getElevenDigitId(id));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_replymsg_api---" + parametersToCall);
        Logger.addRecordToLog("parameters_replymsg_api     " + parametersToCall);
        return parametersToCall;
    }

    public static String makeProductSeen(Activity context, String productId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030400471");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.144", productId);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_seen_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonRemoveFavourite(Activity context, String id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "040400219");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.144", id);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_remove_favourite_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetAddress(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100055");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_address_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonAddAddress(boolean isNew, Activity context, String type, String line1, String line2,
                                            String mob, String country, String state, String city, String pin, String id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            if (isNew)
                obj1.put("101", "030400056");
            else
                obj1.put("101", "020400133");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.53", type);
            obj_param.put("114.12", line1);
            obj_param.put("114.13", line2);
            obj_param.put("114.18", mob);
            obj_param.put("122.87", country);
            obj_param.put("120.13", state);
            obj_param.put("114.14", city);
            obj_param.put("122.107", pin);
            if (isNew) {
                obj_param.put("120.38", "");
                obj_param.put("120.39", "");
                obj_param.put("121.45", "00000000000");
            } else {
                obj_param.put("114.115", id);
            }
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_add_address_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetCountry(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100232");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getcountry_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetFCM(Activity context, String token) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030400008");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("57", Utils.getDeviceId(context));
            obj_param.put("122.12", token);
            obj_param.put("48.6", Utils.getDeviceName());
            obj_param.put("127.30", "84001");

            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_fcm_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetTermsConditions(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100812");
            JSONObject obj_param = new JSONObject();
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_terms_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetState(Activity context, String countrycode) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100233");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("122.87", countrycode);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getstate_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetCity(Activity context, String statecode) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100234");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("120.13", statecode);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getcity_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetPINCode(Activity context, String citycode) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100235");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.14", citycode);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getpincode_api---" + parametersToCall);


        return parametersToCall;
    }


    public static String getProductCategoriesMobile(Activity context, boolean isFilter, String selectedFilter, String selectedGroup, String merchantId) {
        String parametersToCall = "";
        String str101 = "";
        if (merchantId.isEmpty())
            str101 = "010200724";
        else
            str101 = "010200714";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", str101);
            JSONObject obj_param = new JSONObject();
            obj_param.put("120.44", selectedFilter);
            if (!merchantId.isEmpty())
                obj_param.put("53", merchantId);
            if (isFilter) {
                JSONArray arr1 = new JSONArray();
                JSONObject obj2 = new JSONObject();
                obj2.put("122.21", Utils.lengtT(11, selectedGroup));
                obj2.put("operator", "eq");
                arr1.put(obj2);
                obj1.put("FILTER", arr1);
            }
            obj1.put("PARAM", obj_param);
            obj1.put("ORDER", "114.93-ASC");
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "getProductCategoriesMobile_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String getBusiness(Activity context, String selectedFilter) {
        String parametersToCall = "";
        String str101 = "";
        str101 = "010200802";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", str101);
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            if (!selectedFilter.isEmpty()) {
                JSONArray arr1 = new JSONArray();
                JSONObject obj2 = new JSONObject();
                obj2.put("114.93", selectedFilter);
                obj2.put("operator", "eq");
                arr1.put(obj2);
                obj1.put("FILTER", arr1);
                obj1.put("EXPECTED", "ALL");
            }
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "getProductCategoriesMobile_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String getProductCategoriesMobileMerchant(Activity context, boolean isFilter, String selectedFilter, String selectedGroup, String merchantId) {
        String parametersToCall = "";
        String str101 = "";
        if (merchantId.isEmpty())
            str101 = "010200724";
        else
            str101 = "010200714";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", str101);
            JSONObject obj_param = new JSONObject();
            obj_param.put("120.44", selectedFilter);
            if (!merchantId.isEmpty())
                obj_param.put("53", merchantId);
            if (isFilter) {
                JSONArray arr1 = new JSONArray();
                JSONObject obj2 = new JSONObject();
                obj2.put("122.21", Utils.lengtT(11, selectedGroup));
                obj2.put("operator", "eq");
                arr1.put(obj2);
                obj1.put("FILTER", arr1);
            }
            obj1.put("PARAM", obj_param);
            obj1.put("ORDER", "114.93-ASC");
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "getProductCategoriesMobile_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonForHome(Activity context, String order) {
        String parametersToCall = "";
        try {
            String latitude = String.valueOf(App.latitude);
            String longitude = String.valueOf(App.longitude);

            JSONObject obj = new JSONObject();
            obj.put("192", ATPreferences.readString(context, Constants.KEY_USER_DEFAULT));
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", String.valueOf(App.latitude));
            obj.put("120.39", String.valueOf(App.longitude));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            JSONObject obj2 = new JSONObject();
            JSONObject obj3 = new JSONObject();
            JSONObject obj_param = new JSONObject();
            JSONObject object2 = new JSONObject();
            JSONObject obj_param2 = new JSONObject();
            JSONObject obj_param3 = new JSONObject();
            JSONObject obj_param4 = new JSONObject();


            //For Stores
            //  object2.put("101", "010400239");
            object2.put("101", "010400645");
            obj_param.put("127.60", "120.11-ASC");
            obj_param.put("120.38", latitude);
            obj_param.put("120.39", longitude);
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            object2.put("PARAM", obj_param);

            //For Specials
            obj1.put("101", "010100710");
            obj_param2.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param2.put("121.141", Client.getDateTimeStamp());
            obj_param2.put("127.89", Client.getWeekDay());
            obj1.put("ORDER", order);
            obj1.put("PARAM", obj_param2);


            //For Address List
            obj2.put("101", "010100055");
            obj_param3.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj2.put("PARAM", obj_param3);

            //For ads
            obj3.put("101", "010400676");
            obj_param4.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param4.put("114.127", "");
            obj3.put("EXPECTED", "ALL");
            obj3.put("PARAM", obj_param4);

            arr.put(obj1);  // 2 seconds
            arr.put(object2); // 3 seconds
            arr.put(obj2);   // 4 seconds
            arr.put(obj3); //12 seconds

            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getHome_api---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonGetMerchantDetail(Activity context, String id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", ATPreferences.readString(context, Constants.KEY_USER_DEFAULT));
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", String.valueOf(App.latitude));
            obj.put("120.39", String.valueOf(App.longitude));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            JSONObject obj2 = new JSONObject();
            JSONObject obj3 = new JSONObject();
            JSONObject obj_param = new JSONObject();
            JSONObject object2 = new JSONObject();
            JSONObject obj_param2 = new JSONObject();
            JSONObject obj_param3 = new JSONObject();
            JSONObject obj_param4 = new JSONObject();

            object2.put("101", "030400683");  //add store checkin
            obj_param2.put("114.179", id);
            obj_param2.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            object2.put("PARAM", obj_param2);

            obj1.put("101", "010100020");   // get business details
            obj_param.put("53", id);
            obj_param.put("114.179", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("EXPECTED", "114.9");
            obj1.put("EXPECTED", "127.66");
            obj1.put("PARAM", obj_param);


            obj2.put("101", "010400094");   // get Merchant Favourite
            obj_param3.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param3.put("127.10", "001");
            obj2.put("PARAM", obj_param3);

            obj3.put("101", "010200714");  // Browse Merchant Categories
            obj_param4.put("53", id);
            obj_param4.put("120.44", "1");
            obj3.put("PARAM", obj_param4);

            arr.put(obj1);
            // arr.put(object2);
            //arr.put(obj2);
            //arr.put(obj3);

            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getmerchantdetail_api---" + parametersToCall);
        return parametersToCall;
    }


    public static String makeJsonForStoreFrontItems(Activity context, String merchantId, String orderBy) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", ATPreferences.readString(context, Constants.KEY_USER_DEFAULT));
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", String.valueOf(App.latitude));
            obj.put("120.39", String.valueOf(App.longitude));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            JSONObject obj2 = new JSONObject();
            JSONObject obj3 = new JSONObject();
            JSONObject obj_param = new JSONObject();
            JSONObject object2 = new JSONObject();
            JSONObject objectShowCase = new JSONObject();
            JSONObject objectShowCaseTitle = new JSONObject();
            JSONObject obj_param2 = new JSONObject();
            JSONObject obj_param3 = new JSONObject();
            JSONObject obj_param4 = new JSONObject();
            JSONObject obj_paramShowCase = new JSONObject();
            JSONObject obj_paramShowCaseTitle = new JSONObject();

            JSONObject jsonObjectStoreCheckin = new JSONObject();
            JSONObject jsonObjectStoreCheckinParam = new JSONObject();

            JSONObject jsonObjectBusinessDetails = new JSONObject();
            JSONObject jsonObjectBusinessDetailsParam = new JSONObject();

            JSONObject jsonObjectMerchantFav = new JSONObject();
            JSONObject jsonObjectMerchantFavParam = new JSONObject();

            JSONObject jsonObjectMerchantCategory = new JSONObject();
            JSONObject jsonObjectMerchantCategoryParam = new JSONObject();

            jsonObjectStoreCheckin.put("101", "030400683");  //add store checkin
            jsonObjectStoreCheckinParam.put("114.179", merchantId);
            jsonObjectStoreCheckinParam.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            jsonObjectStoreCheckin.put("PARAM", jsonObjectStoreCheckinParam);

            jsonObjectBusinessDetails.put("101", "010100020");   // get business details
            jsonObjectBusinessDetailsParam.put("53", merchantId);
            jsonObjectBusinessDetailsParam.put("114.179", ATPreferences.readString(context, Constants.KEY_USERID));
            jsonObjectBusinessDetails.put("EXPECTED", "114.9");
            jsonObjectBusinessDetails.put("EXPECTED", "127.66");
            jsonObjectBusinessDetails.put("PARAM", jsonObjectBusinessDetailsParam);


            jsonObjectMerchantFav.put("101", "010400094");   // get Merchant Favourite
            jsonObjectMerchantFavParam.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            jsonObjectMerchantFavParam.put("127.10", "001");
            jsonObjectMerchantFav.put("PARAM", jsonObjectMerchantFavParam);

            jsonObjectMerchantCategory.put("101", "010200714");  // Browse Merchant Categories
            jsonObjectMerchantCategoryParam.put("53", merchantId);
            jsonObjectMerchantCategoryParam.put("120.44", "1");
            jsonObjectMerchantCategory.put("PARAM", jsonObjectMerchantCategoryParam);

            //For Showcase
            objectShowCase.put("101", "010100900");
            obj_paramShowCase.put("53", merchantId);
            objectShowCase.put("PARAM", obj_paramShowCase);


            //For Showcase Title
            objectShowCaseTitle.put("101", "010100916");
            obj_paramShowCaseTitle.put("53", merchantId);
            objectShowCaseTitle.put("PARAM", obj_paramShowCaseTitle);

            //For Items
            object2.put("101", "010100709");
            obj_param.put("121.141", Client.getDateTimeStamp());
            obj_param.put("127.89", Client.getWeekDay());
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            object2.put("ORDER", orderBy);
            object2.put("PARAM", obj_param);

            JSONArray arr1 = new JSONArray();
            JSONObject object = new JSONObject();

            object.put("114.179", merchantId);
            object.put("operator", "eq");
            arr1.put(object);
            object2.put("FILTER", arr1);


            //For Specials
            obj1.put("101", "010100710");
            obj_param2.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param2.put("121.141", Client.getDateTimeStamp());
            obj_param2.put("127.89", Client.getWeekDay());
            obj1.put("ORDER", orderBy);
            obj1.put("PARAM", obj_param2);

            JSONArray arr2 = new JSONArray();
            JSONObject object12 = new JSONObject();

            object12.put("114.179", merchantId);
            object12.put("operator", "eq");
            arr2.put(object12);
            obj1.put("FILTER", arr2);


            //For Ads Listing
            obj2.put("101", "010400677");

            obj_param3.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param3.put("114.127", "");
            obj2.put("EXPECTED", "ALL");

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("114.179", merchantId);
            jsonObject.put("operator", "eq");
            jsonArray.put(jsonObject);


            obj2.put("PARAM", obj_param3);
            obj2.put("FILTER", jsonArray);


            //adding into one
            arr.put(objectShowCase);  // for showcase
            arr.put(objectShowCaseTitle);  // for showcase title
            //arr.put(obj1); //SPECIALS
            //arr.put(object2);  //ITEMS
            arr.put(obj2);  // for ads
            arr.put(jsonObjectStoreCheckin);
            arr.put(jsonObjectBusinessDetails);
            arr.put(jsonObjectMerchantFav);
            arr.put(jsonObjectMerchantCategory);

            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getStore_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String getVideoByProduct(Activity context, String productId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", ATPreferences.readString(context, Constants.KEY_USER_DEFAULT));
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject object2 = new JSONObject();
            JSONObject obj_param2 = new JSONObject();
            object2.put("101", "010100631");
            obj_param2.put("114.144", productId);
            object2.put("PARAM", obj_param2);
            arr.put(object2);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "getVideoByProduct_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetMerchantDelivery(Activity context, String id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", ATPreferences.readString(context, Constants.KEY_USER_DEFAULT));
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100015");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", id);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getmerchantdelivery_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String getListOfNotifications(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", ATPreferences.readString(context, Constants.KEY_USER_DEFAULT));
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400817");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_list_notifications---" + parametersToCall);

        return parametersToCall;
    }


    public static String GetMerchantRating(Activity context, String id, String MerchantId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            JSONObject obj_param = new JSONObject();
            obj1.put("101", "010100104");
            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("53", MerchantId);
            obj2.put("operator", "eq");
            arr1.put(obj2);
            obj1.put("FILTER", arr1);
            obj_param.put("127.57", id);
            obj1.put("EXPECTED", "ALL");
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getmerchantrating_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String AddMerchantRatingFilter(Activity context, String id, String MerchantId, String Rating, String desc) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            JSONObject obj_param = new JSONObject();
            obj1.put("101", "030400103");
            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("53", MerchantId);
            obj2.put("operator", "eq");
            arr1.put(obj2);
            obj1.put("FILTER", arr1);
            obj_param.put("127.57", id);
            obj_param.put("122.129", Rating);
            obj_param.put("120.83", desc);
            obj_param.put("120.157", desc);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_addmerchantrating_api---" + parametersToCall);

        return parametersToCall;
    }


    public static String AddMerchantRating(Activity context, String id, String MerchantId, String Rating, String desc) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030400103");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", id);
            obj_param.put("127.57", MerchantId);
            obj_param.put("122.129", Rating);
            obj_param.put("120.83", desc);
            obj_param.put("120.157", desc);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_addmerchantrating_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String fetchAboutUsListing(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100756");
            JSONObject obj_param = new JSONObject();
            //  obj_param.put("122.25", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_addmerchantrating_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetMerchantLocation(Activity context, String id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100368");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", id);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getmerchantlocation_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetMerchantDistance(Context context, String id, String lat, String longitude) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400516");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", id);
            obj_param.put("120.38", lat);
            obj_param.put("120.39", longitude);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getmerchantlocation_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetWriteReview(Activity context, String merchant_id, String title, String desc, String rating) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100368");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("127.57", merchant_id);
            obj_param.put("120.83", title);
            obj_param.put("120.157", desc);
            obj_param.put("122.129", rating);

            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getmerchantlocation_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetInvoiceHistory(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100206");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));

            obj1.put("PARAM", obj_param);
            obj1.put("EXPECTED", "ALL");
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_GetInvoiceHistory_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetInvoiceHistoryById(Activity context, String id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100206");
            JSONObject obj_param = new JSONObject();

            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));

            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("121.75", Utils.getElevenDigitId(id));
            obj2.put("operator", "eq");
            arr1.put(obj2);
            obj1.put("FILTER", arr1);

            obj1.put("PARAM", obj_param);
            obj1.put("EXPECTED", "ALL");
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_GetInvoiceHistory_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetAdDetailById(Activity context, String id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();

            // obj1.put("101", "010400870");
            obj1.put("101", "010400676");
            JSONObject obj_param = new JSONObject();

            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));

            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            //    obj2.put("123.21", Utils.getElevenDigitId(id));
            obj2.put("123.21", id);
            obj2.put("operator", "eq");
            arr1.put(obj2);
            obj1.put("FILTER", arr1);

            obj1.put("PARAM", obj_param);
            obj1.put("EXPECTED", "ALL");
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "makeJsonGetAdDetailById---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetDeleteCard(Activity context, String cardId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030400046");
            JSONObject obj_param = new JSONObject();
            obj_param.put("122.34", "");

            obj1.put("PARAM", obj_param);
            obj1.put("EXPECTED", "ALL");
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_GetInvoiceHistory_api---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonGetInvoiceDetail(Activity context, String invoiceId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100114");
            JSONObject obj_param = new JSONObject();
            obj_param.put("121.75", invoiceId);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_GetInvoiceDetail_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonAllMessages(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400265");

            JSONObject obj_param = new JSONObject();
            obj_param.put("114.9", "true");
            // obj1.put("EXPECTED", "120.138");
            obj1.put("EXPECTED", "ALL");
            obj1.put("PARAM", obj_param);
            //obj1.put("ORDER", "122.114-ASC");
            obj1.put("ORDER", "122.114-ASC");
            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj2.put("operator", "eq");
            //   JSONObject obj3 = new JSONObject();
            // obj3.put("120.16", "92");
            // obj3.put("operator", "eq");
            arr1.put(obj2);
            //arr1.put(obj3);
            obj1.put("FILTER", arr1);

            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_all_message_api---" + parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonAllMerchantMessages(Activity context, String merchantId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400265");
            JSONObject obj_param = new JSONObject();
            obj_param.put("114.9", "false");
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.179", merchantId);
            // obj1.put("EXPECTED", "120.138");
            obj1.put("EXPECTED", "ALL");
            obj1.put("PARAM", obj_param);
            //obj1.put("ORDER", "122.114-ASC");
            obj1.put("ORDER", "122.114-ASC");
            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            //  obj2.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            // obj2.put("114.179", merchantId);
            //obj2.put("operator", "eq");
            //   JSONObject obj3 = new JSONObject();
            // obj3.put("120.16", "92");
            // obj3.put("operator", "eq");
            arr1.put(obj2);
            //arr1.put(obj3);
            //  obj1.put("FILTER", arr1);

            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "makeJsonAllMerchantMessages---" + parametersToCall);
//        Logger.addRecordToLog(parametersToCall);
        return parametersToCall;
    }


    public static String makeJsonGetMessagesInvoice(Activity context, String mmessageId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010400265");
            JSONObject obj_param = new JSONObject();
            obj_param.put("114.9", "false");
            obj_param.put("114.179", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("PARAM", obj_param);
            obj1.put("ORDER", "122.114-ASC");
            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("121.75", mmessageId);
            obj2.put("operator", "eq");
            arr1.put(obj2);
            obj1.put("FILTER", arr1);
            obj1.put("EXPECTED", "ALL");

            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_all_message_api---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonGetAdsListing(Activity context, String merchantId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010200517");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));

            obj1.put("EXPECTED", "ALL");
            if (!merchantId.isEmpty()) {
                JSONArray arr1 = new JSONArray();
                JSONObject obj2 = new JSONObject();
                obj2.put("114.179", merchantId);
                obj2.put("operator", "eq");
                arr1.put(obj2);
                obj1.put("FILTER", arr1);
            }
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_GetAds_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonReorder(Activity context, String invoiceId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030400628");
            JSONObject obj_param = new JSONObject();
            //obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("122.31", invoiceId);
            /*obj_param.put("121.75", invoiceId);
            obj_param.put("121.10", refund_status_Id);
            obj_param.put("124.118",process_status);
            obj_param.put("114.146", Client.getDateTimeStamp());*/
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_makeJsonreturnOrder---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeMessageRead(Activity context, String messageId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "020400377");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("122.114", Utils.getElevenDigitId(messageId));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_makeJsonreturnOrder---" + parametersToCall);
        Logger.addRecordToLog("readmessage   " + parametersToCall);
        return parametersToCall;
    }

    public static String getReturnReasons(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100576");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getReturnReasons---" + parametersToCall);
//        Logger.addRecordToLog("readmessage   " + parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonGuestLogin(Context context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", String.valueOf(App.latitude));
            obj.put("120.39", String.valueOf(App.longitude));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030300850");
            JSONObject obj_param = new JSONObject();
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getGuestLogin---" + parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonLastActivityByGuest(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", String.valueOf(App.latitude));
            obj.put("120.39", String.valueOf(App.longitude));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "020400851");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getGuestLogin---" + parametersToCall);
        return parametersToCall;
    }


    public static String makeJsonreturnOrder(Activity context, String invoiceId,
                                             JSONArray jsonArray) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "020300332");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("121.75", invoiceId);
            obj_param.put("PC", jsonArray);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_makeJsonreturnOrder---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonGetStoresWithItems(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100557");

            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("114.179", ATPreferences.readString(context, Constants.KEY_USERID));

            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getallstores---" + parametersToCall);

        return parametersToCall;
    }

    public static String updateGPSCoordinatesByCartId(Activity context, String id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", String.valueOf(App.latitude));
            obj.put("120.39", String.valueOf(App.longitude));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "020400811");

            JSONObject obj_param = new JSONObject();
            obj_param.put("122.31", id);
            obj_param.put("120.38", String.valueOf(App.latitude));
            obj_param.put("120.39", String.valueOf(App.latitude));

            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_shopping---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonAddCreditCard(Activity context, String type_id, String number, String name, String holderName, String cvv,
                                               String date_expires, String address_id) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030400044");

            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj_param.put("2", number);
            obj_param.put("48.6", name);
            obj_param.put("48.2", holderName);
            obj_param.put("48.4", cvv);
            obj_param.put("118.5", date_expires);
            obj_param.put("114.115", address_id);
            obj_param.put("120.7", type_id);
            obj_param.put("121.70", "00000035001");

            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "parameters_getaddcards---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonSearchItem(Activity context, String address) {
        String dataPlana = "{\"101\":\"010400472\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.127\":\"" + address + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }


    public static String makeJsonSearchForShopAsst(Activity context, String key) {
        String dataPlana = "{\"101\":\"010400478\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.127\":\"" + key + "\" ,\"121.141\":\"" + Client.getDateTimeStamp() + "\",\"127.89\":\"" + Client.getWeekDay() + "\"}}";

        String dataPlana1 = "{\"101\":\"010400479\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.127\":\"" + key + "\" ,\"121.141\":\"" + Client.getDateTimeStamp() + "\",\"127.89\":\"" + Client.getWeekDay() + "\"}}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "," + dataPlana1 + "]}";

        Log.d("makeJsonForShopAsst", parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonSearchAds(Activity context, String ads) {
        String dataPlana = "{\"101\":\"010400549\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.127\":\"" + ads + "\" ,\"121.141\":\"" + Client.getDateTimeStamp() + "\",\"127.89\":\"" + Client.getWeekDay() + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.d("makeJsonSearchAds", parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonSearchSpecial(Activity context, String key) {
        String dataPlana = "{\"101\":\"010400479\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.127\":\"" + key + "\" ,\"121.141\":\"" + Client.getDateTimeStamp() + "\",\"127.89\":\"" + Client.getWeekDay() + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.d("makeJsonSearchSpecial", parametersToCall);

        return parametersToCall;
    }


    public static String getJsonCard(Activity context) {
        String dataPlana = "{\"101\":\"010100017\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }


    public static String makeJsonSearchNearAddress(Activity context) {
        String dataPlana = "{\"101\":\"010100055\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        return parametersToCall;
    }

    public static String makeJsonSearchFavorites(Activity context, String keyword) {
        String dataPlana = "{\"101\":\"010400221\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"127.10\":\"" + "001" + "\" ,\"114.112\":\"" + "21" + "\" ,\"114.127\":\"" + keyword + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        return parametersToCall;
    }

//    public static String makeJsonAddToCartItems(Activity context, String quantity, String productId, String merchantID, String Option_Id,String Option_Id2) {
//        String dataPlana = "{\"101\":\"030400198\",\"PARAM\":{\"53\":\""
//                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"114.121\":\""
//                + quantity + "\",\"114.144\":\""
//                + productId + "\" ,\"114.179\":\""
//                + merchantID + "\" ,\"121.104\":\""
//                + Option_Id + "\"}}";
//        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
//                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
//                + "en" + "\",\"57\":\""
//                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
//                + "0.0" + "\",\"120.39\":\""
//                + "0.0" + "\",\"OPTLST\":["
//                + dataPlana + "]}";
//        Log.d("ParamsAddtocart", parametersToCall);
//        return parametersToCall;
//    }030400198

    public static String makeJsonAddToCartItems(Activity context, String quantity, String productId, String merchantID,
                                                String Option_Id, String Option_Id2, String specialInstructions) {
        String ChoiceArray = "";
        if (!Option_Id2.isEmpty()) {
            ChoiceArray = "{\"121.104\":\"" + Option_Id + "\"},{\"121.104\":\"" + Option_Id2 + "\"}";
        } else if (!Option_Id.isEmpty()) {
            ChoiceArray = "{\"121.104\":\"" + Option_Id + "\"}";
        }
        if (!specialInstructions.isEmpty())
            specialInstructions = Utils.convertStringToHex(specialInstructions);
        String dataPlana = "{\"101\":\"030400198\",\"PARAM\":{"
                + "\"53\":\"" + ATPreferences.readString(context, Constants.KEY_USERID) +
                "\" ,\"114.121\":\"" + quantity +
                "\",\"114.144\":\"" + productId +
                "\" ,\"114.179\":\"" + merchantID +
                "\" ,\"121.55\":\"" + specialInstructions +
                "\" ,\"CH\":[" + ChoiceArray + "]}}";

        // ,"CH":[{"121.104":"00000000030"},{"121.104":"00000000031"}]}}]}
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + App.latitude + "\",\"120.39\":\""
                + App.longitude + "\",\"OPTLST\":["
                + dataPlana + "]}";
        Log.d("ParamsAddtocart", parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonSearchNearBy(Activity context, String keyword, String lat, String longs) {
        String dataPlana = "{\"101\":\"010400228\",\"PARAM\":{\"53\":\""
                + ATPreferences.readString(context, Constants.KEY_USERID) + "\" ,\"127.10\":\"" + "001" + "\" ,\"120.38\":\"" + lat + "\" ,\"120.39\":\"" + longs + "\" ,\"114.127\":\"" + keyword + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + lat + "\",\"120.39\":\""
                + longs + "\",\"OPTLST\":["
                + dataPlana + "]}";
        return parametersToCall;
    }

    public static String makeJsonDeleteItemFromCart(Activity context, String id) {
        String dataPlana = "{\"101\":\"020400203\",\"PARAM\":{\"121.30\":\""
                + Utils.getElevenDigitId(id) + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        Log.d("removeApi", parametersToCall + "");
        return parametersToCall;
    }

    public static String makeJsonDeleteStoreFromCart(Activity context, String id) {
        String dataPlana = "{\"101\":\"020400202\",\"PARAM\":{\"122.31\":\""
                + Utils.getElevenDigitId(id) + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        return parametersToCall;
    }

    public static String makeJsonGetInvoiceItems(Activity context, String id) {
        String dataPlana = "{\"101\":\"010100114\",\"PARAM\":{\"121.75\":\""
                + id + "\"}}";
        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";
        return parametersToCall;
    }
//
//   public static String makeJsonReturnItems(Activity context, String invoiceId ) {
//        String dataPlana = "{\"101\":\"020300332\",\"PARAM\":{\"53\":\""
//                + "00011010000000000002" + "\" ,\"121.75\":\"" + invoiceId + "\"}}";
//        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
//                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
//                + "en" + "\",\"57\":\""
//                + //*Util.GetDeviceId()*//*Utils.getDeviceId(context) + "\",\"120.38\":\""
//                + "0.0" + "\",\"120.39\":\""
//                + "0.0" + "\",\"OPTLST\":["
//                + dataPlana + "]}";
//
//        return parametersToCall;
//    }


    public static String makePaymentJson(Activity context) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("addressId=804").append("&cardNumber=5454545454545454").append("&cardType=62").append("&cvv=233").append("&expireMonth=12").append("&expireYear=2016").append("&firstName=aaaa").append("&lastName=aaaaaa").append("&nickname=aaaaaa").append("&nmcId=196");

            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    public static String validMerchantByProduct(Context context, String productId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100885");
            JSONObject obj_param = new JSONObject();
            obj_param.put("114.144", productId);
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (BuildConfig.DEBUG)
            Log.d(TAG, "validMerchantByProduct_api---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonSeatingAreasByLocation(Activity context, String locationId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100921");
            JSONObject obj_param = new JSONObject();
            obj_param.put("114.47", Utils.getElevenDigitId(locationId));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "makeJsonSeatingAreasByLocation_API---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetTablesBySeatingAreaId(Activity context, String seatingAreaId) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010100922");
            JSONObject obj_param = new JSONObject();
            //obj_param.put("114.47", ATPreferences.readString(context, Constants.LOCATION_ID));
            obj_param.put("115.11", Utils.getElevenDigitId(seatingAreaId));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "makeJsonGetTablesBySeatingAreaId---" + parametersToCall);

        return parametersToCall;
    }

    public static String makeJsonGetAssigns(Activity context, String merchantId) {
        String dataPlana = "{\"101\":\"010100052\",\"PARAM\":{\"114.179\":\"" + merchantId + "\"}," +
                "\"FILTER\":[{\"114.150\":\"" + merchantId + "\",\"operator\":\"eq\"}]}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.v(TAG, "makeJsonGetAssigns---" + parametersToCall);
        return parametersToCall;
    }

    public static String makeJsonGetPromos(Activity context, String merchantId) {
        String dataPlana = "{\"101\":\"010300098\",\"FILTER\":[{\"114.112\":\"23\",\"operator\":\"eq\"}," +
                "{\"53\":\"" + merchantId + "\",\"operator\":\"eq\"}]}";

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.v(TAG, "makeJsonGetPromos---" + parametersToCall);
        return parametersToCall;
    }

    public static String makeAddEditReservation(Activity context,
                                                String reservationId,
                                                String reservationDate,
                                                String reservationStartHour,
                                                String reservationEndHour,
                                                String reservationPeopleQty,
                                                //String reservationChildrenQty, String reservationWheelchairFlag,
                                                // String reservationChildSeatFlag, String locationTableId,
                                                //String diningMethodId,
                                                String reservationName,
                                                //String reservationPhone,
                                                //String reservationEmail1, String reservationEmail2, String reservationSpecialRequest,
                                                String reservationNote,
                                                //String userAssignedTo,
                                                String selectedSeatingAreaId,
                                                //String selectedPromoId,
                                                String locationId, String smokingFlag

    ) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", String.valueOf(App.latitude));
            obj.put("120.39", String.valueOf(App.longitude));
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "030301015");

            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));

            obj_param.put("116.201", reservationDate);
            obj_param.put("116.202", reservationStartHour);
            obj_param.put("116.203", reservationEndHour);
            obj_param.put("116.204", reservationPeopleQty);
            obj_param.put("114.53", Utils.convertStringToHex(reservationName));
            obj_param.put("114.47", Utils.getElevenDigitId(locationId));
            obj_param.put("127.72", smokingFlag);

            if (!selectedSeatingAreaId.isEmpty())
                obj_param.put("117.13", Utils.getElevenDigitId(selectedSeatingAreaId));

            if (!reservationId.isEmpty())
                obj_param.put("116.200", Utils.getElevenDigitId(reservationId));
            if (!reservationNote.isEmpty())
                obj_param.put("117.16", Utils.convertStringToHex(reservationNote));

//
//            if (!selectedPromoId.isEmpty())
//                obj_param.put("114.144", Utils.getElevenDigitId(selectedPromoId));
//            if (!reservationEmail2.isEmpty())
//                obj_param.put("114.51", reservationEmail2);
//            if (!userAssignedTo.isEmpty())
//                obj_param.put("114.179", Utils.getElevenDigitId(userAssignedTo));
//            if (!reservationSpecialRequest.isEmpty())
//                obj_param.put("121.55", Utils.convertStringToHex(reservationSpecialRequest));
//            if (!reservationChildrenQty.isEmpty())
//                obj_param.put("116.205", reservationChildrenQty);
//            if (!reservationEmail1.isEmpty())
//                obj_param.put("114.7", reservationEmail1);
//            if (!reservationChildSeatFlag.isEmpty())
//                obj_param.put("116.207", reservationChildSeatFlag);
//            if (!reservationWheelchairFlag.isEmpty())
//                obj_param.put("116.206", reservationWheelchairFlag);
//            if (!reservationPhone.isEmpty())
//                obj_param.put("48.28", reservationPhone);
//            if (!diningMethodId.isEmpty())
//                obj_param.put("115.71", Utils.getElevenDigitId(diningMethodId));
//            if (!locationTableId.isEmpty())
//                obj_param.put("115.21", Utils.getElevenDigitId(locationTableId));


            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "makeAddEditReservation---" + parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonGetReservationById(Activity context, String reservationId) {
        String lat = String.valueOf(App.latitude);
        String lon = String.valueOf(App.longitude);
        JSONObject obj1 = null;
        try {

            obj1 = new JSONObject();
            obj1.put("101", "010101016");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            //   obj1.put("EXPECTED", "ALL");
            JSONArray arr1 = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("116.200", Utils.getElevenDigitId(reservationId));
            //  obj2.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj2.put("operator", "eq");
            arr1.put(obj2);
            obj1.put("PARAM", obj_param);
            obj1.put("FILTER", arr1);


        } catch (Exception e) {
            e.printStackTrace();
        }

        String dataPlana = obj1.toString();

        String parametersToCall = "{\"192\":\"" + ATPreferences.readString(context, Constants.KEY_USER_DEFAULT)
                + "\",\"11\":\"" + Client.getTimeStamp() + "\",\"122.45\":\""
                + "en" + "\",\"57\":\""
                + /*Util.GetDeviceId()*/Utils.getDeviceId(context) + "\",\"120.38\":\""
                + "0.0" + "\",\"120.39\":\""
                + "0.0" + "\",\"OPTLST\":["
                + dataPlana + "]}";

        Log.d("makeJsonGetReservationById", parametersToCall);

        return parametersToCall;
    }


    public static String makeJsonGetAllReservations(Activity context) {
        String parametersToCall = "";
        try {
            JSONObject obj = new JSONObject();
            obj.put("192", Constants.KEY_DEFAULT);
            obj.put("11", Client.getTimeStamp());
            obj.put("122.45", "en");
            obj.put("57", Utils.getDeviceId(context));
            obj.put("120.38", "0.0");
            obj.put("120.39", "0.0");
            JSONArray arr = new JSONArray();
            JSONObject obj1 = new JSONObject();
            obj1.put("101", "010101016");
            JSONObject obj_param = new JSONObject();
            obj_param.put("53", ATPreferences.readString(context, Constants.KEY_USERID));
            obj1.put("PARAM", obj_param);
            arr.put(obj1);
            obj.put("OPTLST", arr);
            parametersToCall = obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (BuildConfig.DEBUG)
            Log.d(TAG, "makeJsonGetReservationById---" + parametersToCall);

        return parametersToCall;
    }


}