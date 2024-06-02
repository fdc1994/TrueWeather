package com.example.trueweather.main.addlocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.data.objects.WeatherResult
import com.example.domain.data.objects.WeatherResultWrapper
import com.example.domain.data.repositories.WeatherForecastRepository
import com.example.network.persistence.UserPreferencesDataStore
import com.example.trueweather.persistence.WeatherResultDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LocationsBottomSheetViewModel @Inject constructor(
    private val weatherForecastRepository: WeatherForecastRepository,
    private val weatherForecastDataStore: WeatherResultDataStore,
    private val userPreferencesDataStore: UserPreferencesDataStore
) : ViewModel() {

    private val _locationsState = MutableStateFlow<LocationsState>(LocationsState.Loading())
    val locationsState: StateFlow<LocationsState> get() = _locationsState.asStateFlow()

    private var isFirstLoading = true

    private var hasChange = false

    private var firstResultHash = 0

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _locationsState.emit(LocationsState.Loading(isFirstLoading))
            isFirstLoading = false
            try {
                val weatherForecast = weatherForecastDataStore.getWeatherForecast()
                _locationsState.emit(LocationsState.UserLocationsSuccess(weatherForecast))
            } catch (e: Exception) {
                _locationsState.emit(LocationsState.Error)
            }

        }
    }

    fun searchLocations(searchQuery: String) {
        viewModelScope.launch {
            try {
                _locationsState.emit(LocationsState.Loading(isFirstLoading))
                val searchForecast = weatherForecastRepository.trySearchWeatherForecast(searchQuery)
                _locationsState.emit(LocationsState.SearchLocationsSuccess(searchForecast))
            } catch (e: Exception) {
                _locationsState.emit(LocationsState.Error)
            }
        }
    }

    fun addLocation(weatherResultWrapper: WeatherResultWrapper?) {
        viewModelScope.launch {
            try {
                hasChange = true
                _locationsState.emit(LocationsState.Loading(isFirstLoading))

                val userPrefsOpResult = saveUserPreferencesLocation(weatherResultWrapper)
                val weatherPrefsResult = saveWeatherForecast(weatherResultWrapper)

                if (userPrefsOpResult && weatherPrefsResult) {
                    _locationsState.emit(LocationsState.IOSuccess(isAdd = true, locationName = weatherResultWrapper?.address?.local))
                } else {
                    _locationsState.emit(LocationsState.IOError(isAdd = true, locationName = weatherResultWrapper?.address?.local))
                }
            } catch (e: Exception) {
                _locationsState.emit(LocationsState.IOError(isAdd = true, locationName = weatherResultWrapper?.address?.local))
            }
        }
    }

    fun hasChange(): Boolean = hasChange

    private suspend fun saveUserPreferencesLocation(weatherResultWrapper: WeatherResultWrapper?): Boolean {
        val userPreferencesLocations = userPreferencesDataStore.getUserPreferences().locationsList
        val locationId = weatherResultWrapper?.address?.globalIdLocal.toString()
        return userPreferencesDataStore.saveUserPreferences(userPreferencesLocations.toMutableList().apply { add(locationId) })
    }

    private suspend fun saveWeatherForecast(weatherResultWrapper: WeatherResultWrapper?): Boolean {
        var weatherForecastPreferences = weatherForecastDataStore.getWeatherForecast()
        weatherForecastPreferences = weatherForecastPreferences.copy(resultList =  weatherForecastPreferences.resultList.toMutableList().apply {
            if (weatherResultWrapper != null) {
                add(weatherResultWrapper)
            }
        })
        return weatherForecastDataStore.saveWeatherForecast(weatherForecastPreferences)
    }

    fun removeLocation(weatherResultWrapper: WeatherResultWrapper?) {
        viewModelScope.launch {
            try {
                hasChange = true
                _locationsState.emit(LocationsState.Loading(isFirstLoading))
                val operationResultUserPreferences = deleteUserLocationPersistence(weatherResultWrapper)
                val operationResultWeatherForecast = deleteWeatherForecastPersistence(weatherResultWrapper)

                if (operationResultUserPreferences && operationResultWeatherForecast) {
                    _locationsState.emit(LocationsState.IOSuccess(isAdd = false, locationName = weatherResultWrapper?.address?.local))
                    loadData()
                } else {
                    _locationsState.emit(LocationsState.IOError(isAdd = false, locationName = weatherResultWrapper?.address?.local))
                }
            } catch (e: Exception) {
                _locationsState.emit(LocationsState.IOError(isAdd = false, locationName = weatherResultWrapper?.address?.local))
            }
        }
    }

    private suspend fun deleteUserLocationPersistence(weatherResultWrapper: WeatherResultWrapper?): Boolean {
        val userPreferencesLocations = userPreferencesDataStore.getUserPreferences().locationsList
        val locationId = weatherResultWrapper?.address?.globalIdLocal.toString()
        return userPreferencesDataStore.saveUserPreferences(userPreferencesLocations.filter { it != locationId } )
    }

    private suspend fun deleteWeatherForecastPersistence(weatherResultWrapper: WeatherResultWrapper?): Boolean {
        val persistenceWeatherForecast = weatherForecastDataStore.getWeatherForecast()
        val locationId = weatherResultWrapper?.address?.globalIdLocal.toString()

        val filteredResultList = persistenceWeatherForecast.resultList.subList(1, persistenceWeatherForecast.resultList.size).filter {
            it.address?.globalIdLocal.toString() != locationId
        }.toMutableList()

        filteredResultList.add(0, persistenceWeatherForecast.resultList.first())

        return weatherForecastDataStore.saveWeatherForecast(
            persistenceWeatherForecast.copy(resultList = filteredResultList)
        )
    }

    sealed class LocationsState {
        data class Loading(val isFirstLoading: Boolean = false) : LocationsState()
        data class UserLocationsSuccess(
            val userLocationsResult: WeatherResult
        ) : LocationsState()

        data class SearchLocationsSuccess(
            val searchLocationsSuccess: WeatherResult
        ) : LocationsState()

        data object Error : LocationsState()
        data class IOSuccess(val isAdd: Boolean, val locationName: String?) : LocationsState()
        data class IOError(val isAdd: Boolean, val locationName: String?) : LocationsState()
    }
}