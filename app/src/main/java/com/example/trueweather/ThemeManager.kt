package com.example.trueweather

import com.example.network.utils.TimestampUtil

object ThemeManager {
    fun getCurrentTextColor(): Int {
        return if(TimestampUtil.isEvening()) R.color.white
        else R.color.black
    }
}