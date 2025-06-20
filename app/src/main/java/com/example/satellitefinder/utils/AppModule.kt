package com.example.satellitefinder.utils


import androidx.room.Room
import com.example.satellitefinder.database.SatelliteDatabase
import com.example.satellitefinder.db.SubscriptionDb
import com.example.satellitefinder.repo.SatelliteDataManager
import com.example.satellitefinder.repo.SatelliteRepository
import com.example.satellitefinder.repo.SatelliteRepositoryImpl
import com.example.satellitefinder.repo.SatelliteViewModel
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
        single {
            Room.databaseBuilder(get(), SatelliteDatabase::class.java, "satellite_db").build()
        }
        single { SubscriptionRepository(get(), get()) }
        single { SatelliteDataManager(get(), get()) }
        single<SatelliteRepository> { SatelliteRepositoryImpl(get(), get(), get()) }
        viewModel { SatelliteViewModel(get()) }
        viewModel {
            SubscriptionViewModel(get())
        }

    }
}