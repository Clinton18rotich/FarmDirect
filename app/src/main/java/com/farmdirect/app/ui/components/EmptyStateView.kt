package com.farmdirect.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmdirect.app.ui.theme.*

@Composable
fun EmptyStateView(emoji: String = "📭", title: String, subtitle: String = "", actionLabel: String? = null, onAction: (() -> Unit)? = null) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            Text(emoji, fontSize = 56.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, color = TextPrimary, textAlign = TextAlign.Center)
            if (subtitle.isNotEmpty()) { Spacer(modifier = Modifier.height(8.dp)); Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center) }
            if (actionLabel != null && onAction != null) { Spacer(modifier = Modifier.height(20.dp)); Button(onClick = onAction, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)) { Text(actionLabel) } }
        }
    }
}
