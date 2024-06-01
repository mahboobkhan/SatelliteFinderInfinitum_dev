package com.example.satellitefinder.utils

import android.content.Context


open class MyBaseConfig(val context: Context) {
    protected val prefs = context.getSharedPrefs()

    companion object {
        fun newInstance(context: Context) = MyBaseConfig(context)
    }

    var isOnPermissionDone: Boolean
        get() = prefs.getBoolean(IS_PERMISSION_DONE, false)
        set(i) = prefs.edit().putBoolean(IS_PERMISSION_DONE, i).apply()
    var isOnBoardingDone: Boolean
        get() = prefs.getBoolean(IS_ON_BOARDING_DONE, false)
        set(isOnBoardingDone) = prefs.edit().putBoolean(IS_ON_BOARDING_DONE, isOnBoardingDone).apply()

}

