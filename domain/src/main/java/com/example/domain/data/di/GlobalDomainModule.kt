package com.example.domain.data.di

import com.example.domain.data.managers.IpmaRepository
import com.example.domain.data.managers.IpmaRepositoryImpl
import com.example.domain.data.mappers.WeatherMappers
import com.example.domain.data.mappers.WeatherMappersImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface GlobalDomainModule {
    @Binds
    @Singleton
    fun bindIpmaNetworkManager(impl: IpmaRepositoryImpl): IpmaRepository
    // You might have other bindings here

    @Binds
    fun bindWeatherMappers(impl: WeatherMappersImpl): WeatherMappers
}