package com.example.trueweather.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.domain.data.utils.collectWhenResumed
import com.example.trueweather.R
import com.example.trueweather.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        collectWhenResumed(viewModel.navigationState) {
            when (it) {
                is SplashActivityViewModel.NavigationState.AskForPermissions -> {
                    // Ask for permissions
                    requestLocationPermission()
                }

                is SplashActivityViewModel.NavigationState.NavigateToMain -> {
                    // Navigate to the main activity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is SplashActivityViewModel.NavigationState.Error -> Toast.makeText(
                    this, "Ocorreu um erro. Por favor reinicie a aplicação", Toast
                        .LENGTH_LONG
                ).show()

                null -> Unit
            }
        }

        // Load data
        viewModel.loadData()
    }

    // Request location permission
    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted
            viewModel.resumeLoadAfterPermissions()
        } else {
            // Request permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
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