package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.AIAnalysisRequest
import com.farmdirect.app.domain.model.DiseaseDetection
import com.farmdirect.app.domain.model.PricePrediction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIRepository @Inject constructor() {
    suspend fun predictPrice(token: String, cropType: String, lat: Double, lng: Double): PricePrediction? {
        val response = RetrofitClient.apiService.predictPrice("Bearer $token", AIAnalysisRequest("PRICE_PREDICTION", cropType, "", lat, lng))
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun detectDisease(token: String, cropType: String, imageBase64: String): DiseaseDetection? {
        val response = RetrofitClient.apiService.detectDisease("Bearer $token", AIAnalysisRequest("DISEASE_DETECTION", cropType, imageBase64, 0.0, 0.0))
        return if (response.isSuccessful) response.body() else null
    }
}
