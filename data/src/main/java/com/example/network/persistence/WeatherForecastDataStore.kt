package com.example.network.persistence

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder
import com.example.network.data.WeatherForecastDTO
import com.example.network.utils.TimestampUtil
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface WeatherForecastDataStore {

    fun isValid(): Single<Boolean>
    fun getWeatherForecast(): Single<List<WeatherForecastDTO>>
    fun saveWeatherForecast(weatherForecastList: List<WeatherForecastDTO?>): Single<Boolean>
    fun clear(): Completable
}

class WeatherForecastDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val timestampUtil: TimestampUtil
) : WeatherForecastDataStore {

    private val ttl = TimeUnit.DAYS.toMillis(5)
    private val gson = GsonBuilder().create()

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
    override fun getWeatherForecast(): Single<List<WeatherForecastDTO>> {
        return dataStore
            .data()
            .firstOrError()
            .map { preferences ->
                val weatherForecastListString = preferences[PREFS_WEATHER_FORECAST_LIST]
                if (weatherForecastListString.isNullOrEmpty()) {
                    emptyList() // If the weather forecast list is empty or null, return an empty list
                } else {
                    gson.fromJson<List<WeatherForecastDTO>>(
                        weatherForecastListString,
                        object : TypeToken<List<WeatherForecastDTO>>() {}.type
                    ).also { weatherForecastList ->
                        if (weatherForecastList.isEmpty()) preferences.toMutablePreferences().clear()
                    }
                }
            }
            .onErrorReturnItem(emptyList())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun saveWeatherForecast(weatherForecastList: List<WeatherForecastDTO?>): Single<Boolean> {
        val returnvalue: Boolean
        val updateResult: Single<Preferences> = dataStore.updateDataAsync { prefsIn ->
            val mutablePreferences: MutablePreferences = prefsIn.toMutablePreferences()
            mutablePreferences.set(PREFS_WEATHER_FORECAST_LIST, gson.toJson(weatherForecastList))
            Single.just(mutablePreferences)
        }
        returnvalue = updateResult.blockingGet() !== null
        return Single.just(returnvalue)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun clear(): Completable {
        return dataStore.data()
            .firstOrError()
            .flatMapCompletable { preferences ->
                Completable.fromAction { preferences.toMutablePreferences().clear() }
            }
    }

    companion object {
        const val DATASTORE_NAME = "weather_forecast_data_store"
        private val PREFS_LAST_TIMESTAMP = longPreferencesKey("last_timestamp_weather_forecast_data_store")
        private val PREFS_WEATHER_FORECAST_LIST = stringPreferencesKey("prefs_weather_forecast_list")
    }
}