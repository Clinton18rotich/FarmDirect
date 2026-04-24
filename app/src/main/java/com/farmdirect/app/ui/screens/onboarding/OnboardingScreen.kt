package com.farmdirect.app.ui.screens.onboarding

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmdirect.app.ui.theme.*

data class OnboardingPage(
    val emoji: String,
    val title: String,
    val description: String,
    val gradient: List<androidx.compose.ui.graphics.Color>
)

val onboardingPages = listOf(
    OnboardingPage("🌾", "Sell Direct to Buyers", "Post your harvest and connect directly with buyers. No middlemen, fair prices.", listOf(PrimaryGreen, PrimaryDarkGreen)),
    OnboardingPage("🚚", "Track Your Deliveries", "Real-time GPS tracking for both farmers and buyers. Know where your produce is.", listOf(AccentGold, AccentOrange)),
    OnboardingPage("💰", "Get Paid Securely", "M-Pesa escrow protection. Payment released only when buyer confirms delivery.", listOf(SkyBlue, PrimaryGreen)),
    OnboardingPage("🤝", "Join the Community", "Connect with thousands of farmers. Share tips, get support, grow together.", listOf(PrimaryGreen, AccentGold))
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    var currentPage by remember { mutableIntStateOf(0) }
    
    LaunchedEffect(pagerState.currentPage) { currentPage = pagerState.currentPage }
    
    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val onboarding = onboardingPages[page]
            val scaleAnim by animateFloatAsState(if (currentPage == page) 1f else 0.8f, label = "scale")
            
            Box(
                modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(onboarding.gradient)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(40.dp).animateScale(scaleAnim)
                ) {
                    Text(onboarding.emoji, fontSize = 100.sp)
                    Spacer(Modifier.height(24.dp))
                    Text(onboarding.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 28.sp, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(12.dp))
                    Text(onboarding.description, color = TextWhite.copy(alpha = 0.85f), fontSize = 16.sp, textAlign = TextAlign.Center, lineHeight = 24.sp)
                }
            }
        }
        
        // Page indicators + button
        Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            // Dots
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(onboardingPages.size) { index ->
                    Box(
                        modifier = Modifier.size(if (currentPage == index) 24.dp else 8.dp, 8.dp)
                            .clip(CircleShape)
                            .background(if (currentPage == index) PrimaryGreen else TextSecondary.copy(alpha = 0.3f))
                            .animateWidth(if (currentPage == index) 24.dp else 8.dp)
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            
            // Button
            Button(
                onClick = {
                    if (currentPage < onboardingPages.size - 1) {
                        // Go to next page
                    } else {
                        onFinish()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text(
                    if (currentPage < onboardingPages.size - 1) "Next" else "Get Started 🌟",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            
            if (currentPage < onboardingPages.size - 1) {
                Spacer(Modifier.height(12.dp))
                TextButton(onClick = onFinish) {
                    Text("Skip", color = TextSecondary)
                }
            }
        }
    }
}

// Extension for animated width
@Composable
fun Modifier.animateWidth(target: androidx.compose.ui.unit.Dp): Modifier {
    val width by animateDpAsState(targetValue = target, label = "width")
    return this.width(width)
}
