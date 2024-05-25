package com.example.domain.data.repositories

import com.example.domain.data.mappers.OsmLocalisationMappers
import com.example.domain.data.objects.OsmResponse
import com.example.network.interfaces.OsmService

import io.reactivex.Single
import javax.inject.Inject

interface OsmLocalisationRepository {
    suspend fun getLocation(lat: Long, long: Long): OsmResponse
}

class OsmLocalisationRepositoryImpl @Inject constructor(
    private val osmService: OsmService,
    private val osmResponseMappers: OsmLocalisationMappers
): OsmLocalisationRepository {
    override suspend fun getLocation(lat: Long, long: Long): OsmResponse {
        return osmResponseMappers.mapOsmLocalisationResponse(osmService.reverseGeocode(latitude =  lat.toDouble(), longitude = long.toDouble()))
    }
}