package com.example.network.interfaces

import io.reactivex.Single
import retrofit2.http.Query

interface GooglePlacesService {
    @GET("maps/api/geocode/json")
    fun reverseGeocode(
        @Query("latlng") latlng: String,
        @Query("key") apiKey: String
    ): Single<ReverseGeocodeResponse>
}