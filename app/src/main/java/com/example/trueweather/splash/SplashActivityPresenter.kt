package com.example.trueweather.splash

import com.example.domain.data.LocalizationManager
import com.example.domain.data.repositories.DistrictIdentifiersRepository
import com.example.domain.data.utils.ErrorType
import com.example.domain.data.utils.RxResult
import com.example.trueweather.platform.BaseTrueWeatherPresenter
import javax.inject.Inject

class SplashActivityPresenter @Inject constructor(
    private val districtIdentifiersRepository: DistrictIdentifiersRepository,
    private val localizationManager: LocalizationManager
) : SplashActivityMVP.Presenter, BaseTrueWeatherPresenter() {

    private var view: SplashActivityMVP.View? = null

    override fun onAttachView(view: SplashActivityMVP.View) {
        this.view = view
        loadData()
    }

    override fun resumeLoadAfterPermissions() {
        updateCriticalInformationAndStartApp()
    }

    private fun loadData() {
        if(!localizationManager.checkPermissions()){
            view?.askForLocalizationPermissions()
        } else {
            updateCriticalInformationAndStartApp()
        }
    }

    private fun updateCriticalInformationAndStartApp() {
        districtIdentifiersRepository.getDistrictIdentifiersList()
            .doOnSubscribe {
                view?.showLoading()
            }
            .doOnSuccess {
                if (it.getValueOrNull() != null) {
                    view?.showInitResult(RxResult.Success(it.getValueOrNull() != null))
                }
            }.onErrorReturnItem(RxResult.Error(ErrorType.GENERIC_ERROR)).subscribe()
    }
}