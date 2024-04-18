package com.example.trueweather.main

import com.example.domain.data.WeatherForecast

interface MainActivityMVP {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showWeather(weatherInfo: List<WeatherForecast>)

    }

    interface Presenter {
        fun onAttachView(view: View)
    }
}