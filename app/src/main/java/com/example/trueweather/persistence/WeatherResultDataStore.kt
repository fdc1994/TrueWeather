package com.example.trueweather.persistence

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.data.objects.WeatherData
import com.example.domain.data.objects.WeatherFetchStatus
import com.example.domain.data.objects.WeatherForecast
import com.example.domain.data.objects.WeatherResult
import com.example.domain.data.objects.WeatherResultList
import com.example.network.utils.TimestampUtil
import com.example.trueweather.utils.NetworkConnectivityManager
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = WeatherResultDataStoreImpl.DATASTORE_NAME)

interface WeatherResultDataStore {
    suspend fun isValid(): Boolean
    suspend fun getWeatherForecast(): WeatherResult
    suspend fun saveWeatherForecast(weatherForecastList: WeatherResult): Boolean
    suspend fun clear()
}

class WeatherResultDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkConnectivityManager: NetworkConnectivityManager
) : WeatherResultDataStore {

    private val gson = GsonBuilder().create()

    override suspend fun isValid(): Boolean {
        val preferences = context.dataStore.data.map { preferences ->
            val timeStamp = preferences[PREFS_LAST_TIMESTAMP]?.toLong() ?: 0L
            timeStamp != 0L && !TimestampUtil.exceedsTimestamp(timeStamp, TTL)
        }.first()
        return preferences
    }

    override suspend fun getWeatherForecast(): WeatherResult {

        var filteredResult = WeatherResult(mutableListOf())

        context.dataStore.data.map { preferences ->
            val weatherForecastListString = preferences[PREFS_WEATHER_FORECAST_LIST]
            if (weatherForecastListString.isNullOrEmpty()) {
                WeatherResult(mutableListOf(WeatherResultList(status = WeatherFetchStatus.OTHER_ERROR)))
            } else {
                gson.fromJson<WeatherResult>(
                    weatherForecastListString,
                    object : TypeToken<WeatherResult>() {}.type
                ).also { weatherResult ->
                    if (weatherResult.resultList.isEmpty()) {
                        clear()
                    } else {
                        val validResultList = weatherResult.resultList.mapIndexed() { index, result ->
                            val (validData, status) = if(index == 0) {
                                handleCurrentLocationPersistence(result)
                            } else {
                                handleRemoteUserPreferencesPersistence(result)
                            }
                            result.copy(weatherForecast = result.weatherForecast?.copy(data = validData), status = status)
                        }.toMutableList()

                        if(!networkConnectivityManager.hasInternetConnection() && validResultList.size == 0) {
                            validResultList.add(0, WeatherResultList(null, null, WeatherFetchStatus.NO_INTERNET_ERROR))
                        }
                        filteredResult = weatherResult.copy(resultList = validResultList)
                        saveWeatherForecast(filteredResult)
                    }
                }
            }
        }.first()

        return filteredResult
    }

    private fun handleRemoteUserPreferencesPersistence(result: WeatherResultList): Pair<List<WeatherData>, WeatherFetchStatus> {
        val validData = result.weatherForecast?.data?.filter { forecast ->
            !TimestampUtil.isBeforeToday(forecast.forecastDate)
        } ?: mutableListOf()
        val status = if (!networkConnectivityManager.hasInternetConnection()) {
            WeatherFetchStatus.NO_INTERNET_ERROR
        } else if (validData.isNotEmpty()) {
            WeatherFetchStatus.SUCCESS_CURRENT_LOCATION_FROM_PERSISTENCE
        } else {
            WeatherFetchStatus.OTHER_ERROR
        }
        return Pair(validData, status)
    }

    private fun handleCurrentLocationPersistence(result: WeatherResultList): Pair<List<WeatherData>, WeatherFetchStatus> {
        val validData = result.weatherForecast?.data?.filter { forecast ->
            !TimestampUtil.isBeforeToday(forecast.forecastDate)
        } ?: mutableListOf()
        val status = if (!networkConnectivityManager.hasInternetConnection()) {
            WeatherFetchStatus.NO_INTERNET_ERROR
        } else if (validData.isNotEmpty()) {
            WeatherFetchStatus.SUCCESS_CURRENT_LOCATION_FROM_PERSISTENCE
        } else {
            WeatherFetchStatus.OTHER_ERROR
        }
        return Pair(validData, status)
    }

    override suspend fun saveWeatherForecast(weatherForecastList: WeatherResult): Boolean {
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