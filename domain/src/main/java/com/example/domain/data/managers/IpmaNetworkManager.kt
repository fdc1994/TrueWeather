package com.example.domain.data.managers

import com.example.network.data.DailyWeatherDTO
import com.example.network.data.WeatherDataDTO
import io.reactivex.Single
import

interface IpmaRepository {
    fun getWeatherForecast(globalIdLocal: String): Single<List<WeatherDataDTO>>
}

class IpmaNetworkManagerImpl(private val ipmaApiService: IpmaRepository) {

    fun getWeatherForecast(globalIdLocal: String): Single<List<DailyWeatherDTO>> {
        return ipmaApiService.getWeatherForecast(globalIdLocal)
            .map { ipmaResponse ->
                ipmaResponse.data.map { forecastDto ->
                    mapToWeatherForecast(forecastDto)
                }
            }
    }

    private fun mapToWeatherForecast(forecastDto: IpmaForecastDTO): WeatherForecast {
        return WeatherForecast(
            forecastDto.forecastDate,
            forecastDto.tMin,
            forecastDto.tMax,
            forecastDto.predWindDir,
            forecastDto.idWeatherType,
            forecastDto.classWindSpeed,
            forecastDto.probPrecipita,
            forecastDto.classPrecInt,
            forecastDto.latitude,
            forecastDto.longitude
        )
    }
}
