package com.farmdirect.app.util

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmdirect.app.domain.model.Crop
import com.farmdirect.app.ui.theme.*

class AIChatAssistant {
    
    data class AIResponse(
        val message: String,
        val suggestions: List<String> = emptyList(),
        val action: AIAction? = null
    )
    
    enum class AIAction {
        SHOW_PRICES, FIND_BUYERS, POST_LISTING, CHECK_WEATHER, TRACK_ORDER, CONTACT_SUPPORT
    }
    
    fun getResponse(userMessage: String, context: Map<String, Any> = emptyMap()): AIResponse {
        val msg = userMessage.lowercase()
        
        return when {
            msg.contains("price") || msg.contains("bei") -> AIResponse(
                "📊 Current market prices:\n\n🌽 Maize: KES 3,500/bag\n🫘 Beans: KES 8,000/bag\n🥔 Potatoes: KES 2,500/bag\n\nPrices updated today. Tap to see more crops!",
                listOf("Show all prices", "Set price alert", "Compare with last week"),
                AIAction.SHOW_PRICES
            )
            msg.contains("sell") || msg.contains("uza") -> AIResponse(
                "💰 Ready to sell? I can help you post your harvest!\n\nJust tell me:\n• What crop?\n• How much?\n• Your price?\n\nExample: \"500kg maize at KES 3,500\"",
                listOf("Post Maize", "Post Beans", "Post Potatoes"),
                AIAction.POST_LISTING
            )
            msg.contains("weather") || msg.contains("hewa") -> AIResponse(
                "🌤️ Weather for Kitale today:\n\n☀️ Sunny\n🌡️ 24°C (feels like 26°C)\n💧 Humidity: 65%\n🌬️ Wind: 12 km/h\n\nGood day for farming! ✅",
                listOf("7-day forecast", "Farming advisory", "Set weather alert"),
                AIAction.CHECK_WEATHER
            )
            msg.contains("buy") || msg.contains("nunua") -> AIResponse(
                "🛒 Looking to buy? I found 23 listings near you!\n\nTop picks:\n• Maize from John - ⭐4.8\n• Beans from Mary - ⭐4.6\n• Potatoes from Peter - ⭐4.9",
                listOf("Show all listings", "Filter by price", "Filter by distance"),
                AIAction.FIND_BUYERS
            )
            msg.contains("order") || msg.contains("agizo") -> AIResponse(
                "📦 Your recent orders:\n\n1. #1234 - Maize - DELIVERED ✅\n2. #1235 - Beans - IN TRANSIT 🚚\n\nWould you like to track a specific order?",
                listOf("Track #1235", "All orders", "Report issue"),
                AIAction.TRACK_ORDER
            )
            msg.contains("hello") || msg.contains("hi") || msg.contains("habari") -> AIResponse(
                "👋 Hello! I'm FarmBot, your AI farming assistant!\n\nI can help you:\n• Check crop prices\n• Find buyers\n• Post your harvest\n• Track deliveries\n• Check weather\n\nWhat would you like to do?",
                listOf("Check prices", "Find buyers", "Post harvest")
            )
            else -> AIResponse(
                "I understand you're asking about \"${userMessage.take(50)}\". Let me connect you with the right information.\n\nTry asking:\n• \"What's the price of maize?\"\n• \"Help me sell my harvest\"\n• \"Track my delivery\"\n• \"What's the weather today?\"",
                listOf("Check prices", "Find buyers", "Contact support")
            )
        }
    }
}

@Composable
fun AIChatBubble(
    message: String,
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit,
    onAction: () -> Unit
) {
    val pulseScale by rememberInfiniteTransition().animateFloat(1f, 1.05f, infiniteRepeatable(tween(2000), RepeatMode.Reverse), label = "pulse")
    
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable(onClick = onAction),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryGreen.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(PrimaryGreen).scale(pulseScale),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🤖", fontSize = 20.sp)
                }
                Spacer(Modifier.width(12.dp))
                Text("FarmBot AI", fontWeight = FontWeight.Bold, color = PrimaryGreen, fontSize = 16.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text(message, fontSize = 14.sp, lineHeight = 22.sp)
            
            if (suggestions.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    suggestions.take(3).forEach { suggestion ->
                        SuggestionChip(
                            onClick = { onSuggestionClick(suggestion) },
                            label = { Text(suggestion, fontSize = 12.sp) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = PrimaryGreen.copy(alpha = 0.1f)
                            )
                        )
                    }
                }
            }
        }
    }
}
