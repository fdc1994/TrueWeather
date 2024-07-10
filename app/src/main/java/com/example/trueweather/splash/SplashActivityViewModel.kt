package com.example.trueweather.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.data.LocalizationManager
import com.example.domain.data.repositories.DistrictIdentifiersRepository
import com.example.trueweather.utils.NetworkConnectivityManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel @Inject constructor(
    private val districtIdentifiersRepository: DistrictIdentifiersRepository,
    private val localizationManager: LocalizationManager,
    private val networkConnectivityManager: NetworkConnectivityManager
) : ViewModel() {

    private val _navigationState = MutableStateFlow<NavigationState?>(null)
    val navigationState: StateFlow<NavigationState?> get() = _navigationState.asStateFlow()

    fun loadData() {
        if (!localizationManager.checkPermissions()) {
            _navigationState.value = NavigationState.AskForPermissions
            return
        }

        if (!networkConnectivityManager.hasInternetConnection()) {
            _navigationState.value = NavigationState.Error
            return
        }

        updateCriticalInformationAndStartApp()
    }

    fun resumeLoadAfterPermissions() {
        updateCriticalInformationAndStartApp()
    }

    private fun updateCriticalInformationAndStartApp() {
        viewModelScope.launch {
            try {
                val identifiers = districtIdentifiersRepository.getDistrictIdentifiersList()

                if (identifiers != null) {
                    navigateToMain()
                } else {
                    _navigationState.value = NavigationState.Error
                }
            } catch (e: Exception) {
                _navigationState.value = NavigationState.Error
            }
        }
    }

    private fun navigateToMain() {
        _navigationState.value = NavigationState.NavigateToMain
    }

    sealed class NavigationState {
        data object AskForPermissions : NavigationState()
        data object NavigateToMain : NavigationState()
        data object Error : NavigationState()
    }
}
