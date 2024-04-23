package com.example.domain.data.managers

import com.example.domain.data.WeatherLocation
import com.example.domain.data.mappers.DistrictIdentifiersMappers
import com.example.domain.data.utils.ErrorType
import com.example.domain.data.utils.RxResult
import com.example.network.data.WeatherLocationDTO
import com.example.network.interfaces.IPMAService
import com.example.network.persistence.DistrictIdentifiersDataStore
import io.reactivex.Single
import javax.inject.Inject

interface DistrictIdentifiersRepository {
    fun getDistrictIdentifiersList(): Single<RxResult<WeatherLocation?>>
}

class DistrictIdentifiersRepositoryImpl @Inject constructor(
    private val ipmaService: IPMAService,
    private val districtIdentifiersDataStore: DistrictIdentifiersDataStore,
    private val districtIdentifiersMappers: DistrictIdentifiersMappers
) : DistrictIdentifiersRepository {

    override fun getDistrictIdentifiersList(): Single<RxResult<WeatherLocation?>> {
        return districtIdentifiersDataStore.getDistrictIdentifiers().flatMap {
            if (it.data.isEmpty()) {
                ipmaService.getDistrictIdentifiers()
                    .flatMap { districtIdentifiers ->
                        districtIdentifiersDataStore.saveDistrictIdentifiers(districtIdentifiers)
                            .flatMap { resultSuccessful ->
                                if (resultSuccessful) {
                                    districtIdentifiersDataStore.getDistrictIdentifiers()
                                        .map { data ->
                                            RxResult.Success(mapData(data))
                                        }
                                } else {
                                    Single.just(RxResult.Error(ErrorType.GENERIC_ERROR))
                                }
                            }
                            .onErrorReturn {
                                RxResult.Error(ErrorType.GENERIC_ERROR)
                            }
                    }
            } else {
                return@flatMap Single.just(RxResult.Success(mapData(it)))
            }
        }
    }

    private fun mapData(it: WeatherLocationDTO): WeatherLocation {
        return districtIdentifiersMappers.mapDistrictIdentifiersResponse(it)
    }
}
