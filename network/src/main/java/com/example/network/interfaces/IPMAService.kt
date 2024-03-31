package com.example.network.interfaces

import com.example.network.data.WeatherDataDTO
import com.example.network.data.WeatherForecastDTO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface IPMAService {
    @GET("forecast/meteorology/cities/daily/{globalIdLocal}.json")
    fun getWeatherData(@Path("globalIdLocal") globalIdLocal: String): Single<WeatherForecastDTO>
}
