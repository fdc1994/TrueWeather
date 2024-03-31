package com.example.domain.data.managers

import com.example.domain.data.WeatherForecast
import com.example.domain.data.mappers.WeatherMappers
import com.example.network.interfaces.IPMAService
import io.reactivex.Single
import javax.inject.Inject

interface IpmaRepository {
    fun getWeatherForecast(globalIdLocal: String): Single<WeatherForecast>
}

class IpmaNetworkManagerImpl @Inject constructor(
    private val ipmaService: IPMAService,
    private val weatherMappers: WeatherMappers
) : IpmaRepository {

    override fun getWeatherForecast(globalIdLocal: String): Single<WeatherForecast> {
        return weatherMappers.mapWeatherResponse(ipmaService.getWeatherData(globalIdLocal))
    }
}
