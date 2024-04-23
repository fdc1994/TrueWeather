package com.example.domain.data.managers

import com.example.domain.data.WeatherForecast
import com.example.domain.data.mappers.WeatherForecastMappers
import com.example.network.interfaces.IPMAService
import io.reactivex.Single
import javax.inject.Inject

interface WeatherForecastRepository {
    fun getWeatherForecast(globalIdLocal: String? = null, hasValidInternetConnection: Boolean): Single<List<WeatherForecast>>
}

class WeatherForecastRepositoryImpl @Inject constructor(
    private val ipmaService: IPMAService,
    private val weatherForecastMappers: WeatherForecastMappers,
) : WeatherForecastRepository {

    override fun getWeatherForecast(globalIdLocal: String?, hasValidInternetConnection: Boolean): Single<List<WeatherForecast>> {
        return if (hasValidInternetConnection) {
            makeNetworkCall(globalIdLocal)
        } else Single.just(listOf())
    }

    private fun makeNetworkCall(globalIdLocal: String?): Single<List<WeatherForecast>> {
        return if (!globalIdLocal.isNullOrEmpty()) {
            ipmaService.getWeatherData(globalIdLocal).map { listOf(weatherForecastMappers.mapWeatherResponse(it)) }
        } else {
            Single.just(listOf())
        }
    }
}