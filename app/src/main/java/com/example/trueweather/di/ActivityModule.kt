package com.example.trueweather.di

import android.app.Activity
import com.example.trueweather.main.MainActivityMVP
import com.example.trueweather.main.MainActivityPresenter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ActivityComponent::class)
interface ActivityModule {
    @Binds
    @ActivityScoped
    fun bindPresenter(impl: MainActivityPresenter): MainActivityMVP.Presenter

}