package com.apitap.model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apitap.R;
import com.apitap.controller.ModelManager;
import com.apitap.controller.SearchAddressManager;
import com.apitap.model.bean.SearchAddressBeans;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.AdDetailActivity;
import com.apitap.views.FullScreenImage;
import com.apitap.views.HomeActivity;
import com.apitap.views.LoginActivity;
import com.apitap.views.MerchantStoreDetails;
import com.apitap.views.MerchantStoreMap;
import com.apitap.views.MessageDetailActivity;
import com.apitap.views.RateMerchant;
import com.apitap.views.SearchItemActivity;
import com.apitap.views.ZoomImage;
import com.apitap.views.ZoomMessageImage;
import com.apitap.views.adapters.SearchNearByAdapter;
import com.apitap.views.fragments.stores.FragmentStore;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Created by sumit-kumar on 1/8/16.
 */

public class Utils {

    public static String getDeviceId(Context activity) {
        String id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return convertStringToHex(id);
    }

    public final static String TAG_NAME_FRAGMENT = "ACTIVITY_FRAGMENT";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 456;
    public static final String APK_VERSION = "25.0904.520"; // yy - mm-dd-version
    public static String seacrh_key = "";
    public static String locationSearch = "";
    public static ArrayList<String> placeIdList;
    public static RecyclerView recyclerView;
    public static ArrayList<SearchAddressBeans> addressList = new ArrayList<>();
    public static SearchNearByAdapter searchAddressAdapter;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    public static String getFormatAmount(String Amount) {
//        double value = 0;
//        DecimalFormat decimalFormat = null;
//        switch (Amount.length()) {
//            case 9:
//                decimalFormat = new DecimalFormat("000,000.00");
//                break;
//            case 8:
//                decimalFormat = new DecimalFormat("00,000.00");
//                break;
//            case 7:
//                decimalFormat = new DecimalFormat("0,000.00");
//                break;
//            case 6:
//                decimalFormat = new DecimalFormat("000.00");
//                break;
//            case 5:
//                decimalFormat = new DecimalFormat("00.00");
//                break;
//            case 4:
//                decimalFormat = new DecimalFormat("0.00");
//                break;
//
//        }
//
//        if (decimalFormat != null) {
//            return decimalFormat.format(value);
//        }
//    }

    public static String getFormatAmount(String Amount) {
        double value;
        try {
            value = Double.parseDouble(Amount);
        } catch (NumberFormatException e) {
            System.err.println("Invalid amount: " + Amount);
            return null;
        }

        // Split the amount into integer and decimal parts
        String[] parts = Amount.split("\\.");
        String integerPart = parts[0];
        String decimalPart = parts.length > 1 ? parts[1] : "";

        // Generate format string based on the length of integer and decimal parts
        StringBuilder formatBuilder = new StringBuilder();
        for (int i = 0; i < integerPart.length(); i++) {
            if (i > 0 && (integerPart.length() - i) % 3 == 0) {
                formatBuilder.append(',');
            }
            formatBuilder.append('0');
        }
        if (!decimalPart.isEmpty()) {
            formatBuilder.append('.');
            for (int i = 0; i < decimalPart.length(); i++) {
                formatBuilder.append('0');
            }
        } else {
            formatBuilder.append(".00"); // Default to two decimal places if none provided
        }

        // Create the decimal format with the generated format string
        DecimalFormat decimalFormat = new DecimalFormat(formatBuilder.toString(), new DecimalFormatSymbols(Locale.US));

        return decimalFormat.format(value);
    }

    // Get exist Fragment by it's tag name.
    public static Fragment getFragmentByTagName(FragmentManager fragmentManager, String fragmentTagName) {
        Fragment ret = null;

        // Get all Fragment list.
        List<Fragment> fragmentList = fragmentManager.getFragments();

        if (fragmentList != null) {
            int size = fragmentList.size();
            for (int i = 0; i < size; i++) {
                Fragment fragment = fragmentList.get(i);

                if (fragment != null) {
                    String fragmentTag = fragment.getTag();

                    // If Fragment tag name is equal then return it.
                    if (fragmentTag != null && fragmentTag.equals(fragmentTagName)) {
                        ret = fragment;
                    }
                }
            }
        }

        return ret;
    }

    public static void printActivityFragmentList(FragmentManager fragmentManager) {
        // Get all Fragment list.
        List<Fragment> fragmentList = fragmentManager.getFragments();

        if (fragmentList != null) {
            int size = fragmentList.size();
            for (int i = 0; i < size; i++) {
                Fragment fragment = fragmentList.get(i);

                if (fragment != null) {
                    String fragmentTag = fragment.getTag();
                    Log.d(TAG_NAME_FRAGMENT, fragmentTag + "   l");
                }
            }

            Log.d(TAG_NAME_FRAGMENT, "***********************************");
        }
    }


    public static String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "dd-MM-yyyy HH:mm EEEE";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);
        return formattedDate;
    }

    public static String FormatStringAsPhoneNumber(String input) {
        String output;
        switch (input.length()) {
            case 7:
                output = String.format("%s-%s", input.substring(0, 3), input.substring(3, 7));
                break;
            case 10:
                output = String.format("(%s) %s-%s", input.substring(0, 3), input.substring(3, 6), input.substring(6, 10));
                break;
            case 11:
                output = String.format("%s (%s) %s-%s", input.charAt(0), input.substring(1, 4), input.substring(4, 7), input.substring(7, 11));
                break;
            case 12:
                output = String.format("+%s (%s) %s-%s", input.substring(0, 2), input.substring(2, 5), input.substring(5, 8), input.substring(8, 12));
                break;
            default:
                return null;
        }
        return output;
    }

    public static String GetToday() {
        Date date = new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);
        return formattedDate;
    }

    public static boolean checkLocationPermission(final Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new androidx.appcompat.app.AlertDialog.Builder(activity)
                        .setTitle("Location Permissions")
                        .setMessage("Enable GPS to use location services?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public static String currencyRoundOff(String data2) {
        DecimalFormat format = new DecimalFormat("$#");
        format.setMinimumFractionDigits(2);
        return format.format(data2);
    }


    public static String roundOffTo2DecPlaces(double val) {
        return String.format("%.2f", val);
    }


    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    public static String getEditTextString(EditText editText) {
        return editText.getText().toString();
    }
    public static String getEditTextStringWithSeconds(EditText editText) {
        return editText.getText().toString()+":00";
    }

    public static boolean isDateAfter(String startDate, String endDate, String myFormatString) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(myFormatString);
            Date date1 = df.parse(endDate);
            Date startingDate = df.parse(startDate);

            if (date1.after(startingDate))
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String convertStringToHex(String str) {
       /* if (str == null)
            return "x090";
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        try {
           str= URLEncoder.encode(hex.toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.e("DeviceId", "" + hex.toString());
        return str;*/
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer();
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }


    public static String hexToASCII(String hex) {
      /*  StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexValue.length(); i += 2) {
            String str = hexValue.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();*/
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return new String(bytes);
    }

    /**
     * @param lengtT
     * @param data
     * @return
     */

    public static String lengtT(int lengtT, String data) {
        String d = "";
        if (data != null) {
            if (data.length() < lengtT) {
                for (int i = data.length(); i < lengtT; i++) {
                    d += "0";
                }
            }
            data = d + data;
        } else {
            data = "00000000000";
        }
        return data;
    }

    private static String getMacAddress(Activity activity) {
        @SuppressLint("WifiManagerLeak") WifiManager manager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        return address;
    }

    public static String getElevenDigitId(String id) {
        for (int i = id.length(); i < 11; i++) {
            id = "0" + id;
        }
        return id;
    }

    public static String getStringHexaDecimal(String hex) {
       /* String hexString = str;
        byte[] bytes = Hex.decodeHex(hexString.toCharArray());
        String res = new String(bytes, "UTF-8");
        return res;*/
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return new String(bytes);
    }

    public static Boolean checkIfEditextEmpty(EditText editText) {
        return editText.getText().toString().isEmpty();
    }

    public static void showToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static double getDistance(double startLat, double startLong, double endLat, double endLong) {
        Location selected_location = new Location("locationA");
        selected_location.setLatitude(startLat);
        selected_location.setLongitude(startLong);

        Location near_locations = new Location("locationA");
        near_locations.setLatitude(endLat);
        near_locations.setLongitude(endLong);

        double distance = selected_location.distanceTo(near_locations) / 1000;
        return distance;
    }

    public static String setDistance(double value) {
        DecimalFormat myFormatter = new DecimalFormat("############");
        return myFormatter.format(value);
    }

    public static String getTime(String value) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        cal.setTime(sdf.parse(value));

        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
        return sdf1.format(cal.getTime());
    }

    public static String changeInvoiceDateFormat(String date) {
        SimpleDateFormat sdf_old = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat sdf_new = new SimpleDateFormat("dd MMM yyyy");
        Date old = null;
        try {
            old = sdf_old.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf_new.format(old);
    }

    public static long daysBetween(Date startDate, Date endDate) {
        Calendar sDate = getDatePart(startDate);
        Calendar eDate = getDatePart(endDate);

        long daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public static Calendar getDatePart(Date date) {
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second
        return cal;                                  // return the date part
    }

    public static long calculateDays(Date dateEarly, Date dateLater) {
        return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getDateFromMsg(String dates) {

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String newString = new SimpleDateFormat("dd MMM yyyy").format(date); // 9:00
        return newString;
    }

    public static String getTimeFromInvoice(String time) {

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String newString = new SimpleDateFormat("HH:mm").format(date); // 9:00
        return newString;
    }

    public static String getLosAngelesLocalTime(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());
        dfTime.setTimeZone(TimeZone.getDefault());
        //  String wholeDate = df.format(date);

        return dfTime.format(date);


        //   String formattedDate = dfTime.format(wholeDate);  // 20120821
        //  return formattedDate;
        // return new SimpleDateFormat("HH:mm").format(wholeDate);
    }

    public static String getLosAngelesLocalDateTime(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date);
    }

    public static String getTimeAgo(long time, Context ctx) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static Dialog createLoadingDialog(Context context, boolean isCancellable) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(isCancellable);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return dialog;
    }

    public static void ageFilterDialog(Context context, int age) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.dialog_age_filter);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        TextView textViewAge = dialog.findViewById(R.id.text);
        LinearLayout btn_ok = dialog.findViewById(R.id.linearLayoutOk);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        textViewAge.setText("You must be " + age + " years old to buy this product.");


        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public static void showGuestDialog(final Activity activity) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.guest_login);

        Button login = dialog.findViewById(R.id.continueshoping);
        Button cancel = dialog.findViewById(R.id.cancel);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, LoginActivity.class));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, HomeActivity.class));
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //getDialogView(dialog);
        //viewsVisibility(dialog);

    }


    public static Dialog showReloadDialog(final Activity context) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        // GpsLocation gps = new GpsLocation(context);

        dialog.setContentView(R.layout.notification_dialog);

        return dialog;
        //dialog.show();
        //dialog.getWindow().setAttributes(lp);
    }

    public static Dialog showNoDataFromMerchantDialog(final Activity context) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        // GpsLocation gps = new GpsLocation(context);


        dialog.setContentView(R.layout.notification_dialog);
        TextView textViewTitle = dialog.findViewById(R.id.txttitle);
        TextView textViewContent = dialog.findViewById(R.id.txtmessage);

        textViewTitle.setText("Apitap");
        textViewContent.setText("Merhchant don't have anything loaded at this time.Please contact the merchant for more information.");

        return dialog;
        //dialog.show();
        //dialog.getWindow().setAttributes(lp);
    }

    public static Dialog showReturnDialog(final Activity context) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        // GpsLocation gps = new GpsLocation(context);

        dialog.setContentView(R.layout.notification_dialog);

        TextView textViewTitle = dialog.findViewById(R.id.txttitle);
        TextView textViewMessage = dialog.findViewById(R.id.txtmessage);
        TextView txtok = dialog.findViewById(R.id.txtok);
        TextView txtcancel = dialog.findViewById(R.id.txtcancel);
        textViewTitle.setText("Return");
        txtok.setText("Ok");
        txtcancel.setText("Contact Merchant");
        textViewMessage.setText("Product return time period is over.");

        return dialog;
        //dialog.show();
        //dialog.getWindow().setAttributes(lp);
    }

    public static boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    public static boolean isValidPassword(String password, EditText editTextPassword) {
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$#.!%*?&]{8,}$")) {
            return true;
        } else {
            editTextPassword.setError("Invalid Password");
            return false;
        }
    }

    public static void whatsNewDialog(final Activity context) {
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.whats_new_dialog);

        TextView closeButton = dialog.findViewById(R.id.cancel);
        TextView versionName = dialog.findViewById(R.id.versionNumber);
        versionName.setText(APK_VERSION);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public static Dialog reportMessage(final Activity context) {
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.report_dialog);
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setDimAmount(0.5f);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return dialog;
    }

