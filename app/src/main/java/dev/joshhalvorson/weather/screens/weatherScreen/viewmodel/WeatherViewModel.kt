package dev.joshhalvorson.weather.screens.weatherScreen.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dev.joshhalvorson.weather.screens.weatherScreen.repository.WeatherRepository
import dev.joshhalvorson.weather.screens.weatherScreen.model.WeatherReturn

class WeatherViewModel @ViewModelInject constructor(
        val weatherRepository: WeatherRepository,
        @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun getWeather(lat: Double, lon: Double): LiveData<WeatherReturn> {
        return weatherRepository.getWeather(lat, lon)
    }
}