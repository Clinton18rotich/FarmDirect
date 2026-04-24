package com.farmdirect.app.data.ml

import com.farmdirect.app.domain.model.AIFeedback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FeedbackCollector {
    
    private val _pendingFeedback = MutableStateFlow<List<AIFeedback>>(emptyList())
    val pendingFeedback = _pendingFeedback.asStateFlow()
    
    private val trainer = AITrainer(com.farmdirect.app.FarmDirectApplication().applicationContext)
    
    /**
     * Submit feedback after user confirms or corrects a prediction
     */
    fun submitFeedback(
        predictionType: String,
        predictionId: String,
        wasAccurate: Boolean,
        actualValue: Double,
        predictedValue: Double,
        userCorrection: String = ""
    ): AIFeedback {
        val feedback = AIFeedback(
            id = java.util.UUID.randomUUID().toString(),
            predictionType = predictionType,
            predictionId = predictionId,
            wasAccurate = wasAccurate,
            actualValue = actualValue,
            predictedValue = predictedValue,
            userCorrection = userCorrection,
            confidenceBefore = 0.75, // Get from prediction
            confidenceAfter = if (wasAccurate) 0.80 else 0.65
        )
        
        trainer.collectFeedback(feedback)
        
        // Trigger learning if we have enough feedback
        if (_pendingFeedback.value.size >= 10) {
            trainer.learn()
        }
        
        return feedback
    }
    
    /**
     * Quick thumbs up/down feedback
     */
    fun quickFeedback(predictionId: String, wasHelpful: Boolean) {
        submitFeedback(
            predictionType = "GENERAL",
            predictionId = predictionId,
            wasAccurate = wasHelpful,
            actualValue = 0.0,
            predictedValue = 0.0
        )
    }
    
    /**
     * Get AI learning progress
     */
    fun getLearningProgress(): Double {
        return trainer.metrics.value.accuracy
    }
    
    /**
     * Check if AI is improving
     */
    fun isImproving(): Boolean {
        return trainer.metrics.value.trend == "IMPROVING"
    }
    
    /**
     * Get performance insights
     */
    fun getInsights() = trainer.analyzePerformance()
}
