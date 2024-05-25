package com.example.trueweather.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.domain.data.objects.WeatherForecast
import com.example.domain.data.utils.ErrorType
import com.example.trueweather.databinding.ActivityMainBinding
import com.example.domain.data.utils.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private val hasPermission = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            viewModel.updatePermissionState(isGranted)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission()
        } else {
            viewModel.updatePermissionState(true)
        }

        lifecycleScope.launch {
            viewModel.weatherState.collect { state ->
                when (state) {
                    is ResultWrapper.Loading -> showLoading()
                    is ResultWrapper.Success -> {
                        hideLoading()
                        showWeather(state.data)
                    }
                    is ResultWrapper.Error -> {
                        hideLoading()
                        showError(state.errorType)
                    }
                }
            }
        }
    }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun showLoading() {
        // Implement loading UI
    }

    private fun hideLoading() {
        // Implement hide loading UI
    }

    private fun showWeather(weatherForecastList: List<WeatherForecast>) {
        // Implement show weather UI
    }

    private fun showError(errorType: ErrorType) {
        // Implement show error UI
    }
}
