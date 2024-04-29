package com.example.domain.data.repositories

import com.example.domain.data.mappers.OsmLocalisationMappers
import com.example.domain.data.objects.OsmResponse
import com.example.network.interfaces.OsmService

import io.reactivex.Single
import javax.inject.Inject

interface OsmLocalisationRepository {
    fun getLocation(lat: Long, long: Long): Single<OsmResponse>
}

class OsmLocalisationRepositoryImpl @Inject constructor(
    private val osmService: OsmService,
    private val osmResponseMappers: OsmLocalisationMappers
): OsmLocalisationRepository {
    override fun getLocation(lat: Long, long: Long): Single<OsmResponse> {
        return osmService.reverseGeocode(latitude =  lat, longitude = long).map {
            osmResponseMappers.mapOsmLocalisationResponse(it)
        }
    }
}