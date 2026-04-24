package com.farmdirect.app.domain.ml

import com.farmdirect.app.data.ml.FeedbackCollector
import com.farmdirect.app.data.ml.ModelCache
import com.farmdirect.app.domain.model.PricePrediction
import kotlin.math.abs

class PricePredictor(
    private val feedbackCollector: FeedbackCollector,
    private val modelCache: ModelCache
) {
    
    /**
     * Predict price with learning integration
     */
    fun predict(
        cropType: String,
        historicalPrices: List<Double>,
        currentSupply: Double,
        currentDemand: Double,
        seasonalFactor: Double
    ): PricePrediction {
        // Check cache first
        val cacheKey = "price_${cropType}_${historicalPrices.hashCode()}"
        val cached = modelCache.getCachedPrediction(cacheKey)
        if (cached != null) {
            return createPrediction(cropType, cached, 0.85)
        }
        
        // AI prediction logic with learning adjustment
        val basePrice = historicalPrices.lastOrNull() ?: 0.0
        
        // Supply-Demand factor
        val sdFactor = if (currentSupply > 0) currentDemand / currentSupply else 1.0
        val adjustedPrice = basePrice * sdFactor * seasonalFactor
        
        // Apply learning bias from feedback
        val learningAdjustment = if (feedbackCollector.isImproving()) 0.02 else -0.01
        val predictedPrice = adjustedPrice * (1 + learningAdjustment)
        
        // Calculate confidence based on learning progress
        val confidence = feedbackCollector.getLearningProgress().coerceIn(0.5, 0.95)
        
        // Cache result
        modelCache.cachePrediction(cacheKey, predictedPrice)
        
        return createPrediction(cropType, predictedPrice, confidence)
    }
    
    /**
     * Learn from actual outcome
     */
    fun learn(
        predictionId: String,
        predictedPrice: Double,
        actualPrice: Double,
        cropType: String
    ) {
        val wasAccurate = abs(predictedPrice - actualPrice) / actualPrice < 0.10 // Within 10%
        feedbackCollector.submitFeedback(
            predictionType = "PRICE_PREDICTION",
            predictionId = predictionId,
            wasAccurate = wasAccurate,
            actualValue = actualPrice,
            predictedValue = predictedPrice,
            userCorrection = if (!wasAccurate) "Actual price: $actualPrice" else ""
        )
    }
    
    private fun createPrediction(
        crop: String,
        price: Double,
        confidence: Double
    ): PricePrediction {
        return PricePrediction(
            cropType = crop,
            currentPrice = price * 0.95,
            predictedPrice = price,
            confidence = confidence,
            trend = if (confidence > 0.7) "RISING" else "STABLE",
            recommendation = when {
                confidence > 0.8 -> "Strong signal - Consider selling now"
                confidence > 0.6 -> "Moderate signal - Monitor market"
                else -> "Weak signal - Wait for more data"
            }
        )
    }
}
