package com.example.trueweather.splash

import com.example.domain.data.utils.RxResult

interface SplashActivityMVP {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun showInitResult(result: RxResult<Boolean>)

    }

    interface Presenter {
        fun onAttachView(view: View)
    }
}