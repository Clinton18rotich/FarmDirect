package com.farmdirect.app.ui.screens.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Weather", color = TextWhite) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)) }, containerColor = BackgroundLight) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Card(shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth().height(200.dp)) {
                Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(SkyBlue.copy(alpha = 0.3f), SkyBlue))), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("☀️", fontSize = 64.sp)
                        Text("24°C", fontSize = 48.sp, fontWeight = FontWeight.Bold)
                        Text("Sunny • Kitale", color = TextSecondary)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Green50), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("🧑‍🌾 AI Farming Advisory", fontWeight = FontWeight.Bold, color = PrimaryDarkGreen)
                    Spacer(Modifier.height(8.dp))
                    AdvisoryItem("✅","Good time for planting")
                    AdvisoryItem("💧","No irrigation needed")
                    AdvisoryItem("🐛","Pest risk: Low")
                }
            }
        }
    }
}

@Composable
fun AdvisoryItem(emoji: String, text: String) {
    Row(Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) { Text(emoji, fontSize = 16.sp); Spacer(Modifier.width(8.dp)); Text(text, color = PrimaryDarkGreen) }
}
