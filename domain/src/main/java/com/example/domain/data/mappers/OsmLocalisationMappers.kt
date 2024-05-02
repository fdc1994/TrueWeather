package com.example.domain.data.mappers

import com.example.domain.data.objects.OsmAddress
import com.example.domain.data.objects.OsmResponse
import com.example.network.data.OsmAddressDTO
import com.example.network.data.OsmResponseDTO
import javax.inject.Inject

interface OsmLocalisationMappers {
    fun mapOsmLocalisationResponse(response: OsmResponseDTO): OsmResponse
}

class OsmLocalisationMappersImpl @Inject constructor() : OsmLocalisationMappers {
    override fun mapOsmLocalisationResponse(response: OsmResponseDTO): OsmResponse {
        with(response) {
            return OsmResponse(
                placeId, licence, osmType, osmId, lat, lon, category, type, placeRank, importance, addressType, name, displayName, address.toOsmAddress(), boundingbox
            )
        }
    }

    private fun OsmAddressDTO.toOsmAddress(): OsmAddress {
        return OsmAddress(
            locality, hamlet, town, city, county, iso3166Level6, postcode, country, countryCode
        )
    }
}