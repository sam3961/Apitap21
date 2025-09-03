package com.apitap.views.fragments.specials.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.apitap.views.fragments.specials.data.AllProductsListResponse;
import com.apitap.views.fragments.specials.data.ProductItemWrapper;
import com.apitap.views.fragments.specials.data.PromotionListingResponse;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommonFunctions {

    public static ArrayList<PromotionListingResponse> promotionByIdResponse = new ArrayList<>();
    public static ArrayList<AllProductsListResponse> promotionActiveProductResponse = new ArrayList<>();
    public static List<ProductItemWrapper> promotionCombinedProductList = new ArrayList<>();
    public static int promotionMerchantId = 0;
    public static boolean isDialogShowing = false;

    public static void lockOrientation(Activity activity) {
        int currentOrientation = activity.getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
/*
    public static void lockOrientation(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
    }
*/

    public static void unlockOrientation(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    public static String formatTip(String orderTip) {
        if (orderTip == null || orderTip.trim().isEmpty()) {
            return "0.00"; // nothing to show
        }

        try {
            double tipValue = Double.parseDouble(orderTip);

            // Format to 2 decimals
            String formattedTip = String.format("%.2f", tipValue);

            // Align like in your example
            return String.format("%-30s %8s", "Tip:", "$" + formattedTip);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ""; // fallback if invalid number
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

    public static SpannableString formatCategoryText(String categoryRoute, String categoryName) {
        String category = CommonFunctions.extractCategory(categoryRoute);
        if (category == null) category = "";

        if (categoryName == null) categoryName = "";

        String fullText = category + "\n" + categoryName;

        SpannableString spannable = new SpannableString(fullText);
        spannable.setSpan(
                new StyleSpan(Typeface.BOLD),
                0,
                category.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        return spannable;
    }

    public static String formatAllCategoryText(String category, String categoryName) {
        if (categoryName == null) categoryName = "";

//        String fullText = category +
//                "\n" + categoryName;

//        SpannableString spannable = new SpannableString(fullText);
//        spannable.setSpan(
//                new StyleSpan(Typeface.BOLD),
//                0,
//                category.length(),
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        );

        return category;
    }


    public static String extractCategory(String path) {
        if (path == null) return null;

        String[] rawParts = path.split("/");
        List<String> parts = new ArrayList<>();

        for (String part : rawParts) {
            part = part.trim();
            if (!part.isEmpty()) {
                parts.add(part);
            }
        }

        return parts.size() >= 3 ? parts.get(2) : null;
    }

    public static String hexToASCII(String hex) {
        if (hex == null)
            return "";
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
//        return new String(bytes);

        // Convert hex to ASCII
        String ascii = new String(bytes);

        // Decode HTML entities
        Spanned spanned = Html.fromHtml(ascii, Html.FROM_HTML_MODE_LEGACY);
        return spanned.toString();
    }

    public static String ASCIIToHex(String ascii) {
        StringBuilder hexStringBuilder = new StringBuilder();
        for (char c : ascii.toCharArray()) {
            // Convert each character to its hexadecimal representation and append it to the StringBuilder
            String hex = String.format("%02X", (int) c);
            hexStringBuilder.append(hex);
        }
        return hexStringBuilder.toString();
    }

    // Method 1: Convert string to MD5 hash
    public static String stringToMD5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

        // Convert bytes to hexadecimal string
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    public static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date currentDate = new Date(); // Get current date and time
        return simpleDateFormat.format(currentDate); // Format the date to the specified pattern
    }


    public static String[] decodeJWT(String jwt) {
        // JWT has 3 parts: header, payload, and signature
        String[] splitToken = jwt.split("\\.");

        // Decode header and payload (skip the signature)
        String header = null;
        String payload = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            header = new String(Base64.getUrlDecoder().decode(splitToken[0]));
            payload = new String(Base64.getUrlDecoder().decode(splitToken[1]));
        }

        return new String[]{header, payload};
    }

    public static String formatCompactDate(String dateStr) throws ParseException {
        return new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
                .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateStr));
    }

    public static String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        return sdf.format(new Date());
    }

    public static String getOrderFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        return sdf.format(new Date());
    }

    public static String formatTo12HourTime(String dateString) {
        // Define possible input formats
        SimpleDateFormat[] inputFormats = {
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()), // Format 1
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault()) // Format 2 (ISO 8601)
        };

        // Define output format
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        Date date = null;
        for (SimpleDateFormat inputFormat : inputFormats) {
            try {
                date = inputFormat.parse(dateString);
                break; // Stop trying if parsing is successful
            } catch (ParseException ignored) {
                // Ignore and try the next format
            }
        }

        // If no format succeeded, return an empty string
        return (date != null) ? outputFormat.format(date) : "";
    }


    public static String formatToDateTime(String dateString) {
        // Define possible input formats
        SimpleDateFormat[] inputFormats = {
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()), // Format 1
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault()) // Format 2 (ISO 8601)
        };

        // Define output format
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        Date date = null;
        for (SimpleDateFormat inputFormat : inputFormats) {
            try {
                date = inputFormat.parse(dateString);
                break; // Stop trying if parsing is successful
            } catch (ParseException ignored) {
                // Ignore and try the next format
            }
        }

        // If no format succeeded, return an empty string
        return (date != null) ? outputFormat.format(date) : "";
    }

    public static String formatToDateTimeHistory(String dateString) {
        // Define possible input formats
        SimpleDateFormat[] inputFormats = {
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()), // Format 1
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault()) // Format 2 (ISO 8601)
        };

        // Define output format
        SimpleDateFormat outputFormat = new SimpleDateFormat("M/d/yy hh:mm a", Locale.getDefault());

        Date date = null;
        for (SimpleDateFormat inputFormat : inputFormats) {
            try {
                date = inputFormat.parse(dateString);
                break; // Stop trying if parsing is successful
            } catch (ParseException ignored) {
                // Ignore and try the next format
            }
        }

        // If no format succeeded, return an empty string
        return (date != null) ? outputFormat.format(date) : "";
    }


    public static Date convertStringToDate(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            return simpleDateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return convertStringToDate(getOrderFormattedDate());
        }
    }

    public static String getFormattedDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE | h:mm", Locale.ENGLISH);
        return sdf.format(new Date());
    }

    public static String convertTo12Hour(String timeRange) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());

        String[] times = timeRange.split(" - ");
        if (times.length != 2) return timeRange; // fallback if input is invalid

        try {
            String start = outputFormat.format(inputFormat.parse(times[0]));
            String end = outputFormat.format(inputFormat.parse(times[1]));

            return start + " - " + end;
        } catch (ParseException e) {
            e.printStackTrace();
            return timeRange; // fallback in case of error
        }
    }

    public static String getCurrencySymbol(String currencyName) {
        if (currencyName == null || currencyName.isEmpty())
            return "$";
        Currency currency = Currency.getInstance(currencyName);
        return currency.getSymbol();
    }

    public static void logLongJson(String tag, String message) {
        int maxLogSize = 4000;
        for (int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = Math.min((i + 1) * maxLogSize, message.length());
            Log.d(tag, message.substring(start, end));
        }
    }


    public static String formatPrice(double price) {
        return String.format("%.2f", price);
    }


    public static String formatPriceWithDollarSymbol(double price) {
        BigDecimal rounded = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
        return "$" + rounded.toPlainString();
    }

    public static boolean isHexadecimal(String input) {
        return input != null && input.matches("^[0-9A-Fa-f]+$");
    }


    public static double calculateOriginalOrderTotal(double orderTotalAfterTip, String orderTip) {
        double tipAmount = 0.0;
        try {
            if (orderTip != null && !orderTip.trim().isEmpty()) {
                tipAmount = Double.parseDouble(orderTip.trim());
            }
        } catch (NumberFormatException e) {
            // Invalid tip, fallback to 0.0
            tipAmount = 0.0;
        }
        return orderTotalAfterTip - tipAmount;
    }

    public static String parseErrorMessage(String errorBody) {
        try {
            // Assuming the error body is JSON and contains a message field
            JSONObject jsonObject = new JSONObject(errorBody);
            return jsonObject.optString("error");
        } catch (Exception e) {
            Log.e("API_ERROR", "Error parsing error response: " + e.getMessage());
            return "";
        }
    }

    public static double calculateTip(double totalAmount, double tipPercent) {
        if (tipPercent < 0) {
            return 0.0;
        }
        return (totalAmount * tipPercent) / 100;
    }

    public static String subtractAndFormat(double value1, double value2) {
        double result = value1 - value2;
        if (result < 0) {
            result = 0.0;
        }
        DecimalFormat decimalFormat = new DecimalFormat("$0.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP); // <-- ensure proper rounding
        return decimalFormat.format(result);
    }


    public static double subtractAndFormatDouble(double value1, double value2) {
        double result = value1 - value2;
        if (result < 0) {
            result = 0.0;
        }
        return Math.round(result * 100.0) / 100.0;
    }

    public static String subtractAndFormatWithoutDollar(double value1, double value2) {
        double result = value1 - value2;
        if (result < 0) {
            result = 0.0;
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(result);
    }


    public static int convertTo24Hour(String time) {
        if (time == null || time.isEmpty()) {
            throw new IllegalArgumentException("Invalid time format");
        }

        time = time.toLowerCase().trim(); // Handle case-insensitivity and whitespace
        int hour;

        // Check if the time ends with "am" or "pm"
        if (time.endsWith("am")) {
            hour = Integer.parseInt(time.replace("am", "").trim());
            // Special case for 12am (midnight)
            if (hour == 12) {
                hour = 0;
            }
        } else if (time.endsWith("pm")) {
            hour = Integer.parseInt(time.replace("pm", "").trim());
            // Special case for 12pm (noon)
            if (hour != 12) {
                hour += 12;
            }
        } else {
            throw new IllegalArgumentException("Invalid time format: " + time);
        }

        return hour;
    }

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String getReservationCurrentDateFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String convertTo24HourFormat(String timeStr) {
        try {
            if (timeStr == null || timeStr.trim().isEmpty()) {
                return timeStr;
            }
            SimpleDateFormat format12Hour = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            SimpleDateFormat format24Hour = new SimpleDateFormat("HH:mm", Locale.getDefault());

            if (timeStr.toLowerCase().contains("am") || timeStr.toLowerCase().contains("pm") ||
                    timeStr.toLowerCase().contains("AM") || timeStr.toLowerCase().contains("PM")) {
                // It's in 12-hour format
                Date date = format12Hour.parse(timeStr);
                return format24Hour.format(date);
            } else {
                // Already in 24-hour format, ensure it's valid and return it formatted
                Date date = format24Hour.parse(timeStr);
                return format24Hour.format(date);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return timeStr;
        }
    }

    public static String convert24To12Hour(String time24) {
        try {
            SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm");
            SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a");

            Date date = sdf24.parse(time24);
            return sdf12.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return time24; // fallback if parse fails
        }
    }

    public static boolean isSelectedDateTimeInFuture(String dateStr, String timeStr) {
        if (dateStr == null || timeStr == null)
            return false;
        String fullDateTimeStr = dateStr + " " + timeStr; // e.g., "2025-05-04 08:30 PM"
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault());

        try {
            Date selectedDateTime = formatter.parse(fullDateTimeStr);
            Date now = new Date();
            return selectedDateTime != null && selectedDateTime.after(now);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static String bitmapToBase64(Bitmap bitmap) {
        String imagePrefix = "data:image/png;base64,";
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return imagePrefix + android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
    }

    public static void hideKeyboard(@NotNull View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String convertDateFormatInvoice(String originalDate) {
        // Define the original date format
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Define the desired format
        SimpleDateFormat targetFormat = new SimpleDateFormat("h:mm a - MMM dd, yyyy");

        try {
            // Parse the original date string into a Date object
            Date date = originalFormat.parse(originalDate);

            // Format the date into the new format
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;  // Return null in case of parsing error
        }
    }

    public static String convertDateFormatInvoiceTime(String originalDate) {
        // Define the original date format
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Define the desired format
        SimpleDateFormat targetFormat = new SimpleDateFormat("h:mm a");

        try {
            // Parse the original date string into a Date object
            Date date = originalFormat.parse(originalDate);

            // Format the date into the new format
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;  // Return null in case of parsing error
        }
    }

    public static String formatToDateOnly(String inputDate) {
        if (inputDate == null || inputDate.isEmpty()) return "";

        try {
            // Adjust format depending on your input format
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(inputDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
            return outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return inputDate; // fallback
        }
    }


    public static String calculateTaxAmount(double price, double taxPercent) {
        price = formatToTwoDecimalPlaces(price);
        taxPercent = formatToTwoDecimalPlaces(taxPercent);
        Log.d("TAG", "calculateTaxAmount: " + price + "   " + taxPercent);

        // No division by 100, because 0.25 should mean 0.25 as 25%
        double taxAmount = taxPercent * price;

        // Round to 2 decimal places
        taxAmount = Math.round(taxAmount * 100.0) / 100.0;

        // Return as a string with 2 decimal places
        return String.format("%.2f", taxAmount);
    }

    public static double formatToTwoDecimalPlaces(double amount) {
        return Double.parseDouble(String.format("%.2f", amount));
    }

    public static double calculateTaxAmountDouble(double price, double taxPercent) {
        price = formatToTwoDecimalPlaces(price);
        taxPercent = formatToTwoDecimalPlaces(taxPercent);

        // No division by 100, because 0.25 should mean 25%
        double taxAmount = taxPercent * price;

        // Round to 2 decimal places
        taxAmount = Math.round(taxAmount * 100.0) / 100.0;

        return taxAmount;
    }


    public static double calculateTotalAmountDouble(double subtotal, double taxAmount) {
        subtotal = formatToTwoDecimalPlaces(subtotal);
        taxAmount = formatToTwoDecimalPlaces(taxAmount);

        double totalAmount = subtotal + taxAmount;

        // Round to 2 decimal places
        totalAmount = Math.round(totalAmount * 100.0) / 100.0;

        return totalAmount;
    }

    public static String calculateTotalAmount(double subtotal, double taxPercent) {
        subtotal = formatToTwoDecimalPlaces(subtotal);
        taxPercent = formatToTwoDecimalPlaces(taxPercent);
        Log.d("TAG", "calculateTaxAmountDouble: " + subtotal + "   " + taxPercent);

        double taxAmount = taxPercent * subtotal;      // ✅ Remove /100
        double totalAmount = subtotal + taxAmount;

        totalAmount = Math.round(totalAmount * 100.0) / 100.0;
        return String.format("%.2f", totalAmount);
    }

    public static String formatCalendarToDate(Calendar calendar) {
        if (calendar == null) return "";

        SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy", Locale.getDefault());
        return formatter.format(calendar.getTime());
    }

    public static String formatCalendarToISOString(Calendar calendar) {
        if (calendar == null) return "";

        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
//        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        return isoFormat.format(calendar.getTime());
    }

    public static Pair<String, String> getDayRangeIfSame(Calendar start, Calendar end) {
        if (start == null || end == null) return null;

        boolean isSameDay = start.get(Calendar.YEAR) == end.get(Calendar.YEAR) &&
                start.get(Calendar.DAY_OF_YEAR) == end.get(Calendar.DAY_OF_YEAR);

        if (isSameDay) {
            Calendar startClone = (Calendar) start.clone();
            Calendar endClone = (Calendar) end.clone();

            // Set start to 00:00
            startClone.set(Calendar.HOUR_OF_DAY, 0);
            startClone.set(Calendar.MINUTE, 0);
            startClone.set(Calendar.SECOND, 0);
            startClone.set(Calendar.MILLISECOND, 0);

            // Set end to 11:59
            endClone.set(Calendar.HOUR_OF_DAY, 11);
            endClone.set(Calendar.MINUTE, 59);
            endClone.set(Calendar.SECOND, 59);
            endClone.set(Calendar.MILLISECOND, 999);

            String formattedStart = formatCalendarToISOString(startClone);
            String formattedEnd = formatCalendarToISOString(endClone);

            return new Pair<>(formattedStart, formattedEnd);
        }

        return null;
    }
}
