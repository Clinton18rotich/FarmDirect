package com.farmdirect.app.data.ml

import android.content.Context
import com.farmdirect.app.domain.model.AIFeedback
import com.farmdirect.app.domain.model.LearningMetrics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class AITrainer(private val context: Context) {
    
    private val _metrics = MutableStateFlow(LearningMetrics())
    val metrics = _metrics.asStateFlow()
    
    private val feedbackFile: File
        get() = File(context.filesDir, "ai_feedback.json")
    
    private val metricsFile: File
        get() = File(context.filesDir, "ai_metrics.json")
    
    private val feedbackHistory = mutableListOf<AIFeedback>()
    
    init {
        loadHistory()
        recalculateMetrics()
    }
    
    /**
     * Collect user feedback to train the AI
     */
    fun collectFeedback(feedback: AIFeedback) {
        feedbackHistory.add(feedback)
        saveFeedback()
        recalculateMetrics()
    }
    
    /**
     * Learn from feedback - adjust confidence thresholds
     */
    fun learn(): LearningResult {
        val recentFeedback = feedbackHistory.takeLast(100)
        
        if (recentFeedback.isEmpty()) {
            return LearningResult(
                success = false,
                message = "Not enough data to learn from",
                newConfidence = 0.0
            )
        }
        
        // Calculate learning from feedback
        val accurateCount = recentFeedback.count { it.wasAccurate }
        val newAccuracy = accurateCount.toDouble() / recentFeedback.size
        
        // Adjust confidence based on learning
        val avgConfidence = recentFeedback.map { it.confidenceBefore }.average()
        val adjustment = if (newAccuracy > 0.7) 0.05 else -0.05
        val newConfidence = (avgConfidence + adjustment).coerceIn(0.0, 1.0)
        
        // Update metrics
        _metrics.value = _metrics.value.copy(
            accuracy = newAccuracy,
            weeklyAccuracy = calculateWeeklyAccuracy(),
            trend = detectTrend(),
            lastTrainedAt = System.currentTimeMillis(),
            trainingDataPoints = feedbackHistory.size,
            modelVersion = generateVersion(newAccuracy)
        )
        saveMetrics()
        
        return LearningResult(
            success = true,
            message = "AI learned from ${recentFeedback.size} feedback points",
            newConfidence = newConfidence
        )
    }
    
    /**
     * Weighted learning - recent feedback matters more
     */
    fun weightedLearn(): Map<String, Double> {
        val weights = feedbackHistory.mapIndexed { index, feedback ->
            // Recent feedback has higher weight
            val recencyWeight = 1.0 + (index.toDouble() / feedbackHistory.size)
            feedback to recencyWeight
        }
        
        val weightedAccuracy = weights.sumOf { (feedback, weight) ->
            if (feedback.wasAccurate) weight else 0.0
        } / weights.sumOf { it.second }
        
        return mapOf(
            "weighted_accuracy" to weightedAccuracy,
            "confidence_adjustment" to (weightedAccuracy - 0.5) * 0.1
        )
    }
    
    /**
     * Analyze which predictions are most/least accurate
     */
    fun analyzePerformance(): Map<String, PerformanceInsight> {
        val byType = feedbackHistory.groupBy { it.predictionType }
        
        return byType.mapValues { (_, feedbacks) ->
            PerformanceInsight(
                totalPredictions = feedbacks.size,
                accuracy = feedbacks.count { it.wasAccurate }.toDouble() / feedbacks.size,
                averageConfidence = feedbacks.map { it.confidenceBefore }.average(),
                commonCorrections = feedbacks
                    .filter { !it.wasAccurate }
                    .map { it.userCorrection }
                    .groupBy { it }
                    .mapValues { it.value.size }
                    .entries
                    .sortedByDescending { it.value }
                    .take(5)
                    .associate { it.toPair() }
            )
        }
    }
    
    private fun loadHistory() {
        try {
            if (feedbackFile.exists()) {
                val json = feedbackFile.readText()
                val arr = JSONArray(json)
                repeat(arr.length()) { i ->
                    val obj = arr.getJSONObject(i)
                    feedbackHistory.add(AIFeedback(
                        id = obj.optString("id"),
                        predictionType = obj.optString("predictionType"),
                        wasAccurate = obj.optBoolean("wasAccurate"),
                        actualValue = obj.optDouble("actualValue"),
                        predictedValue = obj.optDouble("predictedValue"),
                        userCorrection = obj.optString("userCorrection"),
                        confidenceBefore = obj.optDouble("confidenceBefore"),
                        confidenceAfter = obj.optDouble("confidenceAfter"),
                        timestamp = obj.optLong("timestamp")
                    ))
                }
            }
            if (metricsFile.exists()) {
                val json = metricsFile.readText()
                val obj = JSONObject(json)
                _metrics.value = LearningMetrics(
                    totalPredictions = obj.optInt("totalPredictions"),
                    correctPredictions = obj.optInt("correctPredictions"),
                    accuracy = obj.optDouble("accuracy"),
                    weeklyAccuracy = obj.optDouble("weeklyAccuracy"),
                    trend = obj.optString("trend"),
                    lastTrainedAt = obj.optLong("lastTrainedAt"),
                    trainingDataPoints = obj.optInt("trainingDataPoints"),
                    modelVersion = obj.optString("modelVersion")
                )
            }
        } catch (e: Exception) {
            // Start fresh
        }
    }
    
    private fun saveFeedback() {
        val arr = JSONArray()
        feedbackHistory.takeLast(500).forEach { f ->
            arr.put(JSONObject().apply {
                put("id", f.id)
                put("predictionType", f.predictionType)
                put("wasAccurate", f.wasAccurate)
                put("actualValue", f.actualValue)
                put("predictedValue", f.predictedValue)
                put("userCorrection", f.userCorrection)
                put("confidenceBefore", f.confidenceBefore)
                put("confidenceAfter", f.confidenceAfter)
                put("timestamp", f.timestamp)
            })
        }
        feedbackFile.writeText(arr.toString())
    }
    
    private fun recalculateMetrics() {
        val total = feedbackHistory.size
        val correct = feedbackHistory.count { it.wasAccurate }
        _metrics.value = _metrics.value.copy(
            totalPredictions = total,
            correctPredictions = correct,
            accuracy = if (total > 0) correct.toDouble() / total else 0.0,
            weeklyAccuracy = calculateWeeklyAccuracy(),
            trend = detectTrend(),
            trainingDataPoints = total
        )
        saveMetrics()
    }
    
    private fun calculateWeeklyAccuracy(): Double {
        val weekAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000
        val recentFeedback = feedbackHistory.filter { it.timestamp > weekAgo }
        if (recentFeedback.isEmpty()) return 0.0
        return recentFeedback.count { it.wasAccurate }.toDouble() / recentFeedback.size
    }
    
    private fun detectTrend(): String {
        val recent = feedbackHistory.takeLast(50)
        val older = feedbackHistory.dropLast(50).takeLast(50)
        if (recent.isEmpty() || older.isEmpty()) return "STABLE"
        val recentAcc = recent.count { it.wasAccurate }.toDouble() / recent.size
        val olderAcc = older.count { it.wasAccurate }.toDouble() / older.size
        return when {
            recentAcc > olderAcc + 0.05 -> "IMPROVING"
            recentAcc < olderAcc - 0.05 -> "DECLINING"
            else -> "STABLE"
        }
    }
    
    private fun generateVersion(accuracy: Double): String {
        val major = 1
        val minor = (accuracy * 10).toInt()
        val patch = feedbackHistory.size / 100
        return "$major.$minor.$patch"
    }
    
    private fun saveMetrics() {
        val obj = JSONObject().apply {
            put("totalPredictions", _metrics.value.totalPredictions)
            put("correctPredictions", _metrics.value.correctPredictions)
            put("accuracy", _metrics.value.accuracy)
            put("weeklyAccuracy", _metrics.value.weeklyAccuracy)
            put("trend", _metrics.value.trend)
            put("lastTrainedAt", _metrics.value.lastTrainedAt ?: 0)
            put("trainingDataPoints", _metrics.value.trainingDataPoints)
            put("modelVersion", _metrics.value.modelVersion)
        }
        metricsFile.writeText(obj.toString())
    }
    
    data class LearningResult(
        val success: Boolean,
        val message: String,
        val newConfidence: Double
    )
    
    data class PerformanceInsight(
        val totalPredictions: Int,
        val accuracy: Double,
        val averageConfidence: Double,
        val commonCorrections: Map<String, Int> = emptyMap()
    )
}
