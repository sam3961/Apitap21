package com.apitap.model;

import android.app.Notification;
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

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.apitap.R;
import com.apitap.model.customclasses.Event;
import com.apitap.model.preferences.ATPreferences;
import com.apitap.views.HomeActivity;
import com.apitap.views.MessageDetailActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/*
 * Created by rishav on 6/3/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public static String msg = "A merchant you are following as a favorite has something new to show you. Check out its new Product ";
    public static String amount, requestid, productName = "", storeName = "";
    JSONObject jsonObject;
    NotificationUtils notificationUtils;
    public static String CHANNEL_ID = "1";
    public static String productId = "";
    public static String adId = "";
    public static String invoiceId = "";
    public static String generalMessageId = "";
    public static int msgCount = 0;
    public static NotificationManager notifManager;
    public static int badgeCount = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        //{"101":"010400265","114.70":"4C412053757065722053746F7265","122.114":"18729"}
        Log.d(TAG, "From:" + remoteMessage.getFrom());

        Log.e(TAG, "message" + remoteMessage.getData().toString());
        badgeCount++;
//        QuickBadger.INSTANCE.provideBadger(getApplicationContext()).showBadge(badgeCount);
        //sendNotification(remoteMessage.getFrom() +"aPITAP");

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

//{"msg":{"101":"010400265","114.70":"4C412053757065722053746F7265"}}
            try {
                jsonObject = new JSONObject(remoteMessage.getData().toString());
                JSONObject jsonObject1 = jsonObject.getJSONObject("msg");
                Log.d("Notification_Data", jsonObject + "");
                Logger.addRecordToLog("notification data===>>>>" + jsonObject);
                if (jsonObject1.has("101") && jsonObject1.getString("101").equals("010400265")) {
                    msgCount++;
                    if (jsonObject1.has("114.144")) {
                        productId = jsonObject1.getString("114.144");
                        adId = "";
                        invoiceId = "";
                        generalMessageId="";
                    }
                    if (jsonObject1.has("123.21")) {
                        adId = jsonObject1.getString("123.21");
                        invoiceId="";
                        productId="";
                        generalMessageId="";
                    }
                    if (jsonObject1.has("121.75")) {
                        invoiceId = jsonObject1.getString("121.75");
                        productId = "";
                        adId ="";
                        generalMessageId="";
                    }
                    if (jsonObject1.has("122.114")) {
                        generalMessageId = jsonObject1.getString("122.114");
                        productId = "";
                        adId ="";
                        invoiceId="";
                    }
                    if (jsonObject1.has("120.83"))
                        productName = Utils.hexToASCII(jsonObject1.getString("120.83"));


                    if (jsonObject1.has("114.70"))
                        storeName = Utils.hexToASCII(jsonObject1.getString("114.70"));
                    sendNotify(getApplicationContext(), "New Message from " + Utils.hexToASCII(
                            jsonObject1.getString("114.70")), HomeActivity.class);
                    EventBus.getDefault().post(new Event(Constants.FCM_MSG_NOTIFICATION, ""));
                    return;

                } else {
                    if (jsonObject1.has("114.144")) {
                        productId = jsonObject1.getString("114.144");
                        adId="";
                        invoiceId = "";
                        generalMessageId="";
                    }
                    if (jsonObject1.has("123.21")) {
                        adId = jsonObject1.getString("123.21");
                        invoiceId="";
                        productId="";
                        generalMessageId="";
                    }
                    if (jsonObject1.has("121.75")) {
                        invoiceId = jsonObject1.getString("121.75");
                        adId="";
                        productId="";
                        generalMessageId="";
                    }
                    if (jsonObject1.has("122.114")) {
                        generalMessageId = jsonObject1.getString("122.114");
                        productId = "";
                        adId ="";
                        invoiceId="";
                    }

                    if (jsonObject1.has("120.83"))
                        productName = Utils.hexToASCII(jsonObject1.getString("120.83"));
                    EventBus.getDefault().post(new Event(Constants.NOTIFICATION_ARRIVED, ""));
                }

            } catch (JSONException e) {
                e.printStackTrace();

            }

        } else {
            try {
                jsonObject = new JSONObject(remoteMessage.getData().toString());
                JSONObject jsonObject2 = jsonObject.getJSONObject("msg");
                Log.d("Notification_Data", jsonObject + "");
                Logger.addRecordToLog("notification data===>>>>" + jsonObject);
                if (jsonObject2.has("101") && jsonObject2.getString("101").equals("010400265")) {

                    if (jsonObject2.has("114.144")) {
                        productId = jsonObject2.getString("114.144");
                        adId="";
                        invoiceId="";
                        generalMessageId="";
                    }
                    if (jsonObject2.has("123.21")) {
                        adId = jsonObject2.getString("123.21");
                        productId="";
                        invoiceId="";
                        generalMessageId="";
                    }
                    if (jsonObject2.has("121.75")) {
                        invoiceId = jsonObject2.getString("121.75");
                        adId="";
                        productId="";
                        generalMessageId="";
                    }
                    if (jsonObject2.has("122.114")) {
                        generalMessageId = jsonObject2.getString("122.114");
                        productId = "";
                        adId ="";
                        invoiceId="";
                    }
                    if (jsonObject2.has("120.83"))
                        productName = Utils.hexToASCII(jsonObject2.getString("120.83"));
                    if (jsonObject2.has("114.70"))
                        storeName = Utils.hexToASCII(jsonObject2.getString("114.70"));
                        sendNotify(getApplicationContext(), "New Message from " + Utils.hexToASCII(
                                jsonObject2.getString("114.70")), HomeActivity.class);
                    // EventBus.getDefault().post(new Event(Constants.FCM_MSG_NOTIFICATION, ""));
                    return;
                } else {

                    //   JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    JSONObject jsonObject3 = jsonObject.getJSONObject("msg");
                    if (jsonObject3.has("114.144")) {
                        productId = jsonObject3.getString("114.144");
                        adId="";
                        invoiceId="";
                        generalMessageId="";
                    }
                    if (jsonObject3.has("123.21")) {
                        adId = jsonObject3.getString("123.21");
                        productId="";
                        invoiceId= "";
                        generalMessageId="";
                    }
                    if (jsonObject3.has("121.75")) {
                        invoiceId = jsonObject3.getString("121.75");
                        adId="";
                        productId="";
                        generalMessageId="";
                    }
                    if (jsonObject3.has("122.114")) {
                        generalMessageId = jsonObject3.getString("122.114");
                        productId = "";
                        adId ="";
                        invoiceId="";
                    }

                    //productId = jsonObject3.getString("114.144");
                    if (jsonObject3.has("120.83"))
                        productName = Utils.hexToASCII(jsonObject3.getString("120.83"));
                    // app is in background, show the notification in notification tray
                    ///Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    //LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
                    //showNotificationMessage(getApplicationContext(), "ApiTap", msg, Utils.GetToday(), resultIntent);\
                    // sendmessage(jsonObject1.getString(Utils.hexToASCII(productName)));
                    sendNotify(getApplicationContext(), msg + productName, HomeActivity.class);
                    //EventBus.getDefault().post(new Event(Constants.NOTIFICATION_ARRIVED, ""));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                if (jsonObject.has("101"))
                    try {
                        if (jsonObject.getString("101").equals("010400265"))
                            Toast.makeText(getApplicationContext(), "New Message Received", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "New Arrivals Received", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        });

        // Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());


        //Calling method to generate notification
        //  sendNotification(remoteMessage.getBody());
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    public void sendmessage(String message) {
        Intent pushNotification = new Intent(NotificationUtils.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", "A merchant you are following as a favorite has something new to show you. Check it out " +
                message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        new NotificationUtils(getApplicationContext()).playNotificationSound();
    }

    public static void sendNotify(Context context, String messageBody, Class appCompatActivity) {
        if (ATPreferences.readString(context, Constants.StaySignedIn).equals("false"))
            return;
        ATPreferences.putBoolean(context, Constants.NOTIFICATION_BACKGROUND, true);
        notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Apitap";
            int importance = NotificationManager.IMPORTANCE_HIGH; //Set the importance level
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setLightColor(Color.GREEN); //Set if it is necesssary
            mChannel.enableVibration(true);
            mChannel.setShowBadge(true);
            // Set the Notification Channel for the Notification Manager.
            notifManager.createNotificationChannel(mChannel);
        }


        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder = new NotificationCompat.Builder(context,"Apitap")
                .setContentTitle("Apitap")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_app_notification)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setNumber(badgeCount)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_app_notification))
                .setSound(soundUri)
                .setContentText("A merchant you are following as a favorite has something new to show you. Check it out " +
                        messageBody)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(CHANNEL_ID); // Channel ID
        }


        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notifManager.notify(m, mBuilder.build());
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
        intent.putExtra("merchantId", "");
        intent.putExtra("merchantName", storeName);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pendingIntent);
        notifManager.notify(m, mBuilder.build());

    }


    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {
//        Intent intent = new Intent(this, SplashActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_app_notification)
                .setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary))
                .setContentTitle("Apitap Push Notification")
                .setContentText("A merchant you are following as a favorite has posted new products, promotions, or ads. Please check them out in ApiTap to see what new they have available.")
                .setAutoCancel(true)
                .setSound(defaultSoundUri);
        //  .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}