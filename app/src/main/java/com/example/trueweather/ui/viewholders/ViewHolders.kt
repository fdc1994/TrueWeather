package com.example.trueweather.ui.viewholders

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.TooltipCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.data.objects.WeatherFetchStatus
import com.example.domain.data.objects.WeatherResultWrapper
import com.example.trueweather.R
import com.example.trueweather.ThemeManager
import com.example.trueweather.main.RetryListener
import com.example.trueweather.main.addlocation.OnLocationClickListener
import com.example.trueweather.ui.FutureWeatherAdapter
import com.example.trueweather.ui.WeatherDrawableResolver
import com.example.trueweather.utils.setGone
import com.google.android.material.appbar.CollapsingToolbarLayout

class SuccessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val weatherImageView: ImageView = itemView.findViewById(R.id.weatherImage)
    private val maxTemperatureTv: TextView = itemView.findViewById(R.id.maxTemp)
    private val minTemperatureTv: TextView = itemView.findViewById(R.id.minTemp)
    private val currentWeatherDescription: TextView = itemView.findViewById(R.id.currentWeatherDescription)
    private val futureWeatherRecyclerView: RecyclerView = itemView.findViewById(R.id.futureWeatherRecyclerView)
    private val headerFutureDay: TextView = itemView.findViewById(R.id.header_future_day)
    private val precipitationPercentage: TextView = itemView.findViewById(R.id.precipitation_percentage)
    private val precipitationDescription: TextView = itemView.findViewById(R.id.precipitation_description)
    private val windDirection: TextView = itemView.findViewById(R.id.wind_direction)
    private val windIntensity: TextView = itemView.findViewById(R.id.wind_intensity)
    private val headerFutureTemperature: TextView = itemView.findViewById(R.id.header_future_temperature)
    private val appBarLayout: CollapsingToolbarLayout = itemView.findViewById(R.id.collapsingToolbarLayout)
    private val toolbarTag: TextView = itemView.findViewById(R.id.toolbar_tag)
    private val collapsingToolbarLayout: CollapsingToolbarLayout = itemView.findViewById(R.id.collapsingToolbarLayout)

    fun bind(locationWeather: WeatherResultWrapper?) {
        val locationTitle = locationWeather?.address?.local
        collapsingToolbarLayout.title = locationTitle

        with(locationWeather?.weatherForecast?.data?.first()) {
            minTemperatureTv.text = "${this?.tMin}ºC"
            maxTemperatureTv.text = "${this?.tMax}ºC"
            WeatherDrawableResolver.getWeatherDrawable(this?.weatherType?.id ?: -1)?.let { weatherImageView.setImageResource(it) }
            currentWeatherDescription.text = this?.weatherType?.descWeatherTypePT
            precipitationDescription.text = "${this?.classPrecInt?.descClassPrecIntPT} nas próximas 24H"
            precipitationPercentage.text = "${this?.precipitaProb}%"
            windDirection.text = this?.predWindDir
            windIntensity.text = "${this?.classWindSpeed?.descClassWindSpeedDailyPT} nas próximas 24H"
        }

        futureWeatherRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
        futureWeatherRecyclerView.adapter = locationWeather?.weatherForecast?.data?.let {
            FutureWeatherAdapter(it.subList(1, it.size))
        }
        setWarningTag(locationWeather?.status)
        setTheme()
    }

    private fun setTheme() {
        val themedColor = itemView.context.getColor(ThemeManager.getCurrentTextColor())
        currentWeatherDescription.setTextColor(themedColor)
        headerFutureDay.setTextColor(themedColor)
        headerFutureTemperature.setTextColor(themedColor)
        appBarLayout.setCollapsedTitleTextColor(themedColor)
        appBarLayout.setExpandedTitleColor(themedColor)
        precipitationDescription.setTextColor(themedColor)
        precipitationPercentage.setTextColor(themedColor)
        windDirection.setTextColor(themedColor)
        windIntensity.setTextColor(themedColor)
    }

    private fun setWarningTag(status: WeatherFetchStatus?) {
        when (status) {
            WeatherFetchStatus.SUCCESS_FROM_PERSISTENCE,
            WeatherFetchStatus.SUCCESS_CURRENT_LOCATION_FROM_PERSISTENCE -> {
                toolbarTag.text = "Dados Desatualizados"
                toolbarTag.setGone(false)
                toolbarTag.setOnClickListener {
                    TooltipCompat.setTooltipText(toolbarTag, toolbarTag.tooltipText)
                }
            }
            else -> Unit
        }
    }
}

