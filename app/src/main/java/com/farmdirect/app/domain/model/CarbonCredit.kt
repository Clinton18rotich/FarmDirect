package com.farmdirect.app.domain.model

data class CarbonCredit(
    val id: String = "", val userId: String = "", val totalCredits: Double = 0.0,
    val verifiedCredits: Double = 0.0, val soldCredits: Double = 0.0,
    val totalEarnings: Double = 0.0, val currentPrice: Double = 5.0
)

data class RegisterPracticeRequest(val type: String, val description: String)
