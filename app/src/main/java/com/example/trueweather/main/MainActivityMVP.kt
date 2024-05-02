package com.example.trueweather.main

import com.example.domain.data.objects.WeatherForecast
import com.example.domain.data.utils.RxResult

interface MainActivityMVP {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showWeather(weatherInfo: RxResult.Success<List<WeatherForecast>>)
        fun showError(error: RxResult.Error)
    }

    interface Presenter {
        fun onAttachView(view: View)
    }
}