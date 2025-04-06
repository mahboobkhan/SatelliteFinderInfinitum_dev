package com.example.satellitefinder.ui.activites

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.satellitefinder.R
import com.example.satellitefinder.databinding.ActivitySubscriptionBinding
import com.example.satellitefinder.subscription.SubscriptionSkus
import com.example.satellitefinder.subscription.SubscriptionViewModel
import com.example.satellitefinder.utils.MySharePrefrencesHelper
import com.example.satellitefinder.utils.baseConfig
import com.example.satellitefinder.utils.isAlreadyPurchased
import com.example.satellitefinder.utils.privacyPolicy
import com.example.satellitefinder.utils.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SubscriptionActivity : AppCompatActivity() {
    private var selectedPackage : String = ""
    private val billingViewModel : SubscriptionViewModel by viewModel()
    val binding:ActivitySubscriptionBinding by lazy {
        ActivitySubscriptionBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        this.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupObserver()
        selectedPackage =   setPackageSelected("yearly")

        binding.privaryPolicy.setOnClickListener {
            privacyPolicy()
        }

        binding.ivClose.setOnClickListener {
                startActivity(Intent(this@SubscriptionActivity, MainActivity::class.java))
                finish()

        }

        binding.btnContinueWithAd.setOnClickListener {
            finish()
        }
        binding.restore.setOnClickListener {
            howToUnsubscribe()
        }
        binding.monthlyCard.setOnClickListener {
            selectedPackage =   setPackageSelected("monthly")
        }

        binding.yearlyCard.setOnClickListener {
            selectedPackage =   setPackageSelected("yearly")
        }


        binding.btnUpgrade.isEnabled = !isAlreadyPurchased()
        binding.btnUpgrade.setOnClickListener {
            when(selectedPackage){
                "yearly"->{
                    billingViewModel.getBySkuId(SubscriptionSkus.yearlySubscriptionId)?.let {
                        billingViewModel.makePurchase(this@SubscriptionActivity, it)
                    }
                }
                "monthly" ->{
                    billingViewModel.getBySkuId(SubscriptionSkus.monthlySubscriptionId)?.let {
                        billingViewModel.makePurchase(this@SubscriptionActivity, it)
                    }
                }
               else ->{
                showToast("Please select package")
            }


            }
        }

    }


    private fun setupObserver() {
        billingViewModel.subSkuDetailsModelListLiveData.observe(this) { skuList ->
            Timber.tag("showprices").d("skuList: " + skuList)


            skuList.forEachIndexed { _, augmentedSkuDetails ->
                if (augmentedSkuDetails.sku == SubscriptionSkus.monthlySubscriptionId) {
                    val monthlyPrice = augmentedSkuDetails.price?.split(".")

                    //binding.monthlyPrice.text = getString(R.string.monthly_price_text, monthlyPrice)
                    binding.monthlyPrice1.text = monthlyPrice.toString()

                    Timber.tag("showprices").d("setupObserver: " + monthlyPrice)
                    if (!augmentedSkuDetails.canPurchase) {
                        MySharePrefrencesHelper.putBillingBoolean(this, true)
                        showToast("SuccessFully Purchased")
                        goToHome()
                        showToast("Restarting application")
                    }
                }
                if (augmentedSkuDetails.sku == SubscriptionSkus.yearlySubscriptionId) {
                    val yearlyPrice = augmentedSkuDetails.price?.split(".")
                    Log.d("showprices12", "setupObserver: 12222${yearlyPrice}")
                    //binding.yearlyPrice.text = getString(R.string.yearly_price_text, yearlyPrice)
                    binding.yearlyPrice1.text =  yearlyPrice.toString()
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
            val uri = Uri.parse("https://support.google.com/googleplay/answer/7018481?hl=en&co=GENIE.Platform%3DAndroid")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            this.startActivity(intent)
        }catch (_: ActivityNotFoundException){}
    }

    private fun setPackageSelected(s: String) : String  {
        binding.apply {
            when(s){
                "yearly"->{
                    ivSelectedYearly.setImageResource(R.drawable.ic_check)
                    ivSelectedMonthly.setImageResource(R.drawable.ic_uncheck)

                }
                "monthly"->{
                    ivSelectedMonthly.setImageResource(R.drawable.ic_check)
                    ivSelectedYearly.setImageResource(R.drawable.ic_uncheck)

                }
                           }
        }
        return s
    }

}