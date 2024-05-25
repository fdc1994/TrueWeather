package com.example.network.interfaces

import com.example.network.data.OsmResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface OsmService {

    @GET("reverse")
    suspend fun reverseGeocode(
        @Query("format") format: String = "json",
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): OsmResponseDTO
}