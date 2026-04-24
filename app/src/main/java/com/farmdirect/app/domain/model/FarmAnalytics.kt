package com.farmdirect.app.domain.model

data class FarmAnalytics(
    val totalSales: Double = 0.0,
    val totalOrders: Int = 0,
    val totalRevenue: Double = 0.0,
    val averageRating: Double = 0.0,
    val bestSellingCrop: String = "",
    val bestSellingCropRevenue: Double = 0.0,
    val monthlySales: List<MonthlySale> = emptyList(),
    val cropBreakdown: List<CropBreakdown> = emptyList(),
    val buyerLocations: List<LocationBreakdown> = emptyList(),
    val revenueGrowth: Double = 0.0,
    val orderGrowth: Double = 0.0,
    val repeatBuyers: Int = 0,
    val topBuyers: List<TopBuyer> = emptyList()
)

data class MonthlySale(
    val month: String = "",
    val revenue: Double = 0.0,
    val orders: Int = 0
)

data class CropBreakdown(
    val cropName: String = "",
    val revenue: Double = 0.0,
    val percentage: Double = 0.0,
    val color: String = "#2E7D32"
)

data class LocationBreakdown(
    val location: String = "",
    val buyerCount: Int = 0,
    val revenue: Double = 0.0
)

data class TopBuyer(
    val name: String = "",
    val totalSpent: Double = 0.0,
    val orders: Int = 0,
    val rating: Double = 0.0
)
