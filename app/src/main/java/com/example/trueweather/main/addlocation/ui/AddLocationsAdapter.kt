package com.example.trueweather.main.addlocation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.data.objects.WeatherFetchStatus
import com.example.domain.data.objects.WeatherResult
import com.example.trueweather.R
import com.example.trueweather.ui.viewholders.AddLocationsErrorViewHolder
import com.example.trueweather.ui.viewholders.AddLocationsSuccessViewHolder
import com.example.trueweather.utils.WeatherResultDiffCallback

class AddLocationsAdapter(private var weatherResult: WeatherResult?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ViewType.VIEW_TYPE_SUCCESS.ordinal,
            ViewType.VIEW_TYPE_SUCCESS_FROM_PERSISTENCE.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.add_weather_success_item_layout, parent, false)
                AddLocationsSuccessViewHolder(view)
            }

            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.add_weather_error_item_layout, parent, false)
                AddLocationsErrorViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val locationWeather = weatherResult?.resultList?.get(position)
        when(holder){
            is AddLocationsSuccessViewHolder -> holder.bind(locationWeather, position == 0)
            is AddLocationsErrorViewHolder -> holder.bind(position == 0)
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
            WeatherFetchStatus.SUCCESS_FROM_PERSISTENCE -> ViewType.VIEW_TYPE_SUCCESS_FROM_PERSISTENCE.ordinal
            WeatherFetchStatus.PERMISSION_ERROR,
            WeatherFetchStatus.NETWORK_ERROR,
            WeatherFetchStatus.NO_INTERNET_ERROR,
            WeatherFetchStatus.NOT_IN_COUNTRY_ERROR,
            WeatherFetchStatus.OTHER_ERROR -> ViewType.VIEW_TYPE_ERROR.ordinal
        }
    }


    private enum class ViewType {
        VIEW_TYPE_SUCCESS,
        VIEW_TYPE_SUCCESS_FROM_PERSISTENCE,
        VIEW_TYPE_ERROR
    }
}