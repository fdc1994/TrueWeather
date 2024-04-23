package com.example.domain.data.mappers

import com.example.domain.data.WeatherData
import com.example.domain.data.WeatherForecast
import com.example.network.data.WeatherDataDTO
import com.example.network.data.WeatherForecastDTO
import javax.inject.Inject

interface WeatherForecastMappers {
    fun mapWeatherResponse(weatherForecast: WeatherForecastDTO): WeatherForecast
}

class WeatherForecastMappersImpl @Inject constructor() : WeatherForecastMappers {
    override fun mapWeatherResponse(weatherForecast: WeatherForecastDTO): WeatherForecast {
        return weatherForecast.toWeatherForecast()
    }

    private fun mapWeatherData(dataList: List<WeatherDataDTO>): List<WeatherData> {
        return dataList.map { it.toWeatherData() }
    }

    private fun WeatherForecastDTO.toWeatherForecast() : WeatherForecast = WeatherForecast(
        this.owner,
        this.country,
        mapWeatherData(this.data),
        this.globalIdLocal,
        this.dataUpdate
    )
    private fun WeatherDataDTO.toWeatherData(): WeatherData = WeatherData(
        this.precipitaProb,
        this.tMin,
        this.tMax,
        this.predWindDir,
        this.idWeatherType,
        this.classWindSpeed,
        this.classPrecInt,
        this.longitude,
        this.forecastDate,
        this.latitude
    )
}