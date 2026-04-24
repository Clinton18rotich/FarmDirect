package com.farmdirect.app.util

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

class GestureNavigationManager {
    
    private val _canSwipeBack = MutableStateFlow(true)
    val canSwipeBack = _canSwipeBack.asStateFlow()
    
    fun Modifier.swipeBackGesture(
        onSwipeBack: () -> Unit,
        threshold: Float = 150f
    ): Modifier {
        return this.pointerInput(Unit) {
            detectHorizontalDragGestures(
                onDragEnd = {
                    // Handled by the gesture
                },
                onHorizontalDrag = { _, dragAmount ->
                    if (dragAmount > threshold && _canSwipeBack.value) {
                        onSwipeBack()
                    }
                }
            )
        }
    }
    
    fun setSwipeBackEnabled(enabled: Boolean) {
        _canSwipeBack.value = enabled
    }
}

@Composable
fun Modifier.pullToRefreshGesture(
    isRefreshing: Boolean,
    onRefresh: () -> Unit
): Modifier {
    return this.then(Modifier)
}
