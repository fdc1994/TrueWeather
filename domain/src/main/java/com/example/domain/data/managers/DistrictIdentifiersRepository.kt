package com.example.domain.data.managers

import com.example.domain.data.WeatherLocation
import com.example.domain.data.mappers.DistrictIdentifiersMappers
import com.example.domain.data.utils.ErrorType
import com.example.domain.data.utils.RxResult
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
        return ipmaService.getDistrictIdentifiers()
            .flatMap { districtIdentifiers ->
                districtIdentifiersDataStore.saveDistrictIdentifiers(districtIdentifiers)
                    .flatMap { resultSuccessful ->
                        if (resultSuccessful) {
                            districtIdentifiersDataStore.getDistrictIdentifiers()
                                ?.map { data ->
                                    val mappedData = districtIdentifiersMappers.mapDistrictIdentifiersResponse(data)
                                    RxResult.Success(mappedData)
                                }
                        } else {
                            Single.just(RxResult.Error(ErrorType.GENERIC_ERROR))
                        }
                    }
                    .onErrorReturn {
                        RxResult.Error(ErrorType.GENERIC_ERROR)
                    }
            }
    }
}
