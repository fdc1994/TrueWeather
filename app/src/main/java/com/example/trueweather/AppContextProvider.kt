package com.example.trueweather

import android.app.Application
import android.content.Context

object AppContextProvider {
    private lateinit var applicationContext: Context

    fun initialize(application: TrueWeatherApp) {
        applicationContext = application.applicationContext
    }

    fun getApplicationContext(): Context {
        return applicationContext
    }
}
