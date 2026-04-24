package com.farmdirect.app.domain.model

data class Rider(
    val id: String = "",
    val userId: String = "",
    val fullName: String = "",
    val phoneNumber: String = "",
    val profileImage: String = "",
    val nationalId: String = "",
    val drivingLicense: DrivingLicense = DrivingLicense(),
    val vehicle: Vehicle = Vehicle(),
    val rating: Double = 0.0,
    val totalDeliveries: Int = 0,
    val completionRate: Double = 0.0,
    val isOnline: Boolean = false,
    val totalEarnings: Double = 0.0,
    val isVerified: Boolean = false
)

data class DrivingLicense(val licenseNumber: String = "", val classType: String = "", val verified: Boolean = false)

data class Vehicle(
    val type: String = "MOTORCYCLE", val make: String = "", val model: String = "",
    val year: Int = 2020, val color: String = "", val registrationNumber: String = "",
    val capacity: VehicleCapacity = VehicleCapacity(), val hasRefrigeration: Boolean = false
)

data class VehicleCapacity(val maxWeightKg: Double = 100.0, val maxLivestock: Int = 0)

data class RiderRegistrationRequest(
    val fullName: String, val phoneNumber: String, val nationalId: String,
    val licenseNumber: String, val vehicleType: String, val vehicleMake: String,
    val vehicleModel: String, val vehicleYear: Int, val registrationNumber: String
)
