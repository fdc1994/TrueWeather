package com.example.trueweather.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.data.objects.WeatherResult

class WeatherResultDiffCallback(
    private val oldResult: WeatherResult?,
    private val newResult: WeatherResult?
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldResult?.resultList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newResult?.resultList?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldResult?.resultList == newResult?.resultList
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldResult?.resultList?.get(oldItemPosition)?.weatherForecast == newResult?.resultList?.get(newItemPosition)?.weatherForecast
    }
}
