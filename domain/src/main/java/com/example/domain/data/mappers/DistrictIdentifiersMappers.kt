package com.example.domain.data.mappers

import com.example.domain.data.objects.LocationData
import com.example.domain.data.objects.WeatherLocation
import com.example.network.data.LocationDataDTO
import com.example.network.data.WeatherLocationDTO

interface DistrictIdentifiersMappers {
    fun mapDistrictIdentifiersResponse(districtIdentifiers: WeatherLocationDTO): WeatherLocation
}

class DistrictIdentifiersMappersImpl : DistrictIdentifiersMappers {
    override fun mapDistrictIdentifiersResponse(districtIdentifiers: WeatherLocationDTO): WeatherLocation {
        return districtIdentifiers.toWeatherLocation()
    }

    private fun mapWeatherLocation(dataList: List<LocationDataDTO>): List<LocationData> {
        return dataList.map { it.toLocationData() }
    }

    private fun WeatherLocationDTO.toWeatherLocation() = WeatherLocation(
        owner = this.owner,
        country = this.country,
        data = mapWeatherLocation(this.data)
    )

    private fun LocationDataDTO.toLocationData(): LocationData = LocationData(
        idRegiao = this.idRegiao,
        idAreaAviso = this.idAreaAviso,
        idConcelho = this.idConcelho,
        globalIdLocal = this.globalIdLocal,
        latitude = this.latitude,
        idDistrito = this.idDistrito,
        local = this.local,
        longitude = this.longitude
    )
}