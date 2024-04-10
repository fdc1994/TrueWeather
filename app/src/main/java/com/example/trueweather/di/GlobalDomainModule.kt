package com.example.trueweather.di

import com.example.domain.data.managers.IpmaNetworkManager
import com.example.domain.data.managers.IpmaNetworkManagerImpl
import com.example.domain.data.mappers.WeatherMappers
import com.example.domain.data.mappers.WeatherMappersImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface GlobalDomainModule {
    @Binds
    @Singleton
    fun bindIpmaNetworkManager(impl: IpmaNetworkManagerImpl): IpmaNetworkManager
    // You might have other bindings here

    @Binds
    fun bindWeatherMappers(impl: WeatherMappersImpl): WeatherMappers
}