class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val errorMessageTextView: TextView = itemView.findViewById(R.id.errorMessage)
    private val retryButton: AppCompatButton = itemView.findViewById(R.id.retryButton)
    private val errorImageView: ImageView = itemView.findViewById(R.id.error_logo)
    private val toolbar: Toolbar = itemView.findViewById(R.id.toolbar)

    fun bind(locationWeather: WeatherResultWrapper?, onRetryListener: RetryListener) {
        toolbar.title = locationWeather?.address?.local
        when (locationWeather?.status) {
            WeatherFetchStatus.PERMISSION_ERROR -> {
                errorMessageTextView.text = "Para ter acesso à sua localização deve permitir o Acesso no sistema"
                errorImageView.setImageResource(R.drawable.baseline_back_hand_24)
                retryButton.run {
                    text = "Conceder Permissões"
                    setOnClickListener { onRetryListener.onPermissionsRetry() }
                }
                toolbar.title = "Localização Atual"
            }

            WeatherFetchStatus.NETWORK_ERROR -> {
                errorMessageTextView.text = "Não foi possível obter informação para esta localização"
                errorImageView.setImageResource(R.drawable.baseline_error_outline_24)
                retryButton.run {
                    text = "Tentar de novo"
                    setOnClickListener { onRetryListener.onLocationsRetry() }
                }
            }

            WeatherFetchStatus.NO_INTERNET_ERROR -> {
                errorMessageTextView.text = "Não foi possível estabelecer conexão com a internet"
                errorImageView.setImageResource(R.drawable.wifi_off_icon)
                retryButton.run {
                    text = "Tentar de novo"
                    setOnClickListener { onRetryListener.onLocationsRetry() }
                }
            }

            WeatherFetchStatus.NOT_IN_COUNTRY_ERROR -> {
                errorMessageTextView.text = "Não foi possível determinar a sua localização em Portugal.\nA localização atual apenas funciona em Portugal."
                errorImageView.setImageResource(R.drawable.unavailable_location_icon)
                retryButton.run {
                    text = "Tentar de novo"
                    setOnClickListener { onRetryListener.onLocationsRetry() }
                }
            }

            else -> {
                errorMessageTextView.text = "Ocorreu um erro inesperado. Por favor tente novamente mais tarde."
                errorImageView.setImageResource(R.drawable.baseline_error_outline_24)
                retryButton.run {
                    text = "Tentar de novo"
                    setOnClickListener { onRetryListener.onLocationsRetry() }
                }
            }
        }
        setTheme()
    }

    private fun setTheme() {
        val themedColor = itemView.context.getColor(ThemeManager.getCurrentTextColor())
        errorMessageTextView.setTextColor(themedColor)
    }
}

class ManageLocationsSuccessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val currentLocationLabel: TextView = itemView.findViewById(R.id.current_location_label)
    private val locationTextView: TextView = itemView.findViewById(R.id.location)
    private val weatherImageView: ImageView = itemView.findViewById(R.id.weatherIcon)
    private val minTempTextView: TextView = itemView.findViewById(R.id.minTemp)
    private val maxTempTextView: TextView = itemView.findViewById(R.id.maxTemp)
    private val weatherDescriptionTextView: TextView = itemView.findViewById(R.id.weather_description)
    private val actionButton: Button = itemView.findViewById(R.id.action_button)

    fun bind(locationWeather: WeatherResultWrapper?, onLocationClickListener: OnLocationClickListener) {
        val isCurrentLocation = locationWeather?.status == WeatherFetchStatus.SUCCESS_CURRENT_LOCATION_FROM_PERSISTENCE
        val isCurrentUserLocation = locationWeather?.status == WeatherFetchStatus.SUCCESS && locationWeather.isUserSavedLocation

        setLocationInfo(locationWeather)

        setLocationLabel(isCurrentLocation, isCurrentUserLocation)

        if (!isCurrentUserLocation) {
            actionButton.isEnabled = true
        }
        actionButton.setOnClickListener {
            onLocationClickListener.onLocationClick(locationWeather)
            actionButton.isEnabled = false
        }

        when (locationWeather?.status) {
            WeatherFetchStatus.SUCCESS_CURRENT_LOCATION_FROM_PERSISTENCE -> actionButton.setGone(true)
            WeatherFetchStatus.SUCCESS_FROM_PERSISTENCE -> handleCurrentLocationButton(isCurrentUserLocation)
            else -> handleSearchedLocationButton(isCurrentUserLocation)
        }
    }

    private fun ManageLocationsSuccessViewHolder.setLocationInfo(locationWeather: WeatherResultWrapper?) {
        with(locationWeather?.weatherForecast?.data?.first()) {
            minTempTextView.text = "${this?.tMin}ºC"
            maxTempTextView.text = "${this?.tMax}ºC"
            WeatherDrawableResolver.getWeatherDrawable(this?.weatherType?.id ?: -1)?.let { weatherImageView.setImageResource(it) }
            weatherDescriptionTextView.text = this?.weatherType?.descWeatherTypePT
        }
        locationTextView.text = locationWeather?.address?.local
    }

    private fun setLocationLabel(isCurrentLocation: Boolean, isCurrentUserLocation: Boolean) {
        if (isCurrentLocation) {
            currentLocationLabel.setGone(false)
            currentLocationLabel.text = "Localização atual"
        } else if (isCurrentUserLocation) {
            currentLocationLabel.setGone(false)
            currentLocationLabel.text = "Localização já adicionada"
        } else {
            currentLocationLabel.setGone(true)
        }
    }

    private fun handleCurrentLocationButton(isCurrentUserLocation: Boolean) {
        with(actionButton) {
            if (isCurrentUserLocation) setGone(true) else setGone(false)
            text = "Remover"
        }
    }

    private fun handleSearchedLocationButton(isCurrentUserLocation: Boolean) {
        with(actionButton) {
            if (isCurrentUserLocation) setGone(true) else setGone(false)
            text = "Adicionar"
        }
    }
}

class ManageLocationsErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val errorLabel: TextView = itemView.findViewById(R.id.error_label)
    private val currentLocationLabel: TextView = itemView.findViewById(R.id.current_location_label)
    fun bind(isFirstLocation: Boolean, local: String?) {
        if (isFirstLocation) {
            currentLocationLabel.visibility = View.VISIBLE
        } else {
            currentLocationLabel.visibility = View.GONE
            if (!local.isNullOrEmpty()) {
                errorLabel.text = "Não foi possível obter a localização para $local"
            } else {
                errorLabel.text = " Não foi possível obter esta localização"
            }
        }
    }
}


