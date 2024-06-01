package com.example.satellitefinder.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.satellitefinder.subscription.SkuDetailsModel

@Database(entities = [SkuDetailsModel::class],version = 1,
    exportSchema = true)
abstract class SubscriptionDb:RoomDatabase() {

    abstract fun skuDetailsDao(): SubscriptionDao

}
