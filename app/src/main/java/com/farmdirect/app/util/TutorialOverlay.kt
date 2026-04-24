package com.farmdirect.app.util

import android.content.Context
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmdirect.app.ui.theme.*

class TutorialOverlay(private val context: Context) {
    
    private val prefs = context.getSharedPreferences("farmdirect_prefs", Context.MODE_PRIVATE)
    
    data class TutorialStep(
        val key: String,
        val title: String,
        val description: String,
        val emoji: String,
        val highlightArea: String  // Which part of screen to highlight
    )
    
    val tutorialSteps = listOf(
        TutorialStep("home_intro", "Welcome to FarmDirect! 🌾", "Find fresh produce from farmers near you. Swipe through listings and tap to view details.", "👋", "listings"),
        TutorialStep("search", "Search Made Easy 🔍", "Tap the search bar to find specific crops, livestock, or products by name or category.", "🔍", "search_bar"),
        TutorialStep("post", "Sell Your Harvest 💰", "Tap the + button to post your crops. Add photos, set your price, and reach buyers instantly!", "➕", "post_button"),
        TutorialStep("chat", "Chat with Buyers 💬", "Tap the chat icon to message buyers directly. Negotiate prices and arrange delivery.", "💬", "chat_tab"),
        TutorialStep("profile", "Your Profile 👤", "Track your sales, ratings, and earnings. Build your reputation as a trusted farmer.", "👤", "profile_tab")
    )
    
    fun shouldShowTutorial(): Boolean {
        return !prefs.getBoolean("tutorial_completed", false)
    }
    
    fun markTutorialComplete() {
        prefs.edit().putBoolean("tutorial_completed", true).apply()
    }
    
    fun resetTutorial() {
        prefs.edit().putBoolean("tutorial_completed", false).apply()
    }
}

@Composable
fun TutorialBubble(
    step: TutorialOverlay.TutorialStep,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    isLast: Boolean
) {
    Box(
        modifier = Modifier.fillMaxSize().background(TextPrimary.copy(alpha = 0.7f)).clickable { },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(32.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(step.emoji, fontSize = 48.sp)
                Spacer(Modifier.height(12.dp))
                Text(step.title, fontWeight = FontWeight.Bold, fontSize = 20.sp, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                Text(step.description, color = TextSecondary, textAlign = TextAlign.Center, lineHeight = 22.sp)
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onSkip) { Text("Skip All") }
                    Button(
                        onClick = onNext,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isLast) "Let's Go! 🚀" else "Next →")
                    }
                }
            }
        }
    }
}
