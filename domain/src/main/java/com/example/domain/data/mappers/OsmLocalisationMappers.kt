package com.example.domain.data.mappers

import com.example.domain.data.objects.OsmAddress
import com.example.domain.data.objects.OsmResponse
import com.example.network.data.OsmAddressDTO
import com.example.network.data.OsmResponseDTO
import javax.inject.Inject

interface OsmLocalisationMappers {
    fun mapOsmLocalisationResponse(response: OsmResponseDTO): OsmResponse
}

class OsmLocalisationMappersImpl @Inject constructor(): OsmLocalisationMappers {
    override fun mapOsmLocalisationResponse(response: OsmResponseDTO): OsmResponse {
        with(response) {
            return OsmResponse(
                latitude = this.latitude,
                longitude = this.longitude,
                displayName = this.displayName,
                address = this.address?.toOsmAddress()
            )
        }

    }

    private fun OsmAddressDTO.toOsmAddress(): OsmAddress {
        return OsmAddress(
            road = this.road,
            city = this.city,
            state = this.state,
            country = this.country,
            postcode = this.postcode,
        )
    }
}