/*
    public static void TutorialDialog(final Activity context) {
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.tutoral_dialog);


//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
*/

    public static Dialog showSearchDialog(final Activity context, final String whichClass) {

        final Dialog searchDialog = new Dialog(context);
        searchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(searchDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //   lp.y = 200;
        lp.dimAmount = 0.2f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        lp.gravity = Gravity.TOP;

        searchDialog.setContentView(R.layout.dialog_search);
        searchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final AutoCompleteTextView editTextSearch = searchDialog.findViewById(R.id.editTextSearch);
        TextView textViewSearch = searchDialog.findViewById(R.id.textViewSearch);
        ImageView imageViewClose = searchDialog.findViewById(R.id.imageViewClose);

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDialog.dismiss();
            }
        });

        textViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextSearch.getText().toString().isEmpty())
                    showToast(context, "Please enter word to search products.");
                else
                    context.startActivity(new Intent(context, SearchItemActivity.class)
                            .putExtra("key", seacrh_key)
                            .putExtra("location", ModelManager.getInstance().getHomeManager().listAddresses.get(0)));

            }
        });

        searchDialog.show();
        searchDialog.getWindow().setAttributes(lp);
        searchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        searchDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return searchDialog;


    /*    final ArrayList<String> nickname_list = ModelManager.getInstance().getHomeManager().listAddresses;
        final ArrayList<String> address_list = ModelManager.getInstance().getHomeManager().addressNickName;
        ArrayList<String> looking_forList = new ArrayList<>();
        looking_forList.add("Choose One");
        looking_forList.add("Product or Services");
        looking_forList.add("Business");


        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        // GpsLocation gps = new GpsLocation(context);
        dialog.setContentView(R.layout.quick_search_test);

        Button submit = (Button) dialog.findViewById(R.id.submit);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView);
        final EditText search = (EditText) dialog.findViewById(R.id.search);
        final EditText search_loc = (EditText) dialog.findViewById(R.id.nearby_ed);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
        final Spinner looking_for = (Spinner) dialog.findViewById(R.id.looking_for);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        ArrayAdapter lookinforAdapter = new ArrayAdapter(context, R.layout.simple_list_item_divider,R.id.text, looking_forList);
        looking_for.setAdapter(lookinforAdapter);

        SpinnerAdapter2 arrayAdapter = new SpinnerAdapter2(context, R.layout.spinner_item, nickname_list, address_list);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationSearch = nickname_list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seacrh_key = search.getText().toString();
                if (search_loc.getText().toString().length() > 0)
                    locationSearch = search_loc.getText().toString();
                if (looking_for.getSelectedItemPosition() == 1) {
                    context.startActivity(new Intent(context, SearchItemActivity.class).putExtra("key", seacrh_key).putExtra("location", locationSearch));
                    dismissKeyboard(context, search);
                    dialog.dismiss();
                } else if (looking_for.getSelectedItemPosition() == 2) {
                    Bundle bundle = new Bundle();
                    bundle.putString("key", seacrh_key);
                    checkForCast(context, whichClass, bundle);
                    dismissKeyboard(context, search);
                    dialog.dismiss();
                } else {
                    dismissKeyboard(context, search);
                    Toast.makeText(context, "Please Choose Search Type", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        recyclerTouchListener(recyclerView, context, search_loc);

        search_loc.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (addressList.size() > 0)
                    recyclerView.setVisibility(View.VISIBLE);
                else if (charSequence.toString().length() == 0 || addressList.size() == 0)
                    recyclerView.setVisibility(View.GONE);
                ModelManager.getInstance().getSearchAddressManager().getAddress(context, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
*/
        //        if (gps.canGetLocation()) {
        //            add = getLocations(context);
