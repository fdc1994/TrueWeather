package com.example.trueweather.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
interface ActivityModule {
    @Binds
    @ActivityScoped
    fun bindSplashActivityPresenter(impl: SplashActivityPresenter): SplashActivityMVP.Presenter

    @Binds
    @ActivityScoped
    fun bindMainActivityPresenter(impl: MainActivityPresenter): MainActivityMVP.Presenter

}