package com.example.network.persistence

import android.R.attr.id
import android.R.attr.value
import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder
import com.example.network.data.WeatherLocationDTO
import com.example.network.utils.DateTimeDeserializer
import com.example.network.utils.TimestampUtil
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface DistrictIdentifiersDataStore {

    fun isValid(): Single<Boolean>
    fun getDistrictIdentifiers(): Single<WeatherLocationDTO?>
    fun saveDistrictIdentifiers(identifiers: WeatherLocationDTO): Single<Boolean>
    fun clear(): Completable
}

class DistrictIdentifiersDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val timestampUtil: TimestampUtil
) : DistrictIdentifiersDataStore {

    private val ttl = TimeUnit.DAYS.toMillis(30)
    private val gson = GsonBuilder()
        .registerTypeAdapter(DateTime::class.java, DateTimeDeserializer())
        .create()

    private val dataStore = RxPreferenceDataStoreBuilder(
        context,
        DATASTORE_NAME
    ).build()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun isValid(): Single<Boolean> {
        return dataStore.data().map { preferences ->
            val timeStamp = preferences[PREFS_LAST_TIMESTAMP] ?: 0L
            val isValid = if (timeStamp == 0L) {
                false
            } else {
                !timestampUtil.exceedsTimestamp(timeStamp, ttl)
            }
            isValid
        }.defaultIfEmpty(false).first(false).onErrorReturn { false }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getDistrictIdentifiers(): Single<WeatherLocationDTO?> {
        return dataStore.data().firstOrError()
            .map { preferences ->
                val listString = preferences[PREFS_IDENTIFIERS_LIST]
                if (listString.isNullOrEmpty()) {
                    null // If the identifiers list is empty or null, return null
                } else {
                    val weatherLocation = gson.fromJson(listString, WeatherLocationDTO::class.java)
                    if (weatherLocation.data.isEmpty()) preferences.toMutablePreferences().clear()
                    weatherLocation
                }
            }
            .onErrorReturnItem(WeatherLocationDTO("", "", mutableListOf()))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun saveDistrictIdentifiers(identifiers: WeatherLocationDTO): Single<Boolean> {
        var returnvalue = false
        val updateResult: Single<Preferences> = dataStore.updateDataAsync { prefsIn ->
            val mutablePreferences: MutablePreferences = prefsIn.toMutablePreferences()
            mutablePreferences.set(PREFS_IDENTIFIERS_LIST, gson.toJson(identifiers))
            Single.just(mutablePreferences)
        }
        returnvalue = updateResult.blockingGet() !== null
        return Single.just(returnvalue)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun clear(): Completable {
        return dataStore.data()
            .first(null)
            .flatMapCompletable { preferences ->
                Completable.fromAction { preferences.toMutablePreferences().clear() }
            }
    }

    companion object {
        private const val DATASTORE_NAME = "district_identifiers_data_store"
        private val PREFS_LAST_TIMESTAMP = longPreferencesKey("last_timestamp_district_identifiers_data_store")
        private val PREFS_IDENTIFIERS_LIST = stringPreferencesKey("prefs_identifiers_list")
    }
}