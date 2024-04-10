package com.example.domain.data.managers

import com.example.domain.data.WeatherForecast
import com.example.domain.data.mappers.WeatherMappers
import com.example.network.interfaces.IPMAService
import io.reactivex.Single
import javax.inject.Inject

interface IpmaNetworkManager {
    fun getWeatherForecast(globalIdLocal: String): Single<WeatherForecast>
}

class IpmaNetworkManagerImpl @Inject constructor(
    private val ipmaService: IPMAService,
    private val weatherMappers: WeatherMappers
) : IpmaNetworkManager {

    override fun getWeatherForecast(globalIdLocal: String): Single<WeatherForecast> {
        return weatherMappers.mapWeatherResponse(ipmaService.getWeatherData(globalIdLocal))
    }
}
