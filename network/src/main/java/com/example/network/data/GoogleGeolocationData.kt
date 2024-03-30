package com.example.network.data

import com.google.gson.annotations.SerializedName

data class GoogleReverseGeocodeResponseDTO(
    @SerializedName("results") val results: List<GoogleGeocodeResultDTO>,
    @SerializedName("status") val status: String
)

data class GoogleGeocodeResultDTO(
    @SerializedName("address_components") val addressComponents: List<AddressComponentDTO>,
    @SerializedName("formatted_address") val formattedAddress: String,
    @SerializedName("geometry") val geometry: GeometryDTO,
    @SerializedName("place_id") val placeId: String,
    @SerializedName("types") val types: List<String>
)

data class AddressComponentDTO(
    @SerializedName("long_name") val longName: String,
    @SerializedName("short_name") val shortName: String,
    @SerializedName("types") val types: List<String>
)

data class GeometryDTO(
    @SerializedName("location") val location: LocationDTO,
    @SerializedName("location_type") val locationType: String,
    @SerializedName("viewport") val viewport: ViewportDTO
)

data class LocationDTO(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)

data class ViewportDTO(
    @SerializedName("northeast") val northeast: LocationDTO,
    @SerializedName("southwest") val southwest: LocationDTO
)