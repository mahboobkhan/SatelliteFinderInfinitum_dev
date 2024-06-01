package com.example.satellitefinder.subscription

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel


class SubscriptionViewModel (var subscriptionRepository: SubscriptionRepository) :
    ViewModel() {

    private val viewModelScope = CoroutineScope(Job() + Dispatchers.Main)

    var subSkuDetailsModelListLiveData: LiveData<List<SkuDetailsModel>>


    init {
        subscriptionRepository.startDataSourceConnections()
        subSkuDetailsModelListLiveData = subscriptionRepository.subSkuDetailsModelListLiveData
    }

    fun getBySkuId(skuId: String): SkuDetailsModel? {

        if (subSkuDetailsModelListLiveData.value != null)
            for (item in subSkuDetailsModelListLiveData.value!!) {
                if (item.sku == skuId) {
                    return item
                }
            }
        return null
    }


    override fun onCleared() {
        super.onCleared()
        subscriptionRepository.endDataSourceConnections()
        viewModelScope.coroutineContext.cancel()
    }

    fun makePurchase(activity: Activity, skuDetailsModel: SkuDetailsModel) {
        subscriptionRepository.launchBillingFlow(activity, skuDetailsModel)
    }


    fun addBillingCancelListener(billingResultListener: SubscriptionCancelListener) {
        subscriptionRepository.addBillingResponseListener(billingResultListener)
    }


}