package com.example.domain.data.objects

import java.io.Serializable

data class WeatherForecast(
    val owner: String,
    val country: String,
    val data: List<WeatherData>,
    val globalIdLocal: Int,
    val dataUpdate: String
): Serializable

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
): Serializable


//DISTRICTS IDENTIFIERS
data class WeatherLocation(
    val owner: String,
    val country: String,
    val data: List<LocationData>
): Serializable

data class LocationData(
    val idRegiao: Int,
    val idAreaAviso: String,
    val idConcelho: Int,
    val globalIdLocal: Int,
    val latitude: String,
    val idDistrito: Int,
    val local: String,
    val longitude: String
): Serializable