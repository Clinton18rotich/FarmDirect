package com.farmdirect.app.domain.model

data class AIFeedback(
    val id: String = "",
    val predictionType: String = "",      // PRICE_PREDICTION, DISEASE_DETECTION
    val predictionId: String = "",
    val wasAccurate: Boolean = false,
    val actualValue: Double = 0.0,         // Actual price or confirmed disease
    val predictedValue: Double = 0.0,      // What AI predicted
    val userCorrection: String = "",       // User's correction if wrong
    val confidenceBefore: Double = 0.0,    // AI confidence before feedback
    val confidenceAfter: Double = 0.0,     // AI confidence after learning
    val timestamp: Long = System.currentTimeMillis(),
    val metadata: Map<String, String> = emptyMap()
)

data class LearningMetrics(
    val totalPredictions: Int = 0,
    val correctPredictions: Int = 0,
    val accuracy: Double = 0.0,            // Overall accuracy
    val weeklyAccuracy: Double = 0.0,      // Last 7 days
    val trend: String = "STABLE",          // IMPROVING, STABLE, DECLINING
    val lastTrainedAt: Long? = null,
    val trainingDataPoints: Int = 0,
    val modelVersion: String = "1.0.0"
)
