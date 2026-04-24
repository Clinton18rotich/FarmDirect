package com.farmdirect.app.ui.screens.offline

import androidx.compose.animation.core.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfflineScreen(
    onBack: () -> Unit,
    viewModel: OfflineViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    val pulseScale by rememberInfiniteTransition().animateFloat(1f, 1.05f, infiniteRepeatable(tween(1500), RepeatMode.Reverse), label = "pulse")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Offline Mode", color = TextWhite) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        },
        containerColor = BackgroundLight
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            // Status Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (state.isOnline) Green50 else StatusWarning.copy(alpha = 0.1f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        if (state.isOnline) "📶" else "📡",
                        fontSize = 48.sp,
                        modifier = if (!state.isOnline) Modifier.animateScale(pulseScale) else Modifier
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        if (state.isOnline) "You are Online" else "You are Offline",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        color = if (state.isOnline) PrimaryGreen else StatusWarning
                    )
                    Text(
                        if (state.isOnline) "All features available" else "Using cached data",
                        color = TextSecondary
                    )
                }
            }
            
            Spacer(Modifier.height(20.dp))
            
            // Download for Offline
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Download, null, tint = PrimaryGreen, modifier = Modifier.size(28.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("Download for Offline", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Save listings, prices, and weather to use without internet", color = TextSecondary, fontSize = 14.sp)
                    
                    if (state.downloadProgress < 1f) {
                        Spacer(Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = state.downloadProgress,
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = PrimaryGreen,
                            trackColor = Green100
                        )
                        Text("${(state.downloadProgress * 100).toInt()}% downloaded", color = TextSecondary, fontSize = 12.sp)
                    }
                    
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.downloadForOffline() },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        Icon(Icons.Default.Download, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Download Now")
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Sync Queue
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Sync, null, tint = if (state.pendingSyncCount > 0) StatusWarning else TextSecondary, modifier = Modifier.size(28.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("Pending Sync", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.weight(1f))
                        if (state.pendingSyncCount > 0) {
                            Badge(containerColor = StatusWarning) { Text("${state.pendingSyncCount}") }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        if (state.pendingSyncCount == 0) "All actions synced ✅" else "${state.pendingSyncCount} actions waiting to sync",
                        color = TextSecondary, fontSize = 14.sp
                    )
                    
                    if (state.pendingSyncCount > 0 && state.isOnline) {
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.syncPendingActions() },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = if (state.isSyncing) TextSecondary else StatusInfo)
                        ) {
                            if (state.isSyncing) {
                                CircularProgressIndicator(color = TextWhite, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.Sync, null)
                                Spacer(Modifier.width(8.dp))
                                Text("Sync Now")
                            }
                        }
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Cache Info
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Storage", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Cached data", color = TextSecondary)
                        Text(viewModel.formatCacheSize(state.cacheSize), fontWeight = FontWeight.Medium)
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Last updated", color = TextSecondary)
                        Text(viewModel.formatCacheAge(state.cacheAge), fontWeight = FontWeight.Medium)
                    }
                    if (state.lastSyncTime != null) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Last sync", color = TextSecondary)
                            Text(java.text.SimpleDateFormat("MMM dd, HH:mm", java.util.Locale.getDefault()).format(java.util.Date(state.lastSyncTime)), fontWeight = FontWeight.Medium)
                        }
                    }
                    
                    Spacer(Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { viewModel.clearOfflineData() },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusError)
                    ) {
                        Icon(Icons.Default.Delete, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Clear Offline Data")
                    }
                }
            }
        }
    }
}
