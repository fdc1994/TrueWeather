package com.example.trueweather.main.addlocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.data.objects.WeatherResult
import com.example.trueweather.persistence.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationsBottomSheetViewModel @Inject constructor(
    private val userPreferences: UserPreferencesDataStore
): ViewModel() {

    private val _locationsState = MutableStateFlow<LocationsState>(LocationsState.Loading)
    val locationsState: StateFlow<LocationsState> get() = _locationsState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            delay(5000L)
            _locationsState.emit(LocationsState.Error)
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