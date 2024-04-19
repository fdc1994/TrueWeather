package com.example.trueweather.di

import android.content.Context
import com.example.trueweather.utils.NetworkConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkConnectivityManager(@ApplicationContext appContext: Context): NetworkConnectivityManager {
        return NetworkConnectivityManager(appContext)
    }
}