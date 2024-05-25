package com.example.trueweather.di

import com.example.trueweather.persistence.DistrictIdentifiersDataStore
import com.example.trueweather.persistence.DistrictIdentifiersDataStoreImpl
import com.example.trueweather.persistence.UserPreferencesDataStore
import com.example.trueweather.persistence.UserPreferencesDataStoreImpl
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
    fun bindWeatheForecastDataStore(impl: WeatherResultDataStoreImpl): WeatherResultDataStore

    @Binds
    @Singleton
    fun userPreferencesDataStore(impl: UserPreferencesDataStoreImpl): UserPreferencesDataStore
}