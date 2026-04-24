package com.farmdirect.app.util

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

object MotionTransitionManager {
    
    @Composable
    fun FadeInOut(
        visible: Boolean,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(200)),
            modifier = modifier
        ) {
            content()
        }
    }
    
    @Composable
    fun SlideInFromRight(
        visible: Boolean,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)),
            exit = slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(200)),
            modifier = modifier
        ) {
            content()
        }
    }
    
    @Composable
    fun SlideInFromBottom(
        visible: Boolean,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)),
            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(200)),
            modifier = modifier
        ) {
            content()
        }
    }
    
    @Composable
    fun ScaleIn(
        visible: Boolean,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = scaleIn(animationSpec = tween(300)),
            exit = scaleOut(animationSpec = tween(200)),
            modifier = modifier
        ) {
            content()
        }
    }
    
    @Composable
    fun BounceIn(
        visible: Boolean,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = scaleIn(
                animationSpec = spring(dampingRatio = 0.5f, stiffness = 200f),
                initialScale = 0.5f
            ),
            exit = fadeOut(animationSpec = tween(100)),
            modifier = modifier
        ) {
            content()
        }
    }
}
