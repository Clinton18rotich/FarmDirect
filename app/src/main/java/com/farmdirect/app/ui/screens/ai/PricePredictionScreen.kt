package com.farmdirect.app.ui.screens.ai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PricePredictionScreen(onBack: () -> Unit, viewModel: AIViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    var selectedCrop by remember { mutableStateOf("Maize") }

    Scaffold(topBar = { TopAppBar(title = { Text("AI Price Predictor", color = TextWhite) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
            Text("Select Crop", fontWeight = FontWeight.Bold)
            LazyRow(Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) { items(listOf("Maize","Beans","Potatoes","Rice","Wheat","Coffee")) { crop -> FilterChip(selected = selectedCrop == crop, onClick = { selectedCrop = crop; viewModel.predictPrice(crop) }, label = { Text(crop) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PrimaryGreen)) } }
            Spacer(Modifier.height(20.dp))
            state.pricePrediction?.let { p ->
                Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                    Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔮", fontSize = 48.sp)
                        Text("Price Prediction", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(16.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("Current", color = TextSecondary, fontSize = 12.sp); Text("KES ${"%,.0f".format(p.currentPrice)}", fontWeight = FontWeight.Bold, fontSize = 20.sp) }; Icon(Icons.Default.ArrowForward, null, tint = PrimaryGreen); Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("Predicted", color = TextSecondary, fontSize = 12.sp); Text("KES ${"%,.0f".format(p.predictedPrice)}", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = StatusSuccess) } }
                        Spacer(Modifier.height(12.dp))
                        Card(colors = CardDefaults.cardColors(containerColor = Green50), shape = RoundedCornerShape(12.dp)) { Row(Modifier.padding(16.dp)) { Icon(Icons.Default.Lightbulb, null, tint = PrimaryGreen); Spacer(Modifier.width(12.dp)); Text(p.recommendation, color = PrimaryDarkGreen) } }
                    }
                }
            }
        }
    }
}
