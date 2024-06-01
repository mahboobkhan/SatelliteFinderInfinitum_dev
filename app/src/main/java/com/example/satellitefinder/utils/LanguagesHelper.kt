package com.example.satellitefinder.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.Log
import java.util.*

object LanguagesHelper {

    fun onAttach(context: Context): Context {

        val locale: String = MySharePrefrencesHelper.getKey(context,"langCode", "en").toString()
        Log.e("LanguageTAG", "onAttach locale: $locale")
        return if (locale.isNotEmpty()) {
            setAppLocale(context, locale)
        } else {
            setAppLocale(context, "en")
        }
    }



    private fun setAppLocale(context: Context, localeSpec: String): Context {
        val locale: Locale = if (localeSpec == "system") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Resources.getSystem().configuration.locales[0]
            } else {
                Resources.getSystem().configuration.locale
            }
        } else {
            Locale(localeSpec)
        }
        Locale.setDefault(locale)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, locale)
        } else {
            updateResourcesLegacy(context, locale)
        }
    }

    private fun updateResources(context: Context, locale: Locale): Context {
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
    val languagesList = mutableListOf(
        LanguagesModel("Arabic", "ar"),
        LanguagesModel("Amharic", "am"),
        LanguagesModel("Bengali", "bn"),
        LanguagesModel("Chinese", "zh"),
        LanguagesModel("English", "en"),
        LanguagesModel("French", "fr"),
        LanguagesModel("German", "de"),
        LanguagesModel("Hindi", "hi"),
        LanguagesModel("Italian", "it"),
        LanguagesModel("Japanese", "ja"),
        LanguagesModel("Persian", "fa"),
        LanguagesModel("Russian", "ru"),
        LanguagesModel("Spanish", "es"),
        LanguagesModel("Turkish", "tr"),

    )
}