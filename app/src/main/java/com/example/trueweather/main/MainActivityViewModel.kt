package com.example.trueweather.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.data.objects.WeatherResult
import com.example.domain.data.repositories.WeatherForecastRepository
import com.example.domain.data.utils.ErrorType
import com.example.domain.data.utils.ResultWrapper
import com.example.trueweather.persistence.WeatherResultDataStore
import com.example.trueweather.utils.NetworkConnectivityManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val weatherForecastRepository: WeatherForecastRepository,
    private val weatherResultDataStore: WeatherResultDataStore,
    private val networkConnectivityManager: NetworkConnectivityManager
) : ViewModel() {

    private val _weatherState = MutableStateFlow<ResultWrapper<WeatherResult>>(ResultWrapper.Loading)
    val weatherState: StateFlow<ResultWrapper<WeatherResult>> get() = _weatherState.asStateFlow()

    private val _permissionState = MutableStateFlow(false)
    val permissionState: StateFlow<Boolean> get() = _permissionState.asStateFlow()

    private val hasValidConnection: Boolean
        get() = networkConnectivityManager.hasInternetConnection()

    fun updatePermissionState(isGranted: Boolean) {
        _permissionState.value = isGranted
        if (isGranted) {
            loadData()
        }
    }

    fun loadData() {
        viewModelScope.launch {
            _weatherState.value = ResultWrapper.Loading
            try {
                val weatherForecast = setupData()
                if (weatherForecast.resultList.isNotEmpty()) {
                    _weatherState.value = ResultWrapper.Success(weatherForecast)
                } else {
                    _weatherState.value = ResultWrapper.Error(ErrorType.GENERIC_ERROR)
                }
            } catch (e: Exception) {
                _weatherState.value = ResultWrapper.Error(ErrorType.NETWORK_ERROR)
            }
        }
    }

    private suspend fun setupData(): WeatherResult {
        val weatherResult = if(hasValidConnection) {
            weatherForecastRepository.getWeatherForecast().also {
                weatherResultDataStore.saveWeatherForecast(it)
            }
        } else {
            weatherResultDataStore.getWeatherForecast()
        }
        return weatherResult
    }
}