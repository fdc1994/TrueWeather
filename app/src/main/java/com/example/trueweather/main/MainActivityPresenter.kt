package com.example.trueweather.main

import com.example.domain.data.objects.WeatherForecast
import com.example.domain.data.repositories.WeatherForecastRepository
import com.example.domain.data.utils.ErrorType
import com.example.domain.data.utils.RxResult
import com.example.domain.data.LocalizationManager
import com.example.trueweather.platform.BaseTrueWeatherPresenter
import com.example.trueweather.utils.NetworkConnectivityManager
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivityPresenter @Inject constructor(
    private val weatherForecastRepository: WeatherForecastRepository,
    private val localizationManager: LocalizationManager,
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
                                view?.showWeather(RxResult.Success(weatherForecastList))
                            } else {
                                RxResult.Error(ErrorType.GENERIC_ERROR)
                            }
                        }, { throwable ->
                            view?.showError(RxResult.Error(ErrorType.NETWORK_ERROR))
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

