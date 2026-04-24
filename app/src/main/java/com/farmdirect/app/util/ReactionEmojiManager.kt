package com.farmdirect.app.util

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object ReactionEmojiManager {
    
    val reactions = listOf(
        Reaction("👍", "Like"),
        Reaction("❤️", "Love"),
        Reaction("🔥", "Fire"),
        Reaction("👏", "Applaud"),
        Reaction("😊", "Happy"),
        Reaction("💚", "Green"),
        Reaction("🌾", "Harvest"),
        Reaction("💰", "Rich")
    )
    
    data class Reaction(
        val emoji: String,
        val name: String
    )
    
    @Composable
    fun ReactionPicker(
        onReactionSelected: (Reaction) -> Unit,
        onDismiss: () -> Unit
    ) {
        Surface(
            modifier = Modifier.padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                reactions.forEach { reaction ->
                    ReactionButton(reaction = reaction, onSelected = {
                        onReactionSelected(reaction)
                        onDismiss()
                    })
                }
            }
        }
    }
    
    @Composable
    fun ReactionButton(reaction: Reaction, onSelected: () -> Unit) {
        var pressed by remember { mutableStateOf(false) }
        val scale by animateFloatAsState(if (pressed) 1.5f else 1f, animationSpec = spring(dampingRatio = 0.5f), label = "scale")
        
        Surface(
            modifier = Modifier
                .clickable {
                    pressed = true
                    onSelected()
                },
            shape = RoundedCornerShape(12.dp),
            color = if (pressed) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ) {
            Text(
                reaction.emoji,
                fontSize = 28.sp,
                modifier = Modifier.padding(8.dp).scale(scale)
            )
        }
    }
    
    @Composable
    fun ReactionDisplay(reactions: Map<String, Int>) {
        if (reactions.isEmpty()) return
        
        Row(
            modifier = Modifier.padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            reactions.entries.take(5).forEach { (emoji, count) ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(emoji, fontSize = 14.sp)
                        if (count > 1) {
                            Text(" $count", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
