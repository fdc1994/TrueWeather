package com.example.trueweather.splash

import com.example.domain.data.managers.WeatherForecastRepository
import com.example.trueweather.main.MainActivityMVP
import com.example.trueweather.platform.BaseTrueWeatherPresenter
import com.example.trueweather.utils.NetworkConnectivityManager
import javax.inject.Inject

class SplashActivityPresenter @Inject constructor(
    private val networkConnectivityManager: NetworkConnectivityManager
) : SplashActivityMVP.Presenter, BaseTrueWeatherPresenter() {
    override fun onAttachView(view: SplashActivityMVP.View) {
        TODO("Not yet implemented")
    }
}