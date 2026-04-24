package com.farmdirect.app.ui.screens.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.repository.WeatherRepository
import com.farmdirect.app.domain.model.WeatherForecast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WeatherUiState(val forecast: WeatherForecast? = null, val isLoading: Boolean = false)

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val preferences: FarmDirectPreferences,
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _state = MutableStateFlow(WeatherUiState())
    val state = _state.asStateFlow()

    init { loadWeather() }

    fun loadWeather(lat: Double = 1.0167, lng: Double = 35.0151) {
        viewModelScope.launch {
            _state.value = WeatherUiState(isLoading = true)
            val token = preferences.authToken.firstOrNull() ?: ""
            _state.value = WeatherUiState(forecast = weatherRepository.getWeather(token, lat, lng))
        }
    }
}
