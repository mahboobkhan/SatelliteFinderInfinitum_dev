package com.example.satellitefinder.ui.activites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.loadAndShowSplashInterstitial
import com.example.satellitefinder.admobAds.showPriorityAdmobInterstitial
import com.example.satellitefinder.databinding.ActivityOnBoardingScreenBinding
import com.example.satellitefinder.firebaseRemoteConfigurations.RemoteViewModel
import com.example.satellitefinder.ui.adapters.ViewPagerAdapter
import com.example.satellitefinder.utils.baseConfig
import com.example.satellitefinder.utils.isAlreadyPurchased
import com.example.satellitefinder.utils.isInternetConnected
import loadAndReturnAd
import newLoadAndShowNativeAd
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnBoardingScreen : AppCompatActivity() {
    private var fragmentDestination = 0
    private var isIncomingFromSplash = false
    private val binding :  ActivityOnBoardingScreenBinding by lazy{
        ActivityOnBoardingScreenBinding.inflate(layoutInflater)
    }
    val remoteConfigViewModel: RemoteViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            if (remoteConfigViewModel.getRemoteConfig(this@OnBoardingScreen)?.InterstitialMain?.value == 1 && !isAlreadyPurchased()){
                loadAndShowSplashInterstitial(
                    true,
                    getString(R.string.splashInterstial),
                    getString(R.string.interstialId)
                )
            }
        setContentView(binding.root)


        initViews()
        handleClicks()

        if (remoteConfigViewModel.getRemoteConfig(this@OnBoardingScreen)?.onBoardingNative?.value == 1 && !isAlreadyPurchased()) {
            newLoadAndShowNativeAd(
                binding.layoutNative,
                R.layout.native_ad_layout_small,
                getString(R.string.onBoardingNativeId),
                adLoading = {
                    binding.layoutNative.visibility = View.VISIBLE
                },
                failToLoad = {
                    binding.layoutNative.visibility = View.GONE
                })
        }

        if (remoteConfigViewModel.getRemoteConfig(this@OnBoardingScreen)?.permissionNative?.value == 1 && !isAlreadyPurchased()) {
            loadAndReturnAd(
                this@OnBoardingScreen,
                getString(R.string.permissionNativeId)
            ) {
                PermissionActivity.permissionNativeAd = it
            }
        }
    }


    private fun initViews() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = adapter
        setupIndicatorDots()
    }

    private fun handleClicks() {
        binding.tvNext.setOnClickListener {
            fragmentDestination++
            binding.viewPager.currentItem = fragmentDestination
        }
        binding.tvDone.setOnClickListener {
            baseConfig.isOnBoardingDone = true
            startActivity(Intent(this@OnBoardingScreen, PermissionActivity::class.java))
            finish()
        }
    }


    private fun setupIndicatorDots() {
        val numPages = binding.viewPager.adapter?.count ?: 0
        val dots = arrayOfNulls<ImageView>(numPages)
        for (i in 0 until numPages) {
            dots[i] = ImageView(this)
            dots[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    if (i == 0) R.drawable.ic_selected else R.drawable.ic_unselected
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            binding.indicatorLayout.addView(dots[i], params)
        }

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                for (i in 0 until numPages) {
                    dots[i]?.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@OnBoardingScreen,
                            if (i == position) R.drawable.ic_selected else R.drawable.ic_unselected
                        )
                    )
                }

                if (position == 2) {
                    binding.tvNext.visibility = View.GONE
                    binding.tvDone.visibility = View.VISIBLE
                } else {
                    binding.tvDone.visibility = View.GONE
                    binding.tvNext.visibility = View.VISIBLE
                }
                fragmentDestination = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                Log.d("TAG42", "onPageScrollStateChanged: ")
            }
        })
    }

}