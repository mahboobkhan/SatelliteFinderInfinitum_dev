package com.example.satellitefinder.subscription

object SubscriptionSkus {

 //   test
//"android.test.purchased"
    const val monthlySubscriptionId = "sku_monthly_sub_satellite_finder"

    const val yearlySubscriptionId = "sku_yearly_sub_satellite_finder"


    val SUBS_SKUS = listOf(
        monthlySubscriptionId,
        yearlySubscriptionId
    )

    val NON_CONSUMABLE_SKUS = SUBS_SKUS

}