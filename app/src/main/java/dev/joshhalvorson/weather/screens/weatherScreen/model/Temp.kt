package dev.joshhalvorson.weather.screens.weatherScreen.model

import kotlin.math.roundToInt

data class Temp(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
) {
    fun getMaxRounded(): Int = max.roundToInt()
    fun getMinRounded(): Int = min.roundToInt()
}
