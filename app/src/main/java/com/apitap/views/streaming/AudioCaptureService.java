package com.apitap.views.streaming;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class AudioCaptureService extends Service {

    private MediaProjectionManager mediaProjectionManager;


    public static String LOG_TAG = "AudioCaptureService";
    public static int SERVICE_ID = 123;
    public static String NOTIFICATION_CHANNEL_ID = "AudioCapture channel";
    public MediaProjection mediaProjection;


    public static int NUM_SAMPLES_PER_READ = 1024;
    public static int BYTES_PER_SAMPLE = 2; // 2 bytes since we hardcoded the PCM 16-bit format
    public static int BUFFER_SIZE_IN_BYTES = NUM_SAMPLES_PER_READ * BYTES_PER_SAMPLE;

    public static final String ACTION_START = "AudioCaptureService:Start";
    public static final String ACTION_STOP = "AudioCaptureService:Stop";
    public static final String EXTRA_RESULT_DATA = "AudioCaptureService:Extra:ResultData";

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            createNotificationChannel();
        }

        startForeground(
                SERVICE_ID,
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).build()
        );

        // use applicationContext to avoid memory leak on Android 10.
        // see: https://partnerissuetracker.corp.google.com/issues/139732252
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaProjectionManager = (MediaProjectionManager) getApplicationContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        }


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Audio Capture Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getApplicationContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            switch (intent.getAction()) {
                case ACTION_START: {
                    mediaProjection =
                            mediaProjectionManager.getMediaProjection(
                                    Activity.RESULT_OK,
                                    intent.getParcelableExtra(EXTRA_RESULT_DATA));
                    return Service.START_STICKY;
                }
                case ACTION_STOP: {
                    stopAudioCapture();
                    return Service.START_NOT_STICKY;
                }
                default:
                    break;
            }
        } else {
            return Service.START_NOT_STICKY;
        }
        return Service.START_NOT_STICKY;
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private void stopAudioCapture() {
        mediaProjection.stop();
        stopSelf();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}