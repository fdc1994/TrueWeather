package com.example.network.data

enum class WeatherType(val id: Int, val descWeatherTypeEN: String, val descWeatherTypePT: String) {
    UNKNOWN(-99, "--", "---"),
    NO_INFORMATION(0, "No information", "Sem informação"),
    CLEAR_SKY(1, "Clear sky", "Céu limpo"),
    PARTLY_CLOUDY(2, "Partly cloudy", "Céu pouco nublado"),
    SUNNY_INTERVALS(3, "Sunny intervals", "Céu parcialmente nublado"),
    CLOUDY(4, "Cloudy", "Céu muito nublado ou encoberto"),
    CLOUDY_HIGH(5, "Cloudy (High cloud)", "Céu nublado por nuvens altas"),
    SHOWERS_RAIN(6, "Showers/rain", "Aguaceiros/chuva"),
    LIGHT_SHOWERS_RAIN(7, "Light showers/rain", "Aguaceiros/chuva fracos"),
    HEAVY_SHOWERS_RAIN(8, "Heavy showers/rain", "Aguaceiros/rain fortes"),
    RAIN_SHOWERS(9, "Rain/showers", "Chuva/aguaceiros"),
    LIGHT_RAIN(10, "Light rain", "Chuva fraca ou chuvisco"),
    HEAVY_RAIN_SHOWERS(11, "Heavy rain/showers", "Chuva/aguaceiros forte"),
    INTERMITTENT_RAIN(12, "Intermittent rain", "Períodos de chuva"),
    INTERMITTENT_LIGTH_RAIN(13, "Intermittent ligth rain", "Períodos de chuva fraca"),
    INTERMITTENT_HEAVY_RAIN(14, "Intermittent heavy rain", "Períodos de chuva forte"),
    DRIZZLE(15, "Drizzle", "Chuvisco"),
    MIST(16, "Mist", "Neblina"),
    FOG(17, "Fog", "Nevoeiro ou nuvens baixas"),
    SNOW(18, "Snow", "Neve"),
    THUNDERSTORMS(19, "Thunderstorms", "Trovoada"),
    SHOWERS_AND_THUNDERSTORMS(20, "Showers and thunderstorms", "Aguaceiros e possibilidade de trovoada"),
    HAIL(21, "Hail", "Granizo"),
    FROST(22, "Frost", "Geada"),
    RAIN_AND_THUNDERSTORMS(23, "Rain and thunderstorms", "Chuva e possibilidade de trovoada"),
    CONVECTIVE_CLOUDS(24, "Convective clouds", "Nebulosidade convectiva"),
    PARTLY_CLOUDY2(25, "Partly cloudy", "Céu com períodos de muito nublado"),
    FOG2(26, "Fog", "Nevoeiro"),
    CLOUDY2(27, "Cloudy", "Céu nublado"),
    SNOW_SHOWERS(28, "Snow showers", "Aguaceiros de neve"),
    RAIN_AND_SNOW(29, "Rain and snow", "Chuva e Neve"),
    RAIN_AND_SNOW2(30, "Rain and snow", "Chuva e Neve");

    companion object {
        fun fromId(id: Int): WeatherType = entries.firstOrNull { it.id == id } ?: UNKNOWN
    }
}

enum class PrecipitationIntensity(val classPrecInt: Int, val descClassPrecIntEN: String, val descClassPrecIntPT: String) {
    UNKNOWN(-99, "--", "---"),
    NO_PRECIPITATION(0, "No precipitation", "Sem precipitação"),
    WEAK(1, "Weak", "Fraco"),
    MODERATE(2, "Moderate", "Moderado"),
    STRONG(3, "Strong", "Forte");

    companion object {
        fun fromClassPrecInt(classPrecInt: Int): PrecipitationIntensity = entries.firstOrNull { it.classPrecInt == classPrecInt } ?: UNKNOWN
    }
}

enum class WindSpeed(val classWindSpeed: Int, val descClassWindSpeedDailyEN: String, val descClassWindSpeedDailyPT: String) {
    UNKNOWN(-99, "--", "---"),
    WEAK(1, "Weak", "Fraco"),
    MODERATE(2, "Moderate", "Moderado"),
    STRONG(3, "Strong", "Forte"),
    VERY_STRONG(4, "Very strong", "Muito forte");

    companion object {
        fun fromClassWindSpeed(classWindSpeed: Int): WindSpeed = entries.firstOrNull { it.classWindSpeed == classWindSpeed } ?: UNKNOWN
    }
}