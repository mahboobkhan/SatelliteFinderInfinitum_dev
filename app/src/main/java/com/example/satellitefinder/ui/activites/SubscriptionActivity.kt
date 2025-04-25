package com.example.satellitefinder.ui.activites

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.satellitefinder.R
import com.example.satellitefinder.databinding.ActivitySubscriptionBinding
import com.example.satellitefinder.subscription.SubscriptionSkus
import com.example.satellitefinder.subscription.SubscriptionViewModel
import com.example.satellitefinder.utils.FirebaseEvents
import com.example.satellitefinder.utils.MySharePrefrencesHelper
import com.example.satellitefinder.utils.isAlreadyPurchased
import com.example.satellitefinder.utils.isInternetConnected
import com.example.satellitefinder.utils.privacyPolicy
import com.example.satellitefinder.utils.showToast
import com.example.satellitefinder.utils.startActivityWithSlideTransition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SubscriptionActivity : AppCompatActivity() {
    private var selectedPackage: String = ""
    private val billingViewModel: SubscriptionViewModel by viewModel()
    val binding: ActivitySubscriptionBinding by lazy {
        ActivitySubscriptionBinding.inflate(layoutInflater)
    }
    val fromSplash by lazy {
        intent.getBooleanExtra("fromSplash", false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        /*       this.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                       or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                       or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                       or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                       or View.SYSTEM_UI_FLAG_FULLSCREEN
                       or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)*/

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                FirebaseEvents.logEvent("premium_screen_back_press", "premium_screen_back_press")
                if (fromSplash) {
                    startActivityWithSlideTransition(MainActivity::class.java)
                } else {
                    finish()
                }
            }

        })


        FirebaseEvents.logEventActivity("premium_screen", "premium_screen")
        CoroutineScope(Dispatchers.Main).launch {
            setupObserver()
            delay(500)
            selectedPackage = setPackageSelected("monthly")
        }


        binding.privacyPolicy.setOnClickListener {
            FirebaseEvents.logEvent("premium_screen_click_privacy", "premium_screen_click_privacy")
            privacyPolicy()
        }

        binding.ivClose.setOnClickListener {
            FirebaseEvents.logEvent("premium_screen_click_close", "premium_screen_click_close")
            if (fromSplash) {
                startActivityWithSlideTransition(MainActivity::class.java)
            } else {
                finish()
            }
        }

        /*binding.btnContinueWithAd.setOnClickListener {
            finish()
        }*/
        binding.tvCancelAnytime.setOnClickListener {
            FirebaseEvents.logEvent(
                "premium_screen_click_cancel_any",
                "premium_screen_click_cancel_any"
            )
            howToUnsubscribe()
        }
        binding.weeklyCard.setOnClickListener {
            FirebaseEvents.logEvent("premium_screen_click_weekly", "premium_screen_click_weekly")
            selectedPackage = setPackageSelected("weekly")
        }

        binding.monthlyCard.setOnClickListener {
            FirebaseEvents.logEvent("premium_screen_click_monthly", "premium_screen_click_monthly")
            selectedPackage = setPackageSelected("monthly")
        }

        binding.yearlyCard.setOnClickListener {
            FirebaseEvents.logEvent("premium_screen_click_yearly", "premium_screen_click_yearly")
            selectedPackage = setPackageSelected("yearly")
        }


        binding.btnUpgrade.isEnabled = !isAlreadyPurchased()

        binding.btnUpgrade.setOnClickListener {
            if (isInternetConnected()){
                FirebaseEvents.logEvent(
                    "premium_screen_click_continue_$selectedPackage",
                    "premium_screen_click_continue_$selectedPackage"
                )
                when (selectedPackage) {
                    "weekly" -> {
                        billingViewModel.getBySkuId(SubscriptionSkus.weeklySubscriptionId)?.let {
                            billingViewModel.makePurchase(this@SubscriptionActivity, it)
                        }
                    }

                    "monthly" -> {
                        billingViewModel.getBySkuId(SubscriptionSkus.monthlySubscriptionId)?.let {
                            billingViewModel.makePurchase(this@SubscriptionActivity, it)
                        }
                    }

                    "yearly" -> {
                        billingViewModel.getBySkuId(SubscriptionSkus.yearlySubscriptionId)?.let {
                            billingViewModel.makePurchase(this@SubscriptionActivity, it)
                        }
                    }


                    else -> {
                        showToast("Please select package")
                    }


                }
            }else{
                showToast("Please check internet connection")
            }

        }

    }


    private fun setupObserver() {
        billingViewModel.subSkuDetailsModelListLiveData.observe(this) { skuList ->
            Timber.tag("showprices").d("skuList: " + skuList)


            skuList.forEachIndexed { _, augmentedSkuDetails ->
                if (augmentedSkuDetails.sku == SubscriptionSkus.weeklySubscriptionId) {
                    val weeklyPrice = augmentedSkuDetails.price?.split(".")?.get(0) ?: ""

                    //binding.monthlyPrice.text = getString(R.string.monthly_price_text, monthlyPrice)
                    binding.tvWeeklyPrice.text = weeklyPrice

                    Timber.tag("showprices").d("setupObserver: " + weeklyPrice)
                    if (!augmentedSkuDetails.canPurchase) {
                        MySharePrefrencesHelper.putBillingBoolean(this, true)
                        showToast("SuccessFully Purchased")
                        goToHome()
                        showToast("Restarting application")
                    }
                }

                if (augmentedSkuDetails.sku == SubscriptionSkus.monthlySubscriptionId) {
                    val monthlyPrice = augmentedSkuDetails.price?.split(".")?.get(0) ?: ""

                    //binding.monthlyPrice.text = getString(R.string.monthly_price_text, monthlyPrice)
                    binding.tvMonthlyPrice.text = monthlyPrice

                    Timber.tag("showprices").d("setupObserver: " + monthlyPrice)
                    if (!augmentedSkuDetails.canPurchase) {
                        MySharePrefrencesHelper.putBillingBoolean(this, true)
                        showToast("SuccessFully Purchased")
                        goToHome()
                        showToast("Restarting application")
                    }
                }
                if (augmentedSkuDetails.sku == SubscriptionSkus.yearlySubscriptionId) {
                    val yearlyPrice = augmentedSkuDetails.price?.split(".")?.get(0) ?: ""
                    Log.d("showprices12", "setupObserver: 12222${yearlyPrice}")
                    //binding.yearlyPrice.text = getString(R.string.yearly_price_text, yearlyPrice)
                    binding.tvYearlyPrice.text = yearlyPrice
                    if (!augmentedSkuDetails.canPurchase) {
                        MySharePrefrencesHelper.putBillingBoolean(this, true)
                        showToast("SuccessFully Purchased")
                        goToHome()
                        showToast("Restarting application")
                    }
                }


            }

        }
    }

    private fun goToHome() {
        val i = Intent(this, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
    }

    private fun Context.howToUnsubscribe() {
        try {
            val uri =
                Uri.parse("https://play.google.com/store/account/subscriptions")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            this.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
        }
    }

    private fun setPackageSelected(s: String): String {
        binding.apply {
            when (s) {
                "weekly" -> {
                    ivSelectedWeekly.setImageResource(R.drawable.ic_check)
                    ivSelectedMonthly.setImageResource(R.drawable.ic_uncheck)
                    ivSelectedYearly.setImageResource(R.drawable.ic_uncheck)
                    val text = getString(
                        R.string.subscription_description,
                        tvWeeklyPrice.text.toString(),
                        "Week"
                    )
                    binding.tvPriceInfo.text = text
                }

                "monthly" -> {
                    ivSelectedWeekly.setImageResource(R.drawable.ic_uncheck)
                    ivSelectedMonthly.setImageResource(R.drawable.ic_check)
                    ivSelectedYearly.setImageResource(R.drawable.ic_uncheck)
                    val text = getString(
                        R.string.subscription_description,
                        tvMonthlyPrice.text.toString(),
                        "Month"
                    )
                    binding.tvPriceInfo.text = text
                }

                "yearly" -> {
                    ivSelectedWeekly.setImageResource(R.drawable.ic_uncheck)
                    ivSelectedMonthly.setImageResource(R.drawable.ic_uncheck)
                    ivSelectedYearly.setImageResource(R.drawable.ic_check)
                    val text = getString(
                        R.string.subscription_description,
                        tvYearlyPrice.text.toString(),
                        "Year"
                    )
                    binding.tvPriceInfo.text = text
                }


            }
        }
        return s
    }

}