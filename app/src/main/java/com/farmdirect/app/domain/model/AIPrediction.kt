package com.farmdirect.app.domain.model

data class PricePrediction(
    val cropType: String = "", val currentPrice: Double = 0.0, val predictedPrice: Double = 0.0,
    val confidence: Double = 0.0, val trend: String = "STABLE", val recommendation: String = ""
)

data class DiseaseDetection(
    val cropType: String = "", val disease: String = "", val confidence: Double = 0.0,
    val severity: String = "MILD", val treatment: Treatment = Treatment()
)

data class Treatment(val name: String = "", val dosage: String = "", val costEstimate: Double = 0.0, val organicAlternative: String = "")

data class AIAnalysisRequest(val type: String = "", val cropType: String = "", val imageBase64: String = "", val latitude: Double = 0.0, val longitude: Double = 0.0)
