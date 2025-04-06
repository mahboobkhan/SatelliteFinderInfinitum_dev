
package com.example.satellitefinder.utils


import androidx.room.Room
import com.example.satellitefinder.db.SubscriptionDb
import com.example.satellitefinder.subscription.SubscriptionRepository
import com.example.satellitefinder.subscription.SubscriptionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {

    val getModule = module {

        single {
            Room.databaseBuilder(
                get(),
                SubscriptionDb::class.java,
                "SubscriptionDB"
            ).fallbackToDestructiveMigration()
                .allowMainThreadQueries().build()
        }
        single{ SubscriptionRepository(get(),get()) }
        viewModel {
            SubscriptionViewModel(get())
        }

    }
}