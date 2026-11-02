package com.example.weather.ui.formatter

import androidx.annotation.StringRes
import com.example.weather.R
import com.example.weather.domain.model.WeatherCondition

@StringRes
fun WeatherCondition.toStringRes(): Int = when (this) {
    WeatherCondition.CLEAR -> R.string.weather_condition_clear
    WeatherCondition.MAINLY_CLEAR -> R.string.weather_condition_mainly_clear
    WeatherCondition.PARTLY_CLOUDY -> R.string.weather_condition_partly_cloudy
    WeatherCondition.OVERCAST -> R.string.weather_condition_overcast
    WeatherCondition.FOG -> R.string.weather_condition_fog
    WeatherCondition.DRIZZLE -> R.string.weather_condition_drizzle
    WeatherCondition.FREEZING_DRIZZLE -> R.string.weather_condition_freezing_drizzle
    WeatherCondition.RAIN -> R.string.weather_condition_rain
    WeatherCondition.FREEZING_RAIN -> R.string.weather_condition_freezing_rain
    WeatherCondition.SNOW -> R.string.weather_condition_snow
    WeatherCondition.RAIN_SHOWERS -> R.string.weather_condition_rain_showers
    WeatherCondition.SNOW_SHOWERS -> R.string.weather_condition_snow_showers
    WeatherCondition.THUNDERSTORM -> R.string.weather_condition_thunderstorm
    WeatherCondition.THUNDERSTORM_WITH_HAIL -> R.string.weather_condition_thunderstorm_with_hail
    WeatherCondition.UNKNOWN -> R.string.weather_condition_unknown
}
