package com.apitap.model.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ashok-kumar on 9/1/16.
 */
public class ATPreferences {

    public static String readString(Context context,String key){
        return getPreference(context).getString(key,"");
    }

    private static SharedPreferences getPreference(Context context){
       return context.getSharedPreferences("AT_PREF", Context.MODE_PRIVATE);
    }

    public static void putString(Context context,String key,String value){
        getPreference(context).edit().putString(key,value).apply();
    }
    public static void putInt(Context context,String key,Integer value){
        getPreference(context).edit().putInt(key,value).commit();
    }

    public static void putBoolean(Context context,String key,boolean value){
        getPreference(context).edit().putBoolean(key,value).commit();
    }

    public static String getString(Context context , String key , String value){
        return  getPreference(context).getString(key,value);
    }
    public static Integer getInt(Context context , String key , Integer value){
        return  getPreference(context).getInt(key,value);
    }
    public static boolean readBoolean(Context context,String key){
        return getPreference(context).getBoolean(key,false);
    }
    public static void clearPref(Context context,String key){
        getPreference(context).edit().remove(key).apply();
    }
    public static void clearAllPref(Context context){
        getPreference(context).edit().clear().commit();
    }
}
