package com.farmdirect.app.util

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import kotlin.random.Random

class CelebrationManager {
    
    data class ConfettiPiece(
        val emoji: String,
        val x: Float,
        val y: Float,
        val rotation: Float,
        val scale: Float
    )
    
    fun generateConfetti(
        type: String = "sale",
        count: Int = 30
    ): List<ConfettiPiece> {
        val emojis = when (type) {
            "sale" -> listOf("💰", "🎉", "⭐", "🌾", "🤝", "💚", "🚀", "✨", "🏆", "💎")
            "achievement" -> listOf("🏆", "⭐", "👑", "🎖️", "🌟", "💫", "🔥", "✨", "🎯", "💪")
            "referral" -> listOf("🤝", "👥", "🎉", "💚", "🌱", "✨", "🎊", "💫", "⭐", "🚀")
            "harvest" -> listOf("🌽", "🌾", "🥔", "🫘", "🍅", "🧅", "🥬", "🌶️", "🍌", "🥑")
            else -> listOf("🎉", "✨", "⭐", "💫", "🌟")
        }
        
        return (0 until count).map {
            ConfettiPiece(
                emoji = emojis[Random.nextInt(emojis.size)],
                x = Random.nextFloat(),
                y = -Random.nextFloat() * 0.5f,
                rotation = Random.nextFloat() * 360f,
                scale = 0.5f + Random.nextFloat() * 1.5f
            )
        }
    }
    
    fun getCelebrationMessage(type: String): String {
        return when (type) {
            "first_sale" -> "🎉 Congratulations! Your first sale is complete! 🌾"
            "achievement" -> "🏆 Achievement Unlocked! You're amazing! ⭐"
            "referral" -> "🤝 Referral bonus earned! KES 100 added to your wallet! 💰"
            "premium" -> "👑 Welcome to FarmDirect Premium! Enjoy your benefits! 🚀"
            "milestone" -> "💯 Milestone reached! Keep growing! 🌱"
            else -> "🎉 Congratulations! 🌟"
        }
    }
}

@Composable
fun ConfettiAnimation(confetti: List<CelebrationManager.ConfettiPiece>) {
    val animatedConfetti = confetti.map { piece ->
        val infiniteTransition = rememberInfiniteTransition(label = "confetti_${piece.emoji}")
        val yAnim by infiniteTransition.animateFloat(
            initialValue = piece.y,
            targetValue = 1.5f,
            animationSpec = infiniteRepeatable(tween(3000 + Random.nextInt(2000)), RepeatMode.Restart),
            label = "y"
        )
        val rotAnim by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 720f,
            animationSpec = infiniteRepeatable(tween(3000 + Random.nextInt(2000)), RepeatMode.Restart),
            label = "rotation"
        )
        piece.copy(y = yAnim, rotation = rotAnim)
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        animatedConfetti.forEach { piece ->
            Text(
                text = piece.emoji,
                fontSize = (20 * piece.scale).sp,
                modifier = Modifier
                    .offset(
                        x = (piece.x * 400 - 30).dp,
                        y = (piece.y * 800 - 100).dp
                    )
            )
        }
    }
}
