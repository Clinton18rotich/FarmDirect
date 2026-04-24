package com.farmdirect.app.domain.model

data class Delivery(
    val id: String = "",
    val orderId: String = "",
    val riderId: String = "",
    val riderName: String = "",
    val riderPhone: String = "",
    val vehicleType: String = "",
    val status: String = "PENDING",
    val pickupCode: String = "",
    val deliveryCode: String = "",
    val deliveryFee: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)

data class TrackingUpdate(
    val timestamp: Long = System.currentTimeMillis(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val status: String = "",
    val message: String = ""
)
