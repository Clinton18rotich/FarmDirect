package com.farmdirect.app.ui.screens.legal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy", color = TextWhite) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        },
        containerColor = BackgroundLight
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(24.dp)
        ) {
            Text("Privacy Policy", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = PrimaryGreen)
            Text("Last updated: January 2026", color = TextSecondary, fontSize = 13.sp)
            Spacer(Modifier.height(20.dp))
            
            Section("1. Information We Collect") {
                Text("• Location data (with permission) for delivery tracking\n• Photos you upload for crop listings\n• Phone number for authentication\n• Transaction history for your records\n• Device information for app optimization")
            }
            Section("2. How We Use Your Data") {
                Text("• Connect you with buyers and sellers\n• Process payments securely\n• Send delivery updates\n• Improve our AI recommendations\n• Prevent fraud and ensure safety")
            }
            Section("3. Data Protection") {
                Text("• All data encrypted in transit and at rest\n• Payment processed by M-Pesa (we never store card details)\n• You can export or delete your data anytime\n• We never sell your personal information")
            }
            Section("4. Your Rights") {
                Text("• Access your data: Settings > Export Data\n• Delete your data: Settings > Delete Account\n• Opt out of marketing: Settings > Notifications\n• Update your information: Profile > Edit")
            }
            Section("5. Contact Us") {
                Text("Email: privacy@farmdirect.africa\nPhone: +254 800 123 456\nAddress: FarmDirect Technologies Ltd, Nairobi, Kenya")
            }
            
            Spacer(Modifier.height(24.dp))
            Text(
                "FarmDirect is created and owned by Clinton Rotich. All rights reserved.",
                color = TextSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun Section(title: String, content: @Composable () -> Unit) {
    Spacer(Modifier.height(16.dp))
    Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryDarkGreen)
    Spacer(Modifier.height(8.dp))
    content()
}
