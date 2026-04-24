package com.farmdirect.app.ui.screens.crops

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropDetailScreen(cropId: String, onBack: () -> Unit, onChatClick: (String) -> Unit, onBuyClick: (String) -> Unit, viewModel: CropViewModel = hiltViewModel()) {
    val state by viewModel.cropDetailState.collectAsState()
    LaunchedEffect(cropId) { viewModel.loadCropDetail(cropId) }
    Scaffold(topBar = { TopAppBar(title = { Text("Crop Details", color = TextWhite) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)) }, containerColor = BackgroundLight) { padding ->
        state.crop?.let { crop ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
                AsyncImage(model = crop.images.firstOrNull() ?: "", contentDescription = null, modifier = Modifier.fillMaxWidth().height(250.dp), contentScale = ContentScale.Crop)
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(crop.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.weight(1f)); Text("KES ${"%,.0f".format(crop.totalPrice)}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium, color = PrimaryGreen) }
                    Text("${crop.quantity} ${crop.unit} • ${crop.quality}", color = TextSecondary)
                    Spacer(Modifier.height(12.dp))
                    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(model = "", contentDescription = null, modifier = Modifier.size(48.dp).clip(RoundedCornerShape(24.dp)))
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) { Text(crop.farmerName, fontWeight = FontWeight.Bold); Text("⭐ ${crop.farmerRating} • 📍 ${crop.farmerLocation.take(25)}", color = TextSecondary, fontSize = 12.sp) }
                            IconButton(onClick = { onChatClick(crop.farmerId) }) { Icon(Icons.Default.Chat, null, tint = PrimaryGreen) }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(crop.description.ifEmpty { "No description." }, color = TextSecondary)
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = { onBuyClick(crop.id) }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)) { Icon(Icons.Default.ShoppingCart, null); Spacer(Modifier.width(8.dp)); Text("Buy Now", fontSize = 16.sp) }
                }
            }
        } ?: Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { if (state.isLoading) CircularProgressIndicator(color = PrimaryGreen) else Text(state.error ?: "", color = StatusError) }
    }
}
