package com.example.domain.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.domain.data.mappers.OsmLocalisationMappers
import com.example.domain.data.objects.LocationData
import com.example.domain.data.repositories.DistrictIdentifiersRepository
import com.example.network.interfaces.OsmService
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface LocalizationManager {
    suspend fun getLastKnownLocation(): LocationData?
    suspend fun mapAddressNameToDistrictIdentifier(city: String): LocationData?
    suspend fun mapGlobalIdToDistrictIdentifier(globalId: Int): LocationData?
    fun checkPermissions(): Boolean
}

class LocalizationManagerImpl @Inject constructor(
    private val context: Context,
    private val osmService: OsmService,
    private val osmLocalizationMapper: OsmLocalisationMappers,
    private val districtIdentifiersRepository: DistrictIdentifiersRepository
) : LocalizationManager {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    override suspend fun getLastKnownLocation(): LocationData? {
        return withContext(Dispatchers.IO) {
            if (checkPermissions()) {
                try {
                    val location: Location? = fusedLocationClient.lastLocation.result
                    location?.let {
                        val response = osmService.reverseGeocode(latitude = it.latitude, longitude = it.longitude)
                        osmLocalizationMapper.mapOsmLocalisationResponse(response).address?.county?.let { county -> mapAddressNameToDistrictIdentifier(county) }
                    }
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
        }
    }

    override fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun mapAddressNameToDistrictIdentifier(city: String): LocationData? {
        return withContext(Dispatchers.IO) {
            val response = districtIdentifiersRepository.getDistrictIdentifiersList()
            response?.data?.find { it.local == city }
        }
    }

    override suspend fun mapGlobalIdToDistrictIdentifier(globalId: Int): LocationData? {
        return withContext(Dispatchers.IO) {
            val response = districtIdentifiersRepository.getDistrictIdentifiersList()
            response?.data?.find { it.globalIdLocal == globalId }
        }
    }
}