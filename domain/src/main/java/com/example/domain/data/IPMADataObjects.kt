package com.example.domain.data

data class WeatherForecast(
    val owner: String,
    val country: String,
    val data: List<WeatherData>,
    val globalIdLocal: Int,
    val dataUpdate: String
)

data class WeatherData(
    val precipitaProb: String,
    val tMin: String,
    val tMax: String,
    val predWindDir: String,
    val idWeatherType: Int,
    val classWindSpeed: Int,
    val classPrecInt: Int?,
    val longitude: String,
    val forecastDate: String,
    val latitude: String
)