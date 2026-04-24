package com.farmdirect.app.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmdirect.app.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit, onNavigateToHome: () -> Unit, viewModel: SplashViewModel = hiltViewModel()) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(targetValue = if (startAnimation) 1f else 0f, animationSpec = tween(1000), label = "alpha")
    LaunchedEffect(key1 = true) { startAnimation = true; delay(2000); if (viewModel.isLoggedIn()) onNavigateToHome() else onNavigateToLogin() }
    Box(modifier = Modifier.fillMaxSize().background(brush = Brush.verticalGradient(SunriseGradient)), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.scale(alphaAnim.value)) {
            Text("🌾", fontSize = 80.sp)
            Text("FarmDirect", fontSize = 42.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite)
            Text("Connect Direct • Farm to Table", fontSize = 14.sp, color = TextWhite.copy(alpha = 0.8f))
        }
    }
}
