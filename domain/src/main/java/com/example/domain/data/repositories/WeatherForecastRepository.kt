package com.example.domain.data.repositories

import com.example.domain.data.LocalizationManager
import com.example.domain.data.mappers.WeatherForecastMappers
import com.example.domain.data.objects.LocationData
import com.example.domain.data.objects.WeatherFetchStatus
import com.example.domain.data.objects.WeatherResult
import com.example.domain.data.objects.WeatherResultList
import com.example.network.interfaces.IPMAService
import com.example.trueweather.persistence.UserPreferencesDataStore
import com.example.network.utils.TimestampUtil
import javax.inject.Inject

interface WeatherForecastRepository {
    suspend fun getWeatherForecast(hasValidInternetConnection: Boolean): WeatherResult
}

class WeatherForecastRepositoryImpl @Inject constructor(
    private val ipmaService: IPMAService,
    private val weatherForecastMappers: WeatherForecastMappers,
    private val localizationManager: LocalizationManager,
) : WeatherForecastRepository {

    override suspend fun getWeatherForecast(hasValidInternetConnection: Boolean): WeatherResult {
        return if (hasValidInternetConnection) {
            makeNetworkCall()
        } else WeatherResult(
            listOf(
                WeatherResultList(
                    status = WeatherFetchStatus.NETWORK_ERROR
                )
            )
        )
    }

    private suspend fun makeNetworkCall(): WeatherResult {
        val weatherResultList = mutableListOf<WeatherResultList>()
        getCurrentLocationDataOrError(weatherResultList)
        getSavedLocationsWeatherForecast(weatherResultList, mutableListOf(233232,232323))
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
                        status = WeatherFetchStatus.NETWORK_ERROR
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

    private suspend fun getSavedLocationsWeatherForecast(weatherResult: MutableList<WeatherResultList>, locationsIds: List<Int>) {
        locationsIds.forEach {
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
        val currentLocationForecastDto = ipmaService.getWeatherData()
        val currentLocationForecast = weatherForecastMappers.mapWeatherResponse(currentLocationForecastDto)
        return WeatherResultList(
            currentLocationForecast,
            locationData,
            WeatherFetchStatus.SUCCESS
        )
    }

    /**

    private fun getPersistenceInformation(): Single<List<WeatherForecast>> {
    return userPreferencesDataStore.getUserPreferences().flatMap { userPreferences ->
    weatherForecastDataStore.getWeatherForecast().map { weatherForecastList ->
    val filteredList = weatherForecastList.filter { weatherForecast ->
    userPreferences.locationsList.find { weatherForecast.globalIdLocal.toString() == it } != null
    }
    filteredList.mapNotNull {
    weatherForecastMappers.mapWeatherResponse(it).takeIf { weatherResponse ->
    weatherResponse.data.filter {
    !timestampUtil.exceedsTimestamp(DateTime.parse(it.forecastDate).millis, DateTime.now().plusDays(5).millis)
    }.isNotEmpty()
    }
    }
    }
    }
    }
     **/
}
