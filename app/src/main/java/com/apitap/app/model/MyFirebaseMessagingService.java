package com.apitap.app.model;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.widget.Toast;

import com.apitap.app.R;
import com.apitap.app.controller.ModelManager;
import com.apitap.app.model.customclasses.Event;
import com.apitap.app.model.preferences.ATPreferences;
import com.apitap.app.views.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

/*
 * Created by rishav on 6/3/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static String msg = "A merchant you are following as a favorite has something new to show you. Check out its new Product ";
    public static String amount, requestid, productName = "", storeName = "";
    JSONObject jsonObject;
    NotificationUtils notificationUtils;
    public static String productId = "";
    public static String merchantId = "";
    public static String adId = "";
    public static String invoiceId = "";
    public static String generalMessageId = "";
    public static String messageId = "";
    public static int msgCount = 0;
    public static NotificationManager notifManager;
    public static int badgeCount = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try {
            if (remoteMessage.getFrom() != null)
                Log.d(TAG, "From:" + remoteMessage.getFrom());
            Log.e(TAG, "Data = " + remoteMessage.getData());
            if (remoteMessage.getNotification() != null)
                Log.e(TAG, "Notification = " + remoteMessage.getNotification());
        } catch (Exception e) {
            e.printStackTrace();
        }

        badgeCount++;

//        String msgString = remoteMessage.getData().get("msg");
        Map<String, String> data = remoteMessage.getData();

        String msgString = data.get("msg");
        String title = data.get("title");
        String body = data.get("body");
        String type = data.get("type");

        if (msgString == null || msgString.isEmpty()) {
            Log.e(TAG, "msg field missing in FCM payload");
            return;
        }

        try {

            JSONObject msgJson = new JSONObject(msgString);

            // Keep reference for toast logic
            jsonObject = msgJson;

            Log.d("Notification_Data", msgJson.toString());
            //            Logger.addRecordToLog("notification data===>>>>" + msgJson);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

                if (msgJson.has("101")
                        && "010400265".equals(msgJson.getString("101"))) {

                    msgCount++;

                    if (msgJson.has("114.144")) {
                        productId = msgJson.getString("114.144");
                        adId = "";
                        invoiceId = "";
                        generalMessageId = "";
                    }
                    if (msgJson.has("53")) {
                        merchantId = msgJson.getString("53");
                    }

                    if (msgJson.has("114.150")) {
                        messageId = msgJson.getString("114.150");
                    }

                    if (msgJson.has("123.21")) {
                        adId = msgJson.getString("123.21");
                        invoiceId = "";
                        productId = "";
                        generalMessageId = "";
                    }

                    if (msgJson.has("121.75")) {
                        invoiceId = msgJson.getString("121.75");
                        productId = "";
                        adId = "";
                        generalMessageId = "";
                    }

                    if (msgJson.has("122.114")) {
                            generalMessageId = msgJson.getString("122.114");
                        productId = "";
                        adId = "";
                        invoiceId = "";
                    }

                    if (msgJson.has("120.83")) {
                        productName = Utils.hexToASCII(
                                msgJson.getString("120.83"));
                    }

                    if (msgJson.has("114.70")) {
                        storeName = Utils.hexToASCII(
                                msgJson.getString("114.70"));
                    }

                    sendNotify(
                            getApplicationContext(),
                            "New Message from " + storeName,
                            title,
                            body,
                            type,
                            HomeActivity.class
                    );

                    EventBus.getDefault().post(
                            new Event(Constants.FCM_MSG_NOTIFICATION, "")
                    );

                    return;

                } else {

                    if (msgJson.has("53")) {
                        merchantId = msgJson.getString("53");
                    }

                    if (msgJson.has("114.144")) {
                        productId = msgJson.getString("114.144");
                        adId = "";
                        invoiceId = "";
                        generalMessageId = "";
                    }

                    if (msgJson.has("123.21")) {
                        adId = msgJson.getString("123.21");
                        invoiceId = "";
                        productId = "";
                        generalMessageId = "";
                    }

                    if (msgJson.has("121.75")) {
                        invoiceId = msgJson.getString("121.75");
                        adId = "";
                        productId = "";
                        generalMessageId = "";
                    }

                    if (msgJson.has("122.114")) {
                        generalMessageId = msgJson.getString("122.114");
                        productId = "";
                        adId = "";
                        invoiceId = "";
                    }

                    if (msgJson.has("120.83")) {
                        productName = Utils.hexToASCII(
                                msgJson.getString("120.83"));
                    }

                    sendNotify(
                            getApplicationContext(),
                            msg + productName,
                            title,
                            body,
                            type,
                            HomeActivity.class
                    );

                    EventBus.getDefault().post(
                            new Event(Constants.NOTIFICATION_ARRIVED, "")
                    );
                }

            } else {

                if (msgJson.has("101")
                        && "010400265".equals(msgJson.getString("101"))) {

                    if (msgJson.has("114.144")) {
                        productId = msgJson.getString("114.144");
                        adId = "";
                        invoiceId = "";
                        generalMessageId = "";
                    }

                    if (msgJson.has("123.21")) {
                        adId = msgJson.getString("123.21");
                        productId = "";
                        invoiceId = "";
                        generalMessageId = "";
                    }

                    if (msgJson.has("121.75")) {
                        invoiceId = msgJson.getString("121.75");
                        adId = "";
                        productId = "";
                        generalMessageId = "";
                    }

                    if (msgJson.has("122.114")) {
                        generalMessageId = msgJson.getString("122.114");
                        productId = "";
                        adId = "";
                        invoiceId = "";
                    }

                    if (msgJson.has("120.83")) {
                        productName = Utils.hexToASCII(
                                msgJson.getString("120.83"));
                    }

                    if (msgJson.has("114.70")) {
                        storeName = Utils.hexToASCII(
                                msgJson.getString("114.70"));
                    }
                    if (msgJson.has("53")) {
                        merchantId = msgJson.getString("53");
                    }

                    sendNotify(
                            getApplicationContext(),
                            "New Message from " + storeName,
                            title,
                            body,
                            type,
                            HomeActivity.class
                    );

                    return;

                } else {

                    if (msgJson.has("53")) {
                        merchantId = msgJson.getString("53");
                    }

                    if (msgJson.has("114.144")) {
                        productId = msgJson.getString("114.144");
                        adId = "";
                        invoiceId = "";
                        generalMessageId = "";
                    }

                    if (msgJson.has("123.21")) {
                        adId = msgJson.getString("123.21");
                        productId = "";
                        invoiceId = "";
                        generalMessageId = "";
                    }

                    if (msgJson.has("121.75")) {
                        invoiceId = msgJson.getString("121.75");
                        adId = "";
                        productId = "";
                        generalMessageId = "";
                    }

                    if (msgJson.has("122.114")) {
                        generalMessageId = msgJson.getString("122.114");
                        productId = "";
                        adId = "";
                        invoiceId = "";
                    }

                    if (msgJson.has("120.83")) {
                        productName = Utils.hexToASCII(
                                msgJson.getString("120.83"));
                    }

                    sendNotify(
                            getApplicationContext(),
                            msg + productName,
                            title,
                            body,
                            type,
                            HomeActivity.class
                    );
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "FCM Parse Error", e);
        }

        new Handler(Looper.getMainLooper()).post(() -> {
            try {

                if (jsonObject != null && jsonObject.has("101")) {

                    if ("010400265".equals(
                            jsonObject.getString("101"))) {

                        Toast.makeText(
                                getApplicationContext(),
                                "New Message Received",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        Toast.makeText(
                                getApplicationContext(),
                                "New Arrivals Received",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void sendNotify(Context context, String messageBody,
                                  String title,
                                  String body,
                                  String type,
                                  Class appCompatActivity) {
        String channelId = context.getString(R.string.default_notification_channel_id);

        if (ATPreferences.readString(context, Constants.StaySignedIn).equals("false"))
            return;
        ATPreferences.putBoolean(context, Constants.NOTIFICATION_BACKGROUND, true);
        notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notification_logo)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setNumber(badgeCount)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification_logo))
                .setSound(soundUri)
//                .setContentText("A merchant you are following as a favorite has something new to show you. Check it out " + messageBody)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(channelId); // Channel ID
        }
        mBuilder.setGroup(type);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        Intent intent = new Intent(context, appCompatActivity);

        if (!productId.isEmpty()) {
            intent.putExtra("productId", productId);
            intent.putExtra("productName", productName);
        } else if (!adId.isEmpty()) {
            intent.putExtra("adID", adId);
            intent.putExtra("adName", "");
        } else if (!invoiceId.isEmpty())
            intent.putExtra("invoice", invoiceId);
        else if (!generalMessageId.isEmpty())
            intent.putExtra("generalId", generalMessageId);
        intent.putExtra("merchantId", merchantId);
        intent.putExtra("merchantName", storeName);
        intent.putExtra("type", type);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Log.e("FCM_CLICK",
                "generalId=" + generalMessageId +
                        " productId=" + productId +
                        " adID=" + adId +
                        " merchantName=" + storeName +
                        " productName=" + productName +
                        " invoiceId=" + invoiceId);

        int requestCode = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        mBuilder.setContentIntent(pendingIntent);
        notifManager.notify(m, mBuilder.build());

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        ModelManager.getInstance().getLoginManager().registerFCM(getApplicationContext(),
                Operations.makeJsonGetFCM(getApplicationContext(), token));
    }
}