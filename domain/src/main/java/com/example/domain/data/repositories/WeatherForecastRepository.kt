package com.example.domain.data.repositories

import com.example.domain.data.LocalizationManager
import com.example.domain.data.mappers.WeatherForecastMappers
import com.example.domain.data.objects.LocationData
import com.example.domain.data.objects.WeatherFetchStatus
import com.example.domain.data.objects.WeatherResult
import com.example.domain.data.objects.WeatherResultList
import com.example.network.data.UserPreferences
import com.example.network.interfaces.IPMAService
import com.example.network.persistence.DistrictIdentifiersDataStore
import com.example.network.persistence.UserPreferencesDataStore
import com.example.network.utils.TimestampUtil
import javax.inject.Inject

interface WeatherForecastRepository {
    suspend fun getWeatherForecast(): WeatherResult
    suspend fun trySearchWeatherForecast(searchQuery: String): WeatherResult
}

class WeatherForecastRepositoryImpl @Inject constructor(
    private val ipmaService: IPMAService,
    private val weatherForecastMappers: WeatherForecastMappers,
    private val localizationManager: LocalizationManager,
    private val userpreferencesDataStore: UserPreferencesDataStore
) : WeatherForecastRepository {

    override suspend fun getWeatherForecast(): WeatherResult {
        val weatherResultList = mutableListOf<WeatherResultList>()
        getCurrentLocationDataOrError(weatherResultList)
        getSavedLocationsWeatherForecast(weatherResultList)
        return WeatherResult(weatherResultList)
    }

    override suspend fun trySearchWeatherForecast(searchQuery: String): WeatherResult {
        val weatherResultList = mutableListOf<WeatherResultList>()
        val districtIdentifiers = localizationManager.mapAddressNameToMultipleDistrictIdentifier(city = searchQuery)
        if(districtIdentifiers.isNotEmpty()) {
            districtIdentifiers.forEach {
                it.let {
                    if(it?.globalIdLocal != null) {
                        weatherResultList.add(
                            fetchForecast(it)
                        )
                    }
                }
            }
        }
        return WeatherResult(weatherResultList)
    }

    private suspend fun getCurrentLocationDataOrError(weatherResult: MutableList<WeatherResultList>) {
        if (localizationManager.checkPermissions()) {
            val currentLocationData = localizationManager.getLastKnownLocation()
            if (currentLocationData?.globalIdLocal != null) {
                weatherResult.add(
                    fetchForecast(currentLocationData)
                )
            } else {
                weatherResult.add(
                    WeatherResultList(
                        status = WeatherFetchStatus.NOT_IN_COUNTRY_ERROR
                    )
                )
            }
        } else {
            weatherResult.add(
                WeatherResultList(
                    status = WeatherFetchStatus.PERMISSION_ERROR
                )
            )
        }
    }

    private suspend fun getSavedLocationsWeatherForecast(weatherResult: MutableList<WeatherResultList>) {
        userpreferencesDataStore.saveUserPreferences(UserPreferences(mutableListOf("1010500","1020500")))
        val locationIds = userpreferencesDataStore.getUserPreferences().locationsList

        locationIds.forEach {
            val locationData = localizationManager.mapGlobalIdToDistrictIdentifier(it)
            if(locationData?.globalIdLocal != null) {
                weatherResult.add(
                    fetchForecast(locationData)
                )
            }
            else {
                weatherResult.add(
                    WeatherResultList(status = WeatherFetchStatus.OTHER_ERROR)
                )
            }
        }
    }


    private suspend fun fetchForecast(
        locationData: LocationData?
    ): WeatherResultList {
        val currentLocationForecastDto = ipmaService.getWeatherData(locationData!!.globalIdLocal.toString())
        val currentLocationForecast = weatherForecastMappers.mapWeatherResponse(currentLocationForecastDto)
        val validData = currentLocationForecast.data.filter {
            !TimestampUtil.isBeforeToday(it.forecastDate)
        }
        val status = if(validData.isEmpty()) {
            WeatherFetchStatus.OTHER_ERROR
        } else {
            WeatherFetchStatus.SUCCESS
        }
        return WeatherResultList(
            currentLocationForecast.copy(data = validData),
            locationData,
            status
        )
    }
}
