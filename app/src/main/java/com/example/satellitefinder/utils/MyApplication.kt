package com.example.satellitefinder.utils

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.satellitefinder.admobAds.OpenApp
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.leveler.util.PreferenceHelper
import com.google.android.gms.ads.nativead.NativeAd
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level
import java.util.concurrent.Executors

class MyApplication : Application(), LifecycleObserver {


    companion object {
        var isForegrounded = false
        var canRequestAdByConsent=true
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        GlobalContext.startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            modules(AppModule.getModule)
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (RemoteConfig.appOpen){
            OpenApp(this)
        }

        PreferenceHelper.initPrefs(this)
    }
}