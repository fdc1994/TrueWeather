package com.example.network.data

enum class WeatherType(val id: Int, val descWeatherTypePT: String) {
    UNKNOWN(-99, "---"),
    NO_INFORMATION(0, "Sem informação"),
    CLEAR_SKY(1, "Céu limpo"),
    PARTLY_CLOUDY(2, "Céu pouco nublado"),
    SUNNY_INTERVALS(3, "Céu parcialmente nublado"),
    CLOUDY(4, "Céu muito nublado ou encoberto"),
    CLOUDY_HIGH(5, "Céu nublado por nuvens altas"),
    SHOWERS_RAIN(6, "Aguaceiros/chuva"),
    LIGHT_SHOWERS_RAIN(7, "Aguaceiros/chuva fracos"),
    HEAVY_SHOWERS_RAIN(8, "Aguaceiros/rain fortes"),
    RAIN_SHOWERS(9, "Chuva/aguaceiros"),
    LIGHT_RAIN(10, "Chuva fraca ou chuvisco"),
    HEAVY_RAIN_SHOWERS(11, "Chuva/aguaceiros forte"),
    INTERMITTENT_RAIN(12, "Períodos de chuva"),
    INTERMITTENT_LIGTH_RAIN(13, "Períodos de chuva fraca"),
    INTERMITTENT_HEAVY_RAIN(14, "Períodos de chuva forte"),
    DRIZZLE(15, "Chuvisco"),
    MIST(16, "Neblina"),
    FOG(17, "Nevoeiro ou nuvens baixas"),
    SNOW(18, "Neve"),
    THUNDERSTORMS(19, "Trovoada"),
    SHOWERS_AND_THUNDERSTORMS(20, "Aguaceiros e possibilidade de trovoada"),
    HAIL(21, "Granizo"),
    FROST(22, "Geada"),
    RAIN_AND_THUNDERSTORMS(23, "Chuva e possibilidade de trovoada"),
    CONVECTIVE_CLOUDS(24, "Nebulosidade convectiva"),
    PARTLY_CLOUDY2(25, "Céu com períodos de muito nublado"),
    FOG2(26, "Nevoeiro"),
    CLOUDY2(27, "Céu nublado"),
    SNOW_SHOWERS(28, "Aguaceiros de neve"),
    RAIN_AND_SNOW(29, "Chuva e Neve"),
    RAIN_AND_SNOW2(30, "Chuva e Neve");

    companion object {
        fun fromId(id: Int): WeatherType = entries.firstOrNull { it.id == id } ?: UNKNOWN
    }
}

enum class PrecipitationIntensity(val classPrecInt: Int, val descClassPrecIntPT: String) {
    UNKNOWN(-99, "---"),
    NO_PRECIPITATION(0, "Sem precipitação"),
    WEAK(1, "Fraco"),
    MODERATE(2, "Moderado"),
    STRONG(3, "Forte");

    companion object {
        fun fromClassPrecInt(classPrecInt: Int): PrecipitationIntensity = entries.firstOrNull { it.classPrecInt == classPrecInt } ?: UNKNOWN
    }
}

enum class WindSpeed(val classWindSpeed: Int, val descClassWindSpeedDailyPT: String) {
    UNKNOWN(-99, "---"),
    WEAK(1, "Fraco"),
    MODERATE(2, "Moderado"),
    STRONG(3, "Forte"),
    VERY_STRONG(4, "Muito forte");

    companion object {
        fun fromClassWindSpeed(classWindSpeed: Int): WindSpeed = entries.firstOrNull { it.classWindSpeed == classWindSpeed } ?: UNKNOWN
    }
}