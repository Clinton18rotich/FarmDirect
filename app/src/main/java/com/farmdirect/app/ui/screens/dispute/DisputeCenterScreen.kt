package com.farmdirect.app.ui.screens.dispute

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun DisputeCenterScreen(onBack: () -> Unit, viewModel: DisputeViewModel = hiltViewModel()) {
    val disputes by viewModel.disputes.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadDisputes() }
    Scaffold(topBar = { TopAppBar(title = { Text("Resolution Center", color = TextWhite) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)) }) { padding ->
        if (disputes.isEmpty()) Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("🤝", fontSize = 48.sp); Text("No disputes", color = TextSecondary) } }
        else LazyColumn(Modifier.padding(padding)) {
            items(disputes) { dispute ->
                Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).clickable { }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) { Text("#${dispute.caseNumber}", fontWeight = FontWeight.Bold); Text(dispute.description, color = TextSecondary, fontSize = 14.sp, maxLines = 1) }
                        Surface(shape = RoundedCornerShape(6.dp), color = StatusWarning.copy(alpha = 0.1f)) { Text(dispute.status, Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = StatusWarning, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                    }
                }
            }
        }
    }
}
