package com.farmdirect.app.ui.screens.analytics

import androidx.lifecycle.ViewModel
import com.farmdirect.app.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor() : ViewModel() {
    
    private val _analytics = MutableStateFlow(generateMockAnalytics())
    val analytics = _analytics.asStateFlow()
    
    private fun generateMockAnalytics(): FarmAnalytics {
        return FarmAnalytics(
            totalSales = 245000.0,
            totalOrders = 45,
            totalRevenue = 232750.0,
            averageRating = 4.8,
            bestSellingCrop = "Maize",
            bestSellingCropRevenue = 120000.0,
            monthlySales = listOf(
                MonthlySale("Jan", 35000.0, 8),
                MonthlySale("Feb", 42000.0, 10),
                MonthlySale("Mar", 28000.0, 6),
                MonthlySale("Apr", 55000.0, 12),
                MonthlySale("May", 38000.0, 9)
            ),
            cropBreakdown = listOf(
                CropBreakdown("Maize", 120000.0, 48.9, "#2E7D32"),
                CropBreakdown("Beans", 65000.0, 26.5, "#FF6F00"),
                CropBreakdown("Potatoes", 40000.0, 16.3, "#795548"),
                CropBreakdown("Vegetables", 20000.0, 8.3, "#42A5F5")
            ),
            buyerLocations = listOf(
                LocationBreakdown("Eldoret", 15, 85000.0),
                LocationBreakdown("Kitale", 12, 65000.0),
                LocationBreakdown("Nairobi", 8, 45000.0),
                LocationBreakdown("Nakuru", 6, 25000.0),
                LocationBreakdown("Bungoma", 4, 12750.0)
            ),
            revenueGrowth = 12.5,
            orderGrowth = 8.3,
            repeatBuyers = 18,
            topBuyers = listOf(
                TopBuyer("Mary's Restaurant", 45000.0, 8, 5.0),
                TopBuyer("City Hotel", 32000.0, 5, 4.8),
                TopBuyer("Peter Wholesale", 28000.0, 4, 4.7)
            )
        )
    }
}
