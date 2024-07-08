package com.example.trueweather.ui.viewholders

import android.graphics.Color
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.TooltipCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.data.objects.WeatherFetchStatus
import com.example.domain.data.objects.WeatherResultWrapper
import com.example.network.data.WeatherType
import com.example.trueweather.R
import com.example.trueweather.ThemeManager
import com.example.trueweather.main.RetryListener
import com.example.trueweather.main.addlocation.OnLocationClickListener
import com.example.trueweather.ui.FutureWeatherAdapter
import com.example.trueweather.ui.WeatherDrawableResolver
import com.example.trueweather.utils.setGone
import com.google.android.material.appbar.CollapsingToolbarLayout

class SuccessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val weatherImageView: WebView = itemView.findViewById(R.id.weatherImage)
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
            minTemperatureTv.text = itemView.context.getString(R.string.temp_placeholder, this?.tMin)
            maxTemperatureTv.text = itemView.context.getString(R.string.temp_placeholder, this?.tMax)
            this?.weatherType?.let { setupSvgAnimation(it) }
            weatherImageView.setBackgroundColor(Color.TRANSPARENT)


            currentWeatherDescription.text = this?.weatherType?.descWeatherTypePT
            precipitationDescription.text = itemView.context.getString(R.string.day_hour_weather_placeholder, this?.classPrecInt?.descClassPrecIntPT)
            precipitationPercentage.text = itemView.context.getString(R.string.precipitation_probability, this?.precipitaProb)
            windDirection.text = this?.predWindDir
            windIntensity.text = itemView.context.getString(R.string.wind_intensity_probability_placeholder, this?.classWindSpeed?.descClassWindSpeedDailyPT)
        }

        futureWeatherRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
        futureWeatherRecyclerView.adapter = locationWeather?.weatherForecast?.data?.let {
            FutureWeatherAdapter(it.subList(1, it.size))
        }
        setWarningTag(locationWeather?.status)
        setTheme()
    }

    private fun setupSvgAnimation(weatherType: WeatherType) {
        val svgFilename = WeatherDrawableResolver.getDrawableAnimationFilename(weatherType.id)
        val html = """
        <html>
        <head>
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    background-color: transparent;
                }
                img {
                    width: 100%;
                    height: 100%;
                    object-fit: contain; /* or object-fit: cover; */
                }
            </style>
        </head>
        <body>
            <img src="$svgFilename">
        </body>
        </html>
         """
        weatherImageView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null)
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
                toolbarTag.text = itemView.context.getString(R.string.outdated)
                toolbarTag.setGone(false)
                toolbarTag.setOnClickListener {
                    TooltipCompat.setTooltipText(toolbarTag, toolbarTag.tooltipText)
                }
            }

            else -> toolbarTag.setGone(true)
        }
    }
}

