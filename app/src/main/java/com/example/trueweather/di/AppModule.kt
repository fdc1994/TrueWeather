package com.example.trueweather.di

import com.example.trueweather.main.MainActivityMVP
import com.example.trueweather.main.MainActivityPresenter
import com.example.trueweather.utils.NetworkConnectivityManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
interface AppModule {
    @Binds
    @ActivityScoped
    fun bindPresenter(impl: MainActivityPresenter): MainActivityMVP.Presenter
}