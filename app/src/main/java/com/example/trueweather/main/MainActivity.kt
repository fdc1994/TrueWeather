package com.example.trueweather.main

import android.os.Bundle
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.domain.data.WeatherForecast
import com.example.domain.data.managers.IpmaNetworkManager
import com.example.trueweather.utils.NetworkConnectivityManager
import com.example.trueweather.R
import com.example.trueweather.platform.BaseTrueWeatherActivity
import com.example.trueweather.utils.lazyFindViewById
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: BaseTrueWeatherActivity() {

    @Inject
    lateinit var ipmaNetworkManager: IpmaNetworkManager

    private var networkConnectivityManager: NetworkConnectivityManager = NetworkConnectivityManager()

    private val textView by lazyFindViewById<TextView>(R.id.main_view)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getBasicData()
    }

    private fun getBasicData() {
        subscribeDisposable {
            ipmaNetworkManager.getWeatherForecast("1131200")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext { throwable: Throwable ->
                    // Handle error and return a new Observable emitting a default value
                    Single.just(
                        WeatherForecast(
                            "",
                            "",
                            listOf(),
                            -1,
                            ""
                        )
                    )
                }
                .doOnSubscribe {
                    showProgress()
                }
                .subscribe(
                    { weatherForecast ->
                        // Handle the success case
                        textView.text = weatherForecast.data.toString()
                        hideProgress()
                        textView.text = networkConnectivityManager.hasInternetConnection().toString()
                    },
                    { throwable ->
                        // Handle the error case
                        textView.text = throwable.message
                        hideProgress()
                    }
                )
        }

    }
}