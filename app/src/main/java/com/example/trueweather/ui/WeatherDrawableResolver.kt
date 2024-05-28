package com.example.trueweather.ui

import com.example.network.utils.TimestampUtil
import com.example.trueweather.R


object WeatherDrawableResolver {
    fun getWeatherDrawable(weatherId: Int, overrideTime: Boolean = false): Int? {
        if(!TimestampUtil.isEvening() || overrideTime) {
            return when(weatherId) {
                1 -> R.drawable.weather_icon_day_01
                2 -> R.drawable.weather_icon_day_02
                3 -> R.drawable.weather_icon_day_03
                4 -> R.drawable.weather_icon_day_04
                5 -> R.drawable.weather_icon_day_05
                6 -> R.drawable.weather_icon_day_06
                7 -> R.drawable.weather_icon_day_07
                8 -> R.drawable.weather_icon_day_08
                9 -> R.drawable.weather_icon_day_09
                10 -> R.drawable.weather_icon_day_10
                11-> R.drawable.weather_icon_day_11
                12 -> R.drawable.weather_icon_day_12
                13 -> R.drawable.weather_icon_day_13
                14 -> R.drawable.weather_icon_day_14
                15 -> R.drawable.weather_icon_day_15
                16 -> R.drawable.weather_icon_day_16
                17 -> R.drawable.weather_icon_day_17
                18 -> R.drawable.weather_icon_day_18
                19 -> R.drawable.weather_icon_day_19
                20 -> R.drawable.weather_icon_day_20
                21 -> R.drawable.weather_icon_day_21
                22 -> R.drawable.weather_icon_day_22
                23 -> R.drawable.weather_icon_day_23
                24 -> R.drawable.weather_icon_day_24
                25 -> R.drawable.weather_icon_day_25
                26 -> R.drawable.weather_icon_day_26
                27 -> R.drawable.weather_icon_day_27
                28 -> R.drawable.weather_icon_day_28
                29 -> R.drawable.weather_icon_day_29
                30 -> R.drawable.weather_icon_day_30
                else -> null
            }
        } else {
            return when(weatherId) {
                1 -> R.drawable.weather_icon_night_01
                2 -> R.drawable.weather_icon_night_02
                3 -> R.drawable.weather_icon_night_03
                4 -> R.drawable.weather_icon_night_04
                5 -> R.drawable.weather_icon_night_05
                6 -> R.drawable.weather_icon_night_06
                7 -> R.drawable.weather_icon_night_07
                8 -> R.drawable.weather_icon_night_08
                9 -> R.drawable.weather_icon_night_09
                10 -> R.drawable.weather_icon_night_10
                11 -> R.drawable.weather_icon_night_11
                12 -> R.drawable.weather_icon_night_12
                13 -> R.drawable.weather_icon_night_13
                14 -> R.drawable.weather_icon_night_14
                15 -> R.drawable.weather_icon_night_15
                16 -> R.drawable.weather_icon_night_16
                17 -> R.drawable.weather_icon_night_17
                18 -> R.drawable.weather_icon_night_18
                19 -> R.drawable.weather_icon_night_19
                20 -> R.drawable.weather_icon_night_20
                21 -> R.drawable.weather_icon_night_21
                22 -> R.drawable.weather_icon_night_22
                23 -> R.drawable.weather_icon_night_23
                24 -> R.drawable.weather_icon_night_24
                25 -> R.drawable.weather_icon_night_25
                26 -> R.drawable.weather_icon_night_26
                27 -> R.drawable.weather_icon_night_27
                28 -> R.drawable.weather_icon_night_28
                29 -> R.drawable.weather_icon_night_29
                30 -> R.drawable.weather_icon_night_30
                else -> null
            }
        }
    }
}