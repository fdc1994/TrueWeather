package com.example.domain.data.mappers

import com.example.domain.data.objects.WeatherData
import com.example.domain.data.objects.WeatherForecast
import com.example.network.data.PrecipitationIntensity
import com.example.network.data.WeatherDataDTO
import com.example.network.data.WeatherForecastDTO
import com.example.network.data.WeatherType
import com.example.network.data.WindSpeed
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
        WeatherType.fromId(this.idWeatherType),
        WindSpeed.fromClassWindSpeed(this.classWindSpeed),
        PrecipitationIntensity.fromClassPrecInt(this.classPrecInt),
        this.longitude,
        this.forecastDate,
        this.latitude
    )
}