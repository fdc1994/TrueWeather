package com.example.trueweather.main

import android.os.Bundle
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.domain.data.objects.WeatherForecast
import com.example.domain.data.repositories.WeatherForecastRepository
import com.example.trueweather.R
import com.example.trueweather.platform.BaseTrueWeatherActivity
import com.example.trueweather.utils.lazyFindViewById
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: BaseTrueWeatherActivity(), MainActivityMVP.View {

    @Inject
    lateinit var weatherForecastRepository: WeatherForecastRepository


    @Inject
    lateinit var presenter: MainActivityMVP.Presenter

    private val textView by lazyFindViewById<TextView>(R.id.main_view)
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

    override fun showWeather(weatherInfo: List<WeatherForecast>) {
       textView.text = weatherInfo.toString()
    }
}