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
import com.example.trueweather.ui.FutureWeatherAdapter
import com.example.trueweather.ui.WeatherDrawableResolver
import com.google.android.material.appbar.AppBarLayout

class SuccessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val weatherImageView: ImageView = itemView.findViewById(R.id.weatherImage)
    private val locationInput: TextView = itemView.findViewById(R.id.location)
    private val currentTemperature: TextView = itemView.findViewById(R.id.currentTemperature)
    private val currentWeatherDescription: TextView = itemView.findViewById(R.id.currentWeatherDescription)
    private val futureWeatherRecyclerView: RecyclerView = itemView.findViewById(R.id.futureWeatherRecyclerView)
    private val appBarLayout: AppBarLayout = itemView.findViewById(R.id.appBarLayout)
    private val toolbar: Toolbar = itemView.findViewById(R.id.toolbar)

    fun bind(locationWeather: WeatherResultList?) {
        locationInput.setText(locationWeather?.address?.local)
        currentTemperature.text = locationWeather?.weatherForecast?.data?.first()?.tMax
        WeatherDrawableResolver.getWeatherDrawable(locationWeather?.weatherForecast?.data?.first()?.idWeatherType ?: -1)
            ?.let { weatherImageView.setImageResource(it) }

        futureWeatherRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
        futureWeatherRecyclerView.adapter = locationWeather?.weatherForecast?.data?.let {
            FutureWeatherAdapter(it.subList(1, it.size))
        }
        toolbar.title = locationWeather?.address?.local
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
                errorImageView.setImageResource(R.drawable.baseline_cell_wifi_24)
                retryButton.run {
                    text = "Tentar de novo"
                }
            }
            WeatherFetchStatus.NOT_IN_COUNTRY_ERROR -> {
                errorMessageTextView.text = "Não foi possível determinar a sua localização em Portugal.\nA localização atual apenas funciona em Portugal."
                errorImageView.setImageResource(R.drawable.baseline_airplanemode_active_24)
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
    }
}