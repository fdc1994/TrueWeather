package com.example.trueweather.main.addlocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.data.objects.WeatherResult
import com.example.domain.data.repositories.WeatherForecastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationsBottomSheetViewModel @Inject constructor(
    private val weatherForecastRepository: WeatherForecastRepository
): ViewModel() {

    private val _locationsState = MutableStateFlow<LocationsState>(LocationsState.Loading)
    val locationsState: StateFlow<LocationsState> get() = _locationsState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                val weatherForecast = weatherForecastRepository.getWeatherForecast()
                _locationsState.emit(LocationsState.UserLocationsSuccess(weatherForecast))
            } catch (e: Exception) {
                _locationsState.emit(LocationsState.Error)
            }
        }
    }


    sealed class LocationsState {
        data object Loading : LocationsState()
        data class UserLocationsSuccess(
            val userLocationsResult: WeatherResult
        ) : LocationsState()

        data class SearchLocationsSuccess(
            val searchLocationsSuccess: WeatherResult
        ) : LocationsState()

        data object Error : LocationsState()
    }
}