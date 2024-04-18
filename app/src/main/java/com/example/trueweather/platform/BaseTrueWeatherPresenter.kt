package com.example.trueweather.platform

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseTrueWeatherPresenter {
    private val compositeDisposable = CompositeDisposable()

    fun subscribeDisposable(subscription: () -> Disposable) {
        compositeDisposable.add(subscription.invoke())
    }
}