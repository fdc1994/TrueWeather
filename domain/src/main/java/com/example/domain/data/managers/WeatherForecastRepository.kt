package com.example.domain.data.managers

import com.example.domain.data.WeatherForecast
import com.example.domain.data.mappers.WeatherForecastMappers
import com.example.network.data.WeatherForecastDTO
import com.example.network.interfaces.IPMAService
import com.example.network.persistence.UserPreferencesDataStore
import com.example.network.persistence.WeatherForecastDataStore
import com.example.network.utils.TimestampUtil
import io.reactivex.Single
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import org.joda.time.DateTime
import javax.inject.Inject

interface WeatherForecastRepository {
    fun getWeatherForecast(globalIdLocal: String? = null, hasValidInternetConnection: Boolean): Single<List<WeatherForecast>>
}

class WeatherForecastRepositoryImpl @Inject constructor(
    private val ipmaService: IPMAService,
    private val weatherForecastDataStore: WeatherForecastDataStore,
    private val weatherForecastMappers: WeatherForecastMappers,
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

    private fun getPersistenceInformation(): Single<List<WeatherForecast>> {
        return userPreferencesDataStore.getUserPreferences().flatMap { userPreferences ->
            weatherForecastDataStore.getWeatherForecast().map { weatherForecastList ->
                val filteredList = weatherForecastList.filter { weatherForecast ->
                    userPreferences.locationsList.find { weatherForecast.globalIdLocal.toString() == it } != null
                }
                filteredList.mapNotNull {
                    weatherForecastMappers.mapWeatherResponse(it).takeIf { weatherResponse ->
                        weatherResponse.data.filter {
                            timestampUtil.exceedsTimestamp(it.forecastDate.toSimpleDate(), DateTime.now().millis)
                        }.isNotEmpty()
                    }
                }
            }
        }
    }


    private fun String.toSimpleDate(): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(this, formatter)
        val dateTime = date.atStartOfDay()
        val zoneId = ZoneId.of("Europe/London")
        return dateTime.atZone(zoneId).toEpochSecond() * 1000
    }
}
