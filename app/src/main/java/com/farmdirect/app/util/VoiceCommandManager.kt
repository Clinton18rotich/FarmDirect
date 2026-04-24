package com.farmdirect.app.util

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class VoiceCommandManager(private val context: Context) {
    
    private val _isListening = MutableStateFlow(false)
    val isListening = _isListening.asStateFlow()
    
    private val _lastCommand = MutableStateFlow<VoiceCommand?>(null)
    val lastCommand = _lastCommand.asStateFlow()
    
    data class VoiceCommand(
        val action: VoiceAction,
        val parameters: Map<String, String> = emptyMap(),
        val confidence: Float = 0.0f,
        val rawText: String = ""
    )
    
    enum class VoiceAction {
        CHECK_PRICES,
        FIND_BUYERS,
        POST_LISTING,
        TRACK_DELIVERY,
        WEATHER_UPDATE,
        CALL_SUPPORT,
        OPEN_CHAT,
        CHECK_ORDERS,
        CHECK_EARNINGS,
        START_CALL,
        UNKNOWN
    }
    
    private val commandPatterns = mapOf(
        "check price" to VoiceAction.CHECK_PRICES,
        "what.*price" to VoiceAction.CHECK_PRICES,
        "how much.*cost" to VoiceAction.CHECK_PRICES,
        "bei ya" to VoiceAction.CHECK_PRICES,
        "find buyer" to VoiceAction.FIND_BUYERS,
        "sell my" to VoiceAction.POST_LISTING,
        "post harvest" to VoiceAction.POST_LISTING,
        "track delivery" to VoiceAction.TRACK_DELIVERY,
        "where.*order" to VoiceAction.TRACK_DELIVERY,
        "weather" to VoiceAction.WEATHER_UPDATE,
        "hali ya hewa" to VoiceAction.WEATHER_UPDATE,
        "forecast" to VoiceAction.WEATHER_UPDATE,
        "call support" to VoiceAction.CALL_SUPPORT,
        "help me" to VoiceAction.CALL_SUPPORT,
        "open chat" to VoiceAction.OPEN_CHAT,
        "messages" to VoiceAction.OPEN_CHAT,
        "my orders" to VoiceAction.CHECK_ORDERS,
        "my earnings" to VoiceAction.CHECK_EARNINGS,
        "how much.*earn" to VoiceAction.CHECK_EARNINGS,
        "call" to VoiceAction.START_CALL
    )
    
    fun getVoiceIntent(): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US,sw-KE")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a command...")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
        }
    }
    
    fun processVoiceResult(results: List<String>, launcher: ActivityResultLauncher<Intent>? = null): VoiceCommand? {
        _isListening.value = false
        
        if (results.isEmpty()) return null
        
        val text = results.first().lowercase()
        
        // Find matching command
        val bestMatch = commandPatterns.entries
            .map { (pattern, action) ->
                val regex = Regex(".*${pattern}.*", RegexOption.IGNORE_CASE)
                val matches = regex.matches(text)
                action to if (matches) 0.9f else 0.0f
            }
            .maxByOrNull { it.second }
        
        val action = bestMatch?.first ?: VoiceAction.UNKNOWN
        val confidence = bestMatch?.second ?: 0.0f
        
        // Extract parameters
        val parameters = mutableMapOf<String, String>()
        
        // Extract crop type
        listOf("maize", "beans", "potatoes", "rice", "wheat", "coffee", "tea", "vegetables", "fruits").forEach { crop ->
            if (text.contains(crop)) parameters["crop"] = crop
        }
        
        // Extract quantity
        Regex("(\\d+)\\s*(kg|bags|tons)").find(text)?.let {
            parameters["quantity"] = it.groupValues[1]
            parameters["unit"] = it.groupValues[2]
        }
        
        // Extract price
        Regex("(\\d+)\\s*(kes|shillings)").find(text)?.let {
            parameters["price"] = it.groupValues[1]
        }
        
        // Extract location
        listOf("kitale", "eldoret", "nairobi", "nakuru", "bungoma", "kisumu").forEach { location ->
            if (text.contains(location)) parameters["location"] = location
        }
        
        val command = VoiceCommand(action, parameters, confidence, results.first())
        _lastCommand.value = command
        return command
    }
    
    fun startListening(launcher: ActivityResultLauncher<Intent>) {
        _isListening.value = true
        launcher.launch(getVoiceIntent())
    }
    
    fun stopListening() {
        _isListening.value = false
    }
    
    fun getSuggestedCommands(): List<String> {
        return listOf(
            "Check maize prices",
            "Find buyers near me",
            "Track my delivery",
            "What's the weather?",
            "Check my orders",
            "Call support",
            "Open messages"
        )
    }
}
