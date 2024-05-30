package com.example.trueweather.ui.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.data.objects.WeatherFetchStatus
import com.example.domain.data.objects.WeatherResultList
import com.example.trueweather.R
import com.example.trueweather.ThemeManager
import com.example.trueweather.ui.FutureWeatherAdapter
import com.example.trueweather.ui.WeatherDrawableResolver
import com.google.android.material.appbar.AppBarLayout
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
    private val toolbar: Toolbar = itemView.findViewById(R.id.toolbar)

    fun bind(locationWeather: WeatherResultList?) {
        toolbar.title = locationWeather?.address?.local

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
        toolbar.title = locationWeather?.address?.local

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
}

class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val errorMessageTextView: TextView = itemView.findViewById(R.id.errorMessage)
    private val retryButton: AppCompatButton = itemView.findViewById(R.id.retryButton)
    private val errorImageView: ImageView = itemView.findViewById(R.id.error_logo)
    private val toolbar: Toolbar = itemView.findViewById(R.id.toolbar)


    fun bind(locationWeather: WeatherResultList?) {
        toolbar.title = locationWeather?.address?.local
        when(locationWeather?.status) {
            WeatherFetchStatus.PERMISSION_ERROR -> {
                errorMessageTextView.text = "Para ter acesso à sua localização deve permitir o Acesso no sistema"
                errorImageView.setImageResource(R.drawable.baseline_back_hand_24)
                retryButton.run {
                    text = "Conceder Permissões"
                }
                toolbar.title = "Localização Atual"
            }
            WeatherFetchStatus.NETWORK_ERROR -> {
                errorMessageTextView.text = "Não foi possível obter informação para esta localização"
                errorImageView.setImageResource(R.drawable.baseline_error_outline_24)
                retryButton.run {
                    text = "Tentar de novo"
                }

            }
            WeatherFetchStatus.NO_INTERNET_ERROR -> {
                errorMessageTextView.text = "Não foi possível estabelecer conexão com a internet"
                errorImageView.setImageResource(R.drawable.wifi_off_icon)
                retryButton.run {
                    text = "Tentar de novo"
                }
            }
            WeatherFetchStatus.NOT_IN_COUNTRY_ERROR -> {
                errorMessageTextView.text = "Não foi possível determinar a sua localização em Portugal.\nA localização atual apenas funciona em Portugal."
                errorImageView.setImageResource(R.drawable.unavailable_location_icon)
                retryButton.run {
                    text = "Tentar de novo"
                }
            }
            else -> {
                errorMessageTextView.text = "Ocorreu um erro inesperado. Por favor tente novamente mais tarde."
                errorImageView.setImageResource(R.drawable.baseline_error_outline_24)
                retryButton.run {
                    text = "Tentar de novo"
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

