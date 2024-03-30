package com.example.network.data

data class WeatherDataDTO(
    val owner: String,
    val country: String,
    val data: List<DailyWeatherDTO>,
    val globalIdLocal: Int,
    val dataUpdate: String
)

data class DailyWeatherDTO(
    val precipitaProb: String,
    val tMin: String,
    val tMax: String,
    val predWindDir: String,
    val idWeatherType: Int,
    val classWindSpeed: Int,
    val classPrecInt: Int? = null,
    val longitude: String,
    val forecastDate: String,
    val latitude: String
)
