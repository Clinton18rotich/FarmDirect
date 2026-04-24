package com.farmdirect.app.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.farmdirect.app.ui.theme.*

@Composable
fun OfflineBanner(
    isOnline: Boolean,
    pendingCount: Int,
    onSyncClick: () -> Unit,
    onDownloadClick: () -> Unit
) {
    AnimatedVisibility(
        visible = !isOnline || pendingCount > 0,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = if (!isOnline) StatusWarning.copy(alpha = 0.1f) else StatusInfo.copy(alpha = 0.1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    if (!isOnline) Icons.Default.CloudOff else Icons.Default.Sync,
                    contentDescription = null,
                    tint = if (!isOnline) StatusWarning else StatusInfo,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        if (!isOnline) "You are offline" else "$pendingCount items pending sync",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = if (!isOnline) StatusWarning else StatusInfo
                    )
                    if (!isOnline) {
                        Text(
                            "Showing cached data from last sync",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
                
                if (pendingCount > 0 && isOnline) {
                    TextButton(onClick = onSyncClick) {
                        Text("Sync Now", color = StatusInfo, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                if (!isOnline) {
                    TextButton(onClick = onDownloadClick) {
                        Text("Go Online", color = StatusWarning, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
