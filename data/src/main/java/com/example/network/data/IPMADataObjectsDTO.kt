package com.example.network.data

import java.io.Serializable

//WEATHER RESPONSE 5 DAYS
data class WeatherForecastDTO(
    val owner: String,
    val country: String,
    val data: List<WeatherDataDTO>,
    val globalIdLocal: Int,
    val dataUpdate: String
) : Serializable

data class WeatherDataDTO(
    val precipitaProb: String,
    val tMin: String,
    val tMax: String,
    val predWindDir: String,
    val idWeatherType: Int,
    val classWindSpeed: Int,
    val classPrecInt: Int,
    val longitude: String,
    val forecastDate: String,
    val latitude: String
) : Serializable

//DISTRICTS IDENTIFIERS


data class WeatherLocationDTO(
    val owner: String,
    val country: String,
    val data: List<LocationDataDTO>
) : Serializable

data class LocationDataDTO(
    val idRegiao: Int,
    val idAreaAviso: String,
    val idConcelho: Int,
    val globalIdLocal: Int,
    val latitude: String,
    val idDistrito: Int,
    val local: String,
    val longitude: String
) : Serializable


