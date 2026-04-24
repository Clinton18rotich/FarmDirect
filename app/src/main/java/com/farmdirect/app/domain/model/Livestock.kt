package com.farmdirect.app.domain.model

data class Livestock(
    val id: String = "",
    val farmerId: String = "",
    val farmerName: String = "",
    val farmerLocation: String = "",
    val animalType: String = "CATTLE",
    val breed: String = "",
    val quantity: Int = 1,
    val ageMonths: Int = 0,
    val weightKg: Double = 0.0,
    val price: Double = 0.0,
    val images: List<String> = emptyList(),
    val status: String = "ACTIVE"
)

data class PostLivestockRequest(
    val animalType: String, val breed: String, val quantity: Int,
    val ageMonths: Int, val weightKg: Double, val price: Double,
    val description: String, val latitude: Double, val longitude: Double, val address: String
)
