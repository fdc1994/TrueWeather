package com.example.network.interfaces

import com.example.network.data.WeatherForecastDTO
import com.example.network.data.WeatherLocationDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface IPMAService {

    @GET("open-data/forecast/meteorology/cities/daily/{globalIdLocal}.json")
    suspend fun getWeatherData(@Path("globalIdLocal") globalIdLocal: String? = null): WeatherForecastDTO

    @GET("open-data/distrits-islands.json")
    suspend fun getDistrictIdentifiers(): WeatherLocationDTO
}
