package dev.joshhalvorson.weather.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun Long.getDayFromUnixSeconds(): String {
    val date = Date(this * 1000L)
    val today = Date(Calendar.getInstance().timeInMillis)
    if (Date(today.time + TimeUnit.DAYS.toMillis(1)).day == date.day) {
        return "Tomorrow"
    }
    return SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)
}

fun Long.getTimeFromUnixSeconds(): String {
    val date = Date(this * 1000L)
    return SimpleDateFormat("h:mm a", Locale.ENGLISH).format(date.time)
}