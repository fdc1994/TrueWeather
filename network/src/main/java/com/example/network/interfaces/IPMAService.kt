package com.example.network.interfaces

import com.example.network.data.WeatherDataDTO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface IPMAService {
    @GET("forecast/meteorology/cities/daily/{globalIdLocal}.json")
    fun getWeatherData(@Path("globalIdLocal") globalIdLocal: Int): Single<WeatherDataDTO>
}
