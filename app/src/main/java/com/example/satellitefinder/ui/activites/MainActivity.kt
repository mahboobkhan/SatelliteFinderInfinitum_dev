package com.example.satellitefinder.ui.activites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.showPriorityAdmobInterstitial
import com.example.satellitefinder.databinding.ActivityMainBinding
import com.example.satellitefinder.databinding.ExitDialogBinding
import com.example.satellitefinder.firebaseRemoteConfigurations.RemoteViewModel
import com.example.satellitefinder.ui.dialogs.RattingDialog
import com.example.satellitefinder.utils.LanguagesHelper
import com.example.satellitefinder.utils.exitNativeAd
import com.example.satellitefinder.utils.isAlreadyPurchased
import com.example.satellitefinder.utils.isInternetConnected
import com.example.satellitefinder.utils.isSplash

import com.example.satellitefinder.utils.screenEventAnalytics
import com.google.android.material.bottomsheet.BottomSheetDialog
import loadAndReturnAd
import newLoadAndShowNativeAd
import org.koin.androidx.viewmodel.ext.android.viewModel
import showLoadedNativeAd

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var rateUsDialog: RattingDialog

    private lateinit var dialog: BottomSheetDialog
    private lateinit var bottomSheetBinding: ExitDialogBinding
    val remoteConfigViewModel: RemoteViewModel by viewModel()


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        newBase?.let {
            LanguagesHelper.onAttach(it)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        if (remoteConfigViewModel.getRemoteConfig(this@MainActivity)?.InterstitialMain?.value == 1){

            showPriorityAdmobInterstitial(true,getString(R.string.interstialId))
        }
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        isSplash = false

        screenEventAnalytics("MainActivity")

        showNativeAd()
        rateUsDialog = RattingDialog(
            this@MainActivity,
        )
        bottomSheetBinding = ExitDialogBinding.inflate(layoutInflater, binding.root, false)
        dialog = BottomSheetDialog(this)
        dialog.setContentView(bottomSheetBinding.root)

        if (remoteConfigViewModel.getRemoteConfig(this@MainActivity)?.exitNative?.value == 1 && !isAlreadyPurchased()){

            loadAndReturnAd(this@MainActivity, getString(R.string.exitNativeId)) {
                exitNativeAd = it
            }
        }

        binding.apply {
            btnFindSatellite.setOnClickListener {

                startActivity(Intent(this@MainActivity, SatelliteFindActivity::class.java))
            }
            btnSatelliteMap.setOnClickListener {
                startActivity(Intent(this@MainActivity, MapSatelliteActivity::class.java))
            }
            btnCompass.setOnClickListener {
                startActivity(Intent(this@MainActivity, CompassActivity::class.java))

            }

            animationView.setOnClickListener {
                startActivity(Intent(this@MainActivity, SubscriptionActivity::class.java))

            }

            btnMore.setOnClickListener {
                startActivity(Intent(this@MainActivity, MoreActivity::class.java))
            }

            btnLanguages.setOnClickListener {
                startActivity(Intent(this@MainActivity, LanguagesActivity::class.java))
            }
            btnCurrent.setOnClickListener {
                startActivity(Intent(this@MainActivity, CurrentLocationActivity::class.java))
            }
        }
    }



    private fun showNativeAd() {
        if (remoteConfigViewModel.getRemoteConfig(this@MainActivity)?.mainNative?.value == 1 && !isAlreadyPurchased()){

            newLoadAndShowNativeAd(
                binding.layoutNative,
                R.layout.native_ad_layout_main,
                getString(R.string.mainNativeId),
                adLoading = {
                    binding.layoutNative.visibility = View.VISIBLE
                },
                failToLoad = {binding.layoutNative.visibility = View.GONE})
        }else{
            binding.layoutNative.visibility =  View.GONE
        }

    }


    override fun onBackPressed() {
        dialog.show()
        if (isInternetConnected()) {
            bottomSheetBinding.layoutNative.visibility = View.VISIBLE
            screenEventAnalytics("Exit Dialog")

            if (exitNativeAd != null && remoteConfigViewModel.getRemoteConfig(this@MainActivity)?.exitNative?.value == 1 && !isAlreadyPurchased()) {
                showLoadedNativeAd(
                    this@MainActivity,
                    bottomSheetBinding.layoutNative,
                    R.layout.native_ad_layout_main,
                    exitNativeAd!!
                )

            } else {
                bottomSheetBinding.layoutNative.visibility = View.GONE
            }


        } else {
            bottomSheetBinding.layoutNative.visibility = View.GONE
        }
        bottomSheetBinding.cancel.setOnClickListener {
            dialog.dismiss()
        }

        bottomSheetBinding.exit.setOnClickListener {
            dialog.dismiss()
            finishAffinity()
        }


    }

}