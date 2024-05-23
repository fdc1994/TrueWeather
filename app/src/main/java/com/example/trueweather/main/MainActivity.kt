package com.example.trueweather.main

import android.os.Bundle
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.domain.data.objects.WeatherForecast
import com.example.domain.data.repositories.WeatherForecastRepository
import com.example.domain.data.utils.RxResult
import com.example.trueweather.R
import com.example.trueweather.databinding.ActivityMainBinding
import com.example.trueweather.platform.BaseTrueWeatherActivity
import com.example.trueweather.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: BaseTrueWeatherActivity(), MainActivityMVP.View {

    @Inject
    lateinit var weatherForecastRepository: WeatherForecastRepository

    @Inject
    lateinit var presenter: MainActivityMVP.Presenter

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        presenter.onAttachView(this)
    }

    override fun showLoading() {
        showProgress()
    }

    override fun hideLoading() {
        hideProgress()
    }

    override fun showWeather(weatherInfo: RxResult.Success<List<WeatherForecast>>) {
        if(weatherInfo.isSuccess()) {
            val data = weatherInfo.getValueOrNull()
            binding.mainView.text = data.toString()
        }

    }

    override fun showError(error: RxResult.Error) {
        binding.mainView.text = "Error: ${error.errorType}"
    }
}