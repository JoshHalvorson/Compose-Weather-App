package dev.joshhalvorson.weather.screens.weatherScreen.model

import kotlin.math.roundToInt

data class Hourly(
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,
    val feels_like: Double,
    val humidity: Int,
    val pop: Int,
    val pressure: Int,
    val temp: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_speed: Double
) {
    fun getTempRounded(): Int = temp.roundToInt()
}