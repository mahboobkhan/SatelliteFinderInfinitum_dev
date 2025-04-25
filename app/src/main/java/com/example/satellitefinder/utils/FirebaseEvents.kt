package com.example.satellitefinder.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

object FirebaseEvents {

    fun logEvent(key: String, value: String) {
        Firebase.analytics.logEvent(key, Bundle().apply {
            putString(key, value)
        })
    }

    fun logEventActivity(screenName: String, className: String) {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW){
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, className)
        }
    }

}