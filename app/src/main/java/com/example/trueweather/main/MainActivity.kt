package com.example.trueweather.main

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.domain.data.objects.WeatherResult
import com.example.domain.data.utils.ErrorType
import com.example.domain.data.utils.ResultWrapper
import com.example.domain.data.utils.collectWhenResumed
import com.example.network.utils.TimestampUtil
import com.example.trueweather.ThemeManager
import com.example.trueweather.databinding.ActivityMainBinding
import com.example.trueweather.main.addlocation.LocationsBottomSheet
import com.example.trueweather.platform.BaseTrueWeatherActivity
import com.example.trueweather.splash.SplashActivity
import com.example.trueweather.ui.WeatherViewPagerAdapter
import com.example.trueweather.utils.setGone
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseTrueWeatherActivity(), RetryListener {

    private lateinit var binding: ActivityMainBinding

    private var viewPagerAdapter: WeatherViewPagerAdapter = WeatherViewPagerAdapter(null, this)

    private val viewModel: MainActivityViewModel by viewModels()

    private var isEvening = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissions()
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

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PERMISSION_GRANTED
        ) {
            requestLocationPermission()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
        setTheme()
    }

    private fun setTheme() {
        if (isEvening == TimestampUtil.isEvening()) return
        else binding.viewPager.adapter?.notifyDataSetChanged()
        isEvening = TimestampUtil.isEvening()
        val color = ThemeManager.getCurrentTextColor()
        binding.progressText.setTextColor(color)
        if (isEvening) {
            binding.lottieAnimationView.setAnimation("animation_night.json")
        } else {
            binding.lottieAnimationView.setAnimation("animation_day.json")
        }
    }

    private fun showLoading() {
        binding.progressView.setGone(false)
        binding.addButton.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressView.setGone(true)
        binding.addButton.isEnabled = true
    }

    private fun showWeather(weatherResult: WeatherResult) {
        if (binding.viewPager.adapter == null) {
            binding.viewPager.adapter = viewPagerAdapter
            binding.viewPager.offscreenPageLimit = 1
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
            locationsBottomSheet.onDismissListener = {
                viewModel.loadData()
            }
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

    override fun onLocationsRetry() {
        viewModel.loadData()
    }

    override fun onPermissionsRetry() {
        requestLocationPermission()
    }

    // Request location permission
    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
                viewModel.loadData()
            } else {
                // Request permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), SplashActivity.PERMISSION_REQUEST_CODE)
            }
        } else {
            // Permission already granted for devices below Marshmallow
            viewModel.loadData()
        }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SplashActivity.PERMISSION_REQUEST_CODE) {
            if (grantResults.first() == PERMISSION_GRANTED) {
                viewModel.updatePermissionState(true)
            } else Toast.makeText(this, "Não foi possível obter a permissão. Por favor conceda através das definições", Toast.LENGTH_LONG).show()
        }
    }
}
