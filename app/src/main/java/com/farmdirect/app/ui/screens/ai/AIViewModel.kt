package com.farmdirect.app.ui.screens.ai

import android.graphics.Bitmap
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.ml.FeedbackCollector
import com.farmdirect.app.data.ml.ModelCache
import com.farmdirect.app.data.repository.AIRepository
import com.farmdirect.app.domain.ml.DiseaseClassifier
import com.farmdirect.app.domain.ml.PricePredictor
import com.farmdirect.app.domain.model.AIFeedback
import com.farmdirect.app.domain.model.DiseaseDetection
import com.farmdirect.app.domain.model.PricePrediction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

data class AIUiState(
    val pricePrediction: PricePrediction? = null,
    val diseaseDetection: DiseaseDetection? = null,
    val isLoading: Boolean = false,
    val feedbackSubmitted: Boolean = false,
    val learningProgress: Double = 0.0
)

@HiltViewModel
class AIViewModel @Inject constructor(
    private val preferences: FarmDirectPreferences,
    private val repository: AIRepository,
    private val feedbackCollector: FeedbackCollector,
    private val modelCache: ModelCache
) : ViewModel() {
    
    private val _state = MutableStateFlow(AIUiState())
    val state = _state.asStateFlow()
    
    private val pricePredictor = PricePredictor(feedbackCollector, modelCache)
    private val diseaseClassifier = DiseaseClassifier(feedbackCollector)
    
    init {
        _state.value = _state.value.copy(learningProgress = feedbackCollector.getLearningProgress())
    }
    
    fun predictPrice(cropType: String, lat: Double = 1.0167, lng: Double = 35.0151) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val token = preferences.authToken.firstOrNull() ?: ""
            
            // Get historical prices from API
            val historicalPrices = repository.getHistoricalPrices(token, cropType, 30)
            
            // Use ML predictor
            val prediction = pricePredictor.predict(
                cropType = cropType,
                historicalPrices = historicalPrices,
                currentSupply = 100.0,
                currentDemand = 120.0,
                seasonalFactor = 1.05
            )
            
            _state.value = _state.value.copy(pricePrediction = prediction, isLoading = false)
        }
    }
    
    fun detectDisease(bitmap: Bitmap, cropType: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            // Extract simple features (in production, use TensorFlow Lite)
            val features = extractImageFeatures(bitmap)
            val detection = diseaseClassifier.classify(features)
            
            _state.value = _state.value.copy(diseaseDetection = detection, isLoading = false)
        }
    }
    
    /**
     * Submit feedback to improve AI
     */
    fun submitPriceFeedback(predictionId: String, predictedPrice: Double, actualPrice: Double) {
        pricePredictor.learn(predictionId, predictedPrice, actualPrice, "Maize")
        _state.value = _state.value.copy(
            feedbackSubmitted = true,
            learningProgress = feedbackCollector.getLearningProgress()
        )
    }
    
    fun submitDiseaseFeedback(predictionId: String, predictedDisease: String, actualDisease: String?) {
        diseaseClassifier.learn(predictionId, predictedDisease, actualDisease)
        _state.value = _state.value.copy(
            feedbackSubmitted = true,
            learningProgress = feedbackCollector.getLearningProgress()
        )
    }
    
    fun quickFeedback(predictionId: String, wasHelpful: Boolean) {
        feedbackCollector.quickFeedback(predictionId, wasHelpful)
        _state.value = _state.value.copy(learningProgress = feedbackCollector.getLearningProgress())
    }
    
    private fun extractImageFeatures(bitmap: Bitmap): List<Double> {
        // Simplified feature extraction - in production, use ML model
        return listOf(0.75, 0.3, 0.1, 0.05)
    }
    
    fun getPerformanceInsights() = feedbackCollector.getInsights()
}
