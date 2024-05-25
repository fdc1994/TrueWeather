package com.example.domain.data.repositories

import com.example.domain.data.objects.WeatherLocation
import com.example.domain.data.mappers.DistrictIdentifiersMappers
import com.example.network.data.WeatherLocationDTO
import com.example.network.interfaces.IPMAService
import com.example.trueweather.persistence.DistrictIdentifiersDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

interface DistrictIdentifiersRepository {
    suspend fun getDistrictIdentifiersList(): WeatherLocation?
}

class DistrictIdentifiersRepositoryImpl @Inject constructor(
    private val ipmaService: IPMAService,
    private val districtIdentifiersDataStore: com.example.trueweather.persistence.DistrictIdentifiersDataStore,
    private val districtIdentifiersMappers: DistrictIdentifiersMappers
) : DistrictIdentifiersRepository {

    override suspend fun getDistrictIdentifiersList(): WeatherLocation? {
        return withContext(Dispatchers.IO) {
            try {
                val districtIdentifiers = districtIdentifiersDataStore.getDistrictIdentifiers()
                if (districtIdentifiers?.data?.isEmpty() == true) {
                    val districtIdentifiersRemote = ipmaService.getDistrictIdentifiers()
                    districtIdentifiersDataStore.saveDistrictIdentifiers(districtIdentifiersRemote)
                    mapData(districtIdentifiersRemote)
                } else {
                    if (districtIdentifiers != null) {
                        mapData(districtIdentifiers)
                    } else null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun mapData(it: WeatherLocationDTO): WeatherLocation {
        return districtIdentifiersMappers.mapDistrictIdentifiersResponse(it)
    }
}
