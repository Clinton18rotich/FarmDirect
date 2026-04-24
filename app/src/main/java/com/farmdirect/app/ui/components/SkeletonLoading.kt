package com.farmdirect.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.farmdirect.app.ui.theme.*

@Composable
fun CropListSkeleton(count: Int = 6) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(count) {
            CropCardSkeleton()
        }
    }
}

@Composable
fun CropCardSkeleton() {
    val shimmerColors = listOf(
        Green50.copy(alpha = 0.6f),
        Green100.copy(alpha = 0.3f),
        Green50.copy(alpha = 0.6f)
    )
    
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing), RepeatMode.Restart),
        label = "shimmer"
    )
    
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 200f, 0f),
        end = Offset(translateAnim, 0f)
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .height(100.dp)
            .background(brush)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Box(modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)).background(brush))
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier.fillMaxWidth(0.7f).height(16.dp).clip(RoundedCornerShape(4.dp)).background(brush))
                Spacer(Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth(0.5f).height(12.dp).clip(RoundedCornerShape(4.dp)).background(brush))
                Spacer(Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth(0.3f).height(12.dp).clip(RoundedCornerShape(4.dp)).background(brush))
            end
        }
    }
}

@Composable
fun ProfileSkeleton() {
    val shimmerColors = listOf(Green50.copy(alpha = 0.6f), Green100.copy(alpha = 0.3f), Green50.copy(alpha = 0.6f))
    val brush = Brush.linearGradient(colors = shimmerColors, start = Offset.Zero, end = Offset.Infinite)
    
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Box(modifier = Modifier.size(100.dp).clip(CircleShape).background(brush))
        Spacer(Modifier.height(12.dp))
        Box(modifier = Modifier.width(150.dp).height(20.dp).clip(RoundedCornerShape(4.dp)).background(brush))
        Spacer(Modifier.height(8.dp))
        Box(modifier = Modifier.width(100.dp).height(14.dp).clip(RoundedCornerShape(4.dp)).background(brush))
        Spacer(Modifier.height(20.dp))
        repeat(5) {
            Box(modifier = Modifier.fillMaxWidth().height(50.dp).padding(vertical = 4.dp).clip(RoundedCornerShape(8.dp)).background(brush))
        }
    }
}

@Composable
fun AnalyticsSkeleton() {
    val shimmerColors = listOf(Green50.copy(alpha = 0.6f), Green100.copy(alpha = 0.3f), Green50.copy(alpha = 0.6f))
    val brush = Brush.linearGradient(colors = shimmerColors, start = Offset.Zero, end = Offset.Infinite)
    
    Column(modifier = Modifier.padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            repeat(4) { Box(modifier = Modifier.weight(1f).height(80.dp).clip(RoundedCornerShape(12.dp)).background(brush)) }
        }
        Spacer(Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp)).background(brush))
        Spacer(Modifier.height(16.dp))
        repeat(5) {
            Box(modifier = Modifier.fillMaxWidth().height(40.dp).padding(vertical = 4.dp).clip(RoundedCornerShape(8.dp)).background(brush))
        }
    }
}
