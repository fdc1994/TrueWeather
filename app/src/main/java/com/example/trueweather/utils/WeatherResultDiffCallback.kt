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
        // Compare items by a unique identifier, assuming 'id' is a unique identifier for each item
        val oldItem = oldResult?.resultList?.get(oldItemPosition)
        val newItem = newResult?.resultList?.get(newItemPosition)
        return oldItem?.weatherForecast?.globalIdLocal == newItem?.weatherForecast?.globalIdLocal
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Compare the content of items
        val oldItem = oldResult?.resultList?.get(oldItemPosition)
        val newItem = newResult?.resultList?.get(newItemPosition)
        return oldItem == newItem
    }
}
