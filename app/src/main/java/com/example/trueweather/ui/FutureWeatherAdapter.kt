package com.example.trueweather.ui
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.data.objects.WeatherData
import com.example.trueweather.R
import com.example.trueweather.ThemeManager

class FutureWeatherAdapter(private val forecastList: List<WeatherData>) :
    RecyclerView.Adapter<FutureWeatherAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        private val dayOfTheWeek: TextView = itemView.findViewById(R.id.dayOfWeek)
        private val minTemp: TextView = itemView.findViewById(R.id.minTemp)
        private val maxTemp: TextView = itemView.findViewById(R.id.maxTemp)

        fun bind(weatherData: WeatherData) {
            dayOfTheWeek.text = weatherData.forecastDate
            minTemp.text = "${weatherData.tMin}ºC"
            maxTemp.text = "${weatherData.tMax}ºC"
            WeatherDrawableResolver.getWeatherDrawable(weatherData.idWeatherType, overrideTime = true)?.let { weatherIcon.setImageResource(it) }
            setTheme()
        }
        private fun setTheme() {
            val themedColor = itemView.context.getColor(ThemeManager.getCurrentTextColor())
            dayOfTheWeek.setTextColor(themedColor)
            maxTemp.setTextColor(themedColor)
            minTemp.setTextColor(themedColor)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.future_weather_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.bind(forecast)
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }
}
