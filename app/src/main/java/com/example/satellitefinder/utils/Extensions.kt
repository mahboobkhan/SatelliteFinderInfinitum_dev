package com.example.satellitefinder.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.satellitefinder.BuildConfig
import com.example.satellitefinder.R
import com.google.android.gms.ads.nativead.NativeAd
import com.google.firebase.analytics.FirebaseAnalytics
import com.permissionx.guolindev.PermissionX
import java.lang.reflect.Method
import java.util.regex.Matcher
import java.util.regex.Pattern

val isRatting = "isRatting"
var currentLocation: Location? = null
var isSplash = false
var adCount = 1
var exitNativeAd: AdState? = null
fun Context.getSharedPrefs(): SharedPreferences =
    getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

val Context.baseConfig: MyBaseConfig get() = MyBaseConfig.newInstance(this)


 fun parseDuration(duration: String?): Int {
    val REGEX = "^P((\\d)*Y)?((\\d)*W)?((\\d)*D)?"
    var days = 0
    val pattern: Pattern = Pattern.compile(REGEX)
    val matcher: Matcher = pattern.matcher(duration)
    while (matcher.find()) {
        if (matcher.group(1) != null) {
            days += 365 * Integer.valueOf(matcher.group(2))
        }
        if (matcher.group(3) != null) {
            days += 7 * Integer.valueOf(matcher.group(4))
        }
        if (matcher.group(5) != null) {
            days += Integer.valueOf(matcher.group(6))
        }
    }
    return days
}

fun Activity.screenEventAnalytics(screeName:String) {
    var bundle = Bundle()
    bundle.putString("ActivityName",screeName)

    FirebaseAnalytics.getInstance(this).logEvent(screeName,bundle)
}
fun Context.isAlreadyPurchased(): Boolean {
    return MySharePrefrencesHelper.getBillingBoolean(
        this
    )
}
fun Context.rateUs() {
    val uri = Uri.parse("market://details?id=$packageName")
    val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
    try {
        startActivity(myAppLinkToMarket)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show()
    }
}

fun Context.isInternetConnected(): Boolean {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            }
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            }
        }
    } else {
        val cm =
            this.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }
    return false
}

fun Context.sendEmail() {
    val mIntent = Intent(Intent.ACTION_SEND)
    mIntent.data = Uri.parse("mailto:")
    mIntent.type = "text/plain"
    mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("mailto:infinitumsoft.technologies@gmail.com"))
    mIntent.putExtra(Intent.EXTRA_SUBJECT, "Feed back "+applicationContext.getString(R.string.app_name)+"")
    mIntent.putExtra(Intent.EXTRA_TEXT, "Tell us which issues you are facing using "+applicationContext.getString(R.string.app_name)+" App?")
    startActivity(Intent.createChooser(mIntent, "Send Email"))
}
fun Context.privacyPolicy() {
    try {
        val uri: Uri =
            Uri.parse("https://sites.google.com/view/satellite-finder-app/satellite-finder") // missing 'http://' will cause crashed
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)

    } catch (e: java.lang.Exception) {
        Toast.makeText(
            this, "No application can handle this request."
                    + " Please install a web browser", Toast.LENGTH_LONG
        ).show()
        e.printStackTrace()
    }
}

fun FragmentActivity.requestLocationPermissions(result: ((Boolean) -> Unit)? = null) {

        PermissionX.init(this)
            .permissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "Please Allow Permissions",
                    "OK",
                    "Cancel"
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "Location permissions are required allow them from settings manually",
                    "OK",
                    "Cancel"
                )
            }
            .request { allGranted, _, _ ->
                if (allGranted) {
                    result?.invoke(true)
                } else {
                    showToast("Permission Denied")
                    result?.invoke(false)
                }
            }

}

fun Context.getWindowWidth(percent: Float = 1.0f): Int {
    val displayMetrics = resources.displayMetrics
    return (displayMetrics.widthPixels * percent).toInt()
}

fun Activity.showGpsDialog(){

    AlertDialog.Builder(this)
        .setCancelable(false)
        .setTitle("GPS Location")
        .setMessage("Please Enable GPS Location")
        .setPositiveButton("Yes") { dialog, which -> //if user pressed "yes", then he is allowed to exit from application
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).also {
                startActivity(it)
            }
        }
        .setNegativeButton("No") { dialog, which -> //if user select "No", just cancel this dialog and continue with app
            dialog.cancel()
        }
        .show()
}

fun Activity.showToast(s: String) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}

fun Context.sharesApp(text: String) {
    try {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            getString(R.string.app_name)
        )
        var shareMessage = text
        shareMessage =
            """${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}""".trimIndent()
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(
            Intent.createChooser(
                shareIntent,
                getString(R.string.app_name)
            )
        )
    } catch (e: Exception) {
    }
}