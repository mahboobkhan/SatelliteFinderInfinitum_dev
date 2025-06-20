package com.example.satellitefinder.admobAds

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentInformation.OnConsentInfoUpdateFailureListener
import com.google.android.ump.ConsentInformation.OnConsentInfoUpdateSuccessListener
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform
import java.util.concurrent.atomic.AtomicBoolean

class AdsConsentManager(private val activity: Activity) {
    private var consentInformation: ConsentInformation? = null
    private val isCallbackUMPSuccess = AtomicBoolean(false)

    interface UMPResultListener {
        fun onCheckUMPSuccess(canRequestAds: Boolean)
    }

    fun requestUMP(umpResultListener: UMPResultListener) {
        requestUMP(false, "", false, umpResultListener)
    }

    fun requestUMP(
        enableDebug: Boolean,
        testDevice: String?,
        resetData: Boolean,
        umpResultListener: UMPResultListener
    ) {
        Log.d(TAG, "requestUMP :2enableDebug: $enableDebug resetData: $resetData")
        val builder = ConsentRequestParameters.Builder()
        if (enableDebug) {
            val debugSettings = ConsentDebugSettings.Builder(activity)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId(testDevice)
                .build()
            builder.setConsentDebugSettings(debugSettings)
        }
        val params = builder
            .setTagForUnderAgeOfConsent(false)
            .build()
        consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        if (!isConsentRequested){
            isConsentRequested = true
            if (resetData) consentInformation?.reset()

            consentInformation?.requestConsentInfoUpdate(
                activity,
                params,
                {
                    Log.w(TAG, "requestConsentInfoUpdate Success :")
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                        activity
                    ) { loadAndShowError: FormError? ->
                        Log.w(
                            TAG,
                            "loadAndShowConsentFormIfRequired 3:  loadAndShowError:$loadAndShowError"
                        )
                        if (loadAndShowError != null) {
                            // Consent gathering failed.
                            Log.w(
                                TAG, String.format(
                                    "%s: %s",
                                    loadAndShowError.errorCode,
                                    loadAndShowError.message
                                )
                            )
                        }
                        if (!isCallbackUMPSuccess.getAndSet(true)) {
                            Log.w(TAG, "isCallbackUMPSuccess 11111111114: if code")
                            umpResultListener.onCheckUMPSuccess(
                                getConsentResult(
                                    activity
                                )
                            )
                        }
                    }
                },
                { requestConsentError: FormError ->
                    Log.w(TAG, "requestConsentInfoUpdate Fail :")
                    // Consent gathering failed.
                    Log.w(
                        TAG, String.format(
                            "%s: %s",
                            requestConsentError.errorCode,
                            requestConsentError.message
                        )
                    )
                    if (!isCallbackUMPSuccess.getAndSet(true)) {
                        Log.w(TAG, "isCallbackUMPSuccess 2222222222222225: if code")
                        umpResultListener.onCheckUMPSuccess(
                            getConsentResult(
                                activity
                            )
                        )
                    }
                })
        }

        if (consentInformation?.canRequestAds() == true) {
            Log.w(TAG, "consentInformation.canRequestAds() 6: if code")
            if (!isCallbackUMPSuccess.getAndSet(true)) {
                Log.w(TAG, "getAndSet 33333333337: if code")
                umpResultListener.onCheckUMPSuccess(
                    getConsentResult(
                        activity
                    )
                )
            }
        }
    }

    fun showPrivacyOption(activity: Activity, umpResultListener: UMPResultListener) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity) { formError: FormError? ->
            if (formError != null) {
                // Consent gathering failed.
                Log.w(
                    TAG, String.format(
                        "%s: %s",
                        formError.errorCode,
                        formError.message
                    )
                )
                //FirebaseAnalyticsUtil.logEventTracking(activity, "ump_consent_failed", new Bundle());
            }
            if (getConsentResult(activity)) {
                //AperoAd.getInstance().initAdsNetwork();
            }
            val bundle = Bundle()
            bundle.putBoolean("consent", getConsentResult(activity))
            //FirebaseAnalyticsUtil.logEventTracking(activity, "ump_consent_result", bundle);
            umpResultListener.onCheckUMPSuccess(getConsentResult(activity))
        }
    }

    companion object {
        private const val TAG = "AdsConsentManager"

        var isConsentRequested = false
        fun getConsentResult(context: Context): Boolean {
            val sharedPref = context.getSharedPreferences(
                context.packageName + "_preferences",
                Context.MODE_PRIVATE
            )
            val purposeConsents = sharedPref.getString("IABTCF_PurposeConsents", "")
            Log.d(TAG, "consentResult: $purposeConsents")
            // Purposes are zero-indexed. Index 0 contains information about Purpose 1.
            if (purposeConsents!!.isNotEmpty()) {
                val purposeOneString = purposeConsents[0].toString()
                val hasConsentForPurposeOne = purposeOneString == "1"
                Log.w(TAG, "getConsentResult 8: hasConsentForPurposeOne$hasConsentForPurposeOne")
                return hasConsentForPurposeOne
            }
            Log.w(TAG, "getConsentResult 8: true")
            return true
        }
    }
}