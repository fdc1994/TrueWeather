package com.example.trueweather.di

import com.example.network.persistence.DistrictIdentifiersDataStore
import com.example.network.persistence.DistrictIdentifiersDataStoreImpl
import com.example.network.persistence.UserPreferencesDataStore
import com.example.network.persistence.UserPreferencesDataStoreImpl
import com.example.trueweather.persistence.WeatherResultDataStore
import com.example.trueweather.persistence.WeatherResultDataStoreImpl
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
    fun bindWeatherForecastDataStore(impl: WeatherResultDataStoreImpl): WeatherResultDataStore

    @Binds
    @Singleton
    fun bindUserPreferencesDataStore(impl: UserPreferencesDataStoreImpl): UserPreferencesDataStore
}