package com.farmdirect.app.util

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ARCropMeasurement(private val context: Context) {
    
    private val _measurement = MutableStateFlow<CropMeasurement?>(null)
    val measurement = _measurement.asStateFlow()
    
    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing = _isAnalyzing.asStateFlow()
    
    data class CropMeasurement(
        val cropType: String = "",
        val estimatedArea: Double = 0.0,      // Square meters
        val estimatedYield: Double = 0.0,      // kg
        val plantCount: Int = 0,
        val healthScore: Int = 0,              // 0-100
        val confidence: Float = 0.0f,
        val recommendation: String = ""
    )
    
    // Average yield per square meter for common crops
    private val yieldEstimates = mapOf(
        "maize" to 0.8,      // ~0.8 kg per sq meter
        "beans" to 0.3,
        "potatoes" to 2.0,
        "rice" to 0.6,
        "wheat" to 0.5,
        "vegetables" to 1.5,
        "coffee" to 0.2,
        "tea" to 0.4
    )
    
    fun estimateFieldSize(photoWidth: Int, photoHeight: Int, referenceObject: String = "person"): Double {
        // Use reference object to estimate scale
        val referenceHeightPixels = when (referenceObject) {
            "person" -> 170.0  // Average person height in cm
            "cow" -> 140.0
            "fence_post" -> 150.0
            "door" -> 210.0
            else -> 170.0
        }
        
        // Estimate field of view based on typical phone camera
        val estimatedWidthMeters = (photoWidth / 500.0) * 5.0  // Rough estimate
        val estimatedHeightMeters = (photoHeight / 500.0) * 5.0
        
        return estimatedWidthMeters * estimatedHeightMeters
    }
    
    fun countPlants(bitmap: Bitmap, cropType: String): Int {
        // Simplified plant counting using color detection
        var greenPixels = 0
        val totalPixels = bitmap.width * bitmap.height
        val sampleSize = maxOf(1, totalPixels / 10000)  // Sample 10,000 pixels
        
        for (y in 0 until bitmap.height step sampleSize) {
            for (x in 0 until bitmap.width step sampleSize) {
                val pixel = bitmap.getPixel(x, y)
                val green = (pixel shr 8) and 0xFF
                val red = (pixel shr 16) and 0xFF
                val blue = pixel and 0xFF
                
                // Detect green vegetation
                if (green > red && green > blue && green > 100) {
                    greenPixels++
                }
            }
        }
        
        val greenPercentage = greenPixels.toFloat() / (totalPixels / sampleSize)
        
        // Estimate plant count based on green coverage and crop spacing
        return when (cropType) {
            "maize" -> (greenPercentage * 200).toInt()
            "beans" -> (greenPercentage * 500).toInt()
            "potatoes" -> (greenPercentage * 300).toInt()
            else -> (greenPercentage * 250).toInt()
        }
    }
    
    fun estimateYield(areaSqMeters: Double, cropType: String, healthScore: Int): Double {
        val baseYield = yieldEstimates[cropType.lowercase()] ?: 0.5
        val healthFactor = healthScore / 100.0
        return areaSqMeters * baseYield * healthFactor
    }
    
    fun analyzeCrop(
        bitmap: Bitmap,
        cropType: String,
        referenceObject: String = "person"
    ): CropMeasurement {
        _isAnalyzing.value = true
        
        val area = estimateFieldSize(bitmap.width, bitmap.height, referenceObject)
        val plantCount = countPlants(bitmap, cropType)
        val healthScore = calculateHealthScore(bitmap)
        val yield = estimateYield(area, cropType, healthScore)
        
        val measurement = CropMeasurement(
            cropType = cropType,
            estimatedArea = area,
            estimatedYield = yield,
            plantCount = plantCount,
            healthScore = healthScore,
            confidence = 0.7f,
            recommendation = when {
                healthScore > 80 -> "Crop looks healthy! Expected good yield."
                healthScore > 60 -> "Crop needs some attention. Consider fertilizer."
                else -> "Crop health is low. Consult an expert."
            }
        )
        
        _measurement.value = measurement
        _isAnalyzing.value = false
        return measurement
    }
    
    private fun calculateHealthScore(bitmap: Bitmap): Int {
        var healthyPixels = 0
        var totalSampled = 0
        
        val step = maxOf(1, minOf(bitmap.width, bitmap.height) / 100)
        
        for (y in 0 until bitmap.height step step) {
            for (x in 0 until bitmap.width step step) {
                val pixel = bitmap.getPixel(x, y)
                val green = (pixel shr 8) and 0xFF
                val red = (pixel shr 16) and 0xFF
                
                if (green > red * 1.2 && green > 120) {
                    healthyPixels++
                }
                totalSampled++
            }
        }
        
        return if (totalSampled > 0) (healthyPixels * 100 / totalSampled) else 0
    }
}
