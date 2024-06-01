package com.example.satellitefinder.ui.activites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.satellitefinder.R
import com.example.satellitefinder.databinding.ActivityPermissionBinding
import com.example.satellitefinder.firebaseRemoteConfigurations.RemoteViewModel
import com.example.satellitefinder.utils.AdState
import com.example.satellitefinder.utils.baseConfig
import com.example.satellitefinder.utils.isAlreadyPurchased
import com.example.satellitefinder.utils.requestLocationPermissions
import com.google.android.gms.ads.nativead.NativeAd
import newLoadAndShowNativeAd
import org.koin.androidx.viewmodel.ext.android.viewModel
import showLoadedNativeAd

class PermissionActivity : AppCompatActivity() {

    val binding:ActivityPermissionBinding by lazy{
        ActivityPermissionBinding.inflate(layoutInflater)
    }
    val remoteConfigViewModel: RemoteViewModel by viewModel()


    companion object{
        var permissionNativeAd: AdState? =null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

               showNativeAd()

        binding.btnAllow.setOnClickListener {
            baseConfig.isOnPermissionDone = true

            requestLocationPermissions {
                if (it){
                    startActivity(Intent(this@PermissionActivity,MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        permissionNativeAd = null
    }
    private fun showNativeAd(){
        if (remoteConfigViewModel.getRemoteConfig(this@PermissionActivity)?.permissionNative?.value == 1 && !isAlreadyPurchased()) {
            if (permissionNativeAd == null) {
                newLoadAndShowNativeAd(
                    binding.layoutNative,
                    R.layout.native_ad_layout_small,
                    getString(R.string.permissionNativeId),
                    adLoading = {
                        binding.layoutNative.visibility = View.VISIBLE
                    },
                    failToLoad = {
                        binding.layoutNative.visibility = View.GONE
                    })

            } else {
                showLoadedNativeAd(
                    this@PermissionActivity, binding.layoutNative, R.layout.native_ad_layout_small,
                    permissionNativeAd!!
                )

            }
        }

    }
}