package com.example.network.interfaces

import com.example.network.data.OsmResponseDTO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface OsmService {

    @GET("reverse")
    fun reverseGeocode(
        @Query("format") format: String = "json",
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Single<OsmResponseDTO>
}
