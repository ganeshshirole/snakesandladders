package com.snakes_ladder.dice.util

import android.content.Context

object SharedPreferences {

    private val prefName = "dice_tile"

    // Store data in Shared Preference
    fun saveString(context: Context, key: String,
                   value: String) {
        val editor = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE).edit()
        editor.putString(key, value)
        editor.apply()
    }

    // Store data in Shared Preference
    fun saveInt(context: Context, key: String,
                value: Int) {
        val editor = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE).edit()
        editor.putInt(key, value)
        editor.apply()
    }

    // Store data in Shared Preference
    fun saveBool(context: Context, key: String,
                 value: Boolean) {
        val editor = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE).edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun readString(context: Context, key: String): String {
        val preference = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE)
        return preference.getString(key, "")
    }

    fun readInt(context: Context, key: String): Int {
        val preference = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE)
        return preference.getInt(key, 0)
    }

    fun readBool(context: Context, key: String): Boolean {
        val preference = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE)
        return preference.getBoolean(key, false)
    }

    // Delete all data from Shared Preference
    fun clearAll(context: Context) {
        val editor = context.getSharedPreferences(prefName,
                Context.MODE_PRIVATE).edit()

        editor.clear()
        editor.apply()

    }

    // Delete specific data from Shared Preference
    fun clearOne(context: Context, key: String) {
        val editor = context.getSharedPreferences(
                prefName, Context.MODE_PRIVATE).edit()
        editor.remove(key)
        editor.apply()
    }

    interface PREFERENCES_KEY {
        companion object {
            val USER_NAME = "name"
            val USER_IMAGE = "image"
            val USER_MOBILE = "mobile"
            val USER_ID = "userid"
            val LOGIN_STATUS = "login_status"
            val USER_ADDRESS = "address"
            val USER_TYPE = "type"
        }
    }
}