package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.WeatherForecast
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor() {
    suspend fun getWeather(token: String, lat: Double, lng: Double): WeatherForecast? {
        val response = RetrofitClient.apiService.getWeather("Bearer $token", lat, lng)
        return if (response.isSuccessful) response.body() else null
    }
}
