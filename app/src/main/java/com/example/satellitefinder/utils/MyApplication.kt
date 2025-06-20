package com.example.satellitefinder.utils

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.adssdk.Ads
import com.example.adssdk.open_app_ad.OpenAppAd
import com.example.satellitefinder.BuildConfig
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.leveler.util.PreferenceHelper
import com.example.satellitefinder.repo.SatelliteDataManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level

class MyApplication : Application(), LifecycleObserver {

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

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
        
        // Start pre-loading satellite data
        startSatelliteDataPreload()
    }
    
    private fun startSatelliteDataPreload() {
        appScope.launch {
            try {
                Log.d("MyApplication", "Starting satellite data preload")
                val dataManager = GlobalContext.get().get<SatelliteDataManager>()
                
                // Check internet connectivity before starting preload
                if (!isInternetConnected(this@MyApplication)) {
                    Log.w("MyApplication", "No internet connection available for satellite data preload")
                    return@launch
                }
                
                // Set up callback to log when data is loaded
                dataManager.onDataLoaded = { satelliteType ->
                    Log.d("MyApplication", "Preloaded data for $satelliteType satellites")
                }
                
                // Set up callback for error handling
                dataManager.onDataLoadError = { satelliteType, error ->
                    Log.w("MyApplication", "Failed to preload $satelliteType data: $error")
                }
                
                // Start preloading all satellite types
                dataManager.preloadAllSatelliteData()
                
            } catch (e: Exception) {
                Log.e("MyApplication", "Error during satellite data preload", e)
            }
        }
    }
}