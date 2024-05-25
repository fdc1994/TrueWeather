package com.example.domain.data.repositories

import com.example.network.data.WeatherLocationDTO
import com.example.network.interfaces.IPMAService
import com.example.network.persistence.DistrictIdentifiersDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

interface DistrictIdentifiersRepository {
    suspend fun getDistrictIdentifiersList(): WeatherLocationDTO?
}

class DistrictIdentifiersRepositoryImpl @Inject constructor(
    private val ipmaService: IPMAService,
    private val districtIdentifiersDataStore: DistrictIdentifiersDataStore
) : DistrictIdentifiersRepository {

    override suspend fun getDistrictIdentifiersList(): WeatherLocationDTO? {
        return withContext(Dispatchers.IO) {
            try {
                val districtIdentifiers = districtIdentifiersDataStore.getDistrictIdentifiers()
                if (districtIdentifiers?.data?.isEmpty() == true) {
                    val districtIdentifiersRemote = ipmaService.getDistrictIdentifiers()
                    districtIdentifiersDataStore.saveDistrictIdentifiers(districtIdentifiersRemote)
                    districtIdentifiersRemote
                } else {
                    if (districtIdentifiers != null) {
                        districtIdentifiers
                    } else null
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}
