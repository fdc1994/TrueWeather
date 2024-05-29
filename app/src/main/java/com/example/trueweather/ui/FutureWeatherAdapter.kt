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
        val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        val dayOfTheWeek: TextView = itemView.findViewById(R.id.dayOfWeek)
        val minTemp: TextView = itemView.findViewById(R.id.minTemp)
        val maxTemp: TextView = itemView.findViewById(R.id.maxTemp)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.future_weather_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.dayOfTheWeek.text = forecast.forecastDate
        holder.minTemp.text = forecast.tMin
        holder.maxTemp.text = forecast.tMax
        WeatherDrawableResolver.getWeatherDrawable(forecast.idWeatherType, overrideTime = true)?.let { holder.weatherIcon.setImageResource(it) }
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }
}
