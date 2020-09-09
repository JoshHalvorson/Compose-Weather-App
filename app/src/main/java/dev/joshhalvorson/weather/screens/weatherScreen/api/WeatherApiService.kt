package dev.joshhalvorson.weather.screens.weatherScreen.api

import dev.joshhalvorson.weather.BuildConfig
import dev.joshhalvorson.weather.screens.weatherScreen.model.WeatherReturn
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("onecall")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "imperial",
        @Query("appid") apiKey: String = BuildConfig.API_KEY
    ): Call<WeatherReturn>

}