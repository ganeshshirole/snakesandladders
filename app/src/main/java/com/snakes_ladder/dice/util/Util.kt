package com.snakes_ladder.dice.util

import android.content.Context
import android.content.res.Resources
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Patterns

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object Util {
    fun isValidEmail(email: CharSequence): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhoneNumber(phoneNumber: CharSequence): Boolean {
        return !TextUtils.isEmpty(phoneNumber) && Patterns.PHONE.matcher(phoneNumber).matches()
    }

    fun readFromAssets(context: Context, filename: String): String {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(InputStreamReader(context.assets.open(filename)))

            // do reading, usually loop until end of file reading
            val sb = StringBuilder()
            var mLine: String? = reader.readLine()
            while (mLine != null) {
                sb.append(mLine) // process line
                mLine = reader.readLine()
            }
            reader.close()
            return sb.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return ""
    }

    fun convertPixelsToDp(px: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        val dp = px / (metrics.densityDpi / 160f)
        return Math.round(dp).toFloat()
    }

    fun convertDpToPixel(dp: Float): Int {
        val metrics = Resources.getSystem().displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return Math.round(px)
    }

}
