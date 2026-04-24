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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfServiceScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terms of Service", color = TextWhite) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        },
        containerColor = BackgroundLight
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(24.dp)
        ) {
            Text("Terms of Service", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = PrimaryGreen)
            Text("Last updated: January 2026", color = TextSecondary, fontSize = 13.sp)
            Spacer(Modifier.height(20.dp))
            
            Section("1. Acceptance of Terms") {
                Text("By using FarmDirect, you agree to these terms. If you disagree, please do not use the app.")
            }
            Section("2. User Responsibilities") {
                Text("• Provide accurate information about your products\n• Honor your commitments to buyers/sellers\n• Do not post illegal or harmful content\n• Respect other users\n• Report suspicious activity")
            }
            Section("3. Platform Fees") {
                Text("• 3-5% transaction fee on completed sales\n• Delivery fees as displayed before booking\n• Premium plans: KES 299-999/month (optional)\n• All fees clearly shown before you commit")
            }
            Section("4. Dispute Resolution") {
                Text("• Contact support first: support@farmdirect.africa\n• Community mediation available\n• Escrow protects both parties\n• Legal jurisdiction: Kenya")
            }
            Section("5. Limitation of Liability") {
                Text("FarmDirect connects buyers and sellers. We are not responsible for product quality issues but provide dispute resolution tools. Payment protection via escrow.")
            }
            
            Spacer(Modifier.height(24.dp))
            Text(
                "© 2026 FarmDirect Technologies Ltd. Created by Clinton Rotich. All rights reserved.",
                color = TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
