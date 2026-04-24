package com.farmdirect.app.domain.model

data class WeatherForecast(
    val current: CurrentWeather = CurrentWeather(),
    val daily: List<DailyWeather> = emptyList(),
    val farmingAdvisory: FarmingAdvisory = FarmingAdvisory()
)

data class CurrentWeather(
    val temperature: Double = 0.0,
    val humidity: Int = 0,
    val condition: String = "Clear",
    val description: String = ""
)

data class DailyWeather(
    val date: Long = 0,
    val tempHigh: Double = 0.0,
    val tempLow: Double = 0.0,
    val condition: String = ""
)

data class FarmingAdvisory(
    val plantingAdvice: String = "",
    val irrigationAdvice: String = "",
    val pestRisk: String = "Low"
)
