package com.example.satellitefinder.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.example.satellitefinder.subscription.SkuDetailsModel
import com.example.satellitefinder.utils.parseDuration


@Dao
interface SubscriptionDao {

    @Query("SELECT * FROM SkuDetailsModel WHERE type = '${BillingClient.ProductType.SUBS}'")
    fun getSubscriptionSkuDetails(): LiveData<List<SkuDetailsModel>>

    @Query("SELECT * FROM SkuDetailsModel WHERE type = '${BillingClient.ProductType.INAPP}'")
    fun getInappSkuDetails(): LiveData<List<SkuDetailsModel>>

    @Transaction
    fun insertOrUpdate(skuDetails: ProductDetails) = skuDetails.apply {
        val result = getById(productId)

        val pricingPhases = this.subscriptionOfferDetails?.get(0)?.pricingPhases?.pricingPhaseList
        val trialPeriod = pricingPhases?.get(0)?.billingPeriod


        val bool = true
        val originalJson = toString()
        val detail = SkuDetailsModel(
            canPurchase = bool,
            sku = productId,
            type = productType,
            price = pricingPhases?.get(0)?.formattedPrice,
            title = title,
            description = description,
            originalJson = originalJson,
            introductoryPrice = pricingPhases?.get(0)?.formattedPrice,
            freeTrialPeriod = parseDuration(trialPeriod).toString(),
            priceCurrencyCode = pricingPhases?.get(0)?.priceCurrencyCode
        )

        if (result != null) {

            var isUpdated = false
            if ("$trialPeriod" != result.freeTrialPeriod) {
                isUpdated = true
            } else if (pricingPhases?.get(0)?.formattedPrice != result.price) {
                isUpdated = true
            }

            if (isUpdated) {
                update(productId, bool)
            }
        } else {
            insert(detail)
        }
    }

    @Transaction
    fun insertOrUpdate(sku: String, canPurchase: Boolean) {
        val result = getById(sku)
        if (result != null) {
            if (result.canPurchase != canPurchase)
                update(sku, canPurchase)
        } else {
            insert(
                SkuDetailsModel(
                    canPurchase,
                    sku,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )
            )
        }
    }

    @Query("SELECT * FROM SkuDetailsModel WHERE sku = :sku")
    fun getById(sku: String): SkuDetailsModel?

    @Query("DELETE FROM SkuDetailsModel")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(skuDetailsModel: SkuDetailsModel)

    @Query("UPDATE SkuDetailsModel SET canPurchase = :canPurchase WHERE sku = :sku")
    fun update(sku: String, canPurchase: Boolean)
}