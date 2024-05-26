package com.example.network.persistence

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.network.data.WeatherLocationDTO
import com.example.network.utils.DateTimeDeserializer
import com.example.network.persistence.DistrictIdentifiersDataStoreImpl.Companion.DATASTORE_NAME
import com.example.network.utils.TimestampUtil
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)


interface DistrictIdentifiersDataStore {
    suspend fun isValid(): Boolean
    suspend fun getDistrictIdentifiers(): WeatherLocationDTO?
    suspend fun saveDistrictIdentifiers(identifiers: WeatherLocationDTO): Boolean
    suspend fun clear()
}


class DistrictIdentifiersDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DistrictIdentifiersDataStore {

    private val ttl = TimeUnit.DAYS.toMillis(30)
    private val gson = GsonBuilder()
        .registerTypeAdapter(DateTime::class.java, DateTimeDeserializer())
        .create()

    override suspend fun isValid(): Boolean {
        val preferences = context.dataStore.data.map { preferences ->
            val timeStamp = preferences[PREFS_LAST_TIMESTAMP] ?: 0L
            if (timeStamp == 0L) false else !TimestampUtil.exceedsTimestamp(timeStamp, ttl)
        }.first()
        return preferences
    }

    override suspend fun getDistrictIdentifiers(): WeatherLocationDTO? {
        val preferences = context.dataStore.data.map { preferences ->
            val listString = preferences[PREFS_IDENTIFIERS_LIST]
            if (listString.isNullOrEmpty()) {
                null
            } else {
                val weatherLocation = gson.fromJson(listString, WeatherLocationDTO::class.java)
                if (weatherLocation.data.isEmpty()) {
                    clear()
                    null
                } else {
                    weatherLocation
                }
            }
        }.first()
        return preferences
    }

    override suspend fun saveDistrictIdentifiers(identifiers: WeatherLocationDTO): Boolean {
        context.dataStore.edit { preferences ->
            preferences[PREFS_IDENTIFIERS_LIST] = gson.toJson(identifiers)
            preferences[PREFS_LAST_TIMESTAMP] = System.currentTimeMillis()
        }
        return true
    }

    override suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        const val DATASTORE_NAME = "district_identifiers_data_store"
        private val PREFS_LAST_TIMESTAMP = longPreferencesKey("last_timestamp_district_identifiers_data_store")
        private val PREFS_IDENTIFIERS_LIST = stringPreferencesKey("prefs_identifiers_list")
    }
}