package com.example.trueweather.main.addlocation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.data.objects.WeatherResult
import com.example.trueweather.R
import com.example.trueweather.ui.viewholders.AddLocationsViewHolder
import com.example.trueweather.utils.WeatherResultDiffCallback

class AddLocationsAdapter(private var weatherResult: WeatherResult?) :
    RecyclerView.Adapter<AddLocationsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddLocationsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_weather_item_layout, parent, false)
        return AddLocationsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddLocationsViewHolder, position: Int) {
        val locationWeather = weatherResult?.resultList?.get(position)
        holder.bind(locationWeather, position == 0)
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
}