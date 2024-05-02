package com.example.domain.data.di

import com.example.domain.data.LocalizationManager
import com.example.domain.data.LocalizationManagerImpl
import com.example.domain.data.repositories.DistrictIdentifiersRepository
import com.example.domain.data.repositories.DistrictIdentifiersRepositoryImpl
import com.example.domain.data.repositories.WeatherForecastRepository
import com.example.domain.data.repositories.WeatherForecastRepositoryImpl
import com.example.domain.data.mappers.DistrictIdentifiersMappers
import com.example.domain.data.mappers.DistrictIdentifiersMappersImpl
import com.example.domain.data.mappers.OsmLocalisationMappers
import com.example.domain.data.mappers.OsmLocalisationMappersImpl
import com.example.domain.data.mappers.WeatherForecastMappers
import com.example.domain.data.mappers.WeatherForecastMappersImpl
import com.example.domain.data.repositories.OsmLocalisationRepository
import com.example.domain.data.repositories.OsmLocalisationRepositoryImpl
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

    @Binds
    @Singleton
    fun bindOsmLocalisationRepository(impl: OsmLocalisationRepositoryImpl): OsmLocalisationRepository

    @Binds
    @Singleton
    fun bindOsmLocalisationMappers(impl: OsmLocalisationMappersImpl): OsmLocalisationMappers

    @Binds
    @Singleton
    fun bindLocalizationManager(impl: LocalizationManagerImpl): LocalizationManager
}