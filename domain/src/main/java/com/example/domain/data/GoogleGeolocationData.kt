package com.example.domain.data

data class GoogleReverseGeocodeResponse(
    val results: List<GoogleGeocodeResult>,
    val status: String
)

data class GoogleGeocodeResult(
    val addressComponents: List<AddressComponent>,
    val formattedAddress: String,
    val geometry: Geometry,
    val placeId: String,
    val types: List<String>
)

data class AddressComponent(
    val longName: String,
    val shortName: String,
    val types: List<String>
)

data class Geometry(
    val location: Location,
    val locationType: String,
    val viewport: Viewport
)

data class Location(
    val lat: Double,
    val lng: Double
)

data class Viewport(
    val northeast: Location,
    val southwest: Location
)