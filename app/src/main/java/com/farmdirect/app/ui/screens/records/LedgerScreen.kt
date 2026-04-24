package com.farmdirect.app.ui.screens.records

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LedgerScreen(onBack: () -> Unit, viewModel: RecordViewModel = hiltViewModel()) {
    val records by viewModel.records.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadRecords() }
    Scaffold(topBar = { TopAppBar(title = { Text("Blockchain Ledger", color = TextWhite) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)) }) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            items(records) { record ->
                Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).clickable { }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(if (record.verified) Icons.Default.VerifiedUser else Icons.Default.Pending, null, tint = if (record.verified) StatusSuccess else StatusWarning)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) { Text(record.description, fontWeight = FontWeight.Medium); Text("KES ${"%,.0f".format(record.amount)}", fontWeight = FontWeight.Bold, color = PrimaryGreen) }
                    }
                }
            }
        }
    }
}
