package com.farmdirect.app.ui.screens.finance

import androidx.compose.foundation.clickable
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
fun FinanceDashboardScreen(onBack: () -> Unit, viewModel: FinanceViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("Farm Wallet", color = TextWhite) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)) }, containerColor = BackgroundLight) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp)) {
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                Column(Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Savings, null, tint = AccentGold, modifier = Modifier.size(32.dp)); Spacer(Modifier.width(12.dp)); Column { Text("Savings", color = TextSecondary, fontSize = 14.sp); Text("KES ${"%,.0f".format(state.summary?.savingsBalance ?: 0.0)}", fontWeight = FontWeight.Bold, fontSize = 32.sp, color = PrimaryGreen) } }
                    Text("Credit Score: ${state.summary?.creditScore ?: 0}", color = AccentGold, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Quick Actions", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(Modifier.weight(1f).clickable { }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Green50)) { Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) { Text("🏦", fontSize = 32.sp); Text("Loan", fontWeight = FontWeight.Medium) } }
                Card(Modifier.weight(1f).clickable { }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Gold50)) { Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) { Text("💰", fontSize = 32.sp); Text("Save", fontWeight = FontWeight.Medium) } }
                Card(Modifier.weight(1f).clickable { }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = SkyBlue.copy(alpha = 0.1f))) { Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) { Text("🛡️", fontSize = 32.sp); Text("Insure", fontWeight = FontWeight.Medium) } }
            }
        }
    }
}
