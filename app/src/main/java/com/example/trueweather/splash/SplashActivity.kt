package com.example.trueweather.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.trueweather.R
import com.example.trueweather.main.MainActivity
import com.example.trueweather.platform.BaseTrueWeatherActivity
import com.example.trueweather.utils.RxResult
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseTrueWeatherActivity(), SplashActivityMVP.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        showProgress()
        showInitResult(RxResult.Success(true))
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
}