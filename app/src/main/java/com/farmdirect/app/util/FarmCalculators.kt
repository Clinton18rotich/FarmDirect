package com.farmdirect.app.util

object FarmCalculators {
    
    data class ProfitResult(
        val revenue: Double,
        val costs: Double,
        val profit: Double,
        val profitMargin: Double,
        val breakEvenPrice: Double,
        val roi: Double
    )
    
    data class UnitConversion(
        val bags: Double,
        val kg: Double,
        val tons: Double,
        val sacks: Double
    )
    
    fun calculateProfit(
        quantity: Double,
        unit: String,
        sellingPricePerUnit: Double,
        productionCost: Double,
        transportCost: Double = 0.0,
        platformFee: Double = 3.0
    ): ProfitResult {
        val revenue = quantity * sellingPricePerUnit
        val fee = revenue * (platformFee / 100)
        val totalCosts = productionCost + transportCost + fee
        val profit = revenue - totalCosts
        val profitMargin = if (revenue > 0) (profit / revenue) * 100 else 0.0
        val breakEvenPrice = if (quantity > 0) totalCosts / quantity else 0.0
        val roi = if (productionCost > 0) ((profit / productionCost) * 100) else 0.0
        
        return ProfitResult(revenue, totalCosts, profit, profitMargin, breakEvenPrice, roi)
    }
    
    fun convertUnits(quantity: Double, fromUnit: String): UnitConversion {
        val kg = when (fromUnit.lowercase()) {
            "bag", "bags" -> quantity * 90.0
            "ton", "tons" -> quantity * 1000.0
            "sack", "sacks" -> quantity * 50.0
            "crate", "crates" -> quantity * 25.0
            else -> quantity
        }
        
        return UnitConversion(
            bags = kg / 90.0,
            kg = kg,
            tons = kg / 1000.0,
            sacks = kg / 50.0
        )
    }
    
    fun estimateFertilizer(
        cropType: String,
        areaAcres: Double,
        soilType: String = "loam"
    ): Map<String, Double> {
        return when (cropType.lowercase()) {
            "maize" -> mapOf(
                "DAP (kg)" to areaAcres * 50.0,
                "CAN (kg)" to areaAcres * 50.0,
                "UREA (kg)" to areaAcres * 25.0
            )
            "beans" -> mapOf(
                "DAP (kg)" to areaAcres * 40.0,
                "CAN (kg)" to areaAcres * 20.0
            )
            "potatoes" -> mapOf(
                "DAP (kg)" to areaAcres * 60.0,
                "NPK 17:17:17 (kg)" to areaAcres * 80.0
            )
            else -> mapOf(
                "DAP (kg)" to areaAcres * 40.0,
                "CAN (kg)" to areaAcres * 30.0
            )
        }
    }
    
    fun calculateWaterNeeds(
        cropType: String,
        areaAcres: Double,
        season: String = "dry"
    ): Double {
        val dailyMm = when (cropType.lowercase()) {
            "maize" -> if (season == "dry") 6.0 else 4.0
            "beans" -> if (season == "dry") 4.0 else 3.0
            "potatoes" -> if (season == "dry") 7.0 else 5.0
            "rice" -> 10.0
            else -> 5.0
        }
        return dailyMm * areaAcres * 4046.86 // Convert acres to sq meters, mm to liters
    }
}