//            if (!add.isEmpty()&&!list.contains(add))
//                list.add(0,add);
//        } else {
//            gps.showSettingsAlert(dialog);
//        }
        //getDialogView(dialog);
        //viewsVisibility(dialog);
    }

    private static void checkForCast(Activity context, String whichClass, Bundle bundle) {
        switch (whichClass) {
            case "ZoomImage":
                ((ZoomImage) context).displayView(new FragmentStore(), Constants.TAG_STORESPAGE, bundle);
                break;
            case "SearchItem":
                ((SearchItemActivity) context).displayView(new FragmentStore(), Constants.TAG_STORESPAGE, bundle);
                break;
            case "RateMerchant":
                ((RateMerchant) context).displayView(new FragmentStore(), Constants.TAG_STORESPAGE, bundle);
                break;
            case "Home":
                ((HomeActivity) context).displayView(new FragmentStore(), Constants.TAG_STORESPAGE, bundle);
                break;
            case "AdDetail":
                ((AdDetailActivity) context).displayView(new FragmentStore(), Constants.TAG_STORESPAGE, bundle);
                break;
            case "MessageDetail":
                ((MessageDetailActivity) context).displayView(new FragmentStore(), Constants.TAG_STORESPAGE, bundle);
                break;
            case "MerchantStoreDetails":
                ((MerchantStoreDetails) context).displayView(new FragmentStore(), Constants.TAG_STORESPAGE, bundle);
                break;
            case "MerchantStoreMap":
                ((MerchantStoreMap) context).displayView(new FragmentStore(), Constants.TAG_STORESPAGE, bundle);
                break;
            case "FullScreenImage":
                ((FullScreenImage) context).displayView(new FragmentStore(), Constants.TAG_STORESPAGE, bundle);
                break;
            case "ZoomScreenImage":
                ((ZoomMessageImage) context).displayView(new FragmentStore(), Constants.TAG_STORESPAGE, bundle);
                break;

        }
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) return false;
        final int length = searchStr.length();
        if (length == 0) return true;
        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length)) return true;
        }
        return false;
    }

    public static void dismissKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public static void setRecyclerNearByAdapter(Context activity) {
        placeIdList = SearchAddressManager.placeIdList;
        addressList = SearchAddressManager.addressList;
        searchAddressAdapter = new SearchNearByAdapter(activity, addressList);
        recyclerView.setAdapter(searchAddressAdapter);
    }


    public static String getLocations(Activity context) {
        String address = "";
        GPSService mGPSService = new GPSService(context);
        mGPSService.getLocation();
        boolean b = Utils.checkLocationPermission(context);
        if (!b) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(context, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return "";

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();


            //Toast.makeText(context, "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();


            address = mGPSService.getLocationAddress();
        }

        //Toast.makeText(context, "Your address is: " + address, Toast.LENGTH_SHORT).show();

        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();
        return address;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static void baseshowFeedbackMessage(Activity activity, View view, String message) {
        Snackbar snakbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
//        TextView tv = snakbar.getView().findViewById(R.id.snackbar_text);
        TextView tv = snakbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(activity, R.color.colorBlack));
        snakbar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.colorGreenLogo));
        if (snakbar.isShown()) {
            snakbar.dismiss();
        }
        snakbar.show();
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static boolean isNetworkAvailable(Activity context) {

        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static boolean isOlder(Context context, int ageValid) {
        if (ATPreferences.readString(context, Constants.USER_DOB).isEmpty())
            return false;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(ATPreferences.readString(context, Constants.USER_DOB));
            // Date parsedDate = dateFormat.parse("2005-01-01");
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            long selectedMilli = timestamp.getTime();

            Date dateOfBirth = new Date(selectedMilli);
            Calendar dob = Calendar.getInstance();
            dob.setTime(dateOfBirth);
            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
                age--;
            } else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
                    && today.get(Calendar.DAY_OF_MONTH) < dob
                    .get(Calendar.DAY_OF_MONTH)) {
                age--;
            }

            //do something
            return age >= ageValid;


        } catch (Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
            e.printStackTrace();
        }
        return false;
    }

    public static String checkAvailability(String availability) {
        if (availability.equalsIgnoreCase("82001"))
            return "";
        else if (availability.equalsIgnoreCase("82002"))
            return "(In-Store Only)";
        else if (availability.equalsIgnoreCase("82003"))
            return "(Online/Mobile Only)";
        return "";
    }

    public static String removeLastChar(String str,int count){
        return str.substring(0, str.length() - count);
    }

    public static String removeSecondsFromTime(String time) {
        String inputPattern = "HH:mm:ss";
        String outputPattern = "HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void disableEnableClickable(View view,Boolean value){
        view.setEnabled(value);
        view.setClickable(value);
    }

    public static String bitmapToBase64(Bitmap bitmap) {
            int previewWidth=150;
            int previewHeight=bitmap.getHeight()*previewWidth/ bitmap.getWidth();
            Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
            byte[] bytes=byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    public static boolean isListValid(List<String> list) {
        if (list == null) {
            return false;
        }

        // Check if the size is 1
        if (list.size() == 1) {
            // Check if the single element is null
            return list.get(0) != null;
        }

        return true;
    }

}
