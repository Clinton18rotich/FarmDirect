package com.farmdirect.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmdirect.app.ui.theme.*

@Composable
fun LoadingIndicator(message: String = "Loading...") {
    val alpha by rememberInfiniteTransition().animateFloat(initialValue = 0.3f, targetValue = 1f, animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse), label = "alpha")
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = PrimaryGreen)
            Spacer(modifier = Modifier.height(16.dp))
            Text(message, color = TextSecondary, fontSize = 14.sp, modifier = Modifier.alpha(alpha))
        }
    }
}
