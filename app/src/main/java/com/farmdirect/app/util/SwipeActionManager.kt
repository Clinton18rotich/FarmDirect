package com.farmdirect.app.util

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SwipeToAction(
    onSwipeLeft: (() -> Unit)? = null,
    onSwipeRight: (() -> Unit)? = null,
    leftAction: SwipeAction? = null,
    rightAction: SwipeAction? = null,
    content: @Composable () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val dismissThreshold = 150f
    
    Box(modifier = Modifier.clipToBounds()) {
        // Background actions
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            leftAction?.let { action ->
                SwipeActionIndicator(
                    action = action,
                    visible = offsetX > 50f,
                    isActive = offsetX > dismissThreshold
                )
            }
            rightAction?.let { action ->
                SwipeActionIndicator(
                    action = action,
                    visible = offsetX < -50f,
                    isActive = offsetX < -dismissThreshold
                )
            }
        }
        
        // Foreground content
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX > dismissThreshold) onSwipeRight?.invoke()
                            else if (offsetX < -dismissThreshold) onSwipeLeft?.invoke()
                            offsetX = 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            offsetX = (offsetX + dragAmount).coerceIn(-300f, 300f)
                        }
                    )
                }
        ) {
            content()
        }
    }
}

data class SwipeAction(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String,
    val color: Color
)

@Composable
fun SwipeActionIndicator(action: SwipeAction, visible: Boolean, isActive: Boolean) {
    val alpha by animateFloatAsState(if (visible) 1f else 0f, label = "alpha")
    val scale by animateFloatAsState(if (isActive) 1.2f else 1f, label = "scale")
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp).alpha(alpha).scale(scale)
    ) {
        Icon(action.icon, contentDescription = action.label, tint = action.color, modifier = Modifier.size(24.dp))
        Spacer(Modifier.height(4.dp))
        Text(action.label, color = action.color, fontSize = 11.sp)
    }
}

// Pre-defined swipe actions
val SwipeActionDelete = SwipeAction(Icons.Default.Delete, "Delete", Color(0xFFE53935))
val SwipeActionArchive = SwipeAction(Icons.Default.Archive, "Archive", Color(0xFFFF9800))
val SwipeActionFavorite = SwipeAction(Icons.Default.Favorite, "Save", Color(0xFFE91E63))
val SwipeActionShare = SwipeAction(Icons.Default.Share, "Share", Color(0xFF2196F3))
val SwipeActionCall = SwipeAction(Icons.Default.Call, "Call", Color(0xFF4CAF50))
val SwipeActionChat = SwipeAction(Icons.Default.Chat, "Chat", Color(0xFF2E7D32))

fun Modifier.clipToBounds() = this.then(Modifier)
