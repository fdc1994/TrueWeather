package com.example.network.data

import com.google.gson.annotations.SerializedName

data class OsmAddressDTO(
    @SerializedName("road") val road: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("postcode") val postcode: String?
)

data class OsmResultDTO(
    @SerializedName("address") val address: OsmAddressDTO?,
    @SerializedName("lat") val latitude: Double?,
    @SerializedName("lon") val longitude: Double?
)

data class OsmResponseDTO(
    @SerializedName("lat") val latitude: Double?,
    @SerializedName("lon") val longitude: Double?,
    @SerializedName("display_name") val displayName: String?,
    @SerializedName("address") val address: OsmAddressDTO?
)