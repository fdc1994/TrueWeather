package com.example.trueweather.main

import com.example.domain.data.WeatherForecast
import com.example.domain.data.managers.WeatherForecastRepository
import com.example.trueweather.platform.BaseTrueWeatherPresenter
import com.example.trueweather.utils.NetworkConnectivityManager
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivityPresenter @Inject constructor(
    private val weatherForecastRepository: WeatherForecastRepository,
    private val networkConnectivityManager: NetworkConnectivityManager
) : MainActivityMVP.Presenter, BaseTrueWeatherPresenter() {
    private val hasValidConnection: Boolean
        get() {
            return networkConnectivityManager.hasInternetConnection()
        }

    private var view: MainActivityMVP.View? = null
    override fun onAttachView(view: MainActivityMVP.View) {
        this.view = view
        loadData()
    }

    private fun loadData() {
        view?.showLoading()
        subscribeDisposable {
            setupData().doOnSubscribe {
                view?.showLoading()
            }.subscribe({ weatherForecastList ->
                            view?.hideLoading()
                            if (weatherForecastList.isNotEmpty()) {
                                view?.showWeather(weatherForecastList)
                            }
                        }, { throwable ->
                            view?.hideLoading()
                        })
        }
    }

    private fun setupData(): Single<List<WeatherForecast>> {
        return weatherForecastRepository.getWeatherForecast(hasValidInternetConnection = hasValidConnection).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext {
                Single.just(
                    listOf()
                )
            }
    }
}

