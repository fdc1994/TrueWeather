package com.example.network.data

import com.google.gson.annotations.SerializedName


data class OsmAddressDTO(
    @SerializedName("locality") val locality: String?,
    @SerializedName("hamlet") val hamlet: String?,
    @SerializedName("town") val town: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("county") val county: String?,
    @SerializedName("ISO3166-2-lvl6") val iso3166Level6: String?,
    @SerializedName("postcode") val postcode: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("country_code") val countryCode: String?
)

data class OsmResponseDTO(
    @SerializedName("place_id") val placeId: Long,
    @SerializedName("licence") val licence: String,
    @SerializedName("osm_type") val osmType: String,
    @SerializedName("osm_id") val osmId: Long,
    @SerializedName("lat") val lat: String,
    @SerializedName("lon") val lon: String,
    @SerializedName("category") val category: String,
    @SerializedName("type") val type: String,
    @SerializedName("place_rank") val placeRank: Int,
    @SerializedName("importance") val importance: Double,
    @SerializedName("addresstype") val addressType: String,
    @SerializedName("name") val name: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("address") val address: OsmAddressDTO,
    @SerializedName("boundingbox") val boundingbox: List<String>
)
