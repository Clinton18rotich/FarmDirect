package com.farmdirect.app.domain.model

data class Equipment(
    val id: String = "", val ownerName: String = "", val name: String = "",
    val type: String = "TRACTOR", val make: String = "", val model: String = "",
    val rentalRate: Double = 0.0, val rateUnit: String = "day", val available: Boolean = true,
    val location: String = "", val distanceKm: Double = 0.0, val images: List<String> = emptyList()
)

data class EquipmentRental(val id: String = "", val equipmentId: String = "", val renterName: String = "", val totalCost: Double = 0.0, val status: String = "PENDING")

data class PostEquipmentRequest(val name: String, val type: String, val make: String, val model: String, val year: Int, val description: String, val rentalRate: Double, val rateUnit: String, val location: String)
