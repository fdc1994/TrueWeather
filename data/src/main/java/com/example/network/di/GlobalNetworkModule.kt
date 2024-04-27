package com.example.network.di

import com.example.network.persistence.DistrictIdentifiersDataStore
import com.example.network.persistence.DistrictIdentifiersDataStoreImpl
import com.example.network.persistence.WeatherForecastDataStore
import com.example.network.persistence.WeatherForecastDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface GlobalNetworkModule {

    @Binds
    @Singleton
    fun bindDistrictIdentifiersDataStore(impl: DistrictIdentifiersDataStoreImpl): DistrictIdentifiersDataStore

    @Binds
    @Singleton
    fun bindWeatheForecastDataStore(impl: WeatherForecastDataStoreImpl): WeatherForecastDataStore
}