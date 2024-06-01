package com.example.trueweather.splash
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.trueweather.R
import com.example.trueweather.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Observe navigation state
        viewModel.navigationState.observe(this, Observer { state ->
            when (state) {
                is SplashActivityViewModel.NavigationState.AskForPermissions -> {
                    // Ask for permissions
                    requestLocationPermission()
                }
                is SplashActivityViewModel.NavigationState.NavigateToMain -> {
                    // Navigate to the main activity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        })

        // Load data
        viewModel.loadData()
    }

    // Request location permission
    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted
                viewModel.resumeLoadAfterPermissions()
            } else {
                // Request permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
            }
        } else {
            // Permission already granted for devices below Marshmallow
            viewModel.resumeLoadAfterPermissions()
        }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            viewModel.resumeLoadAfterPermissions()
        }
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 1001
    }
}