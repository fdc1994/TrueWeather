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

    fun getDrawableAnimationFilename(weatherId: Int, overrideTime: Boolean = false): String? {
        if(!TimestampUtil.isEvening() || overrideTime) {
            return when(weatherId) {
                1 -> "w_ic_d_01anim.svg"
                2 -> "w_ic_d_02anim.svg"
                3 -> "w_ic_d_03anim.svg"
                4 -> "w_ic_d_04anim.svg"
                5 -> "w_ic_d_05anim.svg"
                6 -> "w_ic_d_06anim.svg"
                7 -> "w_ic_d_07anim.svg"
                8 -> "w_ic_d_08anim.svg"
                9 -> "w_ic_d_09anim.svg"
                10 -> "w_ic_d_10anim.svg"
                11 -> "w_ic_d_11anim.svg"
                12 -> "w_ic_d_12anim.svg"
                13 -> "w_ic_d_13anim.svg"
                14 -> "w_ic_d_14anim.svg"
                15 -> "w_ic_d_15anim.svg"
                16 -> "w_ic_d_16anim.svg"
                17 -> "w_ic_d_17anim.svg"
                18 -> "w_ic_d_18anim.svg"
                19 -> "w_ic_d_19anim.svg"
                20 -> "w_ic_d_20anim.svg"
                21 -> "w_ic_d_21anim.svg"
                22 -> "w_ic_d_22anim.svg"
                23 -> "w_ic_d_23anim.svg"
                24 -> "w_ic_d_24anim.svg"
                25 -> "w_ic_d_25anim.svg"
                26 -> "w_ic_d_26anim.svg"
                27 -> "w_ic_d_27anim.svg"
                28 -> "w_ic_d_28anim.svg"
                29 -> "w_ic_d_29anim.svg"
                30 -> "w_ic_d_30anim.svg"
                else -> null
            }
        } else {
            return when(weatherId) {
                1 -> "w_ic_n_01anim.svg"
                2 -> "w_ic_n_02anim.svg"
                3 -> "w_ic_n_03anim.svg"
                4 -> "w_ic_n_04anim.svg"
                5 -> "w_ic_n_05anim.svg"
                6 -> "w_ic_n_06anim.svg"
                7 -> "w_ic_n_07anim.svg"
                8 -> "w_ic_n_08anim.svg"
                9 -> "w_ic_n_09anim.svg"
                10 -> "w_ic_n_10anim.svg"
                11 -> "w_ic_n_11anim.svg"
                12 -> "w_ic_n_12anim.svg"
                13 -> "w_ic_n_13anim.svg"
                14 -> "w_ic_n_14anim.svg"
                15 -> "w_ic_n_15anim.svg"
                16 -> "w_ic_n_16anim.svg"
                17 -> "w_ic_n_17anim.svg"
                18 -> "w_ic_n_18anim.svg"
                19 -> "w_ic_n_19anim.svg"
                20 -> "w_ic_n_20anim.svg"
                21 -> "w_ic_n_21anim.svg"
                22 -> "w_ic_n_22anim.svg"
                23 -> "w_ic_n_23anim.svg"
                24 -> "w_ic_n_24anim.svg"
                25 -> "w_ic_n_25anim.svg"
                26 -> "w_ic_n_26anim.svg"
                27 -> "w_ic_n_27anim.svg"
                28 -> "w_ic_n_28anim.svg"
                29 -> "w_ic_n_29anim.svg"
                30 -> "w_ic_n_30anim.svg"
                else -> null
            }
        }
    }
}