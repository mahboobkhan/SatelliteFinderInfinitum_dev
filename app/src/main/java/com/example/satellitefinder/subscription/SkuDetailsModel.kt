package com.example.satellitefinder.subscription

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
@Keep
@Entity
data class SkuDetailsModel(
        val canPurchase: Boolean, /* Not in SkuDetails; it's the augmentation */
        @PrimaryKey val sku: String,
        val type: String?,
        val price: String?,
        val title: String?,
        val description: String?,
        val originalJson: String?,
        val introductoryPrice: String?,
        val freeTrialPeriod: String?,
        val priceCurrencyCode: String?

)