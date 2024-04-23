package com.example.domain.data.managers

import com.example.network.data.WeatherLocationDTO
import com.example.network.interfaces.IPMAService
import com.example.network.persistence.DistrictIdentifiersDataStore
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import javax.inject.Inject

interface DistrictIdentifiersRepository {
    fun getDistrictIdentifiersList(): Single<WeatherLocationDTO?>
}

class DistrictIdentifiersRepositoryImpl @Inject constructor(
    private val ipmaService: IPMAService,
    private val districtIdentifiersDataStore: DistrictIdentifiersDataStore
): DistrictIdentifiersRepository {
    override fun getDistrictIdentifiersList(): Single<WeatherLocationDTO?> {
        return ipmaService.getDistrictIdentifiers()
            .flatMap { districtIdentifiers ->
                districtIdentifiersDataStore.saveDistrictIdentifiers(districtIdentifiers)
                    .flatMap { resultSuccessful ->
                        if (resultSuccessful) {
                            districtIdentifiersDataStore.getDistrictIdentifiers()
                        } else {
                            null
                        }
                    }
                    .onErrorResumeNext {
                        return@onErrorResumeNext null
                    }
            }
    }
}
