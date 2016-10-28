package com.rajeshsaini.dmr.demo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DMRSAINI on 3/11/2016.
 */
public class MySharedPreferences {
    private static final String USER_CREDENTIALS="rajesh";
    public static void setSharedPreferences(Context context,String key,String value){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,0);
        setting.edit()
                .putString(key,value)
                .commit();
    }
    public static String getSharedPreferences(Context context,String key){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,0);
        return setting.getString(key,"");
    }
    public static void logOut(Context context){
        SharedPreferences setting= context.getSharedPreferences(USER_CREDENTIALS,0);
        setting.edit()
                .clear()
                .commit();
    }
}
