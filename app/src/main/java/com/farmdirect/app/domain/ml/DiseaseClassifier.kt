package com.farmdirect.app.domain.ml

import com.farmdirect.app.data.ml.FeedbackCollector
import com.farmdirect.app.domain.model.DiseaseDetection
import com.farmdirect.app.domain.model.Treatment

class DiseaseClassifier(
    private val feedbackCollector: FeedbackCollector
) {
    
    private val diseaseDatabase = mapOf(
        "leaf_spots" to DiseaseInfo(
            name = "Northern Leaf Blight",
            severity = "MODERATE",
            treatments = listOf(
                Treatment("Mancozeb 80WP", "Apply 50g/20L water", 450.0, "Neem oil spray"),
                Treatment("Copper fungicide", "Spray every 7 days", 300.0, "Garlic solution")
            )
        ),
        "rust" to DiseaseInfo(
            name = "Maize Rust",
            severity = "MODERATE",
            treatments = listOf(
                Treatment("Tebuconazole", "Apply 25ml/20L water", 600.0, "Sulfur dusting"),
                Treatment("Propiconazole", "Spray at first sign", 550.0, "Milk spray 1:10")
            )
        ),
        "blight" to DiseaseInfo(
            name = "Early Blight",
            severity = "SEVERE",
            treatments = listOf(
                Treatment("Chlorothalonil", "Apply 40ml/20L water", 800.0, "Copper soap"),
                Treatment("Mancozeb + Metalaxyl", "Apply 50g/20L water", 700.0, "Compost tea")
            )
        ),
        "healthy" to DiseaseInfo(
            name = "Healthy",
            severity = "MILD",
            treatments = listOf(
                Treatment("No treatment needed", "Continue monitoring", 0.0, "Preventive care")
            )
        )
    )
    
    /**
     * Classify disease with confidence based on learning
     */
    fun classify(imageFeatures: List<Double>): DiseaseDetection {
        // Simplified classification - in production, use TensorFlow Lite
        val result = when {
            imageFeatures.any { it > 0.8 } -> "leaf_spots"
            imageFeatures.any { it > 0.6 } -> "rust"
            imageFeatures.any { it > 0.4 } -> "blight"
            else -> "healthy"
        }
        
        val disease = diseaseDatabase[result] ?: diseaseDatabase["healthy"]!!
        val learningProgress = feedbackCollector.getLearningProgress()
        val confidence = (0.6 + learningProgress * 0.3).coerceIn(0.6, 0.95)
        
        return DiseaseDetection(
            cropType = "Maize",
            disease = disease.name,
            confidence = confidence,
            severity = disease.severity,
            treatment = disease.treatments.first()
        )
    }
    
    /**
     * Learn from user confirmation
     */
    fun learn(
        predictionId: String,
        predictedDisease: String,
        actualDisease: String?
    ) {
        val wasAccurate = predictedDisease == actualDisease
        feedbackCollector.submitFeedback(
            predictionType = "DISEASE_DETECTION",
            predictionId = predictionId,
            wasAccurate = wasAccurate,
            actualValue = 0.0,
            predictedValue = 0.0,
            userCorrection = actualDisease ?: ""
        )
    }
    
    private data class DiseaseInfo(
        val name: String,
        val severity: String,
        val treatments: List<Treatment>
    )
}
