package com.example.network.data

data class WeatherForecastDTO(
    val owner: String,
    val country: String,
    val data: List<WeatherDataDTO>,
    val globalIdLocal: Int,
    val dataUpdate: String
)

data class WeatherDataDTO(
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