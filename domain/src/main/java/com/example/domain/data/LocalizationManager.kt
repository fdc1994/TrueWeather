package com.example.domain.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.domain.data.mappers.OsmLocalisationMappers
import com.example.domain.data.repositories.DistrictIdentifiersRepository
import com.example.network.interfaces.OsmService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Single
import javax.inject.Inject

interface LocalizationManager {
    fun getLastKnownLocation(): Single<String?>
    fun checkPermissions(): Boolean
}

class LocalizationManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val osmService: OsmService,
    private val osmLocalizationMapper: OsmLocalisationMappers,
    private val districtIdentifiersRepository: DistrictIdentifiersRepository
) : LocalizationManager {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    override fun getLastKnownLocation(): Single<String?> {
        return Single.create { emitter ->
            if (checkPermissions()) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            emitter.onSuccess(location)
                        } else {
                            emitter.onError(Exception("Last known location is null"))
                        }
                    }
                    .addOnFailureListener { e: Exception ->
                        emitter.onError(e)
                    }
            } else {
                emitter.onError(Exception("Location permission not granted"))
            }
        }.flatMap { location ->
            osmService.reverseGeocode(latitude = location.latitude, longitude = location.longitude)
                .map { osmLocalizationMapper.mapOsmLocalisationResponse(it) }
                .flatMap {
                    it.address?.county?.let {
                        mapAddressNameToGlobalId(it)
                    } ?: return@flatMap null
                }
                .onErrorReturnItem("")
        }
    }

    override fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun mapAddressNameToGlobalId(city: String): Single<String?> {
        return districtIdentifiersRepository.getDistrictIdentifiersList().map {
            if (it.isSuccess()) {
                return@map it.getValueOrNull()?.data?.find { it.local == city }?.globalIdLocal.toString() ?: ""
            } else return@map ""
        }
    }
}
