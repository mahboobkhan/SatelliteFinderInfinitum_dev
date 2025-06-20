package com.example.satellitefinder.utils

import android.content.Context
import android.content.SharedPreferences

object MySharePrefrencesHelper {

    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    fun putKey(context: Context, Key: String?, Value: String?): String? {
        sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE)
        editor = sharedPreferences?.edit()
        editor?.putString(Key, Value)
        editor?.commit()
        return Key
    }


    fun getKey(contextGetKey: Context, Key: String?, default: String?): String? {
        sharedPreferences =
            contextGetKey.getSharedPreferences("Cache", Context.MODE_PRIVATE)
        return sharedPreferences?.getString(Key, default)
    }


    fun deleteAllSharedPrefs() {
        sharedPreferences!!.edit().clear().commit()
    }

    fun putBoolean(context: Context, key: String, value: Boolean) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(
                "Cache", Context.MODE_PRIVATE
            )
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(context: Context, key: String, default: Boolean = false): Boolean {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(
                "Cache", Context.MODE_PRIVATE
            )
        return sharedPref.getBoolean(key, default)
    }

    fun putBillingBoolean(context: Context, value: Boolean) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("key_subscription", value)
        editor.apply()
    }

    fun getBillingBoolean(context: Context, default: Boolean = false): Boolean {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return sharedPref.getBoolean("key_subscription", default)
    }
}