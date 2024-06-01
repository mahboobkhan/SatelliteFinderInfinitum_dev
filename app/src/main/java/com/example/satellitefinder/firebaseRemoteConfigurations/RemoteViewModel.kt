package com.example.satellitefinder.firebaseRemoteConfigurations

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.satellitefinder.R
import com.google.gson.Gson

class RemoteViewModel(application: Application, private val repository: RemoteRepository) :
    AndroidViewModel(application) {
    private val TAG = "RemoteViewModel"
    var remoteConfig: MutableLiveData<RemoteConfig> = MutableLiveData()

    fun getRemoteConfigSplash(context: Activity) {
        repository.getFirebaseRemoteConfig().fetchAndActivate()
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    remoteConfig.value = Gson().fromJson(repository.getFirebaseRemoteConfig()
                        .getString(context.getString(
                            R.string.remote)), RemoteConfig::class.java)

               }
            }
            .addOnFailureListener {

            }
    }

    fun getRemoteConfig(context: Context): RemoteConfig? {
        return Gson().fromJson(repository.getFirebaseRemoteConfig()
            .getString(context.getString(R.string.remote)), RemoteConfig::class.java)
    }
}