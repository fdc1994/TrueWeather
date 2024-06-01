package com.example.domain.data.objects

import com.example.network.data.PrecipitationIntensity
import com.example.network.data.WeatherType
import com.example.network.data.WindSpeed
import java.io.Serializable

data class WeatherResult(
    val resultList: List<WeatherResultWrapper>
)
data class WeatherResultWrapper(
    val weatherForecast: WeatherForecast? = null,
    val address: LocationData? = null,
    val status: WeatherFetchStatus,
    val isUserSavedLocation: Boolean = false
)

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
    val weatherType: WeatherType,
    val classWindSpeed: WindSpeed,
    val classPrecInt: PrecipitationIntensity,
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

enum class WeatherFetchStatus {
    SUCCESS,
    SUCCESS_CURRENT_LOCATION_FROM_PERSISTENCE,
    SUCCESS_FROM_PERSISTENCE,
    PERMISSION_ERROR,
    NETWORK_ERROR,
    NO_INTERNET_ERROR,
    NOT_IN_COUNTRY_ERROR,
    OTHER_ERROR,
    OTHER_ERROR_SEARCH,

}