package com.apitap.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.apitap.app.R;
import com.apitap.app.model.AddTabBar;
import com.apitap.app.model.Constants;
import com.apitap.app.model.ProgressDialogLoading;

import com.apitap.app.model.preferences.ATPreferences;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.FirebaseApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sourcefuse on 16/12/16.
 */

public class App extends Application {
    public HashMap<String, ArrayList<String>> mHashMap;
    public  ArrayList<String> categoryArray = new ArrayList<>();
    public  ArrayList<String> subCategoryArray = new ArrayList<>();
    public String userAgent;
    public static Double latitude=0.0,longitude=0.0;
    public static boolean isGuest = false;

    @Override
    public void onTerminate() {
        super.onTerminate();
        ATPreferences.putBoolean(getApplicationContext(), Constants.HEADER_STORE,false);
        ATPreferences.putString(getApplicationContext(), Constants.MERCHANT_ID, "");
    }

    public static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        FirebaseApp.initializeApp(this);
        instance = this;
        userAgent = Util.getUserAgent(this, "ExoPlayerDemo");
        ATPreferences.putBoolean(getApplicationContext(), Constants.HEADER_STORE,false);
        ATPreferences.putString(getApplicationContext(), Constants.MERCHANT_ID, "");
        new ProgressDialogLoading();
        new AddTabBar();
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                NotificationChannel existingChannel =
                        notificationManager.getNotificationChannel(channelId);

                if (existingChannel == null) {
                    NotificationChannel channel = new NotificationChannel(
                            channelId,
                            channelName,
                            NotificationManager.IMPORTANCE_HIGH
                    );
                    channel.setDescription("General app notifications");
                    channel.enableVibration(true);
                    channel.enableLights(true);

                    notificationManager.createNotificationChannel(channel);
                }
            }
        }
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static App getInstance() {
        return instance;
    }

    public HashMap<String, ArrayList<String>> getmHashMap() {
        return mHashMap;
    }

    public void setmHashMap(HashMap<String, ArrayList<String>> mHashMap) {
        this.mHashMap = mHashMap;
    }

    public ArrayList<String> getCategoryArray() {
        return categoryArray;
    }

    public void setCategoryArray(ArrayList<String> categoryArray) {
        this.categoryArray = categoryArray;
    }

    public ArrayList<String> getSubCategoryArray() {
        return subCategoryArray;
    }

    public void setSubCategoryArray(ArrayList<String> subCategoryArray) {
        this.subCategoryArray = subCategoryArray;
    }

    public void saveHashmap(HashMap<String, ArrayList<String>> myHashMap) {
        File aioDir = new File(getExternalCacheDir() + "/APITAP/" + "/temps");
        if (!aioDir.exists())
            aioDir.mkdirs();
        ObjectOutputStream objectOutputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(aioDir + "/tt.tmp");
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(myHashMap);
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, ArrayList<String>> getSaveHashMap() {
        File aioDir = new File(getExternalCacheDir() + "/APITAP/" + "/temps");
        FileInputStream fileInputStream = null;
        HashMap<String, ArrayList<String>> myNewlyReadInMap = new HashMap<String, ArrayList<String>>();
        ObjectInputStream objectInputStream = null;
        try {
            fileInputStream = new FileInputStream(aioDir + "/tt.tmp");
            objectInputStream = new ObjectInputStream(fileInputStream);
            myNewlyReadInMap = (HashMap) objectInputStream.readObject();
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myNewlyReadInMap;
    }

    public void saveArrayList(ArrayList<String> categoryArray) {
        File aioDir = new File(getExternalCacheDir() + "/APITAP/" + "/temps");
        if (!aioDir.exists())
            aioDir.mkdirs();
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {

            fileOutputStream = new FileOutputStream(aioDir + "/t.tmp");
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(categoryArray);
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public ArrayList<String> getSaveArrayList() {
        File aioDir = new File(getExternalCacheDir() + "/APITAP/" + "/temps");
        FileInputStream fileInputStream = null;
        ArrayList<String> both = new ArrayList<String>();
        try {
            fileInputStream = new FileInputStream(aioDir +"/t.tmp");
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            both = (ArrayList<String>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return both;
    }

    public DataSource.Factory buildDataSourceFactory(TransferListener listener) {
        return new DefaultDataSourceFactory(this, listener, buildHttpDataSourceFactory(listener));
    }

    /** Returns a {@link HttpDataSource.Factory}. */
    public HttpDataSource.Factory buildHttpDataSourceFactory(TransferListener listener) {
        return new DefaultHttpDataSourceFactory(userAgent, listener);
    }

    public boolean useExtensionRenderers() {
        return false;
    }

}
