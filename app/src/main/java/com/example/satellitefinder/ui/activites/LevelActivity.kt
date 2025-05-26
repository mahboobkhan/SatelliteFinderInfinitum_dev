package com.example.satellitefinder.ui.activites

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.adssdk.banner_ads.BannerAdUtils
import com.example.satellitefinder.R
import com.example.satellitefinder.admobAds.RemoteConfig
import com.example.satellitefinder.databinding.ActivityLevelBinding
import com.example.satellitefinder.leveler.orientation.Orientation
import com.example.satellitefinder.leveler.orientation.OrientationListener
import com.example.satellitefinder.leveler.orientation.OrientationProvider
import com.example.satellitefinder.utils.FirebaseEvents
import com.example.satellitefinder.utils.canWeShowAds

class LevelActivity : AppCompatActivity(), OrientationListener {
    private lateinit var binding: ActivityLevelBinding

    companion object {
        private lateinit var provider: OrientationProvider

        fun getProvider(): OrientationProvider {
            return provider
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseEvents.logEventActivity("level_screen", "level screen")
        provider = OrientationProvider.getInstance(this)

        binding.ivBack.setOnClickListener {
            FirebaseEvents.logEvent("level_screen_click_back", "level_screen_click_back")
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this@LevelActivity, callback)

        showBannerAd()
    }

    override fun onResume() {
        super.onResume()

        provider = OrientationProvider.getInstance(this)

        // orientation manager
        if (provider.isSupported()) {
            provider.startListening(this)
        } else {
            Toast.makeText(this, getText(R.string.not_supported), Toast.LENGTH_LONG).show()
        }
    }

    override fun onPause() {
        super.onPause()

        if (provider.isListening) {
            provider.stopListening()
        }
    }

    override fun onOrientationChanged(
        orientation: Orientation?,
        pitch: Float,
        roll: Float,
        balance: Float
    ) {
        binding.mainLevelView.setValues(roll,pitch)
        binding.verticalLevelView.setValues(pitch)
        binding.horizontalLevelView.setValues(roll)
        binding.tvX.text = "X= %.1f°".format(roll)
        binding.tvY.text = "Y= %.1f°".format(pitch)
    }

    override fun onCalibrationSaved(success: Boolean) {}

    override fun onCalibrationReset(success: Boolean) {}

    private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    private fun showBannerAd() {
        if (canWeShowAds(RemoteConfig.levelBanner)) {
            /*showBannerAD(
                binding.bannerAdContainer,
                this@LevelActivity,
                getString(R.string.bannerId)
            )
*/
            binding.bannerAdContainer.visibility = View.VISIBLE
            BannerAdUtils(activity = this, screenName = "level")
                .loadBanner(
                    adsKey = getString(R.string.bannerId),
                    remoteConfig = RemoteConfig.levelBanner,
                    adsView = binding.bannerAdContainer,
                    shimmerLayout = binding.clShimmer,
                    onAdClicked = {},
                    onAdFailedToLoads = { _, _ -> },
                    onAdImpression = {},
                    onAdLoaded = { _, _ -> },
                    onAdOpened = {},
                    onAdValidate = {
                        binding.bannerAdContainer.visibility = View.GONE
                        binding.clShimmer.visibility = View.GONE
                    })
        } else {
            binding.bannerAdContainer.visibility = View.GONE
            binding.clShimmer.visibility = View.GONE
        }
    }
}