package com.jup.oneNotification.core.network

import com.jup.oneNotification.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeather {
    @GET("data/2.5/weather?")
    fun getCurrentWeatherData(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") app_id: String): Call<WeatherResponse>
}