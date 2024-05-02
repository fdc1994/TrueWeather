package com.example.domain.data.repositories

import com.example.domain.data.LocalizationManager
import com.example.domain.data.objects.WeatherForecast
import com.example.domain.data.mappers.WeatherForecastMappers
import com.example.network.data.WeatherForecastDTO
import com.example.network.interfaces.IPMAService
import com.example.network.persistence.UserPreferencesDataStore
import com.example.network.persistence.WeatherForecastDataStore
import com.example.network.utils.TimestampUtil
import io.reactivex.Single
import org.joda.time.DateTime
import javax.inject.Inject

interface WeatherForecastRepository {
    fun getWeatherForecast(globalIdLocal: String? = null, hasValidInternetConnection: Boolean): Single<List<WeatherForecast>>
}

class WeatherForecastRepositoryImpl @Inject constructor(
    private val ipmaService: IPMAService,
    private val weatherForecastDataStore: WeatherForecastDataStore,
    private val weatherForecastMappers: WeatherForecastMappers,
    private val localizationManager: LocalizationManager,
    private val userPreferencesDataStore: UserPreferencesDataStore,
    private val timestampUtil: TimestampUtil
) : WeatherForecastRepository {

    override fun getWeatherForecast(globalIdLocal: String?, hasValidInternetConnection: Boolean): Single<List<WeatherForecast>> {
        return if (hasValidInternetConnection) {
            makeNetworkCall(globalIdLocal)
        } else getPersistenceInformation()
    }

    private fun makeNetworkCall(globalIdLocal: String?): Single<List<WeatherForecast>> {
        return if (!globalIdLocal.isNullOrEmpty()) {
            ipmaService.getWeatherData(globalIdLocal).map { weatherForecastDto ->
                listOf(weatherForecastMappers.mapWeatherResponse(weatherForecastDto))
            }
        } else {
            userPreferencesDataStore.getUserPreferences().flatMap { userPreferences ->
                val weatherForecastListDTO = mutableListOf<WeatherForecastDTO>()
                val weatherForecastList = mutableListOf<WeatherForecast>()
                if (localizationManager.checkPermissions()) {
                    localizationManager.getLastKnownLocation().flatMap { lastKnownLocation ->
                        ipmaService.getWeatherData(lastKnownLocation).doOnSuccess {
                            weatherForecastListDTO.add(it)
                            weatherForecastList.add(weatherForecastMappers.mapWeatherResponse(it))
                        }
                    }
                } else {
                    Single.just(Unit)
                }.flatMap {
                    Single.concat(userPreferences.locationsList.map { location ->
                        ipmaService.getWeatherData(location)
                            .doOnSuccess { weatherForecastDto ->
                                weatherForecastListDTO.add(weatherForecastDto)
                                weatherForecastList.add(weatherForecastMappers.mapWeatherResponse(weatherForecastDto))
                            }
                    }).toList().flatMap { _ ->
                        with(weatherForecastDataStore) {
                            clear()
                            if (weatherForecastListDTO.isNotEmpty()) {
                                saveWeatherForecast(weatherForecastListDTO.toList()) // Save DTOs for persistence
                            }
                        }
                        Single.just(weatherForecastList)
                    }
                }
            }
        }
    }


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

}
