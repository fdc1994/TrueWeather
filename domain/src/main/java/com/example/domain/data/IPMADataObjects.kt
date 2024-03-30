package com.example.domain.data

data class WeatherData(
    val owner: String,
    val country: String,
    val data: List<DailyWeather>,
    val globalIdLocal: Int,
    val dataUpdate: String
)

data class DailyWeather(
    val precipitaProb: String,
    val tMin: Double,
    val tMax: Double,
    val predWindDir: String,
    val idWeatherType: Int,
    val classWindSpeed: Int,
    val classPrecInt: Int? = null,
    val longitude: Double,
    val forecastDate: String,
    val latitude: Double
)