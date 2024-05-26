package com.example.network.di

import com.example.network.interfaces.IPMAService
import com.example.network.interfaces.OsmService
import com.example.network.utils.TimestampUtil
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.components.SingletonComponent
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private val gson: Gson
        get() = GsonBuilder().setLenient().create()

    private const val IPMA_BASE_URL = "https://api.ipma.pt/"

    private const val OSM_BASE_URL = "https://nominatim.openstreetmap.org/"

    @Provides
    @Singleton
    fun provideIpmaService(): IPMAService {
        return Retrofit.Builder()
            .baseUrl(IPMA_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
            .create(IPMAService::class.java)
    }

    @Provides
    @Singleton
    fun provideOSMService(): OsmService {
        return Retrofit.Builder()
            .baseUrl(OSM_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
            .create(OsmService::class.java)
    }
}

