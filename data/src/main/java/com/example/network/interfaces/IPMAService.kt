package com.example.network.interfaces

import com.example.network.data.WeatherForecastDTO
import com.example.network.data.WeatherLocation
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface IPMAService {
    @GET("open-data/forecast/meteorology/cities/daily/{globalIdLocal}.json")
    fun getWeatherData(@Path("globalIdLocal") globalIdLocal: String? = null): Single<WeatherForecastDTO>

    @GET("open-data/forecast/meteorology/cities/daily/distrits-islands.json")
    fun getWeatherData(): Single<WeatherLocation>
}
