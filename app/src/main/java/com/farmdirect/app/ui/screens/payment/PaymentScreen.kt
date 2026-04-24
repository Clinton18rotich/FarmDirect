package com.farmdirect.app.ui.screens.payment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(orderId: String, onBack: () -> Unit, viewModel: PaymentViewModel = hiltViewModel()) {
    var phone by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Payment", color = TextWhite) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)) }, containerColor = BackgroundLight) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(20.dp))
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("💳", fontSize = 48.sp)
                    Text("Payment Summary", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(16.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Order Total", color = TextSecondary); Text("KES 5,000", fontWeight = FontWeight.SemiBold) }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Platform Fee (3%)", color = TextSecondary); Text("KES 150") }
                    Divider(Modifier.padding(vertical = 8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Total", fontWeight = FontWeight.Bold); Text("KES 5,150", fontWeight = FontWeight.Bold, color = PrimaryGreen, fontSize = 18.sp) }
                }
            }
            Spacer(Modifier.height(20.dp))
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(24.dp)) {
                    Text("Pay with M-Pesa", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("M-Pesa Phone") }, leadingIcon = { Text("📱") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), singleLine = true, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.initiatePayment(phone, 5150.0, orderId) }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen), enabled = !state.isLoading && phone.isNotEmpty()) {
                        if (state.isLoading) CircularProgressIndicator(color = TextWhite, modifier = Modifier.size(24.dp)) else Text("Pay KES 5,150", fontSize = 16.sp)
                    }
                    if (state.isSuccess) { Spacer(Modifier.height(12.dp)); Card(colors = CardDefaults.cardColors(containerColor = Green50)) { Row(Modifier.padding(12.dp)) { Icon(Icons.Default.CheckCircle, null, tint = PrimaryGreen); Spacer(Modifier.width(8.dp)); Text("Check your phone for M-Pesa prompt!", color = PrimaryDarkGreen) } } }
                }
            }
        }
    }
}
