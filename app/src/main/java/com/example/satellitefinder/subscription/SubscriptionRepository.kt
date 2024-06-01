package com.example.satellitefinder.subscription

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.example.satellitefinder.subscription.SubscriptionSkus.NON_CONSUMABLE_SKUS
import com.example.satellitefinder.subscription.SubscriptionSkus.SUBS_SKUS
import com.example.satellitefinder.db.SubscriptionDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber


class SubscriptionRepository (private val application: Context, val subscriptionDb: SubscriptionDb) :
    PurchasesUpdatedListener, BillingClientStateListener {

    lateinit var storeBillingClient: BillingClient
    private val productsWithProductDetails = MutableLiveData<Map<String, ProductDetails>>()
    private val purchaseResultResponseListener: MutableList<SubscriptionCancelListener> = mutableListOf()


    fun startDataSourceConnections() {
        Timber.d("startDataSourceConnections")
        instantiateAndConnectToPlayBillingService()

    }

    fun endDataSourceConnections() {
        if (storeBillingClient.isReady)
            storeBillingClient.endConnection()
        Timber.d("endDataSourceConnections")
    }

    private fun instantiateAndConnectToPlayBillingService() {
        storeBillingClient = BillingClient.newBuilder(application.applicationContext)
            .enablePendingPurchases() // required or app will crash
            .setListener(this).build()
        connectToPlayBillingService()
    }

    private fun connectToPlayBillingService(): Boolean {
        Timber.d("connectToPlayBillingService")
        if (!storeBillingClient.isReady) {
            storeBillingClient.startConnection(this)
            return true
        }
        return false
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                Timber.d("onBillingSetupFinished successfully")
                queryProductDetails()
                queryPurchasesAsync()


            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.e("TAG", "onPurchasesUpdated: User canceled the purchase")
            }
            BillingClient.BillingResponseCode.BILLING_UNAVAILABLE -> {
                //Some apps may choose to make decisions based on this knowledge.
                Timber.d(billingResult.debugMessage)
            }
            else -> {
                //do nothing. Someone else will connect it through retry policy.
                //May choose to send to server though
                Timber.d(billingResult.debugMessage)
            }
        }
    }

    private fun queryProductDetails() {
        Log.d("TAG", "queryProductDetails")
        val params = QueryProductDetailsParams.newBuilder()

        val productList: MutableList<QueryProductDetailsParams.Product> = arrayListOf()
        for (product in SUBS_SKUS) {
            productList.add(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(product)
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            )
        }

        params.setProductList(productList).let { productDetailsParams ->
            Log.i("TAG", "queryProductDetailsAsync")
            storeBillingClient.queryProductDetailsAsync(productDetailsParams.build()) { billingResult, productDetailsList ->
                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        val expectedProductDetailsCount = SUBS_SKUS.size
                        if (productDetailsList.isNullOrEmpty()) {
                            productsWithProductDetails.postValue(emptyMap())
                            Timber.e(
                                "onProductDetailsResponse: " +
                                        "Expected ${expectedProductDetailsCount}, " +
                                        "Found null ProductDetails. " +
                                        "Check to see if the products you requested are correctly published " +
                                        "in the Google Play Console."
                            )
                        } else {
                            productsWithProductDetails.postValue(HashMap<String, ProductDetails>().apply {
                                for (productDetails in productDetailsList) {
                                    put(productDetails.productId, productDetails)

                                    subscriptionDb.skuDetailsDao().insertOrUpdate(productDetails)
                                }
                            })
                        }
                    }
                }
            }

        }
    }


    override fun onBillingServiceDisconnected() {
        Timber.d("onBillingServiceDisconnected")
        connectToPlayBillingService()
    }

    fun queryPurchasesAsync() {
        Timber.d("queryPurchasesAsync called")
        if (isSubscriptionSupported()) {
            storeBillingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            ) { billingResult, purchasesResult -> processPurchases(purchasesResult.toSet()) }
        }
    }

    private fun processPurchases(purchasesResult: Set<Purchase>) =
        CoroutineScope(Job() + Dispatchers.IO).launch {
            Timber.d("processPurchases called")
            val validPurchases = HashSet<Purchase>(purchasesResult.size)
            Timber.d("processPurchases newBatch content $purchasesResult")
            purchasesResult.forEach { purchase ->
                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                    if (isSignatureValid(purchase)) {
                        validPurchases.add(purchase)
                    }
                } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                    Timber.d("Received a pending purchase of SKU: ${purchase.products[0]}")
                }
            }
            val (nonConsumables, _) = validPurchases.partition {
                NON_CONSUMABLE_SKUS.contains(it.products[0])
            }

            acknowledgeNonConsumablePurchasesAsync(nonConsumables)
        }

    private fun acknowledgeNonConsumablePurchasesAsync(nonConsumables: List<Purchase>) {

        if (!nonConsumables.isNullOrEmpty()) {
            nonConsumables.forEach { purchase ->
                val params =
                    AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken)
                        .build()
                storeBillingClient.acknowledgePurchase(params) { billingResult ->
                    when (billingResult.responseCode) {
                        BillingClient.BillingResponseCode.OK -> {
                            disburseNonConsumableEntitlement(purchase)
                        }
                        else -> {
                            Timber.d("acknowledgeNonConsumablePurchasesAsync response is ${billingResult.debugMessage}")
                        }
                    }
                }
            }
        } else {

            val monthly = subscriptionDb.skuDetailsDao().getById(SUBS_SKUS[0])
            monthly?.let {
                if (!it.canPurchase) {
                    subscriptionDb.skuDetailsDao().insertOrUpdate(SUBS_SKUS[0], true)
                }
            }

            val annual = subscriptionDb.skuDetailsDao().getById(SUBS_SKUS[1])
            annual?.let {
                if (!it.canPurchase) {
                    subscriptionDb.skuDetailsDao().insertOrUpdate(SUBS_SKUS[1], true)
                }
            }
        }
    }

    private fun disburseNonConsumableEntitlement(purchase: Purchase) =
        CoroutineScope(Job() + Dispatchers.IO).launch {
            when (purchase.products[0]) {
                SUBS_SKUS[0] -> {
                    val monthly = subscriptionDb.skuDetailsDao().getById(SUBS_SKUS[0])
                    monthly?.let {
                        if (it.canPurchase) {
                            subscriptionDb.skuDetailsDao()
                                .insertOrUpdate(purchase.products[0], false)
                        }
                    }
                }
                SUBS_SKUS[1] -> {
                    val annual = subscriptionDb.skuDetailsDao().getById(SUBS_SKUS[1])
                    annual?.let {
                        if (it.canPurchase) {
                            subscriptionDb.skuDetailsDao()
                                .insertOrUpdate(purchase.products[0], false)
                        }
                    }
                }

                SUBS_SKUS[2] -> {
                    val weekly = subscriptionDb.skuDetailsDao().getById(SUBS_SKUS[2])
                    weekly?.let {
                        if (it.canPurchase) {
                            subscriptionDb.skuDetailsDao()
                                .insertOrUpdate(purchase.products[0], false)
                        }
                    }
                }

            }
        }

    private fun isSignatureValid(purchase: Purchase): Boolean {
        return Security.verifyPurchaseKey(
            Security.BASE_64_ENCODED_PUBLIC_KEY,
            purchase.originalJson,
            purchase.signature)
    }

    private fun isSubscriptionSupported(): Boolean {
        val billingResult =
            storeBillingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS)
        var succeeded = false
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> connectToPlayBillingService()
            BillingClient.BillingResponseCode.OK -> succeeded = true
            else -> {
                Timber.w(
                    "isSubscriptionSupported() error: ${billingResult.debugMessage}"
                )
            }
        }
        return succeeded
    }

    fun launchBillingFlow(activity: Activity, skuDetailsModel: SkuDetailsModel) {
        val productDetails = productsWithProductDetails.value?.get(skuDetailsModel.sku)
        if (productDetails != null) {
            launchBillingFlow(activity, productDetails)
        }
    }

    private fun launchBillingFlow(activity: Activity, skuDetails: ProductDetails) {
        val offerToken = skuDetails.subscriptionOfferDetails?.get(0)?.offerToken
        val productDetailsParamsList =
            listOf(
                offerToken?.let {
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(skuDetails)
                        .setOfferToken(it)
                        .build()
                }
            )
        val billingFlowParams =
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()
        storeBillingClient.launchBillingFlow(activity, billingFlowParams)
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                // will handle server verification, consumables, and updating the local cache
                purchases?.apply { processPurchases(this.toSet()) }
            }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                // item already owned? call queryPurchasesAsync to verify and process all such items
                Timber.d(billingResult.debugMessage)
                queryPurchasesAsync()
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.e("TAG", "onPurchasesUpdated: User canceled the purchase")
                callBillingResponseListener(billingResult.responseCode)
            }
            BillingClient.BillingResponseCode.SERVICE_DISCONNECTED -> {
                connectToPlayBillingService()
            }
            else -> {
                Timber.i(billingResult.debugMessage)
            }
        }
    }

    val subSkuDetailsModelListLiveData: LiveData<List<SkuDetailsModel>> by lazy {
       /* if (!::billingDbInstance.isInitialized) {
            billingDbInstance = BillingDbInstance.getInstance(application)
        }*/
        subscriptionDb.skuDetailsDao().getSubscriptionSkuDetails()
    }



    private fun callBillingResponseListener(responseCode: Int) {
        for (resListener in purchaseResultResponseListener) {
                resListener.onResponseCode(responseCode)

        }
    }

    fun addBillingResponseListener(billingResultListener: SubscriptionCancelListener) {
        purchaseResultResponseListener.add(billingResultListener)
    }


}

