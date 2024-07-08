package com.example.trueweather.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.data.LocalizationManager
import com.example.domain.data.repositories.DistrictIdentifiersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel @Inject constructor(
    private val districtIdentifiersRepository: DistrictIdentifiersRepository,
    private val localizationManager: LocalizationManager
) : ViewModel() {

    private val _navigationState = MutableStateFlow<NavigationState?>(null)
    val navigationState: StateFlow<NavigationState?> get() = _navigationState.asStateFlow()

    fun loadData() {
        if (!localizationManager.checkPermissions()) {
            _navigationState.value = NavigationState.AskForPermissions
            return
        }

        // Fetch district identifiers
        updateCriticalInformationAndStartApp()
    }

    fun resumeLoadAfterPermissions() {
        // Resume loading after permissions are granted
        updateCriticalInformationAndStartApp()
    }

    private fun updateCriticalInformationAndStartApp() {
        viewModelScope.launch {
            try {
                // Fetch district identifiers
                val identifiers = districtIdentifiersRepository.getDistrictIdentifiersList()

                // Handle result
                if (identifiers != null) {
                    navigateToMain()
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun navigateToMain() {
        _navigationState.value = NavigationState.NavigateToMain
    }

    sealed class NavigationState {
        data object AskForPermissions : NavigationState()
        data object NavigateToMain : NavigationState()
    }
}
