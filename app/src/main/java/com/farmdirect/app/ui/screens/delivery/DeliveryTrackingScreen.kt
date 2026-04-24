package com.farmdirect.app.ui.screens.delivery

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryTrackingScreen(deliveryId: String, onBack: () -> Unit, viewModel: DeliveryViewModel = hiltViewModel()) {
    val delivery by viewModel.delivery.collectAsState()
    LaunchedEffect(deliveryId) { viewModel.loadDelivery(deliveryId) }
    val pulseScale by rememberInfiniteTransition().animateFloat(1f, 1.3f, infiniteRepeatable(tween(1000), RepeatMode.Reverse), label = "pulse")

    Scaffold(topBar = { TopAppBar(title = { Text("Track Delivery", color = TextWhite) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)) }) { padding ->
        delivery?.let { d ->
            Column(Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                    Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Delivery Progress", fontWeight = FontWeight.Bold)
                        LinearProgressIndicator(progress = 0.45f, modifier = Modifier.fillMaxWidth().height(8.dp).padding(vertical = 12.dp).clip(RoundedCornerShape(4.dp)), color = PrimaryGreen, trackColor = Green100)
                        Text("45% Complete", color = PrimaryGreen, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(48.dp).clip(CircleShape).background(PrimaryGreen), contentAlignment = Alignment.Center) { Text("🏍️", fontSize = 24.sp) }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) { Text(d.riderName.ifEmpty { "Finding rider..." }, fontWeight = FontWeight.Bold); Text(d.vehicleType, color = TextSecondary, fontSize = 14.sp) }
                        Box(Modifier.size(40.dp).clip(CircleShape).background(Green50), contentAlignment = Alignment.Center) { Icon(Icons.Default.Phone, null, tint = PrimaryGreen, modifier = Modifier.size(20.dp)) }
                    }
                }
            }
        }
    }
}
