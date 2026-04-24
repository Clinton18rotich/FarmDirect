package com.farmdirect.app.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var showConfirmDialog by remember { mutableStateOf(false) }
    var typedConfirmation by remember { mutableStateOf("") }
    
    // IMMUTABLE CREATOR PROTECTION
    val creatorPhone = "+254700000000" // Replace with your actual phone
    val prefs = context.getSharedPreferences("farmdirect_prefs", Context.MODE_PRIVATE)
    val currentPhone = prefs.getString("user_phone", "") ?: ""
    val isCreator = currentPhone == creatorPhone
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Delete Account", color = TextWhite) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        },
        containerColor = BackgroundLight
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))
            
            if (isCreator) {
                // Creator protection
                Icon(Icons.Default.Shield, null, tint = PrimaryGreen, modifier = Modifier.size(80.dp))
                Spacer(Modifier.height(16.dp))
                Text("🔒 Creator Account Protected", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = PrimaryGreen, textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
                Text(
                    "This is the FarmDirect creator account. It cannot be deleted.\n\nCreated by Clinton Rotich\nFounder & CEO, FarmDirect Technologies Ltd",
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
                Spacer(Modifier.height(24.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Green50)
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = AccentGold)
                        Spacer(Modifier.width(12.dp))
                        Text("Thank you for building FarmDirect! 🌾", color = PrimaryDarkGreen, fontWeight = FontWeight.Medium)
                    }
                }
            } else {
                // Regular user deletion
                Icon(Icons.Default.Warning, null, tint = StatusError, modifier = Modifier.size(80.dp))
                Spacer(Modifier.height(16.dp))
                Text("Delete Your Account", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = StatusError, textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
                Text(
                    "This action cannot be undone. All your data including:\n\n• Listings\n• Transaction history\n• Messages\n• Account details\n\nwill be permanently deleted.",
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StatusError)
                ) {
                    Icon(Icons.Default.Delete, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Delete My Account", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
            
            if (showConfirmDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    title = { Text("Final Confirmation", fontWeight = FontWeight.Bold) },
                    text = {
                        Column {
                            Text("Type DELETE to confirm:", fontWeight = FontWeight.Medium)
                            Spacer(Modifier.height(12.dp))
                            OutlinedTextField(
                                value = typedConfirmation,
                                onValueChange = { typedConfirmation = it },
                                placeholder = { Text("Type DELETE here") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (typedConfirmation == "DELETE") {
                                    // Perform deletion
                                }
                                showConfirmDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StatusError),
                            enabled = typedConfirmation == "DELETE"
                        ) {
                            Text("Permanently Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showConfirmDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}
