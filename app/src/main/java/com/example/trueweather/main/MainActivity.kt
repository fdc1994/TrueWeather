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
import com.example.domain.data.objects.WeatherResult
import com.example.domain.data.utils.ErrorType
import com.example.domain.data.utils.ResultWrapper
import com.example.domain.data.utils.collectWhenResumed
import com.example.domain.data.utils.collectWhenStarted
import com.example.network.utils.TimestampUtil
import com.example.trueweather.databinding.ActivityMainBinding
import com.example.trueweather.main.addlocation.LocationsBottomSheet
import com.example.trueweather.ui.WeatherViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var viewPagerAdapter: WeatherViewPagerAdapter = WeatherViewPagerAdapter(null)

    private val viewModel: MainActivityViewModel by viewModels()

    private val hasPermission = false

    private var isEvening = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            viewModel.updatePermissionState(isGranted)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
        } else {
            viewModel.updatePermissionState(true)
        }
        setTranslucentStatusBar()
        setAddLocationButton()

        collectWhenResumed(viewModel.weatherState) {
            when (it) {
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
        viewModel.loadData()
        if (isEvening == TimestampUtil.isEvening()) return
        isEvening = TimestampUtil.isEvening()
        if (isEvening) {
            binding.lottieAnimationView.setAnimation("animation_night.json")
        } else {
            binding.lottieAnimationView.setAnimation("animation_day.json")
        }
        viewPagerAdapter.notifyDataSetChanged()
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
        if (binding.viewPager.adapter == null) {
            binding.viewPager.adapter = viewPagerAdapter
        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tabLayout, position ->

        }.attach()
        viewPagerAdapter.updateWeatherResult(weatherResult)
    }

    private fun showError(errorType: ErrorType) {
        // Implement show error UI
    }

    private fun setAddLocationButton() {
        binding.addButton.setOnClickListener {
            // Show the custom bottom sheet dialog
            val locationsBottomSheet = LocationsBottomSheet()
            locationsBottomSheet.show(supportFragmentManager, locationsBottomSheet.tag)
        }
    }

    private fun setTranslucentStatusBar() {
        if (Build.VERSION.SDK_INT in 24..29) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= 30) {
            window.statusBarColor = Color.TRANSPARENT
            // Making status bar overlaps with the activity
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }
}
