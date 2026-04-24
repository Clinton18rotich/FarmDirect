package com.farmdirect.app.ui.screens.profile

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

@Composable
fun ProfileScreen(onLogout: () -> Unit, viewModel: ProfileViewModel = hiltViewModel()) {
    Column(Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(40.dp))
        Box(Modifier.size(100.dp).clip(CircleShape).background(PrimaryGreen), contentAlignment = Alignment.Center) { Text("👨‍🌾", fontSize = 48.sp) }
        Spacer(Modifier.height(12.dp))
        Text("John Kimani", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium)
        Text("Kitale • ⭐ 4.8", color = TextSecondary)
        Spacer(Modifier.height(20.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            StatItem("234","Sales"); StatItem("189","Orders"); StatItem("4.8","Rating")
        }
        Spacer(Modifier.height(20.dp))
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite), modifier = Modifier.fillMaxWidth()) {
            Column {
                ProfileMenuItem(Icons.Default.ShoppingBag, "My Orders") {}
                ProfileMenuItem(Icons.Default.Inventory, "My Listings") {}
                ProfileMenuItem(Icons.Default.Star, "Reviews") {}
                Divider()
                ProfileMenuItem(Icons.Default.Settings, "Settings") {}
                ProfileMenuItem(Icons.Default.Help, "Help") {}
                Divider()
                ProfileMenuItem(Icons.Default.ExitToApp, "Sign Out", true) { viewModel.logout(); onLogout() }
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PrimaryGreen); Text(label, color = TextSecondary, fontSize = 12.sp) }
}

@Composable
fun ProfileMenuItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, isDestructive: Boolean = false, onClick: () -> Unit) {
    Surface(onClick = onClick, modifier = Modifier.fillMaxWidth(), color = SurfaceWhite) {
        Row(Modifier.padding(horizontal = 20.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = if (isDestructive) StatusError else PrimaryGreen)
            Spacer(Modifier.width(16.dp))
            Text(title, color = if (isDestructive) StatusError else TextPrimary)
        }
    }
}
