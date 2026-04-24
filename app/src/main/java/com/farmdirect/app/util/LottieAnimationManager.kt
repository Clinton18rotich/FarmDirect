package com.farmdirect.app.util

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmdirect.app.ui.theme.*

object LottieAnimationManager {
    
    @Composable
    fun SuccessAnimation(message: String = "Success!") {
        val scale by rememberInfiniteTransition().animateFloat(0.8f, 1.2f, infiniteRepeatable(tween(600), RepeatMode.Reverse), label = "scale")
        
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("✅", fontSize = 80.sp, modifier = Modifier.scale(scale))
                Spacer(Modifier.height(16.dp))
                Text(message, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PrimaryGreen, textAlign = TextAlign.Center)
            }
        }
    }
    
    @Composable
    fun LoadingAnimation(type: String = "default") {
        val emoji = when (type) {
            "harvest" -> "🌾"
            "delivery" -> "🚚"
            "payment" -> "💰"
            "weather" -> "🌤️"
            "chat" -> "💬"
            else -> "⏳"
        }
        
        val bounce by rememberInfiniteTransition().animateFloat(0f, -20f, infiniteRepeatable(tween(500), RepeatMode.Reverse), label = "bounce")
        val fade by rememberInfiniteTransition().animateFloat(0.3f, 1f, infiniteRepeatable(tween(800), RepeatMode.Reverse), label = "fade")
        
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(emoji, fontSize = 60.sp, modifier = Modifier.offset(y = bounce.dp))
                Spacer(Modifier.height(12.dp))
                Text("Loading...", color = TextSecondary.copy(alpha = fade))
            }
        }
    }
    
    @Composable
    fun RewardAnimation(amount: Double, reason: String) {
        val scale by rememberInfiniteTransition().animateFloat(1f, 1.3f, infiniteRepeatable(tween(400), RepeatMode.Reverse), label = "reward")
        val rotation by rememberInfiniteTransition().animateFloat(-10f, 10f, infiniteRepeatable(tween(300), RepeatMode.Reverse), label = "rotate")
        
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("💰", fontSize = 80.sp, modifier = Modifier.scale(scale))
                Spacer(Modifier.height(12.dp))
                Text("+ KES ${"%,.0f".format(amount)}", fontWeight = FontWeight.Bold, fontSize = 32.sp, color = AccentGold)
                Text(reason, color = TextSecondary, fontSize = 16.sp, textAlign = TextAlign.Center)
            }
        }
    }
    
    @Composable
    fun CelebrationOverlay(emoji: String = "🎉", message: String = "Congratulations!") {
        val particles = listOf("✨","⭐","💫","🌟","🎉","🎊","💎","🔥","🚀","💚")
        
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            particles.forEachIndexed { index, particle ->
                val xAnim by rememberInfiniteTransition().animateFloat(
                    (index * 37f) % 300f - 150f,
                    (index * 73f) % 300f - 150f,
                    infiniteRepeatable(tween(2000 + index * 300), RepeatMode.Reverse), label = "x$index"
                )
                val yAnim by rememberInfiniteTransition().animateFloat(
                    -200f, 200f,
                    infiniteRepeatable(tween(2500 + index * 200), RepeatMode.Reverse), label = "y$index"
                )
                val rot by rememberInfiniteTransition().animateFloat(0f, 360f, infiniteRepeatable(tween(1500 + index * 250), RepeatMode.Restart), label = "rot$index")
                
                Text(particle, fontSize = (16 + index % 4 * 8).sp, modifier = Modifier.offset(x = xAnim.dp, y = yAnim.dp))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(emoji, fontSize = 80.sp)
                Spacer(Modifier.height(12.dp))
                Text(message, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = PrimaryGreen, textAlign = TextAlign.Center)
            }
        }
    }
    
    @Composable
    fun EmptyStateAnimation(type: String = "empty") {
        val (emoji, title, subtitle) = when (type) {
            "no_crops" -> Triple("🌾", "No Crops Yet", "Post your first harvest to start selling!")
            "no_orders" -> Triple("📦", "No Orders Yet", "Your orders will appear here")
            "no_messages" -> Triple("💬", "No Messages", "Start a conversation with a buyer")
            "no_notifications" -> Triple("🔔", "All Caught Up!", "You have no new notifications")
            "no_search" -> Triple("🔍", "Nothing Found", "Try a different search term")
            "offline" -> Triple("📡", "You're Offline", "Check your internet connection")
            else -> Triple("📭", "Nothing Here", "Content will appear soon")
        }
        
        val float by rememberInfiniteTransition().animateFloat(0f, -10f, infiniteRepeatable(tween(2000), RepeatMode.Reverse), label = "float")
        
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                Text(emoji, fontSize = 64.sp, modifier = Modifier.offset(y = float.dp))
                Spacer(Modifier.height(16.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                Text(subtitle, color = TextSecondary, fontSize = 14.sp, textAlign = TextAlign.Center)
            }
        }
    }
}
