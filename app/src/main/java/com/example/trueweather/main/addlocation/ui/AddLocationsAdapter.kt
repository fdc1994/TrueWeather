package com.example.trueweather.main.addlocation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.data.objects.WeatherFetchStatus
import com.example.domain.data.objects.WeatherResult
import com.example.trueweather.R
import com.example.trueweather.main.addlocation.OnLocationClickListener
import com.example.trueweather.ui.viewholders.ManageLocationsErrorViewHolder
import com.example.trueweather.ui.viewholders.ManageLocationsSuccessViewHolder
import com.example.trueweather.utils.WeatherResultDiffCallback

class AddLocationsAdapter(
    private var weatherResult: WeatherResult?,
    private val onLocationClickListener: OnLocationClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.VIEW_TYPE_SUCCESS.ordinal,
            ViewType.VIEW_TYPE_SUCCESS_FROM_PERSISTENCE.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.current_weather_location_success_item_layout, parent, false)
                ManageLocationsSuccessViewHolder(view)
            }

            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.current_weather_location_error_item_layout, parent, false)
                ManageLocationsErrorViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val locationWeather = weatherResult?.resultList?.get(position)
        when (holder) {
            is ManageLocationsSuccessViewHolder -> holder.bind(locationWeather, onLocationClickListener)
            is ManageLocationsErrorViewHolder -> holder.bind(position == 0, locationWeather, onLocationClickListener)
        }
    }

    override fun getItemCount(): Int {
        return weatherResult?.resultList?.size ?: 0
    }

    fun updateWeatherResult(newWeatherResult: WeatherResult?) {
        val diffCallback = WeatherResultDiffCallback(
            weatherResult,
            newWeatherResult
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        weatherResult = newWeatherResult
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        val status = weatherResult?.resultList?.get(position)?.status ?: WeatherFetchStatus.OTHER_ERROR
        return when (status) {
            WeatherFetchStatus.SUCCESS -> ViewType.VIEW_TYPE_SUCCESS.ordinal
            WeatherFetchStatus.SUCCESS_CURRENT_LOCATION_FROM_PERSISTENCE,
            WeatherFetchStatus.SUCCESS_FROM_PERSISTENCE -> ViewType.VIEW_TYPE_SUCCESS_FROM_PERSISTENCE.ordinal
            WeatherFetchStatus.PERMISSION_ERROR,
            WeatherFetchStatus.NETWORK_ERROR,
            WeatherFetchStatus.NO_INTERNET_ERROR,
            WeatherFetchStatus.NOT_IN_COUNTRY_ERROR,
            WeatherFetchStatus.OTHER_ERROR,
            WeatherFetchStatus.OTHER_ERROR_SEARCH -> ViewType.VIEW_TYPE_ERROR.ordinal
        }
    }

    private enum class ViewType {
        VIEW_TYPE_SUCCESS,
        VIEW_TYPE_SUCCESS_FROM_PERSISTENCE,
        VIEW_TYPE_ERROR
    }
}