package dev.joshhalvorson.weather.screens.weatherScreen.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import dev.joshhalvorson.weather.screens.weatherScreen.api.WeatherApiService
import dev.joshhalvorson.weather.screens.weatherScreen.model.WeatherReturn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class WeatherRepository @Inject constructor(val weatherApiService: WeatherApiService) {
    companion object {
        const val TAG = "WeatherRepository"
    }

    val weatherLiveData: MutableLiveData<WeatherReturn> = MutableLiveData<WeatherReturn>()

    fun getWeather(lat: Double, lon: Double): MutableLiveData<WeatherReturn> {
        val call = weatherApiService.getWeather(lat, lon)
        call.enqueue(object : Callback<WeatherReturn> {
            override fun onResponse(call: Call<WeatherReturn>, response: Response<WeatherReturn>) {
                if (response.isSuccessful) {
                    response.body()?.let {  weatherReturn ->
                        weatherLiveData.postValue(weatherReturn)
                    }
                }
            }

            override fun onFailure(call: Call<WeatherReturn>, t: Throwable) {
                weatherLiveData.postValue(null)
                Log.i(TAG, "getCurrentWeather: OnFailure - ${t.stackTrace}")
            }

        })
        return weatherLiveData
    }
}