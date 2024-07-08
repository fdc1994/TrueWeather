package com.example.trueweather.platform

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity

abstract class BaseTrueWeatherActivity : AppCompatActivity() {
    open fun handlePermissionResult(isGranted: Boolean): Unit = Unit


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               handlePermissionResult(true)
            } else {
                handlePermissionResult(false)
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
