package com.example.trueweather.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.example.domain.data.objects.WeatherForecast
import com.example.domain.data.objects.WeatherResult
import com.example.domain.data.utils.ErrorType
import com.example.trueweather.databinding.ActivityMainBinding
import com.example.domain.data.utils.ResultWrapper
import com.example.domain.data.utils.collectWhenResumed
import com.example.domain.data.utils.collectWhenStarted
import com.example.network.utils.TimestampUtil
import com.example.trueweather.R
import com.example.trueweather.ui.WeatherViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var viewPagerAdapter : WeatherViewPagerAdapter = WeatherViewPagerAdapter(null)

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
        setTranslucentStatusBar()

        collectWhenStarted(viewModel.weatherState) {
            when(it) {
                is ResultWrapper.Loading -> showLoading()
                is ResultWrapper.Success -> {
                    hideLoading()
                    showWeather(it.data)
                }
                is ResultWrapper.Error -> {
                    hideLoading()
                    showError(it.errorType)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(TimestampUtil.isEvening()) {
            binding.lottieAnimationView.setAnimation("animation_night.json")
        } else {
            binding.lottieAnimationView.setAnimation("animation_day.json")
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

    private fun showWeather(weatherResult: WeatherResult) {
        binding.viewPager.adapter = viewPagerAdapter
        viewPagerAdapter.updateWeatherResult(weatherResult)
    }

    private fun showError(errorType: ErrorType) {
        // Implement show error UI
    }

    internal fun setTranslucentStatusBar() {
        if (Build.VERSION.SDK_INT in 24..29) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= 30) {
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
            // Making status bar overlaps with the activity
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }
}
