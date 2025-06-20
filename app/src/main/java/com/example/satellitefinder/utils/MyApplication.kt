package com.example.satellitefinder.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.adssdk.Ads
import com.example.adssdk.open_app_ad.OpenAppAd
import com.example.satellitefinder.BuildConfig
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.leveler.util.PreferenceHelper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level

class MyApplication : Application(), LifecycleObserver {


    companion object {
        var isForegrounded = false
        var canRequestAdByConsent = true
    }

    override fun onCreate() {
        super.onCreate()

        Ads(
            this,
            premiumUser = isAlreadyPurchased(),
            listOf(),
            BuildConfig.BUILD_TYPE,
            if (BuildConfig.DEBUG) "appDev" else "appProd"
        )

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        GlobalContext.startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            modules(AppModule.getModule)
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (RemoteConfig.appOpen) {
//            OpenApp(this)
            OpenAppAd(
                application = this@MyApplication,
                openAppAdIdLow = getString(R.string.appOpenId),
                openAppAdIdMedium = null,
                openAppAdIdHigh = null
            ).setReloadOnPause(true).setReloadOnDismiss(value = false)
        }

        PreferenceHelper.initPrefs(this)
    }
}