package com.example.trueweather

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TrueWeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppContextProvider.initialize(this)
    }
}