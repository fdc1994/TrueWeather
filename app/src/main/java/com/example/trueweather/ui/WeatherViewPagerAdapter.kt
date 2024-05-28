package com.example.trueweather.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.data.objects.WeatherFetchStatus
import com.example.domain.data.objects.WeatherResult
import com.example.trueweather.R
import com.example.trueweather.ui.viewholders.ErrorViewHolder
import com.example.trueweather.ui.viewholders.SuccessViewHolder
import com.example.trueweather.utils.WeatherResultDiffCallback

class WeatherViewPagerAdapter(private var weatherResult: WeatherResult?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.VIEW_TYPE_SUCCESS.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_page_layout, parent, false)
                SuccessViewHolder(view)
            } else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_error_page_layout, parent, false)
                ErrorViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val locationWeather = weatherResult?.resultList?.get(position)
        when (holder) {
            is SuccessViewHolder -> {
                holder.bind(locationWeather)
            }
            is ErrorViewHolder -> {
                holder.bind(locationWeather)
            }
        }
    }

    override fun getItemCount(): Int {
        return weatherResult?.resultList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        val status = weatherResult?.resultList?.get(position)?.status ?: WeatherFetchStatus.OTHER_ERROR
        return when (status) {
            WeatherFetchStatus.SUCCESS,
            WeatherFetchStatus.SUCCESS_FROM_PERSISTENCE -> ViewType.VIEW_TYPE_SUCCESS.ordinal
            WeatherFetchStatus.PERMISSION_ERROR -> ViewType.VIEW_TYPE_PERMISSION_ERROR.ordinal
            WeatherFetchStatus.NETWORK_ERROR -> ViewType.VIEW_TYPE_NETWORK_ERROR.ordinal
            WeatherFetchStatus.NO_INTERNET_ERROR -> ViewType.VIEW_TYPE_NO_INTERNET_ERROR.ordinal
            WeatherFetchStatus.NOT_IN_COUNTRY_ERROR -> ViewType.VIEW_TYPE_NOT_IN_COUNTRY_ERROR.ordinal
            WeatherFetchStatus.OTHER_ERROR -> ViewType.VIEW_TYPE_GENERIC_ERROR.ordinal
        }
    }

    // Method to update the data using DiffUtil
    fun updateWeatherResult(newWeatherResult: WeatherResult?) {
        val diffCallback = WeatherResultDiffCallback(
            weatherResult,
            newWeatherResult
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        weatherResult = newWeatherResult
        diffResult.dispatchUpdatesTo(this)
    }

    private enum class ViewType {
        VIEW_TYPE_SUCCESS,
        VIEW_TYPE_PERMISSION_ERROR,
        VIEW_TYPE_NOT_IN_COUNTRY_ERROR,
        VIEW_TYPE_NETWORK_ERROR,
        VIEW_TYPE_NO_INTERNET_ERROR,
        VIEW_TYPE_GENERIC_ERROR
    }
}

