package com.farmdirect.app.domain.model

data class User(
    val id: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val location: String = "",
    val profileImageUrl: String = "",
    val userType: String = "FARMER",
    val rating: Double = 0.0,
    val totalSales: Int = 0,
    val isVerified: Boolean = false
)

data class AuthResponse(val userId: String, val token: String, val user: User)
data class LoginRequest(val phoneNumber: String, val password: String)
data class RegisterRequest(val fullName: String, val phoneNumber: String, val password: String, val userType: String, val county: String)
