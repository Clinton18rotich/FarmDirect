package com.farmdirect.app.domain.model

data class Crop(
    val id: String = "",
    val farmerId: String = "",
    val farmerName: String = "",
    val farmerRating: Double = 0.0,
    val farmerLocation: String = "",
    val title: String = "",
    val category: String = "",
    val variety: String = "",
    val quantity: Double = 0.0,
    val unit: String = "kg",
    val pricePerUnit: Double = 0.0,
    val totalPrice: Double = 0.0,
    val quality: String = "Grade 1",
    val description: String = "",
    val images: List<String> = emptyList(),
    val isOrganic: Boolean = false,
    val status: String = "ACTIVE",
    val createdAt: Long = System.currentTimeMillis()
)

data class PostCropRequest(
    val title: String, val category: String, val variety: String,
    val quantity: Double, val unit: String, val pricePerUnit: Double,
    val quality: String, val description: String, val isOrganic: Boolean,
    val latitude: Double, val longitude: Double, val address: String
)
