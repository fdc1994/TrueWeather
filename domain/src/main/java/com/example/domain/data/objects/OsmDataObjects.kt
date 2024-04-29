package com.example.domain.data.objects

import com.google.gson.annotations.SerializedName

data class OsmAddress(
    @SerializedName("road") val road: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("postcode") val postcode: String?
)

data class OsmResult(
    @SerializedName("address") val address: OsmAddress?,
    @SerializedName("lat") val latitude: Double?,
    @SerializedName("lon") val longitude: Double?
)

data class OsmResponse(
    @SerializedName("lat") val latitude: Double?,
    @SerializedName("lon") val longitude: Double?,
    @SerializedName("display_name") val displayName: String?,
    @SerializedName("address") val address: OsmAddress?
)