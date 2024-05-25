package com.example.network.persistence

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.network.data.WeatherForecastDTO
import com.example.network.utils.TimestampUtil
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = WeatherForecastDataStoreImpl.DATASTORE_NAME)

interface WeatherForecastDataStore {
    suspend fun isValid(): Boolean
    suspend fun getWeatherForecast(): List<WeatherForecastDTO>
    suspend fun saveWeatherForecast(weatherForecastList: List<WeatherForecastDTO?>): Boolean
    suspend fun clear()
}

class WeatherForecastDataStoreImpl @Inject constructor(
    private val context: Context,
    private val timestampUtil: TimestampUtil
) : WeatherForecastDataStore {

    private val gson = GsonBuilder().create()

    override suspend fun isValid(): Boolean {
        val preferences = context.dataStore.data.map { preferences ->
            val timeStamp = preferences[PREFS_LAST_TIMESTAMP]?.toLong() ?: 0L
            timeStamp != 0L && !timestampUtil.exceedsTimestamp(timeStamp, TTL)
        }.first()
        return preferences
    }

    override suspend fun getWeatherForecast(): List<WeatherForecastDTO> {
        val preferences = context.dataStore.data.map { preferences ->
            val weatherForecastListString = preferences[PREFS_WEATHER_FORECAST_LIST]
            if (weatherForecastListString.isNullOrEmpty()) {
                emptyList() // If the weather forecast list is empty or null, return an empty list
            } else {
                gson.fromJson<List<WeatherForecastDTO>>(
                    weatherForecastListString,
                    object : TypeToken<List<WeatherForecastDTO>>() {}.type
                ).also { weatherForecastList ->
                    if (weatherForecastList.isEmpty()) {
                        clear()
                    }
                }
            }
        }.first()
        return preferences
    }

    override suspend fun saveWeatherForecast(weatherForecastList: List<WeatherForecastDTO?>): Boolean {
        context.dataStore.edit { preferences ->
            preferences[PREFS_WEATHER_FORECAST_LIST] = gson.toJson(weatherForecastList)
        }
        return true
    }

    override suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        const val DATASTORE_NAME = "weather_forecast_data_store"
        private val PREFS_LAST_TIMESTAMP = stringPreferencesKey("last_timestamp_weather_forecast_data_store")
        private val PREFS_WEATHER_FORECAST_LIST = stringPreferencesKey("prefs_weather_forecast_list")
        private val TTL = TimeUnit.DAYS.toMillis(5)
    }
}