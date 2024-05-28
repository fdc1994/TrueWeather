package com.example.trueweather.ui
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.data.objects.WeatherData
import com.example.trueweather.R

class FutureWeatherAdapter(private val forecastList: List<WeatherData>) :
    RecyclerView.Adapter<FutureWeatherAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val day: TextView = itemView.findViewById(R.id.day)
        val minMaxTemp: TextView = itemView.findViewById(R.id.minMaxTemp)
        val weatherDescription: TextView = itemView.findViewById(R.id.weatherDescription)
        val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.future_weather_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.day.text = forecast.forecastDate
        holder.minMaxTemp.text = "${forecast.tMin} / ${forecast.tMax}"
        holder.weatherDescription.text = forecast.precipitaProb

        // Set the end drawable
        WeatherDrawableResolver.getWeatherDrawable(forecast.idWeatherType, overrideTime = true)?.let { holder.weatherIcon.setImageResource(it) }
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }
}
