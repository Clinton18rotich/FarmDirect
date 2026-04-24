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
fun ErrorView(message: String, onRetry: (() -> Unit)? = null) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            Text("😞", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(message, style = MaterialTheme.typography.bodyLarge, color = StatusError, textAlign = TextAlign.Center)
            if (onRetry != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onRetry, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)) { Text("Try Again") }
            }
        }
    }
}
