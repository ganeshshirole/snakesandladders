package com.snakes_ladder.dice.util;

import android.content.Context;

public class SharedPreferences {

    private static String prefName = "dice";

    // Store data in Shared Preference
    public static void saveString(Context context, String key,
                            String value) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Store data in Shared Preference
    public static void saveInt(Context context, String key,
                            int value) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    // Store data in Shared Preference
    public static void saveBool(Context context, String key,
                               boolean value) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String readString(Context context, String key) {
        android.content.SharedPreferences preference = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE);
        return preference.getString(key, "");
    }

    public static int readInt(Context context, String key) {
        android.content.SharedPreferences preference = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE);
        return preference.getInt(key, 0);
    }

    public static boolean readBool(Context context, String key) {
        android.content.SharedPreferences preference = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE);
        return preference.getBoolean(key, false);
    }

    // Delete all data from Shared Preference
    public static void clearAll(Context context) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE).edit();

        editor.clear();
        editor.apply();

    }

    // Delete specific data from Shared Preference
    public static void clearOne(Context context, String key) {
        android.content.SharedPreferences.Editor editor = context.getSharedPreferences(
                prefName, Context.MODE_PRIVATE).edit();
        editor.remove(key);
        editor.apply();
    }

    public interface PREFERENCES_KEY {
        String USER_NAME = "name";
        String USER_IMAGE = "image";
        String USER_MOBILE = "mobile";
        String USER_ID = "userid";
        String LOGIN_STATUS = "login_status";
        String USER_ADDRESS = "address";
        String USER_TYPE = "type";
    }
}