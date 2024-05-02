package com.example.trueweather.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import com.example.domain.data.LocalizationManager
import com.example.trueweather.R
import com.example.trueweather.main.MainActivity
import com.example.trueweather.platform.BaseTrueWeatherActivity
import com.example.domain.data.utils.RxResult
import com.example.trueweather.main.MainActivityMVP
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity @Inject constructor() : BaseTrueWeatherActivity(), SplashActivityMVP.View {

    @Inject
    lateinit var presenter: SplashActivityMVP.Presenter

    @Inject
    lateinit var localizationManager: LocalizationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        presenter.onAttachView(this)
        showProgress()
    }

    private fun startMainActivity(startIntent: Intent) {
        startActivity(startIntent).also {
            finish()
        }
    }

    override fun showLoading() {
        showProgress()
    }

    override fun hideLoading() {
        hideProgress()
    }

    override fun showInitResult(result: RxResult<Boolean>) {
        if(result.getValueOrNull() == true) {
            val startIntent = Intent(this, MainActivity::class.java)
            startMainActivity(startIntent)
        }
    }

    override fun askForLocalizationPermissions() {
        requestLocationPermission()
    }

    override fun handlePermissionResult(isGranted: Boolean) {
        if(isGranted) {
            presenter.resumeLoadAfterPermissions()
        } else {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show()
        }
    }
}