class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val errorMessageTextView: TextView = itemView.findViewById(R.id.errorMessage)
    private val retryButton: AppCompatButton = itemView.findViewById(R.id.retryButton)
    private val errorImageView: ImageView = itemView.findViewById(R.id.error_logo)

    fun bind(locationWeather: WeatherResultWrapper?, onRetryListener: RetryListener) {
        when (locationWeather?.status) {
            WeatherFetchStatus.PERMISSION_ERROR -> {
                errorMessageTextView.text = itemView.context.getString(R.string.no_internet_permission_warning_title)
                errorImageView.setImageResource(R.drawable.baseline_back_hand_24)
                retryButton.run {
                    text = context.getString(R.string.no_internet_permission_warning_cta)
                    setOnClickListener { onRetryListener.onPermissionsRetry() }
                }
            }

            WeatherFetchStatus.NETWORK_ERROR -> {
                errorMessageTextView.text = itemView.context.getString(R.string.location_error)
                errorImageView.setImageResource(R.drawable.baseline_error_outline_24)
                retryButton.run {
                    text = context.getString(R.string.try_again)
                    setOnClickListener { onRetryListener.onLocationsRetry() }
                }
            }

            WeatherFetchStatus.NO_INTERNET_ERROR -> {
                errorMessageTextView.text = itemView.context.getString(R.string.no_internet_error_warning_title)
                errorImageView.setImageResource(R.drawable.wifi_off_icon)
                retryButton.run {
                    text = context.getString(R.string.try_again)
                    setOnClickListener { onRetryListener.onLocationsRetry() }
                }
            }

            WeatherFetchStatus.NOT_IN_COUNTRY_ERROR -> {
                errorMessageTextView.text = itemView.context.getString(R.string.not_in_country_warning_title)
                errorImageView.setImageResource(R.drawable.unavailable_location_icon)
                retryButton.run {
                    text = context.getString(R.string.try_again)
                    setOnClickListener { onRetryListener.onLocationsRetry() }
                }
            }

            else -> {
                errorMessageTextView.text = itemView.context.getString(R.string.generic_error_warning_title)
                errorImageView.setImageResource(R.drawable.baseline_error_outline_24)
                retryButton.run {
                    text = context.getString(R.string.try_again)
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
            minTempTextView.text = itemView.context.getString(R.string.temp_placeholder, this?.tMin)
            maxTempTextView.text = itemView.context.getString(R.string.temp_placeholder, this?.tMax)
            WeatherDrawableResolver.getWeatherDrawable(this?.weatherType?.id ?: -1)?.let { weatherImageView.setImageResource(it) }
            weatherDescriptionTextView.text = this?.weatherType?.descWeatherTypePT
        }
        locationTextView.text = locationWeather?.address?.local
    }

    private fun setLocationLabel(isCurrentLocation: Boolean, isCurrentUserLocation: Boolean) {
        if (isCurrentLocation) {
            currentLocationLabel.setGone(false)
            currentLocationLabel.text = itemView.context.getString(R.string.current_location)
        } else if (isCurrentUserLocation) {
            currentLocationLabel.setGone(false)
            currentLocationLabel.text = itemView.context.getString(R.string.location_already_added)
        } else {
            currentLocationLabel.setGone(true)
        }
    }

    private fun handleCurrentLocationButton(isCurrentUserLocation: Boolean) {
        with(actionButton) {
            if (isCurrentUserLocation) setGone(true) else setGone(false)
            text = itemView.context.getString(R.string.remove)
        }
    }

    private fun handleSearchedLocationButton(isCurrentUserLocation: Boolean) {
        with(actionButton) {
            if (isCurrentUserLocation) setGone(true) else setGone(false)
            text = context.getString(R.string.add)
        }
    }
}

class ManageLocationsErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val errorLabel: TextView = itemView.findViewById(R.id.error_label)
    private val currentLocationLabel: TextView = itemView.findViewById(R.id.current_location_label)
    private val actionButton: TextView = itemView.findViewById(R.id.action_button)
    fun bind(isFirstLocation: Boolean, weatherResultWrapper: WeatherResultWrapper?, onLocationClickListener: OnLocationClickListener) {
        val local = weatherResultWrapper?.address?.local
        val isSearch = weatherResultWrapper?.status == WeatherFetchStatus.OTHER_ERROR_SEARCH
        if (isFirstLocation && !isSearch) {
            currentLocationLabel.visibility = View.VISIBLE
            actionButton.setGone(true)
            currentLocationLabel.text = itemView.context.getString(R.string.current_location_fetch_error)
        } else {
            currentLocationLabel.visibility = View.GONE
            if (!local.isNullOrEmpty()) {
                errorLabel.text = itemView.context.getString(R.string.specific_location_error_placeholder, local)
            } else {
                errorLabel.text = itemView.context.getString(R.string.location_error)
            }
            actionButton.run {
                if (!isSearch) {
                    setGone(false)
                } else {
                    setGone(true)
                }
                setOnClickListener {
                    onLocationClickListener.onLocationClick(weatherResultWrapper)
                }
            }
        }
    }
}


