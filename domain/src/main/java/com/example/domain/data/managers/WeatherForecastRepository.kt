package com.example.domain.data.managers

import com.example.domain.data.WeatherForecast
import com.example.domain.data.mappers.WeatherForecastMappers
import com.example.network.interfaces.IPMAService
import com.example.network.persistence.WeatherForecastDataStore
import io.reactivex.Single
import javax.inject.Inject

interface WeatherForecastRepository {
    fun getWeatherForecast(globalIdLocal: String? = null, hasValidInternetConnection: Boolean): Single<List<WeatherForecast>>
}

class WeatherForecastRepositoryImpl @Inject constructor(
    private val ipmaService: IPMAService,
    private val weatherForecastDataStore: WeatherForecastDataStore,
    private val weatherForecastMappers: WeatherForecastMappers,
) : WeatherForecastRepository {

    override fun getWeatherForecast(globalIdLocal: String?, hasValidInternetConnection: Boolean): Single<List<WeatherForecast>> {
        return if (hasValidInternetConnection) {
            makeNetworkCall(globalIdLocal)
        } else getPersistenceInformation(globalIdLocal)
    }

    private fun makeNetworkCall(globalIdLocal: String?): Single<List<WeatherForecast>> {
        return if (!globalIdLocal.isNullOrEmpty()) {
            ipmaService.getWeatherData(globalIdLocal).map { weatherForecastDto ->
                with(weatherForecastDataStore) {
                    clear()
                    saveWeatherForecast(weatherForecastDto)
                }
                return@map listOf(weatherForecastMappers.mapWeatherResponse(weatherForecastDto))
            }
        } else {
            Single.just(listOf())
        }
    }

    private fun getPersistenceInformation(globalIdLocal: String?): Single<List<WeatherForecast>> {
        return if (!globalIdLocal.isNullOrEmpty()) {
            weatherForecastDataStore.getWeatherForecast().map {
                if (it.globalIdLocal.toString() == globalIdLocal) return@map listOf(
                    weatherForecastMappers.mapWeatherResponse(it)
                ) else listOf()
            }
        } else {
            Single.just(listOf())
        }
    }
}