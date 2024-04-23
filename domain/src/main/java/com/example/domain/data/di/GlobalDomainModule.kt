package com.example.domain.data.di

import com.example.domain.data.managers.DistrictIdentifiersRepository
import com.example.domain.data.managers.DistrictIdentifiersRepositoryImpl
import com.example.domain.data.managers.WeatherForecastRepository
import com.example.domain.data.managers.WeatherForecastRepositoryImpl
import com.example.domain.data.mappers.DistrictIdentifiersMappers
import com.example.domain.data.mappers.DistrictIdentifiersMappersImpl
import com.example.domain.data.mappers.WeatherForecastMappers
import com.example.domain.data.mappers.WeatherForecastMappersImpl
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
    fun bindWeatherForecastRepository(impl: WeatherForecastRepositoryImpl): WeatherForecastRepository

    @Binds
    @Singleton
    fun bindWeatherForecastMappers(impl: WeatherForecastMappersImpl): WeatherForecastMappers

    @Binds
    @Singleton
    fun bindDistrictIdentifiersRepository(impl: DistrictIdentifiersRepositoryImpl): DistrictIdentifiersRepository

    @Binds
    @Singleton
    fun bindDistrictIdentifierMappers(impl: DistrictIdentifiersMappersImpl): DistrictIdentifiersMappers
}