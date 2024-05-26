package com.example.trueweather.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.data.objects.WeatherResult
import com.example.trueweather.R
import com.example.trueweather.utils.WeatherResultDiffCallback
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class WeatherViewPagerAdapter(private var weatherResult: WeatherResult?) :
    RecyclerView.Adapter<WeatherViewPagerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weatherImageView: ImageView = itemView.findViewById(R.id.weatherImage)
        val locationInput: TextView = itemView.findViewById(R.id.location)
        val currentTemperature: TextView = itemView.findViewById(R.id.currentTemperature)
        val currentWeatherDescription: TextView = itemView.findViewById(R.id.currentWeatherDescription)
        val futureWeatherRecyclerView: RecyclerView = itemView.findViewById(R.id.futureWeatherRecyclerView)
        val appBarLayout: AppBarLayout = itemView.findViewById(R.id.appBarLayout)
        val toolbar: Toolbar = itemView.findViewById(R.id.toolbar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_page_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val locationWeather = weatherResult?.resultList?.get(position)
        holder.locationInput.setText(locationWeather?.address?.local)
        holder.currentTemperature.text = locationWeather?.weatherForecast?.data?.first()?.tMax
        WeatherDrawableResolver.getWeatherDrawable(locationWeather?.weatherForecast?.data?.first()?.idWeatherType ?: -1)
        ?.let { holder.weatherImageView.setImageResource(it) }
        // holder.currentWeatherDescription.text = locationWeather?.weatherForecast?.data?.first()?.description

        holder.futureWeatherRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.futureWeatherRecyclerView.adapter = locationWeather?.weatherForecast?.data?.let {
            FutureWeatherAdapter(it.subList(1, it.size))
        }
        holder.toolbar.title = locationWeather?.address?.local
    }

    override fun getItemCount(): Int {
        return weatherResult?.resultList?.size ?: 0
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
}

